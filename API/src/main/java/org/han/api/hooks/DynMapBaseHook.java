package org.han.api.hooks;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.function.Consumer;

import org.dynmap.DynmapAPI;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.han.api.BaseData;
import org.han.api.IPGrabber;
import org.han.api.PluginHook;
import org.han.api.defaults.Message;
import org.han.api.events.DiscordChatEvent;
import org.han.api.events.DiscordListener;
import org.han.api.events.EventHandler;

/**
 * Dear Dynmap Devs, if you ever read this.... Please update to using UUID
 * instead of names...
 * 
 * @author hanro
 *
 */
public abstract class DynMapBaseHook implements DiscordListener, PluginHook {
	DynmapCommonAPI dynmap;
	Object Listener;

	public class DynmapListener extends DynmapCommonAPIListener {
		@Override
		public void apiEnabled(DynmapCommonAPI arg0) {
			dynmap = arg0;
		}

		@Override
		public void apiDisabled(DynmapCommonAPI api) {
			dynmap = null;
		}

		@Override
		public boolean webChatEvent(String source, String name, String message) {
			try {
				@SuppressWarnings("deprecation")
				// You could have given me the UUID code DynMap team. Just...saying
				UUID uuid = BaseData.getPluginbase().getUUID(name);
				BaseData.getPluginbase().getOutput().sendMessage(new Message(uuid, message));
				return true;
			} catch (IllegalArgumentException e) {
			}
			BaseData.getPluginbase().getOutput().sendMessage(new Message(name,
					"https://styles.redditmedia.com/t5_2kl3ct/styles/communityIcon_am5zopqnjhs41.png?width=256&s=90af1296b864fc4e559a6a85a20a5f45fd1478f1",
					message));
			return true;
		}

	}

	@Override
	public void onDiscordChatEvent(DiscordChatEvent event) {
		if (event.isLinked()) {
			String name = BaseData.getPluginbase().getMCName(event.getPlayer());
			dynmap.postPlayerMessageToWeb(name, name, event.getMessage());
		} else {
			dynmap.sendBroadcastToWeb(event.getName(), event.getMessage());
		}

	}

	@Override
	public final void disable() {
		EventHandler.unsubscribe(this);
		DynmapCommonAPIListener.unregister((DynmapCommonAPIListener) Listener);
		onDisable();
	}

	@Override
	public final void activate() {
		if (Listener !=null) 
			DynmapCommonAPIListener.unregister((DynmapCommonAPIListener) Listener);
		Listener = new DynmapListener();
		DynmapCommonAPIListener.register((DynmapCommonAPIListener) Listener);
		EventHandler.subscribe(this);
		onEnable();
	};

	public abstract void onDisable();

	public abstract void onEnable();

	protected String getLink() {
		// Get DynmapCore java.lang.reflect.
		try {
			Object core = dynmap;
			if (dynmap instanceof DynmapAPI) {
				Field corecorefield = dynmap.getClass().getDeclaredField("core");
				boolean access = corecorefield.isAccessible();
				corecorefield.setAccessible(true);
				core = corecorefield.get(dynmap);
				corecorefield.setAccessible(access);
			}

			// Get configuration o
			Field configfield = core.getClass().getField("configuration");
			Object config = configfield.get(core);

			// get webhook
			int webport = (int) config.getClass().getMethod("getInteger", String.class, int.class).invoke(config,
					"webserver-port", 8123);
			return "http://" + IPGrabber.getIP() + ":" + webport + "/";
		} catch (RuntimeException | IllegalAccessException | InvocationTargetException | NoSuchMethodException
				| NoSuchFieldException | IOException e) {

		}
		return null;
	}

	protected void ToggleVisable(UUID uuid, Consumer<String> out) {
		String OP = BaseData.getPluginbase().getMCName(uuid);
		boolean visable;
		dynmap.setPlayerVisiblity(OP, visable = !dynmap.getPlayerVisbility(OP));
		out.accept(visable ? "You are now visible" : "You are no longer visible");
	}

	/*
	 * @Override public void onActivate() { // TODO Auto-generated method stub //
	 * Bukkit.getServer().getPluginManager().registerEvents(this, JPin);
	 * reg("get-link", "Get a link to the dynmap page", run -> { // configuration
	 * try { // Get DynmapCore java.lang.reflect.Field corecorefield =
	 * dynmap.getClass().getDeclaredField("core"); boolean access =
	 * corecorefield.isAccessible(); corecorefield.setAccessible(true); Object core
	 * = corecorefield.get(dynmap); corecorefield.setAccessible(access);
	 * 
	 * // Get configuration object java.lang.reflect.Field configfield =
	 * core.getClass().getField("configuration"); Object config =
	 * configfield.get(core);
	 * 
	 * // get webhook int webport = (int) config.getClass().getMethod("getInteger",
	 * String.class, int.class).invoke(config, "webserver-port", 8123); String link
	 * = "http://" + IPGrabber.getIP() + ":" + webport + "/";
	 * 
	 * // Print webhook new Printer(run).setup("Dynmap link", "[Click me!](" + link
	 * + ")").Print(run);
	 * 
	 * } catch (RuntimeException | NoSuchFieldException | IllegalAccessException |
	 * InvocationTargetException | NoSuchMethodException | IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); Printer.err(run,
	 * "Internal exception."); } });
	 * 
	 * reg("toggle-hide", "Hide yourself on Dynmap", run -> { UUID uuid =
	 * AccountLink.getUUID(run.getUser().getIdLong()); if (uuid == null) {
	 * Printer.err(run, "Please link your account so I can attempt to hide you");
	 * return; } String OP = Bukkit.getOfflinePlayer(uuid).getName();
	 * dynmap.setPlayerVisiblity(OP, dynmap.getPlayerVisbility(OP));
	 * 
	 * Printer.suc(run, dynmap.getPlayerVisbility(OP) ? "You are now visible" :
	 * "You are no longer visible"); }); }
	 * 
	 * @Override public void disable() { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void activate(DisgotBaseAPI<?> JPin) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */
}
