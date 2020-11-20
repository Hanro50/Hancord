package org.han.server.bungee;

import static org.han.client.bungee.ChannelMappings.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.han.api.BaseData;
import org.han.client.bungee.DynMapHookCapsule;
import org.han.debug.Log;
import org.han.server.core.PluginComObj;
import org.han.server.core.Printer;
import org.han.server.core.types.Msg;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.md_5.bungee.api.config.ServerInfo;

public class Dynmap extends PluginComObj {

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
		return "Dynmap";
	}

	@Override
	public String getDisc() {
		// TODO Auto-generated method stub
		return "Dynmap integration";
	}

	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivate() {
		// TODO Auto-generated method stub
		reg("get-link", "attempt to get the plugin link", run -> {
			BiMap<String, Long> map = HashBiMap.create();
			map.putAll(BaseData.getPluginbase().getOutput().availableChannels());
			String serv = map.inverse().get(run.getChannel().getIdLong());
			if (serv != null) {
				Log.out("sending chat event to " + serv);

				ServerInfo info = Boot.serv.getServerInfo(serv);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(stream);
				try {
					out.writeUTF(PluginCall);
					out.writeUTF(DynMapHookCapsule.DynMapPluginMessage);
					out.writeUTF(DynMapHookCapsule.getlink);
					
					info.sendData(ServeroutChannel, stream.toByteArray(),true);
					run.getChannel().sendMessage("Sending request to client")
							.queue(f -> f.delete().delay(2, TimeUnit.MINUTES));
				} catch (IOException e) {
					Printer.err(run, "Internal exception");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Printer.err(run, "This channel isn't linked!");
			}

		});

	}
}
