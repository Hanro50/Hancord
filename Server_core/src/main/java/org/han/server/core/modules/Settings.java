package org.han.server.core.modules;

import org.han.debug.Log;
import org.han.files.DisgotConfigObj;

public class Settings extends DisgotConfigObj {

	private static final long serialVersionUID = 1L;
	private static Settings me;

	public static Settings get() {

		return me;
	}

	public static void reload() {
		Log.out("Loading settings");
		me = new Settings();
		me.load();
		me.save();
	}

	static {
		reload();
	}

	@Option(propertyComment = "The ID for the discord server. "
			+ "This can be set within the discord server itself with the aid of the bot owner")
	public Long DiscordGuildID = 000000000000000L;
	@Option(propertyComment = "The main output channel for the server. "
			+ "This can be set within the discord server itself with the aid of the bot owner")
	public Long DefaultChannel = 000000000000000L;
	@Option(propertyComment = "The discord bot token to use" + "This needs to be set by hand")
	public String Token = "Token here";
	

	public Settings() {
		super("", "Settings", "yml");
		// TODO Auto-generated constructor stub
	}

}
