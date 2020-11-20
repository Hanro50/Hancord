package org.han.client.spigot.events;

import java.awt.Color;
import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.han.api.events.DiscordChatEvent;

public class AsyncDiscordChatEvent extends Event {
	private DiscordChatEvent event;
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public AsyncDiscordChatEvent(DiscordChatEvent event) {
		this(event, true);

	}

	public AsyncDiscordChatEvent(DiscordChatEvent event, boolean isAsync) {
		super(isAsync);
		this.event = event;

	}

	/**
	 * Is this person's discord account linked?
	 * 
	 * @return
	 */
	public boolean isLinked() {
		return event.isLinked();
	}

	/**
	 * Do {{@link #isLinked()} to check whether this is null or not.
	 * 
	 * @return
	 */

	public UUID getPlayer() {
		return event.getPlayer();
	}

	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return event.isCancelled();
	}

	public void setCancelled(boolean cancel) {
		event.setCancelled(cancel);
	}

	public String getFormat() {
		return event.getFormat();
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
		event.setFormat(format);
	}

	/**
	 * The discord username of the user
	 * 
	 * @return
	 */
	public String getName() {
		return event.getName();
	}

	/**
	 * The message from discord
	 * 
	 * @return
	 */
	public String getMessage() {
		return event.getMessage();
	}

	/**
	 * Set the message from discord
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		event.setMessage(message);
	}

	/**
	 * This will use the same logic as discord
	 * 
	 * @return
	 */
	public Color getTopRoleColor() {
		return event.getTopRoleColor();
	}

	/**
	 * The discord ID of the user
	 * 
	 * @return
	 */
	public long getDiscordID() {
		return event.getDiscordID();
	}

	public String getPfpURL() {
		return event.getPfpURL();
	}

}
