package org.han.api.defaults;

import java.util.Map;
import java.util.UUID;

import org.han.api.types.DeathMessageAPI;
import org.han.vectors.Vector3;

public class DeathMessage implements DeathMessageAPI{

	public Vector3<Double> Deathpos;
	public String DeathMsg;
	public String MobName;
	public UUID Victem;
	public UUID Attacker;
	public float XP;
	public String Weapon;
	public Map<String, Integer> Enchantments;
	public long Creationdate;

	public DeathMessage(Vector3<Double> pos, String DM, String MobName, UUID Victem, UUID Attacker, float XP,
			String Weapon, Map<String, Integer> Enchantments) {
		this.Deathpos = pos;
		this.DeathMsg = (DM);
		this.MobName = MobName;
		this.Victem = Victem;
		this.Attacker = Attacker;
		this.XP = XP;
		this.Weapon = Weapon;
		this.Creationdate = System.nanoTime();
		this.Enchantments = Enchantments;
	}

	@Override
	public Vector3<Double> getDeathpos() {
		// TODO Auto-generated method stub
		return Deathpos;
	}

	@Override
	public String getDeathMsg() {
		// TODO Auto-generated method stub
		return DeathMsg;
	}

	@Override
	public String getMobName() {
		// TODO Auto-generated method stub
		return MobName;
	}

	@Override
	public UUID getVictem() {
		// TODO Auto-generated method stub
		return Victem;
	}

	@Override
	public UUID getAttacker() {
		// TODO Auto-generated method stub
		return Attacker;
	}

	@Override
	public float getXP() {
		// TODO Auto-generated method stub
		return XP;
	}

	@Override
	public String getWeapon() {
		// TODO Auto-generated method stub
		return Weapon;
	}

	@Override
	public Map<String, Integer> getEnchantments() {
		// TODO Auto-generated method stub
		return Enchantments;
	}

	@Override
	public long getCreationdate() {
		// TODO Auto-generated method stub
		return Creationdate;
	}

}
