package org.han.server.spigot;

import java.io.File;

import javax.security.auth.login.LoginException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.han.api.PluginHook;
import org.han.bukkit.HanLibBukkit;
import org.han.client.spigot.DiscraftClient;
import org.han.debug.Log;
import org.han.server.core.DiscraftServerbase;
import org.han.server.core.Printer;
import org.han.server.core.DiscraftServerbase.Data;
import org.han.server.core.data.AccountLink;
import org.han.server.core.modules.Settings;
import org.han.server.core.types.Msg;
import org.han.server.core.types.FunctionalInterfaces.AccCheck;
import org.han.types.AsyncProcess;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class SpigotServ extends JavaPlugin {
	Server server;
	Data data;

	@Override
	public void onDisable() {
		if (server != null)
			server.stop();
	}

	@Override
	public void onEnable() {
		AsyncProcess.run(f -> {
			HanLibBukkit.registerLogger(this);

			if (server != null)
				server.stop();
			try {
				server = new Server();
				data = new Data(server);
			} catch (java.util.concurrent.CompletionException | LoginException e) {
				Log.out("Invalid token, please provide a valid discord bot token in this plugin's config options before proceeding");
				this.setEnabled(false);
			}

		});
	}

	public class Server extends DiscraftClient implements DiscraftServerbase {
		public Server() {
			super(SpigotServ.this);
			SpigotServ.this.getCommand("link").setExecutor((sender, command, label, args) -> {
				if (sender instanceof Player) {
					AccountLink.startLink(((Player) sender).getUniqueId(), s -> sender.sendMessage(s));
				} else {
					sender.sendMessage("You're not a player...I can't link you");
				}
				return true;
			});

			SpigotServ.this.getCommand("unlink").setExecutor((sender, command, label, args) -> {
				if (sender instanceof Player) {
					AccountLink.unLink(((Player) sender).getUniqueId(), s -> sender.sendMessage(s));
				} else {
					sender.sendMessage("You're not a player...I can't link you");
				}
				return true;
			});

			PluginHook.create(Luckperms.class, "Luckperms intergration");
			PluginHook.create(Dynmap.class, "Dynmap intergration");

		}

		@Override
		public void reload() {
			// TODO Auto-generated method stub

		}

		@Override
		public Data getData() {
			// TODO Auto-generated method stub
			return data;
		}

		

		@Override
		public Field getUserStatus(Member usr, Guild guild) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Field getGuildStatus(Msg run) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Field Settings(Msg run) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void regComs() {
			// TODO Auto-generated method stub
			reg("set-channel", "Link a channel to use for output", run -> {
				Settings.get().DefaultChannel = run.getChannel().getIdLong();
				getOutput().setChannel(run.getChannel().getIdLong());
				Settings.get().save();
				Printer.suc(run, "set channel");
			}).setGuild().setPerms(AccCheck.BotOwner, AccCheck.Trusted);
		}

		@Override
		public File getDataFile() {
			return SpigotServ.this.getDataFolder();
		}

	}

}