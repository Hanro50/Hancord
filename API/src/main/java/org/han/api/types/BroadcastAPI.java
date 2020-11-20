package org.han.api.types;

public interface BroadcastAPI extends BaseAPIObject {

	public String getContent();

	public String getHeader();
	
	public boolean isError() ;
}
