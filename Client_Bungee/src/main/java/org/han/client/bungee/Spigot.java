package org.han.client.bungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.han.api.PluginHook;
import org.han.api.events.DiscordChatEvent;
import org.han.api.events.DiscordLoginEvent;
import org.han.api.events.EventHandler;
import org.han.api.types.Jsonable;
import org.han.api.types.OutputHandlerAPI;
import org.han.api.types.RoleManagerAPI;
import org.han.client.spigot.DiscraftClient;
import org.han.debug.Log;

public class Spigot extends JavaPlugin implements PluginMessageListener, Plugin {

	public DataBundle bundle;
	public Client client;

	// Jsonable
	public void onEnable() {
		boolean isBungee = Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord", false);
		try {
		if (!isBungee) {
			Log.err("This is only for bungeecord servers. Please edit the configuration accordingly");
		}

		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, ChannelMappings.ClientoutChannel);
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, ChannelMappings.ServeroutChannel, this);
		if (client != null)
			client.Stop();
		client = new Client(this);
		}catch (Error e) {
			Log.err("Incompatible minecraft version");
			this.setEnabled(false);
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		Log.out("got message!");
		if (!channel.equals(ChannelMappings.ServeroutChannel)) {
			return;
		}

		final ByteArrayInputStream stream = new ByteArrayInputStream(message.clone());
		final DataInputStream in = new DataInputStream(stream);
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
			try {
				String key = in.readUTF();
				switch (key) {
				case ChannelMappings.databundle:
					bundle = Jsonable.decode(in.readUTF(), DataBundle.class);
					return;

				case ChannelMappings.DiscordChatEvent:
					Log.out("revieved message chat event");
					DiscordChatEvent DCE = Jsonable.decode(in.readUTF(), DiscordChatEvent.class);
					EventHandler.callEvent(DCE);
					return;

				case ChannelMappings.DiscordLoginEvent:
					EventHandler.callEvent(new DiscordLoginEvent(false));
					return;

				case ChannelMappings.PluginCall:
					PluginHook.callAll(in.readUTF(), in.readUTF());

				default:
					Log.out("unknown key:" + key);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public class Client extends DiscraftClient {

		public Client(JavaPlugin plugin) {
			super(plugin);
			if (client != null)
				client.Stop();
			PluginHook.create(DynMapHookCapsule.DynMapHook.class, "Dynmap client integration");
			// TODO Auto-generated constructor stub
		}

		@Override
		public void reload() {
			// TODO Auto-generated method stub

		}

		@Override
		public OutputHandlerAPI getOutput() {
			// TODO Auto-generated method stub
			return new ChannelMappings(Spigot.this);
		}

		@Override
		public RoleManagerAPI getRoleManager() {
			// TODO Auto-generated method stub
			return bundle;
		}

		@Override
		public String getIconURL() {
			// TODO Auto-generated method stub
			return bundle.getURL();
		}

		@Override
		public File getDataFile() {
			// TODO Auto-generated method stub
			return this.getDataFile();
		}

	}

	@Override
	public void SendMessage(String Channel, String json) {
		// TODO Auto-generated method stub
		Bukkit.getScheduler().runTask(this, () -> {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			try {
				out.writeUTF(Channel);
				out.writeUTF(json);
				this.getServer().sendPluginMessage(this, ChannelMappings.ClientoutChannel, stream.toByteArray());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

}
