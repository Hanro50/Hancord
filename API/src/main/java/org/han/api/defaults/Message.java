package org.han.api.defaults;

import java.util.UUID;

import org.han.api.types.MessageAPI;

public class Message implements MessageAPI {
	private UUID player;
	private String name;
	private String AvatarURL;
	private String message;
	private boolean Linked;
	private long Creationdate = System.nanoTime();

	public Message(UUID player, String message) {
		this.player = player;
		this.message = message;

	}

	public Message(String name, String AvatarURL, String message) {
		this.name = name;
		this.AvatarURL = AvatarURL;
		this.message = message;

	}

	@Override
	public UUID getPlayer() {
		// TODO Auto-generated method stub
		return player;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getAvatarURL() {
		// TODO Auto-generated method stub
		return AvatarURL;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

	@Override
	public boolean isLinked() {
		// TODO Auto-generated method stub
		return Linked;
	}

	@Override
	public long getCreationdate() {
		// TODO Auto-generated method stub
		return Creationdate;
	}

}
