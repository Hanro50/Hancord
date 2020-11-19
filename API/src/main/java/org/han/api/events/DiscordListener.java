package org.han.api.events;

public interface DiscordListener {
	public default void onDiscordEvent(DiscordEvent event){};
	public default void onDiscordLogin(DiscordLoginEvent event){};
	public default void onDiscordChatEvent(DiscordChatEvent event){};
}
