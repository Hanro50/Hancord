package org.han.api.defaults;

import org.han.api.types.BroadcastAPI;

public class Broadcast implements BroadcastAPI {
	private String header;
	private String content;
	private boolean isError = false;

	
	public Broadcast(String header, String content) {
		this.header = header;
		this.content = content;

	}

	public Broadcast(String header, String content, boolean isError) {
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
	public long getCreationdate() {
		// TODO Auto-generated method stub
		return 0;
	}

}
