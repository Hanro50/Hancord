package org.han.api;

import java.io.File;
import java.util.UUID;

import org.han.api.types.OutputHandlerAPI;
import org.han.api.types.RoleManagerAPI;

public interface DiscraftBaseAPI {

	public void reload();

	public OutputHandlerAPI getOutput();

	@Deprecated
	public UUID getUUID(String MCName);

	public String getMCName(UUID uuid);

	public void command(long DiscordID, UUID uuid, String message);

	public RoleManagerAPI getRoleManager();

	public String getIconURL();

	public File getDataFile();
}
