package org.han.api.types;

public interface BroadcastAPI {

	public String getContent();

	public String getHeader();
	
	public long getDiscordID();
	
	public boolean isError() ;
}
