package org.han.server.core.hooks;

import org.han.api.hooks.LuckPermshookbase;
import org.han.server.core.DiscordDataHolder;
import org.han.server.core.Printer;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.Msg;
import org.han.server.core.types.FunctionalInterfaces.AccCheck;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public abstract class LuckPermshook<T> extends LuckPermshookbase<T> implements ComBase {

//kept in server version
	public void coms() {
		reg("bind-role", "Bind a discord role to a luckperms group", run -> {

			Role role;
			if (run.getMessage().getMentionedRoles().size() < 1) {
				role = run.getGuild().getRoleById(run.getText().trim());
				if (role == null) {
					Printer.err(run, "Invalid role id provided");
					return;
				}
			} else {
				role = run.getMessage().getMentionedRoles().get(0);
			}

			Datastore.add(role.getIdLong());
			Printer.suc(run, "Added role to list");

		}).setGuild().setPerms(AccCheck.Trusted, AccCheck.BotOwner);

		reg("unbind-role",
				"unbind a discord role to a luckperms group. This will lead to the Luckperms group being deleted",
				run -> {

					Role role;
					if (run.getMessage().getMentionedRoles().size() < 1) {
						role = run.getGuild().getRoleById(run.getText().trim());
						if (role == null) {
							Printer.err(run, "Invalid role id provided");
							return;
						}
					} else {
						role = run.getMessage().getMentionedRoles().get(0);
					}

					Datastore.remove(role.getIdLong());
					Printer.suc(run, "Removed role role from list");

				}).setGuild().setPerms(AccCheck.Trusted, AccCheck.BotOwner);

	}
	
	@Override
	public Field Settings(Msg run) {
		// TODO Auto-generated method stub
		return new Field(getName(), "Connected roles:" + Datastore.print(), false);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "luckperms";
	}

	@Override
	public String getDisc() {
		// TODO Auto-generated method stub
		return "Disgot luck perms hook";
	}

	@Override
	protected void onEnable() {
		coms();
		DiscordDataHolder.CB.add(this);
	}

	@Override
	public void onDisable() {
		DiscordDataHolder.CB.remove(this);
	}
}
