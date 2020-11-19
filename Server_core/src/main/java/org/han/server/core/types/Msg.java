package org.han.server.core.types;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public interface Msg {
	public boolean isBotOwner();

	public boolean isGuildOwner();

	public boolean isGuildAdmin();

	public boolean isGuild();

	public boolean isCom();

	public User getUser();

	public Member getMember();

	public Guild getGuild();

	public JDA getJDA();

	public Message getMessage();

	public String getCom();

	public String getText();
	
	public String getPrefix();

	public MessageChannel getChannel();

	public MessageChannel GetDM();

	public MessageChannel GetDM(User user);

}
