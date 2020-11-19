package org.han.server.core.types;

import org.han.server.core.data.DiscordData;

public class FunctionalInterfaces {
	@FunctionalInterface
	public interface Check {
		public boolean chk(Msg input);
	}

	@FunctionalInterface
	public interface AccCheck extends Check {
		AccCheck BotOwner = f -> f.isBotOwner(); //
		AccCheck GuildOwner = f -> f.isGuildOwner(); //
		AccCheck GuildAdmin = f -> f.isGuildAdmin();//
		AccCheck Trusted = f -> new DiscordData().isTrusted(f);
	}

	@FunctionalInterface
	public interface tpeCheck extends Check {
		tpeCheck Guild = f -> f.isGuild(); //
		tpeCheck Private = f -> !f.isGuild(); //
	}
	
	@FunctionalInterface
	public interface HelpInf {
		public String Compile(String key,String input);
	}

}