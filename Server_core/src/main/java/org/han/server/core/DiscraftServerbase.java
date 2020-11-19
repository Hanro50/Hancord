package org.han.server.core;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.han.api.DiscraftBaseAPI;
import org.han.api.IPGrabber;
import org.han.api.PluginHook;
import org.han.api.types.OutputHandlerAPI;
import org.han.api.types.RoleAPI;
import org.han.api.types.RoleManagerAPI;
import org.han.debug.Log;
import org.han.server.core.data.AccountLink;
import org.han.server.core.data.LangLoader;
import org.han.server.core.data.ServerData;
import org.han.server.core.hooks.DefaultOut;
import org.han.server.core.hooks.DiscordListenerAdapter;
import org.han.server.core.modules.Settings;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.FunctionalInterfaces.AccCheck;
import org.han.types.AsyncProcess;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;

public interface DiscraftServerbase extends DiscraftBaseAPI, ComBase {

	public default Guild getGuild() {
		return getData().getGuild();
	}

	public default ShardManager getSH() {
		return getData().SH;
	}

	public default LangLoader getLangsys() {
		return getData().Langsys;
	}

	public default void stop() {
		DiscordDataHolder.CB.remove(this);
		PluginHook.disableAll();
		if (getData() != null && getData().SH != null)
			getData().SH.shutdown();
	}

	@Override
	public default OutputHandlerAPI getOutput() {
		return getData().output;
	}

	@Override
	public default RoleManagerAPI getRoleManager() {
		// TODO Auto-generated method stub
		return getData().RoleManager;
	}

	@Override
	public default String getIconURL() {
		// TODO Auto-generated method stub
		return getData().getpfpURL();
	}

	@Override
	public default String getName() {
		// TODO Auto-generated method stub
		return "Server-core";
	}

	@Override
	public default String getDisc() {
		// TODO Auto-generated method stub
		return "Core server based commands";
	}

	public Data getData();

	public void regComs();

	public class Data {

		LangLoader Langsys = new LangLoader();
		OutputHandlerAPI output = new DefaultOut();
		ShardManager SH;

		public Data(DiscraftServerbase base) throws LoginException {
			try {
				ServerData.setServer(base);
				DiscordDataHolder.CB.add(base);
				Langsys = LangLoader.Load();

				String Token = Settings.get().Token;

				if (SH != null)
					SH.shutdown();

				SH = DefaultShardManagerBuilder.createDefault(Token)//
						.setChunkingFilter(ChunkingFilter.ALL)//
						.enableIntents(GatewayIntent.GUILD_MEMBERS).build();//
				DiscordListenerAdapter E = new DiscordListenerAdapter(SH);
				SH.addEventListener(E);//
				Log.out("Shards:" + SH.getShardsTotal());

				// new SpigotHeaderUpdater();

			} catch (IllegalArgumentException e) {
				Log.trace(e);

			}
			// Bukkit.dispatchCommand(sender, commandLine);
			registercoms(base);
			base.regComs();
		}

		private Guild guild;

		public String getpfpURL() {
			return getGuild().getIconUrl();
		}

		public Guild getGuild() {
			// TODO Auto-generated method stub
			if (guild != null)
				return guild;

			if (Settings.get().DiscordGuildID != null && SH != null)
				return SH.getGuildById(Settings.get().DiscordGuildID);
			Log.out("Please select a guild");
			return null;
		}

		public RoleAPI getRole(Role role) {
			return new RoleAPI() {

				@Override
				public long getID() {
					// TODO Auto-generated method stub
					return role.getIdLong();
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return role.getName();
				}

				@Override
				public Color getColor() {
					// TODO Auto-generated method stub
					return role.getColor();
				}
			};
		}

		RoleManagerAPI RoleManager = new RoleManagerAPI() {
			@Override
			public List<RoleAPI> getAllRoles() throws Exception {
				if (getGuild() == null)
					throw new Exception("No guild set");
				List<RoleAPI> roles = new ArrayList<RoleAPI>();
				for (Role role : getGuild().getRoles())
					roles.add(getRole(role));
				return roles;
			}

			@Override
			public List<RoleAPI> getRolesOf(UUID user) throws Exception {
				Long id = AccountLink.GetDiscordID(user);
				if (getGuild() == null || id == null) {
					throw new Exception("No guild set or account not linked");
				}
				Member mem = getGuild().retrieveMemberById(id).complete();
				if (mem == null) {
					throw new Exception("user not in guild anymore");
				}
				List<RoleAPI> roles = new ArrayList<RoleAPI>();
				for (Role role : mem.getRoles())
					roles.add(getRole(role));
				return roles;
			}

			@Override
			public boolean isBooster(UUID user) {
				Long id = AccountLink.GetDiscordID(user);
				if (getGuild() == null || id == null) {
					return false;
				}
				Member mem = getGuild().retrieveMemberById(id).complete();
				if (mem == null) {
					return false;
				}
				return mem.getTimeBoosted() != null;
			}
		};
		
		
		private void registercoms(ComBase base) {
			base.reg("set-guild", "Set the guild this bot should focus on", run -> {
				Settings.get().DiscordGuildID = run.getGuild().getIdLong();
				Settings.get().save();
				guild = run.getGuild();
				Printer.suc(run, "I will now link data to this guild");

			}).setPerms(AccCheck.BotOwner).setGuild();
			base.reg("link", "Link a minecraft and discord account", run -> {
				AccountLink.completeLink(run);
			}).setPrivate();

			base.reg("command", "Run a minecraft command", run -> {
				UUID uuid = AccountLink.getUUID(run.getUser().getIdLong());
				if (uuid == null) {
					Printer.err(run, "Please link your account before running this command");
					return;
				}
				ServerData.getServer().command(run.getUser().getIdLong(), uuid, run.getText().trim());
			}).setGuild();
			base.reg("ip", "Get the server's IP", run -> {
				AsyncProcess.run(f -> {
					try {
						new Printer(run).setup("Current server IPv4 address", IPGrabber.getIP()).Print(run);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			});
		}
	}
}
