package org.han.server.core;

import org.han.api.PluginHook;
import org.han.server.core.types.ComBase;

public abstract class PluginComObj implements PluginHook, ComBase {

	public final void disable() {
		DiscordDataHolder.CB.add(this);
		onDisable();
	}

	public final void activate() {
		DiscordDataHolder.CB.remove(this);
		onActivate();
	}

	protected abstract void onDisable();

	protected abstract void onActivate();

}
//