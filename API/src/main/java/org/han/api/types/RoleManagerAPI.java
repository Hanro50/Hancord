package org.han.api.types;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleManagerAPI {
	public List<RoleAPI> getAllRoles()throws Exception;
	public List<RoleAPI> getRolesOf(UUID user) throws Exception;
	public Set<UUID> allMembers();
	public boolean isBooster(UUID user);
}
