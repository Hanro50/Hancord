package org.han.client.spigot;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.han.api.BaseData;
import org.han.api.defaults.Broadcast;
import org.han.debug.Log;

public class DiscordPlayer implements CommandSender, AnimalTamer {
	protected final PermissibleBase perm;
	private final OfflinePlayer player;

	private String out = "";

	public DiscordPlayer(OfflinePlayer player) {
		this.player = player;
		perm = new PermissibleBase(this);
	}

	@Override
	public boolean isPermissionSet(String name) {
		return perm.isPermissionSet(name);

	}

	@Override
	public boolean isPermissionSet(org.bukkit.permissions.Permission perm) {
		return this.perm.isPermissionSet(perm);
	}

	@Override
	public boolean hasPermission(String name) {
		return perm.hasPermission(name);
	}

	@Override
	public boolean hasPermission(org.bukkit.permissions.Permission perm) {
		return this.perm.hasPermission(perm);

		// TODO Auto-generated method stub
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return perm.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return perm.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return perm.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return perm.addAttachment(plugin, ticks);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		perm.removeAttachment(attachment);

	}

	@Override
	public void recalculatePermissions() {
		perm.recalculatePermissions();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return perm.getEffectivePermissions();
	}

	@Override
	public boolean isOp() {
		return player.isOp();
	}

	@Override
	public void setOp(boolean value) {
		Log.out("t");
		player.setOp(value);

	}

	@Override
	public void sendMessage(String message) {
		out += message + "\n";
	}

	@Override
	public void sendMessage(String[] messages) {
		String out = "";
		for (String line : messages) {
			out += "\n" + line;
		}
		sendMessage(out.trim());
	}

	@Override
	public Server getServer() {
		// TODO Auto-generated method stub
		return Bukkit.getServer();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return player.getName();
	}

	@Override
	public UUID getUniqueId() {
		// TODO Auto-generated method stub
		return player.getUniqueId();
	}

	public void printInput() {
		out = ChatColor.stripColor(out).trim();
		Log.out("broadcasting message");
		if (out.length() < 1) {
			BaseData.getPluginbase().getOutput()
					.broadcastMessage(new Broadcast("", "Got no console respone", true));

		} else if (out.length() < 2000) {
			BaseData.getPluginbase().getOutput()
					.broadcastMessage(new Broadcast("Got the following respose!", out));

		} else {
			BaseData.getPluginbase().getOutput().broadcastMessage(new Broadcast( "Error",
					"Respone was way to long to show here. Rather run this ingame", true));
		}
	}

	@Override
	public void sendMessage(UUID sender, String message) {
		out += message + "\n";

	}

	@Override
	public void sendMessage(UUID sender, String[] messages) {
		sendMessage(messages);

	}

	public Spigot spigot() {
		return null;
	}

}
