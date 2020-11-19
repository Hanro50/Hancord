package org.han.api.events;

import java.util.ArrayList;
import java.util.List;

public final class EventHandler {
	private static List<DiscordListener> eventListeners = new ArrayList<DiscordListener>();

	public static void callEvent(DiscordEvent event) {
		for (DiscordListener listener : eventListeners)
			listener.onDiscordEvent(event);
		if (event instanceof DiscordLoginEvent) {
			for (DiscordListener listener : eventListeners)
				listener.onDiscordLogin((DiscordLoginEvent) event);
		}
		if (event instanceof DiscordChatEvent) {
			for (DiscordListener listener : eventListeners)
				listener.onDiscordChatEvent((DiscordChatEvent) event);
		}

	}
	
	public static void subscribe(DiscordListener listener) {
		eventListeners.remove(listener);
		eventListeners.add(listener);
	}
	
	public static void unsubscribe(DiscordListener listener) {
		eventListeners.remove(listener);
	}

}
