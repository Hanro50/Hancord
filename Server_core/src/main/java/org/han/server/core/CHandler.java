package org.han.server.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.han.debug.Log;
import org.han.server.core.types.ComBase;
import org.han.server.core.types.ComObj;
import org.han.server.core.types.Msg;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public final class CHandler {

	private static Map<String, ComObj> commands = new ConcurrentHashMap<String, ComObj>();

	private static String filter(String in) {
		return in.trim().toLowerCase().replaceAll(" ", "_");
	}

	public static synchronized ComObj reg(String name, String disc, ComBase base, Consumer<Msg> run) {
		name = filter(name);

		ComObj CO = new ComObj(run, disc, base);
		if (commands.containsKey(name)) {
			Log.trace(new Exception());
			int i = 0;
			do {
				i++;
			} while (commands.containsKey(name + "_" + i));
			name += "_" + i;

		}
		commands.put(name, CO);
		return CO;
	}

	public static Map<String, ComObj> getComs() {
		Map<String, ComObj> coms = new ConcurrentHashMap<String, ComObj>();
		coms.putAll(commands);
		return coms;
	}

	public static void compute(Msg m) {
		DiscordDataHolder.invoke();
		if (!m.isCom()) {
			return;
		}
		String com = filter(m.getCom());
		ComObj CO = commands.get(com);
		boolean irp = false;
		boolean hp = false;
		if (CO == null || (irp = !CO.isrightplace(m)) || (hp = !CO.hasPerms(m))) {

			if (irp)
				Printer.err(m, "Cannot run this command here");
			else if (hp)
				Printer.err(m, "Insufficient Permissions");
			return;
		}

		try {
			CO.getRun().accept(m);
		} catch (RuntimeException e) {
			try {
				if (e instanceof InsufficientPermissionException) {
					InsufficientPermissionException error = (InsufficientPermissionException) e;
					if (error.getPermission().equals(Permission.MESSAGE_WRITE))
						m.GetDM().sendMessage("Permission error :" + e.getMessage()).queue();
					m.getChannel().sendMessage("Permission error :" + e.getMessage()).queue();
					return;
				}
				if (e instanceof ErrorResponseException) {
					m.getChannel().sendMessage("general error :" + ((ErrorResponseException) e).getErrorResponse())
							.queue();
					return;
				}
			} catch (RuntimeException e1) {
			}
			Log.trace(e);
		}
	}
}
