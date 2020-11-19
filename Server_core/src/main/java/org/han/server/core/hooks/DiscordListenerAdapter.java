package org.han.server.core.hooks;

import java.awt.Color;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.han.api.BaseData;
import org.han.api.events.DiscordChatEvent;
import org.han.api.events.DiscordLoginEvent;
import org.han.api.events.EventHandler;
import org.han.debug.Log;
import org.han.server.core.data.AccountLink;
import org.han.server.core.types.MsgGenericMessage;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

public class DiscordListenerAdapter extends ListenerAdapter {
	public final ShardManager SH;
	public boolean close = false;

	public DiscordListenerAdapter(ShardManager SH) {
		this.SH = SH;
	}

	public void onReady(@Nonnull ReadyEvent event) {
		Log.out(SH.retrieveApplicationInfo().complete().getInviteUrl(Permission.ADMINISTRATOR));
		EventHandler.callEvent(new DiscordLoginEvent(true));
	}

//UsrData
	// AsyncDiscordLoginEvent
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage())
			return;
		new MsgGenericMessage(event);
	}

	// Bukkit.getServer().getPluginManager().callEvent(
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

		if (!BaseData.getPluginbase().getOutput().availableChannels().values().contains(event.getChannel().getIdLong())
				|| event.getMessage().getContentRaw().startsWith(BaseData.Discordcomchars)) {
			return;
			// BaseData.Discordcomchars
		}
		Color color = Color.WHITE;
		if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage())
			return;
		UUID uuid = AccountLink.getUUID(event.getAuthor().getIdLong());

		for (Role role : event.getMember().getRoles()) {
			if (role.getColor() != null) {
				color = role.getColor();
				break;
			}
		}
		EventHandler.callEvent(
				new DiscordChatEvent(uuid, event.getAuthor().getIdLong(), color, event.getMember().getEffectiveName(),
						event.getAuthor().getAvatarUrl(), event.getMessage().getContentDisplay()));

	}
}
