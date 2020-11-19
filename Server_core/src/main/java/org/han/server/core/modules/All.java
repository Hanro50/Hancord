package org.han.server.core.modules;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.Msg;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class All implements ComBase{

	@Override
	public Field getUserStatus(Member usr, Guild guild) {
		return null;
	}
	@Override
	public Field getGuildStatus(Msg run) {
		return null;
	}

	@Override
	public Field Settings(Msg run) {
		return null;
	}

	@Override
	public String getName() {
		return "all";
	}

	@Override
	public String getDisc() {
		return "List all available commands regardless if you can run them or not";
	}

}
