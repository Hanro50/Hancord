package org.han.api;

import java.util.HashSet;
import java.util.Set;

import org.han.debug.Log;

public interface PluginHook {
	public void disable();

	public void activate();

	static Set<PluginHook> hooks = new HashSet<PluginHook>();

	public static void create(Class<? extends PluginHook> PH, String HookName) {
		try {
			Log.out("Loading "+HookName);
			PluginHook pluginhook = PH.getConstructor().newInstance();
			hooks.add(pluginhook);
			pluginhook.activate();

		} catch (Throwable e) {
			Log.wrn("Could not load: " + HookName + " this can be ignored for the most part");
			Log.trace(e);
			
		}
	}
	
	public static void create(Class<? extends PluginHook> PH) {
		create(PH,PH.getName());
	}

	public static void disableAll() {
		for (PluginHook PH : hooks) {
			PH.disable();
		}
	}

	
}
