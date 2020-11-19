package org.han.server.core.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.han.files.DisgotJsonObj;
import org.han.server.core.Printer;
import org.han.server.core.types.Msg;

import com.google.gson.annotations.Expose;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DiscordData extends DisgotJsonObj {
	private static final long serialVersionUID = 1L;
	@Expose
	private Map<Long, Struct> Settings = new HashMap<Long, Struct>();

	public DiscordData() {
		super("", "BaseDB", "json");
		// TODO Auto-generated constructor stub
	}

	public class Struct {
		@Expose
		public Long LogChannel;
		@Expose
		Set<Long> trustedList = new HashSet<Long>();

		public void Save() {
			save();
		}
	}

	public TextChannel getLogChannel(Guild guild) {
		this.makeLoaded();
		Struct struct = Settings.get(guild.getIdLong());
		if (struct == null || struct.LogChannel == null)
			return null;
		return guild.getTextChannelById(struct.LogChannel);
	}

	public Struct getStruct(Guild guild) {
		this.makeLoaded();
		Struct struct = Settings.get(guild.getIdLong());
		if (struct == null) {
			return new Struct();
		}
		return struct;
	}

	public void saveStruct(Guild guild, Struct struct) {
		this.makeLoaded();
		Settings.remove(guild.getIdLong());
		Settings.put(guild.getIdLong(), struct);
		save();
	}

	private void check(long id) {
		if (Settings.get(id) == null) {
			Settings.put(id, new Struct());
		}
	}

	public void trust(Msg run, List<User> list) {
		if (list.size() < 1) {
			Printer.err(run, "No one was mentioned");
			return;
		}
		this.makeLoaded();

		check(run.getGuild().getIdLong());

		for (User user : list) {
			Settings.get(run.getGuild().getIdLong()).trustedList.remove(user.getIdLong());
			Settings.get(run.getGuild().getIdLong()).trustedList.add(user.getIdLong());
		}
		this.save();
		Printer.suc(run, "Added mentioned users to the trusted list");
	}

	public boolean isTrusted(Msg run) {
		if (!run.isGuild())
			return false;

		this.makeLoaded();
		check(run.getGuild().getIdLong());
		return Settings.get(run.getGuild().getIdLong()).trustedList.contains(run.getUser().getIdLong());
	}

	public void deTrust(Msg run, List<User> list) {
		if (list.size() < 1) {
			Printer.err(run, "No one was mentioned");
			return;
		}
		this.makeLoaded();
		check(run.getGuild().getIdLong());
		for (User user : list) {
			Settings.get(run.getGuild().getIdLong()).trustedList.remove(user.getIdLong());
		}
		this.save();
		Printer.suc(run, "Added mentioned users to the trusted list");

	}

}
