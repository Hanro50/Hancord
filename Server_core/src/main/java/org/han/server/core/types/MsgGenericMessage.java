package org.han.server.core.types;

import org.han.Misc;
import org.han.api.BaseData;
import org.han.debug.Log;
import org.han.server.core.CHandler;
import org.han.types.AsyncProcess;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MsgGenericMessage implements Msg {
	final MessageReceivedEvent event;

	public MsgGenericMessage(MessageReceivedEvent event) {
		this.event = event;
		if (isCom()) {
			final long time = System.nanoTime();
			AsyncProcess.run(f -> {
				CHandler.compute(this);
				long delta = System.nanoTime() - time;
				Log.out("took " + delta / 1000000 + " milli seconds to process command");
			});
		}
	}

	@Override
	public boolean isBotOwner() {
		ApplicationInfo AI = event.getJDA().retrieveApplicationInfo().complete();
		if (AI.getTeam() != null) {
			return AI.getTeam().getMember(event.getAuthor()) != null;
		}
		return event.getJDA().retrieveApplicationInfo().complete().getOwner().getId().equals(event.getAuthor().getId());
	}

	@Override
	public boolean isGuildOwner() {
		if (event.isFromGuild()) {
			return event.getMember().isOwner();
		}
		return false;
	}

	@Override
	public boolean isGuildAdmin() {
		if (event.isFromGuild()) {
			return event.getMember().hasPermission(Permission.ADMINISTRATOR);
		}
		return false;
	}

	@Override
	public boolean isGuild() {
		// TODO Auto-generated method stub
		return event.isFromGuild();
	}

	@Override
	public boolean isCom() {
		// TODO Auto-generated method stub
		return (event.getMessage().getContentRaw().startsWith(this.getPrefix()));
	}

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return event.getAuthor();
	}

	@Override
	public Member getMember() {
		// TODO Auto-generated method stub
		return event.getMember();
	}

	@Override
	public Guild getGuild() {
		if (event.isFromGuild())
			return event.getGuild();
		return null;
	}

	@Override
	public JDA getJDA() {
		// TODO Auto-generated method stub
		return event.getJDA();
	}

	@Override
	public Message getMessage() {
		// TODO Auto-generated method stub
		return event.getMessage();
	}

	private String[] processtext() {
		String[] T = event.getMessage().getContentRaw().split(Misc.SplitFormat(' '), 2);
		return (T.length > 0) ? T : new String[] { event.getMessage().getContentRaw() };
	}

	@Override
	public String getCom() {
		return processtext()[0].substring(getPrefix().length());
	}

	@Override
	public String getText() {
		String[] T = processtext();
		return T.length > 1 ? T[1] : "";
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return BaseData.Discordcomchars;
	}

	@Override
	public MessageChannel getChannel() {
		// TODO Auto-generated method stub
		return event.getChannel();
	}

	@Override
	public MessageChannel GetDM() {
		// TODO Auto-generated method stub
		return GetDM(event.getAuthor());
	}

	@Override
	public MessageChannel GetDM(User user) {
		// TODO Auto-generated method stub
		return user.openPrivateChannel().complete();
	}

}
