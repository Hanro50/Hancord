package org.han.api;

import java.util.HashSet;
import java.util.Set;

import org.han.api.defaults.Broadcast;
import org.han.debug.Log;

public interface PluginHook {
	public void disable();

	public void activate();

	/**
	 * A call for comment from a server to a client. This isn't used in
	 * implementations where the two are combined
	 * 
	 * @param key  The message key
	 * @param data The data
	 * @return If this hook executed
	 */
	public default boolean call(String key, String data) {
		return false;
	}

	static Set<PluginHook> hooks = new HashSet<PluginHook>();

	public static void create(Class<? extends PluginHook> PH, String HookName) {
		try {
			Log.out("Loading " + HookName);
			PluginHook pluginhook = PH.getConstructor().newInstance();
			hooks.add(pluginhook);
			pluginhook.activate();

		} catch (Throwable e) {
			Log.wrn("Could not load: " + HookName + " this can be ignored for the most part");
			Log.trace(e);

		}
	}

	public static void create(Class<? extends PluginHook> PH) {
		create(PH, PH.getName());
	}

	public static void callAll(String key, String data) {
		for (PluginHook hook : hooks) {
			if (hook.call(key, data))
				return;
		}
		BaseData.getPluginbase().getOutput()
				.broadcastMessage(new Broadcast("", "A required module is not loaded within the client", true));
	}

	public static void disableAll() {
		for (PluginHook PH : hooks) {
			PH.disable();
		}
	}

}
