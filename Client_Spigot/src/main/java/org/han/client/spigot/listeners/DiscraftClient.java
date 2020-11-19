package org.han.client.spigot.listeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.plugin.java.JavaPlugin;
import org.han.api.BaseData;
import org.han.api.DiscraftBaseAPI;
import org.han.api.PluginHook;
import org.han.api.defaults.Broadcast;
import org.han.api.events.DiscordChatEvent;
import org.han.api.events.DiscordListener;
import org.han.api.events.DiscordLoginEvent;
import org.han.api.events.EventHandler;
import org.han.client.spigot.DiscordPlayer;
import org.han.client.spigot.events.AsyncDiscordChatEvent;
import org.han.client.spigot.events.AsyncDiscordLoginEvent;
import org.han.client.spigot.hook.HeaderUpdate;

public abstract class DiscraftClient implements DiscordListener, DiscraftBaseAPI {
	JavaPlugin plugin;

	@Deprecated
	public UUID getUUID(String MCName) {
		return Bukkit.getOfflinePlayer(MCName).getUniqueId();

	}

	public String getMCName(UUID uuid) {
		return Bukkit.getOfflinePlayer(uuid).getName();
	}
	@Override
	public void command(long DiscordID,UUID uuid, String message) {

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,

				() -> {
					DiscordPlayer DP = new DiscordPlayer(Bukkit.getOfflinePlayer(uuid),DiscordID);
					try {
						Bukkit.dispatchCommand(DP, message);
						DP.printInput();
					} catch (CommandException e) {
						getOutput().broadcastMessage(new Broadcast(DiscordID,"", "This command is incompatible", true));
					}

				}, 1L);
	}

	public DiscraftClient(JavaPlugin plugin) {
		BaseData.setDataFile(getfile());
		BaseData.setPluginbase(this);
		this.plugin = plugin;
		EventHandler.subscribe(this);
		plugin.getServer().getPluginManager().registerEvents(new MessageListener(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new Advancementlistener(), plugin);// "org.dynmap.DynmapWebChatEvent"
		PluginHook.create(HeaderUpdate.class);

	}

	public void Stop() {
		EventHandler.unsubscribe(this);
	}
	
	public abstract File getfile();

	public void onDiscordLogin(DiscordLoginEvent event) {
		Bukkit.getServer().getPluginManager().callEvent(new AsyncDiscordLoginEvent(event));
	};

	public void onDiscordChatEvent(DiscordChatEvent event) {
		Bukkit.getServer().getPluginManager().callEvent(new AsyncDiscordChatEvent(event));
	};

}
