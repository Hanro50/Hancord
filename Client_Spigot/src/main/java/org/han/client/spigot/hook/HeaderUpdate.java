package org.han.client.spigot.hook;

import org.bukkit.Bukkit;
import org.han.api.hooks.HeaderUpdaterBase;

public class HeaderUpdate extends HeaderUpdaterBase{

	@Override
	public int getPlayersOnline() {
		// TODO Auto-generated method stub
		return Bukkit.getOnlinePlayers().size();
	}

	@Override
	public int getMaxPlayers() {
		// TODO Auto-generated method stub
		return Bukkit.getMaxPlayers();
	}

	@Override
	public String Method() {
		// TODO Auto-generated method stub
		return Bukkit.getMotd();
	}

}
