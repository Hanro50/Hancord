package org.han.server.bungee;

import java.util.UUID;

import org.han.server.core.hooks.LuckPermshook;
import org.han.server.core.types.Msg;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Luckperms extends LuckPermshook<ProxiedPlayer> {

	@Override
	public Field getGuildStatus(Msg run) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getUUID(ProxiedPlayer object) {
		// TODO Auto-generated method stub
		return object.getUniqueId();
	}

	@Override
	public Field getUserStatus(Member usr, Guild guild) {
		// TODO Auto-generated method stub
		return null;
	}

}
