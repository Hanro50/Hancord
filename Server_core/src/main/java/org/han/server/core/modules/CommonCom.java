package org.han.server.core.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import org.han.server.core.CHandler;
import org.han.server.core.DiscordDataHolder;
import org.han.server.core.Printer;
import org.han.server.core.data.AccountLink;
import org.han.server.core.data.DiscordData;
import org.han.server.core.data.ServerData;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.ComObj;
import org.han.server.core.types.Msg;
import org.han.server.core.types.FunctionalInterfaces.AccCheck;
import org.han.server.core.types.FunctionalInterfaces.HelpInf;
import org.han.api.IPGrabber;
import org.han.types.AsyncProcess;
import org.han.types.Feed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;;

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
				title = "Thank you for adding me";
			run.getChannel().sendMessage(new Printer(run).setup(title,
					"Programmer: [Hanro](https://github.com/Hanro50)\nArtist: [Kiba](https://www.instagram.com/pirateshit/)\n"//
							+ Rtext[(int) (Math.random() * (double) Rtext.length)])
					.build()).queue();
		});
// .setAuthor(title, "https://github.com/Hanro50", null)
// clean
		reg("clean",
				"Remove messages posted by removed users or by any mentioned users for around 10'000 messages up within this chat\n"
						+ "Deleted messages shall be saved and sent to you in DM. (Only one operation allowed per guild)"
						+ "\n Cannot delete more then 200 messages at once due to rate limits and general bot-server load",
				run -> {
					if (guilds.contains(run.getGuild().getId())) {
						run.getChannel().sendMessage("Only one of these operations allowed at a time per guild")
								.queue();
						return;
					}
					Message ms = run.GetDM().sendMessage("Scanned 0 messages").complete();
					guilds.add(run.getGuild().getId());
					String out = "";
					String OutMess = "Deleted messages";
					int idelmessages = 0;
					try {

						MessageHistory MH = run.getChannel().getHistory();
						int i = -1;

						Set<Message> Messages = new HashSet<Message>();
						Messages.addAll(MH.getRetrievedHistory());
						for (int count = 0; count < 100; count++) {
							Messages.addAll(MH.retrievePast(100).complete());
							if (Messages.size() == i) {
								break;
							}
							ms.editMessage("Scanning: " + Messages.size() + " messages")
									.deadline(System.currentTimeMillis() + 100).queue();
							i = Messages.size();
						}

						int Pcount = 0;
						Feed<Message, Boolean> chk = run.getMessage().getMentionedUsers().size() > 0
								? msg -> run.getMessage().isMentioned(msg.getAuthor())
								: msg -> !(run.getGuild().isMember(msg.getAuthor()) || msg.isWebhookMessage());

						for (Message msg : Messages) {
							Pcount++;

							if (Pcount % 50 == 0)
								ms.editMessage("Processed: " + Pcount + "/" + Messages.size() + " messages")
										.deadline(System.currentTimeMillis() + 100).queue();
							if (chk.get(msg)) {
								out += format(msg);
								msg.delete().complete();
								idelmessages++;
							}
							if (idelmessages > 200) {
								OutMess = "Max deleted messages reached!";
							}
						}

					} catch (ErrorResponseException e) {
						OutMess = "Got the following message from discord: " + e.getErrorResponse().toString();
					} catch (RuntimeException e) {
						OutMess = "An error occured";
					} finally {
						try {
							ms.delete().queue();

							run.GetDM().sendMessage(OutMess).addFile(out.getBytes(), "messages.txt").complete();

							TextChannel Lchan = new DiscordData().getLogChannel(run.getGuild());
							if (Lchan != null) {
								Lchan.sendMessage(OutMess).addFile(out.getBytes(), "messages.txt").complete();
							}

						} finally {
							guilds.remove(run.getGuild().getId());
						}
					}
				}).setPar("[@mentions...]").setPerms(AccCheck.GuildAdmin).setGuild();
// Stop
		reg("stop", "Kills the bot", run -> {
			run.getChannel().sendMessage("Shutting down...").complete();
			run.getJDA().getShardManager().shutdown();
			System.runFinalization();
			System.gc();
			System.exit(0);
		}).setPerms(AccCheck.BotOwner).hide();
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
		reg("command", "Run a minecraft command", run -> {
			UUID uuid = AccountLink.getUUID(run.getUser().getIdLong());
			if (uuid == null) {
				Printer.err(run, "Please link your account before running this command");
				return;
			}
			ServerData.getServer().command(run.getUser().getIdLong(),uuid, run.getText().trim());
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
		reg("link", "Link a minecraft and discord account", run -> {
			AccountLink.completeLink(run);
		}).setPrivate();
	}

	private static String format(Message msg) {
		return msg.getTimeCreated().toString() + "\n" + "[" + msg.getAuthor().getId() + ":" + msg.getAuthor() + "]"
				+ msg.getContentDisplay() + "\n";
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
