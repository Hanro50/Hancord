package org.han.client.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.han.api.BaseData;
import org.han.api.types.AdvancementAPI;

public class Advancementlistener implements Listener {
	@EventHandler
	void PlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
		if (e.getAdvancement().getKey().getKey().split("/").length < 1
				|| e.getAdvancement().getKey().getKey().split("/")[0].equalsIgnoreCase("recipes")) {
			return;
		}
		AdvancementAPI A = new org.han.api.defaults.Advancement(e.getPlayer().getUniqueId(),
				e.getAdvancement().getKey().getKey());

		BaseData.getPluginbase().getOutput().advancement(A);

	}

}
