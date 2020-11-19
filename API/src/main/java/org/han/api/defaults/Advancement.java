package org.han.api.defaults;

import java.util.UUID;

import org.han.api.types.AdvancementAPI;


public class Advancement implements AdvancementAPI {
	 private UUID PlayerID;
	 private String AchievementID;
	 private long Creationdate;
	public Advancement(UUID PlayerID,String AchievementID) {
		this.PlayerID = PlayerID;
		this.AchievementID = AchievementID;
		this.Creationdate = System.nanoTime();
	}
	@Override
	public long getCreationdate() {
		// TODO Auto-generated method stub
		return Creationdate;
	}
	@Override
	public UUID getPlayer() {
		// TODO Auto-generated method stub
		return PlayerID;
	}
	@Override
	public String getAchievementID() {
		// TODO Auto-generated method stub
		return AchievementID;
	}
	
}
