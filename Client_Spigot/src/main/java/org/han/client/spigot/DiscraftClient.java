package org.han.client.spigot;

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
import org.han.client.spigot.events.AsyncDiscordChatEvent;
import org.han.client.spigot.events.AsyncDiscordLoginEvent;
import org.han.client.spigot.hook.HeaderUpdate;
import org.han.client.spigot.listeners.Advancementlistener;
import org.han.client.spigot.listeners.MessageListener;

import net.md_5.bungee.api.ChatColor;

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
	public void command(long DiscordID, UUID uuid, String message) {

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			DiscordPlayer DP;
			try {
				DP = new DiscordPlayer_new(Bukkit.getOfflinePlayer(uuid), DiscordID);
			} catch (NoClassDefFoundError e) {
				DP = new DiscordPlayer(Bukkit.getOfflinePlayer(uuid), DiscordID);
			}
			try {
				Bukkit.dispatchCommand(DP, message);
				DP.printInput();
			} catch (CommandException e) {
				getOutput().broadcastMessage(new Broadcast(DiscordID, "", "This command is incompatible", true));
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

	}

	public void Stop() {
		EventHandler.unsubscribe(this);
	}

	public abstract File getfile();

	public void onDiscordLogin(DiscordLoginEvent event) {
		Bukkit.getServer().getPluginManager().callEvent(new AsyncDiscordLoginEvent(event));
		PluginHook.create(HeaderUpdate.class);
	};

	public void onDiscordChatEvent(DiscordChatEvent event) {
		AsyncDiscordChatEvent ADCE = new AsyncDiscordChatEvent(event);

		Bukkit.getServer().getPluginManager().callEvent(new AsyncDiscordChatEvent(event));
		if (ADCE.isCancelled())
			return;
		try {
			String out = ChatColor.translateAlternateColorCodes('&', event.getFormat());

			Bukkit.getServer()
					.broadcastMessage(String.format(out,
							event.isLinked() ? BaseData.getPluginbase().getMCName(event.getPlayer()) : event.getName(), //
							event.getMessage(), //
							ChatColor.of(event.getTopRoleColor())));
		} catch (RuntimeException | java.lang.NoSuchMethodError e) {
			String out = org.bukkit.ChatColor.translateAlternateColorCodes('&', event.getFormat());
			Bukkit.getServer()
					.broadcastMessage(String.format(out,
							event.isLinked() ? BaseData.getPluginbase().getMCName(event.getPlayer()) : event.getName(), //
							event.getMessage(), //
							org.bukkit.ChatColor.AQUA));

			// org.bukkit.ChatColor;
		}

	};

}
