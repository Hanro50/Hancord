package org.han.server.core.hooks;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.han.api.BaseData;
import org.han.api.types.AdvancementAPI;
import org.han.api.types.BroadcastAPI;
import org.han.api.types.DeathMessageAPI;
import org.han.api.types.MessageAPI;
import org.han.api.types.OutputHandlerAPI;
import org.han.debug.Log;
import org.han.server.core.Printer;
import org.han.server.core.data.AccountLink;
import org.han.server.core.data.ServerData;
import org.han.server.core.modules.Settings;
import org.han.types.AsyncProcess;

import com.google.gson.annotations.Expose;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;

public class DefaultOut implements OutputHandlerAPI {
	@Expose
	Map<String, Long> Channels = new HashMap<String, Long>();

	@Override
	public void playerJoin(String ChannelName, UUID player) {
		long ChannelID = availableChannels().get(ChannelName);
		if (Channels.get(ChannelName) == 0L) {
			Log.err("Server not set up yet");
			return;
		}

		TextChannel CHL = ServerData.getServer().getGuild().getTextChannelById(ChannelID);
		if (CHL == null) {

			Log.err("Invalid channel (NULL return value)");
			return;
		}

		Long id = AccountLink.GetDiscordID(player);
		playerorleave(id, CHL, player, ServerData.getServer().getLangsys().StringJoinText(), Color.green);

	}

	@Override
	public void playerLeft(String ChannelName, UUID player) {
		long ChannelID = availableChannels().get(ChannelName);
		if (Channels.get(ChannelName) == 0L) {
			Log.err("Server not set up yet");
			return;
		}
		TextChannel CHL = ServerData.getServer().getGuild().getTextChannelById(ChannelID);
		if (CHL == null) {

			Log.err("Invalid channel (NULL return value)");
			return;
		}

		Long id = AccountLink.GetDiscordID(player);
		playerorleave(id, CHL, player, ServerData.getServer().getLangsys().StringLeftText(), Color.red);

	}

	public void playerorleave(Long id, TextChannel CHL, UUID player, String format, Color color) {
		if (id != null) {
			ServerData.getServer().getGuild().retrieveMemberById(id).queue(mem -> {
				CHL.sendMessage(new Printer().getbuilder().setColor(color)
						.setAuthor(String.format(format, mem.getEffectiveName()),
								"https://discord.com/users/" + mem.getId(), Printer.getUserIcon(mem.getUser()))
						.build()).queue();
			}, err -> {
				ServerData.getServer().getSH().retrieveUserById(id).queue(user -> {
					CHL.sendMessage(new Printer().getbuilder().setColor(color)
							.setAuthor(String.format(format, user.getName()),
									"https://discord.com/users/" + user.getId(), Printer.getUserIcon(user))
							.build()).queue();

				});
			});

		} else {

			CHL.sendMessage(new Printer().getbuilder().setColor(color)
					.setAuthor(String.format(format, BaseData.getPluginbase().getMCName(player)), null,
							"https://crafatar.com/avatars/" + player.toString() + "?overlay")
					.build()).queue();
		}
	}

	@Override
	public void advancement(String ChannelName, AdvancementAPI advance) {
		// TODO Auto-generated method stub
		long ChannelID = availableChannels().get(ChannelName);
		if (Channels.get(ChannelName) == 0L) {
			Log.err("Server not set up yet");
			return;
		}
		String ICON = null;
		String PlayerName = null;

		Long L = AccountLink.GetDiscordID(advance.getPlayer());
		if (L != null) {
			Member f = ServerData.getServer().getGuild().retrieveMemberById(L).complete();
			if (f != null) {
				ICON = f.getUser().getAvatarUrl();
				PlayerName = f.getEffectiveName();
			}
		}
		if (ICON == null) {
			ICON = "https://crafatar.com/avatars/" + advance.getPlayer().toString() + "?overlay";
		}

		if (PlayerName == null) {
			PlayerName = BaseData.getPluginbase().getMCName(advance.getPlayer());
		}

		String Type = "";
		if (advance.getAchievementID().split("/").length >= 1) {
			Type = advance.getAchievementID().split("/")[0];
		}
		String Title = ServerData.getServer().getLangsys().GetAdvancement_title(advance.getAchievementID());
		String Disc = "";
		Disc = ServerData.getServer().getLangsys().GetAdvancement_Disc(advance.getAchievementID());
		Color Clr = new Color(128, 128, 128);
		if (Type.equals("recipes")) {
			Log.err("Recipy advancements aren't being blocked by the spigot portion of this plugin again...please report");
			return;
		} else if (Type.equals("story"))
			Clr = Color.green;
		else if (Type.equals("nether"))
			Clr = Color.red;
		else if (Type.equals("end"))
			Clr = new Color(139, 0, 139);
		else if (Type.equals("husbandry"))
			Clr = Color.yellow;
		else if (Type.equals("adventure"))
			Clr = Color.orange;

		Type = ServerData.getServer().getLangsys().GetAdvancement_Root_title(Type) + " : "
				+ ServerData.getServer().getLangsys().GetAdvancement_Root_Disc(Type);

		try {
			TextChannel CHL = ServerData.getServer().getSH().getTextChannelById(ChannelID);
			if (CHL != null) {
				try {
					MessageEmbed E = new EmbedBuilder()
							.setAuthor(ServerData.getServer().getLangsys().GetAdvancement_toast(), null, ICON)
							.setTitle(String.format(ServerData.getServer().getLangsys().StringadvancementtaskText(),
									PlayerName, "[**" + Title + "**]")) //
							.setColor(Clr).setDescription(Disc).setFooter(Type).build();
					CHL.sendMessage(E).queue();
				} catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
					Log.trace(e);
					Log.err("correcting error");
				}
			} else {
				Log.err("Could not find channel");
			}
		} catch (NullPointerException e) {

		}

	}

	@Override
	public void deathMessage(String ChannelName, DeathMessageAPI dm) {
		long ChannelID = availableChannels().get(ChannelName);
		if (Channels.get(ChannelName) == 0L) {
			Log.err("Server not set up yet");
			return;
		}
		// TODO Auto-generated method stub
		String BaseStr = ServerData.getServer().getLangsys().GetDM_title(dm.getDeathMsg(),
				(dm.getAttacker() != null || dm.getMobName() != null), (dm.getWeapon() != null));

		Long vic = AccountLink.GetDiscordID(dm.getVictem());
		String vicName = null;
		String AtkName = null;
		Member Vic = null;
		if (vic != null) {
			Vic = ServerData.getServer().getGuild().retrieveMemberById(vic).complete();
			if (Vic != null) {
				vicName = Vic.getEffectiveName();
			}
		}
		if (vicName == null) {
			vicName = BaseData.getPluginbase().getMCName(dm.getVictem());
		}
		if (dm.getAttacker() != null) {
			Long Atk = AccountLink.GetDiscordID(dm.getAttacker());

			if (Atk != null) {
				Member mem = ServerData.getServer().getGuild().retrieveMemberById(Atk).complete();
				if (mem != null) {
					AtkName = mem.getEffectiveName();
				}
			}
			if (AtkName == null) {
				AtkName = BaseData.getPluginbase().getMCName(dm.getAttacker());
			}
		} else if (dm.getMobName() != null) {
			AtkName = ServerData.getServer().getLangsys().GetEntity(dm.getMobName());
		} else {
			AtkName = "Translation error";
		}

		String ICON = (Vic == null ? "https://crafatar.com/avatars/" + dm.getVictem().toString() + "?overlay"
				: Vic.getUser().getAvatarUrl());
		Color Clr = new Color(0, 127, 255);

		String Weapon = dm.getWeapon() == null ? "Translation error" : dm.getWeapon();
		vicName = MarkdownSanitizer.escape(vicName);
		BaseStr = MarkdownSanitizer.escape(BaseStr);
		AtkName = MarkdownSanitizer.escape(AtkName);
		Weapon = MarkdownSanitizer.escape(Weapon);
		String Disc = "";

		Disc = Disc + String.format("**[_%s_]** \n```", vicName);
		if (dm.getXP() > 0) {
			Disc = Disc + "\n\t" + (String.format(ServerData.getServer().getLangsys().EXPString(), "",
					String.format("%.2f", dm.getXP()))).trim();
		}
		Disc = Disc + "\n\tXYZ:" + String.format("(x:%.2f,y:%.2f,z:%.2f)", dm.getDeathpos().x(), dm.getDeathpos().y(),
				dm.getDeathpos().z());
		Disc = Disc + "```\n";

		if (dm.getEnchantments() != null) {
			Disc = Disc + "**[_" + Weapon + "_]** \n```";
			for (String Ench : dm.getEnchantments().keySet()) {
				Disc = Disc + "\t" + ServerData.getServer().getLangsys().EnchantmentName(Ench) + " "
						+ ServerData.getServer().getLangsys().EnchantmentLevel(dm.getEnchantments().get(Ench)) + "\n";

				Disc = Disc + "```";
			}
		}
		try {
			TextChannel CHL = ServerData.getServer().getGuild().getTextChannelById(ChannelID);
			if (CHL != null) {
				try {
					MessageEmbed E = new EmbedBuilder()
							.setAuthor(ServerData.getServer().getLangsys().GetGameOver(), null, ICON)
							.setTitle(String.format(BaseStr, vicName, AtkName, "**[_" + Weapon + "_]**")).setColor(Clr)
							.setDescription(Disc).build();
					CHL.sendMessage(E).queue();
				} catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
					Log.trace(e);

				}
			}
		} catch (NullPointerException e) {
			Log.trace(e);
		}
	}

	@Override
	public void sendMessage(String ChannelName, MessageAPI message) {
		AsyncProcess.run(f -> {
			long ChannelID = availableChannels().get(ChannelName);
			if (Channels.get(ChannelName) == 0L) {
				Log.err("Server not set up yet");
				return;
			}
			try {
				User user = null;
				Member member = null;
				Guild guild = null;
				ServerData.getServer();
				TextChannel CHL = ServerData.getServer().getGuild().getTextChannelById(ChannelID);
				if (CHL == null) {

					Log.err("Invalid channel (NULL return value)");
					return;
				}

				try {
					Webhook F;
					List<Webhook> W = CHL.retrieveWebhooks().complete();
					L1: {
						for (Webhook webhook : W) {
							if (webhook.getOwner().getUser().getIdLong() == ServerData.getServer().getSH().getShards()
									.get(0).getSelfUser().getIdLong()) {
								F = webhook;
								break L1;
							}
						}
						F = CHL.createWebhook("MCLINK").complete();
					} // L1;
					WebhookClient client = new WebhookClientBuilder(F.getUrl()).build();

					WebhookMessageBuilder builder = new WebhookMessageBuilder();

					builder.setContent(message.getMessage());
					if (message.getAvatarURL() != null)
						builder.setAvatarUrl(message.getAvatarURL());
					if (message.getName() != null)
						builder.setUsername(message.getName());

					if (message.getPlayer() != null) {
						try {
							Long DiscordID = AccountLink.GetDiscordID(message.getPlayer());
							if (DiscordID != null)
								user = ServerData.getServer().getSH().retrieveUserById(DiscordID).complete();
							if (user == null) {

								builder.setUsername(BaseData.getPluginbase().getMCName(message.getPlayer()));
								builder.setAvatarUrl(
										"https://crafatar.com/avatars/" + message.getPlayer().toString() + "?overlay");
							} else {
								guild = ServerData.getServer().getGuild();
								if (guild != null) {
									member = guild.retrieveMember(user).complete();
								}

								builder.setUsername(member != null ? member.getEffectiveName() : user.getName());
								builder.setAvatarUrl(user.getAvatarUrl());
							}

						} catch (ErrorResponseException e) {
							Log.err(e.getMessage());
						}
					}

					WebhookMessage webmessage = builder.build();
					client.send(webmessage);
					client.close();
					return;
				} catch (InsufficientPermissionException e) {
					Log.err(e.getMessage());
				} catch (NullPointerException e) {
					Log.trace(e);
				}
				if (user != null) {
					new Printer().setAuthor(member != null ? member : user).setup("User Message:", message.getMessage())
							.Print(CHL);
				} else if (message.getPlayer() != null) {
					EmbedBuilder E = new EmbedBuilder()
							.setAuthor(BaseData.getPluginbase().getMCName(message.getPlayer()), null,
									"https://crafatar.com/avatars/" + message.getPlayer().toString() + "?overlay")
							.setDescription(message.getMessage());
					if (guild != null)
						E = new Printer(E).setGuildFooter(guild).getbuilder();

					CHL.sendMessage(E.build()).queue();
				}
			} catch (RuntimeException e) {
				Log.trace(e);
			}
		});
	}

	@Override
	public void broadcastMessage(String ChannelName, BroadcastAPI message) {
		long ChannelID = availableChannels().get(ChannelName);
		if (Channels.get(ChannelName) == 0L) {
			Log.err("Server not set up yet");
			return;
		}
		TextChannel CHL = ServerData.getServer().getGuild().getTextChannelById(ChannelID);
		if (message.isError()) {
			IMentionable mem = ServerData.getServer().getGuild().retrieveMemberById(message.getDiscordID()).complete();
			if (mem == null) {
				mem = ServerData.getServer().getSH().retrieveUserById(message.getDiscordID()).complete();
			}

			Printer.err(CHL, mem, (message.getHeader().trim().length() > 0 ? "**" + message.getHeader() + "** :" : "")
					+ message.getContent());
		}

	}

	@Override
	public Map<String, Long> availableChannels() {
		Long d = getDefaultchannel();
		if (d != null && !Channels.containsKey(DEFAULT))
			Channels.put(DEFAULT, getDefaultchannel());
		return Channels;
	}

	@Override
	public Long getDefaultchannel() {
		// TODO Auto-generated method stub
		return Settings.get().DefaultChannel;
	}

	@Override
	public void updateTopic(String ChannelName, String newHeader) {
		long ChannelID = availableChannels().get(ChannelName);
		if (Channels.get(ChannelName) == 0L) {
			Log.err("Server not set up yet");
			return;
		}
		TextChannel CHL = ServerData.getServer().getGuild().getTextChannelById(ChannelID);
		CHL.getManager().setTopic(newHeader).queue();
	}

	@Override
	public void serverStateUpdate(String ChannelName, boolean startup) {
		long ChannelID = availableChannels().get(ChannelName);
		if (Channels.get(ChannelName) == 0L) {
			Log.err("Server not set up yet");
			return;
		}
		TextChannel CHL = ServerData.getServer().getGuild().getTextChannelById(ChannelID);
		CHL.sendMessage(new EmbedBuilder().setAuthor(startup ? "🟢 server just started!" : "🔴 server was just stopped!")
				.setColor(startup ? Color.green : Color.red).build()).queue();

	}

}
