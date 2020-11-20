package org.han.server.bungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.login.LoginException;

import org.han.api.BaseData;
import org.han.api.PluginHook;
import org.han.api.defaults.Advancement;
import org.han.api.defaults.Broadcast;
import org.han.api.defaults.DeathMessage;
import org.han.api.defaults.Message;
import org.han.api.events.DiscordChatEvent;
import org.han.api.events.DiscordListener;
import org.han.api.events.DiscordLoginEvent;
import org.han.api.events.EventHandler;
import org.han.api.types.Jsonable;
import org.han.client.bungee.DataBundle;
import org.han.debug.Log;
import org.han.server.core.DiscraftServerbase;
import org.han.server.core.Printer;
import org.han.server.core.types.FunctionalInterfaces.AccCheck;
import org.han.server.core.types.Msg;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import static org.han.client.bungee.ChannelMappings.*;

public class Boot extends Plugin implements DiscraftServerbase, Listener, DiscordListener {
	Data data;
	Timer timer;
public static ProxyServer serv;
	/**
	 * Called when this plugin is enabled.
	 */
	public void onEnable() {
		serv = this.getProxy();
		try {
			BaseData.setPluginbase(this);
			data = new Data(this);
			EventHandler.subscribe(this);
			this.getProxy().registerChannel(ServeroutChannel);
			this.getProxy().registerChannel(ClientoutChannel);
			this.getProxy().getPluginManager().registerListener(this, this);
			if (timer != null)
				timer.cancel();
			timer = new Timer();
			timer.schedule(new Update(), BaseData.miliInMinute, BaseData.miliInMinute * 10);
			timer.schedule(new KeepAlive(), BaseData.miliInMinute, BaseData.miliInMinute);
		} catch (LoginException e) {
			Log.out("Invalid discord token");
		}
		PluginHook.create(Luckperms.class, "Luck perms integration");
		PluginHook.create(Dynmap.class, "Dynmap server side integration");
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Commands.Link());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Commands.UnLink());
	}

	public void onDiscordLogin(DiscordLoginEvent event) {

		// TODO Auto-generated method stub
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			out.writeUTF(DiscordLoginEvent);
			for (ServerInfo server : getProxy().getServers().values()) {
				server.sendData(ServeroutChannel, stream.toByteArray());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onDiscordChatEvent(DiscordChatEvent event) {
		// new BiHashMap(getOutput().availableChannels());
		Log.out("sending chat event");
		BiMap<String, Long> map = HashBiMap.create();
		map.putAll(getOutput().availableChannels());
		String serv = map.inverse().get(event.getChannelID());
		if (serv != null) {
			Log.out("sending chat event to " + serv);

			ServerInfo info = this.getProxy().getServerInfo(serv);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			try {
				out.writeUTF(DiscordChatEvent);
				out.writeUTF(event.encode());
				info.sendData(ServeroutChannel, stream.toByteArray());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Called when this plugin is disabled.
	 */
	public void onDisable() {
		EventHandler.unsubscribe(this);
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	public UUID getUUID(String MCName) {
		// TODO Auto-generated method stub
		return getProxy().getPlayer(MCName).getUniqueId();
	}

	@Override
	public String getMCName(UUID uuid) {
		// TODO Auto-generated method stub
		return getProxy().getPlayer(uuid).getDisplayName();
	}

	@Override
	public void command(long DiscordID, UUID uuid, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public Field getUserStatus(Member usr, Guild guild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Field getGuildStatus(Msg run) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Field Settings(Msg run) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data getData() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void regComs() {
		// TODO Auto-generated method stub
		reg("set-channel", "Link a discord channel to a server", run -> {
			ServerInfo PS = getProxy().getServers().get(run.getText().trim());
			if (PS == null) {
				Printer.err(run, "invalid server");
				return;
			}
			getOutput().setChannel(PS.getName(), run.getChannel().getIdLong());
			Printer.suc(run, "Linked this channel to " + PS.getName());
		}).setGuild().setPerms(AccCheck.BotOwner, AccCheck.Trusted);
		reg("list-servers", "Link a discord channel to a server", run -> {
			String out = "";
			for (ServerInfo mcserver : getProxy().getServers().values()) {
				out += mcserver.getName() + " : " + mcserver.getMotd();
			}
			new Printer(run).setup("available servers:", out).Print(run);
			;
		}).setGuild();

	}

	@Override
	public File getDataFile() {
		// TODO Auto-generated method stub
		return getDataFolder();
	}

//ServerDisconnectEvent
	@net.md_5.bungee.event.EventHandler
	public void onPlayerConnect(ServerConnectEvent e) {
		getOutput().playerJoin(e.getTarget().getName(), e.getPlayer().getUniqueId());
	}

	@net.md_5.bungee.event.EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
		if (event != null && event.getPlayer() != null && event.getPlayer().getServer() != null
				&& event.getPlayer().getServer().getInfo() != null)
			getOutput().playerLeft(event.getPlayer().getServer().getInfo().getName(), event.getPlayer().getUniqueId());
	}

	@net.md_5.bungee.event.EventHandler
	public void onPluginMessage(PluginMessageEvent ev) {
		if (!ev.getTag().equals(ClientoutChannel)) {
			return;
		}

		if (!(ev.getSender() instanceof Server)) {
			Log.out(ev.getSender().getClass().getName());
			return;
		}
		Server serv = (Server) ev.getSender();
		ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
		DataInputStream in = new DataInputStream(stream);
		try {
			String key = in.readUTF();
			Log.wrn("Got key :" + key);
			switch (key) {
			case advancement:
				getOutput().advancement(serv.getInfo().getName(), Jsonable.decode(in.readUTF(), Advancement.class));
				return;
			case broadcastMessage:
				getOutput().broadcastMessage(serv.getInfo().getName(), Jsonable.decode(in.readUTF(), Broadcast.class));
				return;
			case deathMessage:
				getOutput().deathMessage(serv.getInfo().getName(), Jsonable.decode(in.readUTF(), DeathMessage.class));
				return;
			case sendMessage:
				getOutput().sendMessage(serv.getInfo().getName(), Jsonable.decode(in.readUTF(), Message.class));
				return;
			case updateTopic:
				getOutput().updateTopic(serv.getInfo().getName(), in.readUTF());
				return;
			case serverStateUpdate:
				getOutput().serverStateUpdate(serv.getInfo().getName(), in.readUTF().equals("true"));
				// TODO Auto-generated method stub
				ByteArrayOutputStream stateoutstream = new ByteArrayOutputStream();
				DataOutputStream stateout = new DataOutputStream(stateoutstream);

				try {
					stateout.writeUTF(databundle);
					stateout.writeUTF(new DataBundle(getRoleManager(), getIconURL()).encode());
					serv.getInfo().sendData(ServeroutChannel, stateoutstream.toByteArray(),true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			default:
				Log.wrn("Unknown key:" + key);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class Update extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			try {
				out.writeUTF(databundle);
				out.writeUTF(new DataBundle(getRoleManager(), getIconURL()).encode());
				for (ServerInfo server : getProxy().getServers().values()) {
					server.sendData(ServeroutChannel, stream.toByteArray(),true);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class KeepAlive extends TimerTask {
		Map<String, Boolean> Alive = new ConcurrentHashMap<String, Boolean>();

		@Override
		public void run() {
			for (ServerInfo server : getProxy().getServers().values()) {

				final String name = server.getName();

				server.ping(new Callback<ServerPing>() {

					@Override
					public void done(ServerPing result, Throwable error) {
						if (!Alive.containsKey(name)) {
							Alive.put(name, false);
						}

						if (error == null && !Alive.get(name)) {
							getOutput().serverStateUpdate(name, true);
							Alive.remove(name);
							Alive.put(name, true);
						} else if (error != null && Alive.get(name)) {
							getOutput().serverStateUpdate(name, false);
							Alive.remove(name);
							Alive.put(name, false);
						}
					}
				});
			}
		}
	}
}
