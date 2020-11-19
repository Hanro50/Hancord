package org.han.server.core;

import java.awt.Color;
import java.util.Random;

import org.han.server.core.types.Msg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Printer {
	static String[] Images = new String[] { //
			"https://discordapp.com/assets/6debd47ed13483642cf09e832ed0bc1b.png",
			"https://discordapp.com/assets/322c936a8c8be1b803cd94861bdfa868.png",
			"https://discordapp.com/assets/dd4dbc0016779df1378e7812eabaa04d.png",
			"https://discordapp.com/assets/0e291f67c9274a1abdddeb3fd919cbaa.png",
			"https://discordapp.com/assets/1cbd08c76f8af6dddce02c5138971129.png" };
	static Random Rand = new Random();
	private EmbedBuilder embed = new EmbedBuilder();

	public void Print(Msg TC) {
		Print(TC.getChannel());
	}

	public void Print(MessageChannel TC) {
		TC.sendMessage(build()).queue();
	}

	public static String getUserIcon(User user) {
		return ChkUser(user);
	}

	private static String ChkUser(User user) {
		if (user.getAvatarUrl() != null)
			return user.getAvatarUrl();
		return Images[Integer.valueOf(user.getDiscriminator()) % 5];

	}

	private String ChkGuild(Guild guild) {
		if (guild.getIconUrl() != null)
			return guild.getIconUrl();
		return Images[Rand.nextInt(Images.length)];
	}

	public EmbedBuilder getbuilder() {
		return embed;
	}

	public MessageEmbed build() {
		return embed.build();
	}

	public Printer() {
	}

	public Printer(JDA jda) {
		embed.setThumbnail(ChkUser(jda.getSelfUser()));
	}

	public Printer(EmbedBuilder embed) {
		this.embed = embed;
	}

	public Printer(Guild guild, IMentionable Mentionable) {
		this(guild.getJDA());
		this.setRandomColour();
		if (Mentionable != null) {
			if (Mentionable instanceof Member) {
				this.setAuthor(Mentionable);
				return;
			} else if (Mentionable instanceof User) {
				this.setAuthor(guild.getMember((User) Mentionable));
				return;
			}
		}
		setGuildFooter(guild);
	}

	public Printer(Msg m, boolean randomcolour) {
		this(m.getJDA());
		if (randomcolour)
			setRandomColour();
		if (m.isGuild()) {
			setGuildFooter(m.getGuild());
			setAuthor(m.getMember());
		} else {
			setAuthor(m.getUser());
		}

	}

	public Printer(Msg m) {
		this(m, true);
	}

	public Printer setup(String header, String Disc) {
		if (header.trim().length() > 0)
			embed.setTitle(header);
		if (Disc.trim().length() > 0)
		embed.setDescription(Disc);
		return this;
	}

	public Printer setRandomColour() {
		embed.setColor(Rand.nextInt(16777215));
		return this;
	}

//.setThumbnail(run.getJDA().getSelfUser().getAvatarUrl());
	public Printer setGuildFooter(Guild guild) {
		if (guild != null) {
			embed.setFooter(guild.getName(), ChkGuild(guild));
		}
		return this;
	}

	public Printer setAuthor(IMentionable Mentionable) {
		if (Mentionable != null) {
			if (Mentionable instanceof Member) {
				Member member = (Member) Mentionable;
				embed.setAuthor(member.getEffectiveName(), "https://discord.com/users/" + member.getId(),
						ChkUser(member.getUser()));//
				setGuildFooter(member.getGuild());
			} else if (Mentionable instanceof User) {
				User user = (User) Mentionable;
				embed.setAuthor(user.getName(), "https://discord.com/users/" + user.getId(), ChkUser(user));//
			}
		}
		return this;
	}

	public static void err(Msg m, String message) {
		err(m.getChannel(), m.isGuild() ? m.getMember() : m.getUser(), message);
	}

	public static void err(MessageChannel mess, IMentionable user, String message) {
		mess.sendMessage(new Printer(mess.getJDA()).setAuthor(user).getbuilder()
				.setTitle("Encountered the following exception:").setDescription(message).setColor(Color.red).build())
				.queue();
	}

	public static void suc(Msg m, String message) {
		suc(m.getChannel(), m.isGuild() ? m.getMember() : m.getUser(), message);
	}

	public static void suc(MessageChannel mess, IMentionable user, String message) {
		mess.sendMessage(new Printer(mess.getJDA()).setAuthor(user).getbuilder().setTitle("Success:")
				.setDescription(message).setColor(Color.green).build()).queue();
	}
}
