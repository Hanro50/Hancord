package org.han.server.spigot;

import java.util.UUID;

import org.han.api.hooks.DynMapBaseHook;
import org.han.server.core.DiscordDataHolder;
import org.han.server.core.Printer;
import org.han.server.core.data.AccountLink;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.Msg;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class Dynmap extends DynMapBaseHook implements ComBase {

	@Override
	public void onDisable() {
		DiscordDataHolder.CB.remove(this);
	}

	@Override
	public void onEnable() {
		DiscordDataHolder.CB.add(this);

		reg("get-link", "Get a link to the dynmap page", run -> {
			String link = this.getLink();
			if (link != null)
				new Printer(run).setup("Dynmap link", "[Click me!](" + link + ")").Print(run);
			else
				Printer.err(run, "Could not get link");
		});

		reg("toggle-hide", "Hide yourself on Dynmap", run -> {
			UUID uuid = AccountLink.getUUID(run.getUser().getIdLong());
			if (uuid == null) {
				Printer.err(run, "Please link your account first");
				return;
			}

			this.ToggleVisable(uuid, f -> Printer.suc(run, f));
		});
	}

	@Override
	public Field getUserStatus(Member usr, Guild guild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Field getGuildStatus(Msg run) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Field Settings(Msg run) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "DynMap";
	}

	@Override
	public String getDisc() {
		// TODO Auto-generated method stub
		return "DynMap intergration";
	}

}
