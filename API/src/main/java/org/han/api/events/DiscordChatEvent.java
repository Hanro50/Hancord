package org.han.api.events;

import java.awt.Color;
import java.util.UUID;

public class DiscordChatEvent implements DiscordEvent {

	private boolean Linked;
	private UUID player;
	private long DiscordID;
	private Color TopRoleColor;
	private String name;
	private String pfpURL;
	private String Message;

	boolean isCancelled = false;
	private String format = "&9[%3$sDiscord&9]&f <%1$s> %2$s";

	public DiscordChatEvent(UUID player, long DiscordID, Color TopRoleColor, String name, String pfpURL,
			String Message) {
		this.Linked = player != null;
		this.player = player;
		this.DiscordID = DiscordID;
		this.TopRoleColor = TopRoleColor;
		this.name = name;
		this.pfpURL = pfpURL;
		this.Message = Message;
	}

	/**
	 * Is this person's discord account linked?
	 * 
	 * @return
	 */
	public boolean isLinked() {
		return Linked;
	}

	/**
	 * Do {{@link #isLinked()} to check whether this is null or not.
	 * 
	 * @return
	 */

	public UUID getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return isCancelled;
	}

	public void setCancelled(boolean cancel) {
		isCancelled = cancel;
	}

	public String getFormat() {
		return format;
	}

	/**
	 * *
	 * <h2>The chat output format</h2><br/>
	 * %1$s -> Username<br/>
	 * %2$s -> Message<br/>
	 * %3$s -> The role color<br/>
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * The discord username of the user
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * The message from discord
	 * 
	 * @return
	 */
	public String getMessage() {
		return Message;
	}

	/**
	 * Set the message from discord
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		Message = message;
	}

	/**
	 * This will use the same logic as discord
	 * 
	 * @return
	 */
	public Color getTopRoleColor() {
		return TopRoleColor;
	}

	/**
	 * The discord ID of the user
	 * 
	 * @return
	 */
	public long getDiscordID() {
		return DiscordID;
	}

	public String getPfpURL() {
		return pfpURL;
	}

}
