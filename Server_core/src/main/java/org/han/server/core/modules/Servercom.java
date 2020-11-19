package org.han.server.core.modules;

import java.io.IOException;
import java.util.UUID;

import org.han.api.IPGrabber;
import org.han.server.core.Printer;
import org.han.server.core.data.AccountLink;
import org.han.server.core.data.ServerData;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.Msg;
import org.han.server.core.types.FunctionalInterfaces.AccCheck;
import org.han.types.AsyncProcess;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
@Deprecated
public class Servercom implements ComBase {

	public Servercom() {
		reg("command", "Run a minecraft command", run -> {
			UUID uuid = AccountLink.getUUID(run.getUser().getIdLong());
			if (uuid == null) {
				Printer.err(run, "Please link your account before running this command");
				return;
			}
			ServerData.getServer().command(run.getUser().getIdLong(),uuid,  run.getText().trim());
			/*
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(DisgotCore.getPlugin(),

					() -> {
						DiscordPlayer DP = new DiscordPlayer(Bukkit.getOfflinePlayer(uuid), run);
						try {
							Bukkit.dispatchCommand(DP, run.getText().trim());
							DP.printInput();
						} catch (CommandException e) {
							Printer.err(run, "This command is not supported!");
						}

					}, 1L);
*/
		}).setGuild();

		reg("ip", "Get the server's IP", run -> {
			AsyncProcess.run(f -> {
				try {
					new Printer(run).setup("Current server IPv4 address", IPGrabber.getIP()).Print(run);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		});

		reg("set-guild", "Set the guild this bot should focus on", run -> {
			Settings.get().DiscordGuildID = run.getGuild().getIdLong();
			Settings.get().save();
			Printer.suc(run, "I will now link data to this guild");
		}).setPerms(AccCheck.BotOwner).setGuild();

		//reg("set-channel", "Set the channel", run -> {
		//	Settings.get().ChannelID = run.getChannel().getIdLong();
		//	Settings.get().save();
		//	Printer.suc(run, "I will now send data between this and the game chat");
		//}).setPerms(AccCheck.BotOwner).setGuild();

		reg("link", "Link a minecraft and discord account", run -> {
			AccountLink.completeLink(run);
		}).setPrivate();

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
	public String getName() {
		// TODO Auto-generated method stub
		return "core";
	}

	@Override
	public String getDisc() {
		// TODO Auto-generated method stub
		return "Core commands";
	}

}
