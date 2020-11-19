package org.han.api.hooks;

import java.util.Timer;
import java.util.TimerTask;

import org.han.api.BaseData;
import org.han.api.PluginHook;

public abstract class HeaderUpdaterBase extends TimerTask implements PluginHook {
	static Timer timer;

	@Override
	public void run() {
		BaseData.getPluginbase().getOutput()
				.updateTopic(getPlayersOnline() + "/" + getMaxPlayers() + " Players online\n" + Method());
	}

	@Override
	public void disable() {
		timer.cancel();
		BaseData.getPluginbase().getOutput().serverStateUpdate(false);
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		BaseData.getPluginbase().getOutput().serverStateUpdate(true);
		timer.schedule(this, 1000, BaseData.miliInMinute * 10);
	}

	public abstract int getPlayersOnline();

	public abstract int getMaxPlayers();

	public abstract String Method();
}
