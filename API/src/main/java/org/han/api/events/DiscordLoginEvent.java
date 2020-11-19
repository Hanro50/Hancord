package org.han.api.events;

public class DiscordLoginEvent {
	private boolean isServer;

	public DiscordLoginEvent(boolean isServer) {
		this.isServer = isServer;
	}

	public boolean isServer() {
		return isServer;
	}
}
