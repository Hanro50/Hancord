package org.han.api.types;

import java.util.Map;
import java.util.UUID;

import org.han.vectors.Vector3;

public interface DeathMessageAPI extends BaseAPIObject {

	public Vector3<Double> getDeathpos();

	public String getDeathMsg();

	public String getMobName();

	public UUID getVictem();

	public UUID getAttacker();

	public float getXP();

	public String getWeapon();

	public Map<String, Integer> getEnchantments();


}
