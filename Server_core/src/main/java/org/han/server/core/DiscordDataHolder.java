package org.han.server.core;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.han.server.core.modules.All;
import org.han.server.core.modules.CommonCom;
import org.han.server.core.types.ComBase;


public final class DiscordDataHolder {

	public static final Random rand = new Random(System.currentTimeMillis() * System.nanoTime());

	public static Set<ComBase> CB = new HashSet<ComBase>();// { };
	static {
		ComBase[] c = { new All(), new CommonCom()};
		for (ComBase a : c) {
			CB.add(a);
		}
	}

	public static final void invoke() {
	}
}
