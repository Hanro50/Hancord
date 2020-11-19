package org.han.api.types;

import java.util.Map;
import java.util.UUID;

public interface OutputHandlerAPI {
	public static final String DEFAULT = "default";

	public void setChannel(String ChannelName, long id);
	
	public void advancement(String ChannelName, AdvancementAPI advance);

	public void deathMessage(String ChannelName, DeathMessageAPI dm);

	public void sendMessage(String ChannelName, MessageAPI message);

	public void broadcastMessage(String ChannelName, BroadcastAPI message);

	public void playerJoin(String ChannelName, UUID player);

	public void playerLeft(String ChannelName, UUID player);
	
	public void updateTopic(String ChannelName, String newHeader);
	
	public void serverStateUpdate(String ChannelName, boolean startup);

	public Map<String, Long> availableChannels();

	public default boolean isDefault(String channel) {
		return DEFAULT.equals(channel);
	}
	
	public default void advancement(AdvancementAPI advance) {
		advancement(DEFAULT, advance);
	}

	public default void deathMessage(DeathMessageAPI dm) {
		deathMessage(DEFAULT, dm);
	}

	public default void sendMessage(MessageAPI message) {
		sendMessage(DEFAULT, message);
	}

	public default void broadcastMessage(BroadcastAPI message) {
		broadcastMessage(DEFAULT, message);
	}

	public default void playerJoin(UUID player) {
		playerJoin(DEFAULT, player);
	}

	public default void playerLeft(UUID player) {
		playerLeft(DEFAULT, player);
	}
	public default void updateTopic( String newHeader) {
		updateTopic(DEFAULT, newHeader);
	}
	
	public default void serverStateUpdate(boolean startup) {
		serverStateUpdate(DEFAULT, startup);
	}
	
	public default void setChannel(long id) {
		setChannel(DEFAULT,id);
	}
	
	public Long getDefaultchannel();
}
