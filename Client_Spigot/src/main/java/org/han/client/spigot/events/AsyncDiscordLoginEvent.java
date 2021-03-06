package org.han.client.spigot.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.han.api.events.DiscordLoginEvent;

public class AsyncDiscordLoginEvent extends Event {

	private DiscordLoginEvent event;

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public AsyncDiscordLoginEvent(DiscordLoginEvent event, boolean isAsync) {
		super(isAsync);
		this.event = event;

	}

	public AsyncDiscordLoginEvent(DiscordLoginEvent event) {
		this(event, true);

	}

	public boolean isServer() {
		return event.isServer();
	}

}
