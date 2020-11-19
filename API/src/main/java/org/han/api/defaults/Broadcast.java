package org.han.api.defaults;

import org.han.api.types.BroadcastAPI;

public class Broadcast implements BroadcastAPI {
	private String header;
	private String content;
	private boolean isError = false;
	private long discordID;
	
	public Broadcast(long discordID, String header, String content) {
		this.header = header;
		this.content = content;
		this.discordID=discordID;
	}

	public Broadcast(long discordID,String header, String content, boolean isError) {
		this(discordID,header, content);
		this.isError = isError;
	}

	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return header;
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}
	@Override
	public boolean isError() {
		return isError;
	}

	@Override
	public long getDiscordID() {
		// TODO Auto-generated method stub
		return discordID;
	}

}
