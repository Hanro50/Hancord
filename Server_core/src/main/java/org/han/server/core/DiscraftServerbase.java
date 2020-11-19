package org.han.server.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.han.api.DiscraftBaseAPI;
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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
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

		public Data(DiscraftServerbase base) {
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
				for (JDA jda : SH.getShards()) {
					try {
						jda.awaitReady();
					} catch (InterruptedException e) {
						Log.trace(e);
					}
				}

				Log.out(SH.retrieveApplicationInfo().complete().getInviteUrl(Permission.ADMINISTRATOR));

				// new SpigotHeaderUpdater();

			} catch (IllegalArgumentException e) {
				Log.trace(e);

			}
			// Bukkit.dispatchCommand(sender, commandLine);
			catch (LoginException e1) {
				Log.err("Please provide a valid discord bot token");
				return;
			}
			for (JDA shards : SH.getShards()) {
				try {
					shards.awaitReady();
				} catch (InterruptedException e) {
				}
			}
			base.regComs();
		}

		private Guild guild;

		public String getpfpURL() {
			return getGuild().getIconUrl();
		}
		
		
		public Guild getGuild() {
			// TODO Auto-generated method stub
			if (guild != null && Settings.get().DiscordGuildID == guild.getIdLong())
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

	}

}
