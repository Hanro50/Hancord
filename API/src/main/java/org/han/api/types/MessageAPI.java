package org.han.api.types;

import java.util.UUID;



public interface MessageAPI extends BaseAPIObject {
	public UUID getPlayer();
	public String getName();
	public String getAvatarURL();
	public String getMessage();
	public boolean isLinked();
}
