package org.han.server.core.types;

import java.util.function.Consumer;

import org.han.server.core.CHandler;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public interface ComBase {

	public abstract Field getUserStatus(Member usr, Guild guild);

	public abstract Field getGuildStatus(Msg run);

	public abstract Field Settings(Msg run);

	public default ComObj reg(String name, String disc, Consumer<Msg> run) {
		return CHandler.reg(name, disc, this, run);
	}

	public abstract String getName();

	public abstract String getDisc();
}
