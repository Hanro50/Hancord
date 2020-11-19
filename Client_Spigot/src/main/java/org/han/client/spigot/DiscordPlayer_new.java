package org.han.client.spigot;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordPlayer_new extends DiscordPlayer{

	public DiscordPlayer_new(OfflinePlayer player, Long discordID) {
		super(player, discordID);
		// TODO Auto-generated constructor stub
	}
	public Spigot spigot() {		
		try {
			return new Spigot() {
				public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
					DiscordPlayer_new.this.sendMessage(component.toLegacyText());
				}

				/**
				 * Sends an array of components as a single message to the sender.
				 *
				 * @param components the components to send
				 */
				public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
					for (net.md_5.bungee.api.chat.BaseComponent com : components) {
						sendMessage(com);
					}
				}

				/**
				 * Sends this sender a chat component.
				 *
				 * @param component the components to send
				 * @param sender    the sender of the message
				 */
				public void sendMessage(@Nullable UUID sender,
						@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
					sendMessage(component);
				}

				/**
				 * Sends an array of components as a single message to the sender.
				 *
				 * @param components the components to send
				 * @param sender     the sender of the message
				 */
				public void sendMessage(@Nullable UUID sender,
						@NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
					sendMessage(components);
				}
			};
		} catch (Error e) {
			return null;
			// mainly for outdated versions of minecraft

		}
	}
}
