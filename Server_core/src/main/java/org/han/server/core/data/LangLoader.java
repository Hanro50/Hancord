package org.han.server.core.data;

import java.io.IOException;
import java.util.Map;

import org.han.debug.Log;
import org.han.files.DisgotFileObj;
import org.han.files.FIleUtil;
import org.han.files.FileObj;


import com.google.gson.annotations.Expose;

public class LangLoader {
	@Expose
	Map<String, String> LangMap;

	public static LangLoader Load() {
		FileObj FO =  new DisgotFileObj("", "lang", "json");
		if (!FO.exists()) {
			try {
				Log.out("Copying internal language files into config files");

				String content = FIleUtil.readJar(LangLoader.class.getClassLoader(),"en_us.json","");
				FO.write(content);
				return FIleUtil.GsonInstance.fromJson("{\"LangMap\":" + content + "}", LangLoader.class);
			} catch (IOException e) {
				Log.trace(e);
			}
		} else {
			try {
				return FIleUtil.GsonInstance.fromJson("{\"LangMap\":" + FO.read("") + "}", LangLoader.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.trace(e);
			}
		}
		Log.trace(new Exception());
		
		Log.err("Could not load language support");
		return new LangLoader();
	}

	public String GetAdvancement_title(String AdvID) {
		return LangMap.getOrDefault("advancements." + AdvID.replace("/", ".") + ".title",
				AdvID.replaceAll("_", " ").split("/")[AdvID.replaceAll("_", " ").split("/").length - 1]);
	}

	public String GetAdvancement_Disc(String AdvID) {
		return LangMap.getOrDefault("advancements." + AdvID.replace("/", ".") + ".description", "Translation error");
	}

	public String GetAdvancement_Root_title(String type) {
		return LangMap.getOrDefault("advancements." + type + ".root.title", "Translation error");
	}

	public String GetAdvancement_Root_Disc(String type) {
		return LangMap.getOrDefault("advancements." + type + ".root.description", "Translation error");
	}

	public String GetAdvancement_toast() {
		return LangMap.getOrDefault("advancements.toast.task", "Advancement Made!");
	}

	public String GetDM_title(String DMID, boolean Checkplr, boolean item) {
		if (Checkplr && item) {
			String F = LangMap.get(DMID.toLowerCase() + ".player.item");
			if (F != null)
				return F;
		}

		if (item) {
			String F = LangMap.get(DMID.toLowerCase() + ".item");
			if (F != null)
				return F;
		}

		if (Checkplr) {
			String F = LangMap.get(DMID.toLowerCase() + ".player");
			if (F != null)
				return F;
		}
		return LangMap.getOrDefault(DMID, DMID);
	}

	public String GetEntity(String type) {
		return LangMap.getOrDefault(type.toLowerCase(), type);
	}

	public String StringJoinText() {
		return LangMap.getOrDefault("multiplayer.player.joined", "%s joined the game");

	}

	public String StringLeftText() {
		return LangMap.getOrDefault("multiplayer.player.left", "%s left the game");
	}

	public String StringJoinNetworkText() {
		return LangMap.getOrDefault("bungeecord.player.joined", "%s joined the network");

	}

	public String StringLeftNetworkText() {
		return LangMap.getOrDefault("bungeecord.player.left", "%s left the network");
	}

	public String StringadvancementtaskText() {
		return LangMap.getOrDefault("chat.type.advancement.task", "%s has made the advancement %s");
	}

	public String EnchantmentName(String Enchantment) {
		return LangMap.getOrDefault("enchantment.minecraft." + Enchantment, Enchantment);
	}

	public String EnchantmentLevel(int Enchlv) {
		return LangMap.getOrDefault("enchantment.level." + Enchlv, "" + Enchlv);
	}

	public String GetGameOver() {
		return LangMap.getOrDefault("deathScreen.title.hardcore", "Game over!");
	}

	public String EXPString() {
		return LangMap.getOrDefault("commands.experience.query.levels", "%s has %s experience levels");
	}
}