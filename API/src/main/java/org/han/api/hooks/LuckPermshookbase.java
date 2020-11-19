package org.han.api.hooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.han.api.types.RoleAPI;
import org.han.debug.Log;
import org.han.api.BaseData;
import org.han.api.PluginHook;
import org.han.files.DisgotJsonObj;
import org.han.files.FIleUtil;

import com.google.gson.annotations.Expose;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.group.GroupLoadAllEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.DisplayNameNode;
import net.luckperms.api.node.types.InheritanceNode;

public abstract class LuckPermshookbase<T> implements PluginHook {
	final String ROLE = "discord.role";
	final String BOOSTER = "discord.booster";
	final String LINKED = "discord.linked";
	private LuckPerms api;
	private Set<EventSubscription<?>> subs = new HashSet<EventSubscription<?>>();
	private LuckPermContext LPC = new LuckPermContext();

	protected abstract void onEnable();

	public void activate() {
		api = LuckPermsProvider.get();
		api.getContextManager().registerCalculator(LPC);
		subs.add(api.getEventBus().subscribe(GroupLoadAllEvent.class, group -> {
			List<RoleAPI> allrole;
			try {
				allrole = BaseData.getPluginbase().getRoleManager().getAllRoles();
			} catch (Exception e) {
				Log.trace(e);
				return;
			}
			api.getGroupManager().createAndLoadGroup(LINKED);
			api.getGroupManager().createAndLoadGroup(BOOSTER);

			Map<String, RoleAPI> DRL = new HashMap<String, RoleAPI>();

			for (RoleAPI role : allrole)
				if (Datastore.check(role.getID()))
					DRL.put(ROLE + "." + FIleUtil.SafeStr(role.getName()), role);

			for (Group F : api.getGroupManager().getLoadedGroups()) {
				if (F.getName().startsWith(ROLE)) {
					if (DRL.containsKey(F.getName())) {
						F.getNodes().add(DisplayNameNode.builder(DRL.get(F.getName()).getName()).build());
						api.getGroupManager().saveGroup(F);
						DRL.remove(F.getName());
					} else {
						api.getGroupManager().deleteGroup(F);
					}

				}
			}
			for (String Fn : DRL.keySet()) {
				api.getGroupManager().createAndLoadGroup(Fn).handle((F, e) -> {
					F.getNodes().add(DisplayNameNode.builder(DRL.get(F.getName()).getName()).build());
					api.getGroupManager().saveGroup(F);
					return F;
				});

			}
		}));

		subs.add(api.getEventBus().subscribe(UserDataRecalculateEvent.class, event -> {
			event.getUser().data().remove(InheritanceNode.builder(ROLE).build());
			try {
				List<RoleAPI> memroles = BaseData.getPluginbase().getRoleManager()
						.getRolesOf(event.getUser().getUniqueId());

				event.getUser().data().add(Node.builder(LINKED).value(true).build());
				event.getUser().data()
						.add(Node.builder(BOOSTER).value(
								BaseData.getPluginbase().getRoleManager().isBooster(event.getUser().getUniqueId()))
								.build());
				for (RoleAPI role : memroles) {
					if (Datastore.check(role.getID()))
						event.getUser().data()
								.add(InheritanceNode.builder(ROLE + "." + FIleUtil.SafeStr(role.getName())).build());
				}
				return;

			} catch (Exception e) {
			}

			event.getUser().data().add(Node.builder(LINKED).value(false).build());
			event.getUser().data().add(Node.builder(BOOSTER).value(false).build());
		}));
		onEnable();
	}

	protected static class Datastore extends DisgotJsonObj {
		private static final long serialVersionUID = 1L;
		static Datastore me = new Datastore();
		@Expose
		List<Long> RegisteredRoles = new ArrayList<Long>();

		Datastore() {
			super("", "luckperms", "json");
		}

		public static void add(Long data) {
			me.makeLoaded();
			me.RegisteredRoles.remove(data);
			me.RegisteredRoles.add(data);
			me.save();
		}

		public static void remove(Long data) {
			me.makeLoaded();
			me.RegisteredRoles.remove(data);
			me.save();
		}

		public static boolean check(Long data) {
			me.makeLoaded();
			return me.RegisteredRoles.contains(data);
		}

		public static String print() {
			String out = "";
			if (me.RegisteredRoles.isEmpty())
				return "No roles connected";

			for (Long ID : me.RegisteredRoles) {
				out += "<@&" + ID + ">\n";
			}

			//
			return out.trim();

		}

	}

	public abstract UUID getUUID(T object);

	public class LuckPermContext implements ContextCalculator<T> {
		@Override
		public void calculate(T target, ContextConsumer consumer) {
			try {
				List<RoleAPI> memroles = BaseData.getPluginbase().getRoleManager().getRolesOf(getUUID(target));

				consumer.accept(LINKED, "true");
				consumer.accept(BOOSTER,
						BaseData.getPluginbase().getRoleManager().isBooster(getUUID(target)) ? "true" : "false");
				for (RoleAPI role : memroles) {
					consumer.accept(ROLE, role.getName());
				}
				return;

			} catch (Exception e) {
			}
			consumer.accept(LINKED, "false");
			consumer.accept(BOOSTER, "false");
		}

		@Override
		public ContextSet estimatePotentialContexts() {
			ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
			try {
				List<RoleAPI> allroles = BaseData.getPluginbase().getRoleManager().getAllRoles();

				for (RoleAPI role : allroles) {
					builder.add(ROLE, role.getName());
				}
				builder.add(BOOSTER, "true");
				builder.add(BOOSTER, "false");
				builder.add(LINKED, "true");
				builder.add(LINKED, "false");

			} catch (Exception e) {
			}
			return builder.build();

		}
	}

	protected abstract void onDisable();

	@Override
	public void disable() {

		if (api != null)
			subs.forEach(f -> {
				if (f != null)
					f.close();
			});
		onDisable();
	}
}
