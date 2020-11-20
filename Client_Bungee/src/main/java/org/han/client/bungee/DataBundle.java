package org.han.client.bungee;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.han.api.types.Jsonable;
import org.han.api.types.RoleAPI;
import org.han.api.types.RoleManagerAPI;

public class DataBundle implements RoleManagerAPI,Jsonable {
	public static Random rand = new Random();

	class UserObj {
		public UserObj(List<Role> Roles, boolean booster) {
			this.Roles = Roles;
			this.booster = booster;
		}

		List<Role> Roles;
		boolean booster;
	}

	class Role implements RoleAPI {
		long ID;
		String name;
		Color color;

		public Role(long ID, String name, Color color) {
			this.ID = ID;
			this.name = name;
			this.color = color;
		}

		@Override
		public long getID() {
			// TODO Auto-generated method stub
			return ID;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		@Override
		public Color getColor() {
			// TODO Auto-generated method stub
			return color;
		}

	}

	List<Role> AllRoles = new ArrayList<Role>();
	Map<UUID, UserObj> Users = new HashMap<UUID, UserObj>();
	String iconURL;
	public DataBundle(RoleManagerAPI base, String iconURL) throws Exception {
		this.iconURL = iconURL;
		for (RoleAPI role : base.getAllRoles()) {
			AllRoles.add(new Role(role.getID(), role.getName(), role.getColor()));
		}
		for (UUID uuid : allMembers()) {
			List<Role> Roles = new ArrayList<Role>();
			for (RoleAPI role : base.getRolesOf(uuid)) {
				Roles.add(new Role(role.getID(), role.getName(), role.getColor()));
			}
			Users.put(uuid, new UserObj(Roles, base.isBooster(uuid)));
		}
	}

	public String getURL() {
		return iconURL;
	}

	@Override
	public List<RoleAPI> getAllRoles() throws Exception {
		List<RoleAPI> T = new ArrayList<RoleAPI>();
		T.addAll(AllRoles);
		return T;
	}

	@Override
	public List<RoleAPI> getRolesOf(UUID user) throws Exception {
		List<RoleAPI> T = new ArrayList<RoleAPI>();
		T.addAll(Users.get(user).Roles);
		return T;
	}

	@Override
	public boolean isBooster(UUID user) {
		// TODO Auto-generated method stub
		return Users.get(user).booster;
	}

	@Override
	public Set<UUID> allMembers() {
		return Users.keySet();
	}

}
