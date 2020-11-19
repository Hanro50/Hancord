package org.han.server.core.data;

import org.han.server.core.DiscraftServerbase;

public class ServerData {
	private static DiscraftServerbase server;

	public static DiscraftServerbase getServer() {
		return server;
	}

	public static void setServer(DiscraftServerbase server) {
		if (ServerData.server == null)
			ServerData.server = server;
	}
	

}
