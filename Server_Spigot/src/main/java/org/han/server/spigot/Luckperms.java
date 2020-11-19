package org.han.server.spigot;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.han.server.core.hooks.LuckPermshook;
import org.han.server.core.types.Msg;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class Luckperms extends LuckPermshook<Player>{

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
	public UUID getUUID(Player object) {
		// TODO Auto-generated method stub
		return object.getUniqueId();
	}
	
	

}
