package org.han.server.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.han.files.DisgotJsonObj;
import org.han.server.core.Printer;
import org.han.server.core.types.Msg;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.Expose;

public class AccountLink extends DisgotJsonObj {

	private static final long serialVersionUID = 1L;
	private static Random rand = new Random();

	AccountLink() {
		super("", "Linked Account", "json");

	}

	static AccountLink me;
	static {
		me = new AccountLink();
		me.makeLoaded();
	}

	@Expose
	Map<UUID, Long> MinecratfToDiscord = new HashMap<UUID, Long>();
	BiMap<UUID, Long> mapfrontmap = HashBiMap.create();
	static BiMap<UUID, String> LinkCode = HashBiMap.create();

	public static Set<UUID> getLinkedPlayers() {
		return me.MinecratfToDiscord.keySet();
	}

	public static UUID getUUID(Long DiscordID) {
		BiMap<UUID, Long> map = HashBiMap.create();
		map.putAll(me.MinecratfToDiscord);
		return map.inverse().get(DiscordID);
	}

	public static Long GetDiscordID(UUID playerID) {
		// TODO Auto-generated method stub
		return me.MinecratfToDiscord.get(playerID);
	}

	public static void completeLink(Msg run) {
		if (!LinkCode.inverse().containsKey(run.getText().trim())) {
			Printer.err(run, "Invalid code");
			return;
		}
		UUID out = LinkCode.inverse().get(run.getText());

		me.MinecratfToDiscord.remove(out);
		me.MinecratfToDiscord.put(out, run.getUser().getIdLong());
		me.save();
		Printer.suc(run, "Your account should now be linked!");
	}

	public static void unLink(UUID player, Consumer<String> out) {
		me.MinecratfToDiscord.remove(player);
		me.save();
		out.accept("Your account should no longer be linked!");
	}

	public static void startLink(UUID player, Consumer<String> out) {
		LinkCode.remove(player);
		String value = String.valueOf(rand.nextInt(90000) + 10000).trim();
		LinkCode.put(player, value);

		out.accept("DM the bot the following: \"!link " + value + "\" to link your accounts");
		// player.sendMessage("DM the bot the following: \"!link " + value + "\" to link
		// your accounts");
	}
}
