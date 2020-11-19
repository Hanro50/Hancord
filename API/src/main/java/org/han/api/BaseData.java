package org.han.api;

import java.io.File;

import org.han.debug.Log;
import org.han.files.FIleUtil;

public class BaseData {
	public static final long miliInDay = 86400000;
	public static final long miliInHour = 3600000;
	public static final long miliInMinute = 60000;
	
	private static File dataFile;
	private static DiscraftBaseAPI pluginbase;

	public static FIleUtil getFU() {
		return new FIleUtil(dataFile);
	}

	public static void setDataFile(File dataFile) {
		if (BaseData.dataFile == null && dataFile != null) {
			BaseData.dataFile = dataFile;
			Log.out("set data file to " + dataFile.getPath());
		} else if (dataFile == null) {
			Log.trace(new Exception("Nullpointer averted. Please FIX ME"));

		} else {
			Log.wrn("2 data set events?!");
		}

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
