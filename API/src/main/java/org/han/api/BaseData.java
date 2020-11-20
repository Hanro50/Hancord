package org.han.api;

import org.han.files.FIleUtil;

public class BaseData {
	public static final long miliInDay = 86400000;
	public static final long miliInHour = 3600000;
	public static final long miliInMinute = 60000;
	
	private static DiscraftBaseAPI pluginbase;

	public static FIleUtil getFU() {
		return new FIleUtil(getPluginbase().getDataFile());
	}

	public static DiscraftBaseAPI getPluginbase() {
		return pluginbase;
	}
	
	

	public static void setPluginbase(DiscraftBaseAPI pluginbase) {
		if (BaseData.pluginbase == null)
			BaseData.pluginbase = pluginbase;
	}

	public static String Discordcomchars = "!";

}
