package org.han.client.bungee;

import org.han.api.BaseData;
import org.han.api.defaults.Broadcast;
import org.han.api.hooks.DynMapBaseHook;

public class DynMapHookCapsule {
	public static String DynMapPluginMessage = "dynmap";
	public static String getlink = "link";

	public static class DynMapHook extends DynMapBaseHook {

		public boolean call(String key, String data) {
			if (key.equals(DynMapPluginMessage)) {
				if (data.equals(getlink)) {
					String link = this.getLink();
					if (link == null) {
						BaseData.getPluginbase().getOutput().broadcastMessage(new Broadcast("", "could not get link",true));
					} else
						BaseData.getPluginbase().getOutput().broadcastMessage(new Broadcast("Here is the link", link));
					return true;
				}
			}
			return false;
		}

		@Override
		public void onDisable() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onEnable() {
			// TODO Auto-generated method stub

		}

	}

}
