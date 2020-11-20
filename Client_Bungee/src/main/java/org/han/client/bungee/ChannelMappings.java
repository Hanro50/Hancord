package org.han.client.bungee;

import java.util.Map;
import java.util.UUID;

import org.han.api.types.AdvancementAPI;
import org.han.api.types.BroadcastAPI;
import org.han.api.types.DeathMessageAPI;
import org.han.api.types.MessageAPI;
import org.han.api.types.OutputHandlerAPI;

public class ChannelMappings implements OutputHandlerAPI {

	public final static String ClientoutChannel = "discraft:client";
	public final static String ServeroutChannel = "discraft:server";
	public final static String databundle = "bundle";

	public final static String DiscordChatEvent = "discordchatevent";
	public final static String DiscordLoginEvent = "discordloginevent";
	
	public final static String PluginCall = "plugincall";
	Plugin mapping;

	public ChannelMappings(Plugin mapping) {
		this.mapping = mapping;
	}

	public final static String advancement = "advancement";

	@Override
	public void advancement(String ChannelName, AdvancementAPI advance) {
		mapping.SendMessage(advancement, advance.encode());
	}

	public final static String deathMessage = "deathmessage";

	@Override
	public void deathMessage(String ChannelName, DeathMessageAPI dm) {
		mapping.SendMessage(deathMessage, dm.encode());
	}

	public final static String sendMessage = "sendmessage";

	@Override
	public void sendMessage(String ChannelName, MessageAPI message) {
		mapping.SendMessage(sendMessage, message.encode());
	}

	public final static String broadcastMessage = "broadcastmessage";

	@Override
	public void broadcastMessage(String ChannelName, BroadcastAPI message) {
		mapping.SendMessage(broadcastMessage, message.encode());
	}

	public final static String updateTopic = "updatetopic";

	@Override
	public void updateTopic(String ChannelName, String newHeader) {
		mapping.SendMessage(updateTopic, newHeader);
	}

	public final static String serverStateUpdate = "serverstateupdate";

	@Override
	public void serverStateUpdate(String ChannelName, boolean startup) {
		mapping.SendMessage(updateTopic, startup ? "true" : "false");
	}

	// not used in client
	@Override
	public Map<String, Long> availableChannels() {
		// TODO Auto-generated method stub
		return null;
	}

	// not used in client
	@Override
	public Long getDefaultchannel() {
		// TODO Auto-generated method stub
		return null;
	}

	// not used in client
	@Override
	public void setChannel(String ChannelName, long id) {
		// TODO Auto-generated method stub
	}

	// not used in client because couldn't get it to work
	@Override
	public void playerJoin(String ChannelName, UUID player) {

	}

	// not used in client because couldn't get it to work
	@Override
	public void playerLeft(String ChannelName, UUID player) {

	}
}
