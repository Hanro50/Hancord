package org.han.server.core.modules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.han.server.core.CHandler;
import org.han.server.core.DiscordDataHolder;
import org.han.server.core.Printer;
import org.han.server.core.data.DiscordData;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.ComObj;
import org.han.server.core.types.FunctionalInterfaces.AccCheck;
import org.han.server.core.types.FunctionalInterfaces.HelpInf;
import org.han.server.core.types.Msg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;;

public class CommonCom implements ComBase {
	static Set<String> guilds = new ConcurrentSkipListSet<String>();
	final static String[] Rtext = { "Favourite animal: Cats", "Meow?", "go go references!", "The bird is the word",
			"Kernal Musterd seems sus", "What's cooking?", "There is a snake in my code", "The printer is on fire!",
			"Written in JAVA", "Stack overflow exception averted", "Now Mincecraft compatible", "{Insert joke here}",
			"Has anyone seen stabby...\nahh my ankle, ahh there he is", "Take me to your leader, human", "Space orcs!",
			"Ordering pizza", "STOP BURNING MY HOUSE DOWN WITH LEMONS", "The spirit of lovecraft compels thee",
			"Swing like the 1920s, don't crash like the 1930s", "Reading wikipedia", "Writing more jokes",
			"Everything's Coming Up Methos!", "My eyes! Google did Nothing!", "You gots the Dud!", "the cake is a lie",
			"Worst. Comic Book Guy. Reference. Ever", "Eat my shiny metal shorts", "Calling awesome Express",
			"My Story Is A Lot Like Yours, Only More Interesting /'Cause It Involves Discord/'.", "I am the same",
			"hiding _homework folder_", "_Escaping the matrix_", "_flooding enrichment center with nerve gas_",
			"Intelligence is the ability to avoid doing work, yet getting the work done.", "**D'oh!**",
			"Plotting world \'peace\'", "Destroy all \'kill humans\' memes", "Error 404, reference not found",
			"I do get my pizzas paid for by my programmer indirectly.", "I sell butane and butane accessories" };

	public CommonCom() {
//info
		reg("info", "get information about this bot :)", run -> {
			String title = "Hello there frend!";
			if (run.isGuild())
				title = "Thank you for installing me";
			run.getChannel()
					.sendMessage(new Printer(run).setup(title, "Programmer: [Hanro](https://github.com/Hanro50)\n"//
							+ Rtext[(int) (Math.random() * (double) Rtext.length)]).build())
					.queue();
		});

// Restart
		reg("restart", "Restarts the bot", run -> {
			run.getChannel().sendMessage("Restarting!").complete();
			run.getJDA().getShardManager().restart();
			System.runFinalization();
			System.gc();
		}).setPerms(AccCheck.BotOwner).hide();
// LogChannel
		reg("set-log-channel", "Set a log channel", run -> {
			DiscordData CC = new DiscordData();
			DiscordData.Struct struct = CC.getStruct(run.getGuild());
			TextChannel messagechannel;
			if (run.getMessage().getMentionedChannels().size() > 0)
				messagechannel = run.getMessage().getMentionedChannels().get(0);
			else
				messagechannel = (TextChannel) run.getChannel();
			struct.LogChannel = messagechannel.getIdLong();
			messagechannel.sendMessage("Will now use this channel as a Log channel").queue();
			CC.saveStruct(run.getGuild(), struct);
			if (!struct.LogChannel.equals(run.getChannel().getIdLong())) {
				run.getChannel().sendMessage("Success").queue();
			}

		}).setPar("[@Channel mentions...]").setPerms(AccCheck.GuildAdmin).setGuild();
//server settings
		reg("settings", "Get the settings for the bot", run -> {
			EmbedBuilder embed = new Printer(run).getbuilder().setTitle("Module Settings:");
			for (ComBase CBI : DiscordDataHolder.CB) {
				Field f = CBI.Settings(run);
				if (f != null)
					embed.addField(f);
			}
			run.getChannel().sendMessage(embed.build()).queue();
		}).setGuild();
		// status-user
		reg("status-user", "Check stored information about a set user", run -> {
			Member usr = run.getMessage().getMentionedUsers().size() > 0 ? run.getMessage().getMentionedMembers().get(0)
					: run.getMember();
			EmbedBuilder embed = new Printer(run).getbuilder().setTitle("Status:");

			// , "Status:", "", usr.getUser()).setTitle("Status:");
			for (ComBase CBI : DiscordDataHolder.CB) {
				Field f = CBI.getUserStatus(usr, run.getGuild());
				if (f != null)
					embed.addField(f);
			}
			run.getChannel().sendMessage(embed.build()).queue();
		}).setGuild().setPar("[user]");
		// status-guild
		reg("status-guild", "Check stored information about a this guild", run -> {
			EmbedBuilder embed = new Printer(run).getbuilder().setTitle("Module Status:", "");
			for (ComBase CBI : DiscordDataHolder.CB) {
				Field f = CBI.getGuildStatus(run);
				if (f != null)
					embed.addField(f);
			}
			run.getChannel().sendMessage(embed.build()).queue();
		}).setGuild();
		// trust

		reg("trust", "Mark a user as trusted. Trusted users can preform certian tasks", run -> {
			new DiscordData().trust(run, run.getMessage().getMentionedUsers());
		}).setPar("[@mentions...]").setPerms(AccCheck.GuildAdmin).setGuild();
		reg("untrust", "Unmark a user as trusted.", run -> {
			new DiscordData().deTrust(run, run.getMessage().getMentionedUsers());
		}).setPar("[@mentions...]").setPerms(AccCheck.GuildAdmin).setGuild();
		reg("help", "Shows the help command", run -> {
			String header = "";

			List<String> Out = new ArrayList<String>();
			List<String> keys = new ArrayList<String>();
			Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
			keys.addAll(CHandler.getComs().keySet());
			keys.sort(cmp);
			HelpInf Compute;

			if (run.getText().contains("all")) {
				header = "All available commands";
				Compute = (key, res) -> {
					ComObj comobj = CHandler.getComs().get(key);
					String temp = "**" + run.getPrefix() + key + "** " + comobj.getPar() + "\n> "
							+ CHandler.getComs().get(key).getDisc().replaceAll("\n", "\n> \n> ") + "\n";

					if ((res + temp).length() > 2047) {
						Out.add(res);
						return temp;
					} else {
						return res + temp;
					}
				};

			} else {
				Check: {
					if (run.getText().length() > 0) {
						for (ComBase Com : DiscordDataHolder.CB) {
							if (run.getText().equalsIgnoreCase(Com.getName())) {
								header = Com.getName();
								Compute = (key, res) -> {
									ComObj comobj = CHandler.getComs().get(key);
									if (comobj.getComBase().equals(Com) && comobj.hasPerms(run) && !comobj.isHidden(run)
											&& comobj.isrightplace(run)) {
										String temp = "**" + run.getPrefix() + key + "** " + comobj.getPar() + "\n> "
												+ CHandler.getComs().get(key).getDisc().replaceAll("\n", "\n> \n> ")
												+ "\n";
										if ((res + temp).length() > 2047) {
											Out.add(res);
											return temp;
										} else {
											return res + temp;
										}
									}
									return res;
								};
								break Check;
							}
						}
						Printer.err(run, "Invalid module mentioned");
						return;
					}
					List<String> temp = new ArrayList<String>();
					header = "Available modules:";
					for (ComBase Com : DiscordDataHolder.CB) {
						temp.add("**" + Com.getName() + "** " + "\n> " + Com.getDisc().replaceAll("\n", "\n> \n> ")
								+ "\n");
					}
					temp.sort(cmp);
					String out = "";
					for (String Com : temp) {
						out += Com;
					}
					temp.clear();
					temp.add(out);
					print(run, header, temp);
					run.getMessage().addReaction("📩").queue();
					return;
				}
			}
			String resout = "";
			for (String key : keys) {
				resout = Compute.Compile(key, resout);
			}
			if (resout.length() > 0)
				Out.add(resout);
			if (Out.size() < 1)
				Out.add("You don't appear to be able to run any commands in this modules");

			print(run, header, Out);
			run.getMessage().addReaction("📩").queue();

		});

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

	private static void print(Msg m, String header, List<String> Out) {
		for (int i = 0; i < Out.size(); i++) {

			if (Out.get(i).trim().length() < 1)
				return;

			final int PageNum = i;
			m.getUser().openPrivateChannel()
					.queue(chl -> chl
							.sendMessage(new Printer(m)
									.setup(header + " (Page " + (PageNum + 1) + ")", Out.get(PageNum)).build())
							.queue());
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Common";
	}

	@Override
	public String getDisc() {
		// TODO Auto-generated method stub
		return "A common set of commands for the bot";
	}
}
