package org.han.server.bungee;

import org.han.server.core.data.AccountLink;

import net.md_5.bungee.api.CommandSender;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Commands {
	public static class Link extends Command {
		public Link() {
			super("link", null, "db:link");
		}

		@Override
		public void execute(CommandSender sender, String[] args) {
			if (sender instanceof ProxiedPlayer) {
				AccountLink.startLink(((ProxiedPlayer) sender).getUniqueId(), f -> {
					TextComponent T = new TextComponent();
					T.setText(f);
					sender.sendMessage(T);
				});
			}

		}
	}

	public static class UnLink extends Command {
		public UnLink() {
			super("unlink", null, "db:unlink");
		}

		@Override
		public void execute(CommandSender sender, String[] args) {
			if (sender instanceof ProxiedPlayer) {
				AccountLink.unLink(((ProxiedPlayer) sender).getUniqueId(), f -> {
					TextComponent T = new TextComponent();
					T.setText(f);
					sender.sendMessage(T);
				});
			}

		}
	}

}
