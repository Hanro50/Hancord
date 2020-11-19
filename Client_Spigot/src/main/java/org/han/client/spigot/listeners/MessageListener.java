package org.han.client.spigot.listeners;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.han.api.BaseData;
import org.han.api.defaults.DeathMessage;
import org.han.api.defaults.Message;
import org.han.client.spigot.NameSpaceMappings;
import org.han.client.spigot.events.AsyncDiscordChatEvent;
import org.han.client.spigot.events.AsyncDiscordLoginEvent;
import org.han.debug.Log;
import org.han.vectors.Vector3;

import net.md_5.bungee.api.ChatColor;

public class MessageListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDiscordLogin(AsyncDiscordLoginEvent event) {

		// PluginHook.create(event.Disgot, DynMapHook.class, "Dynmap integration");
		// PluginHook.create(event.Disgot, LuckPermshook.class, "Luckperms
		// integration");
		// PluginHook.create(event.Disgot, HeaderUpdater.class);
		// PluginHook.create(event.Disgot, MarkerEvents.class);

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDiscordChat(AsyncDiscordChatEvent event) {
		try {
			String out = ChatColor.translateAlternateColorCodes('&', event.getFormat());

			Bukkit.getServer()
					.broadcastMessage(String.format(out,
							event.isLinked() ? BaseData.getPluginbase().getMCName(event.getPlayer()) : event.getName(), //
							event.getMessage(), //
							ChatColor.of(event.getTopRoleColor())));
		} catch (RuntimeException | java.lang.NoSuchMethodError e) {
			String out = org.bukkit.ChatColor.translateAlternateColorCodes('&', event.getFormat());
			Bukkit.getServer()
					.broadcastMessage(String.format(out,
							event.isLinked() ? BaseData.getPluginbase().getMCName(event.getPlayer()) : event.getName(), //
							event.getMessage(), //
							org.bukkit.ChatColor.AQUA));

			// org.bukkit.ChatColor;
		}
	}

//PlayerLoginEvent
	@EventHandler
	void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		BaseData.getPluginbase().getOutput().sendMessage(new Message(e.getPlayer().getUniqueId(), e.getMessage()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	void onPlayerLoginEvent(PlayerLoginEvent e) {
		BaseData.getPluginbase().getOutput().playerJoin(e.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	void onPlayerLeaveEvent(PlayerQuitEvent e) {
		BaseData.getPluginbase().getOutput().playerLeft(e.getPlayer().getUniqueId());
	}

	@EventHandler
	void onPlayerDeath(PlayerDeathEvent e) {

		Map<String, Integer> Enchantments = null;
		DeathMessage A = null;
		Vector3<Double> DP = new Vector3<Double>(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(),
				e.getEntity().getLocation().getY());

		float XPLoast = ((float) e.getEntity().getLevel() + e.getEntity().getExp());
		UUID player = null;

		if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent LastD = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
			String Weapon = null;
			Entity Damager = LastD.getDamager();
			String DeathMessage = NameSpaceMappings.DMTranslate(LastD.getCause());
			String Mob = null;
			// TNT is annoying
			if (Damager instanceof TNTPrimed) {
				Damager = ((TNTPrimed) Damager).getSource();
			} else if (Damager instanceof Projectile) {
				ProjectileSource shooter = ((Projectile) Damager).getShooter();
				if (shooter instanceof Entity) {
					Damager = (Entity) shooter;
				} else {
					Log.out(shooter.toString());
				}
			} else if (Damager instanceof FallingBlock) {
				// death.attack.anvil
				FallingBlock block = (FallingBlock) Damager;
				try {
					if (block.getBlockData().getMaterial() == Material.ANVIL) {
						DeathMessage = "death.attack.anvil";
					}
				} catch (NoSuchMethodError e1) {
					@SuppressWarnings("deprecation")
					Material f = block.getMaterial();
					if (f == Material.ANVIL) {
						DeathMessage = "death.attack.anvil";
					}
				}
				Damager = null;
			}

			// TNT can sometimes do weird stuff
			if (Damager != null) {
				try {
					Mob = "entity.minecraft." + Damager.getType().toString();
				} catch (NoSuchMethodError e1) {
					@SuppressWarnings("deprecation")
					String mobT = "entity.minecraft." + Damager.getType().getName();
					Mob = mobT;
				}

				if (Damager.getCustomName() != null) {
					Mob = Damager.getCustomName();
				}
				if (Damager instanceof LivingEntity) {
					ItemMeta Meta = null;
					EntityEquipment inv = ((LivingEntity) Damager).getEquipment();
					try {
						Meta = inv.getItemInMainHand().getItemMeta();
					} catch (NoSuchMethodError e1) {
						@SuppressWarnings("deprecation")
						ItemMeta MetaT = inv.getItemInHand().getItemMeta();
						Meta = MetaT;
					}
					// }
					try {
						if (Meta.hasDisplayName()) {
							Weapon = Meta.getDisplayName();
							if (Meta.hasEnchants()) {
								Enchantments = new HashMap<String, Integer>();
								for (Enchantment f : Meta.getEnchants().keySet()) {
									try {
										Enchantments.put(f.getKey().getKey(), Meta.getEnchants().get(f));
									} catch (NoSuchMethodError e1) {
										@SuppressWarnings("deprecation")
										String NSK = NameSpaceMappings.OLDenchantmentnames(f.getName());
										Enchantments.put(NSK, Meta.getEnchants().get(f));
									}
								}
							}
						}
					} catch (NullPointerException err) {
						Log.wrn("Nullpointer? Assuming item has no meta data then");
					}
				}

				if (Damager instanceof Player) {
					player = Damager.getUniqueId();
				}
			}

			A = new DeathMessage(DP, DeathMessage, Mob, e.getEntity().getUniqueId(), player, XPLoast, Weapon,
					Enchantments);
			Log.rep("DC:" + LastD.getCause().name());
			if (DP != null)
				Log.rep("DP:" + DP.tostring());
			Log.rep("C:" + NameSpaceMappings.DMTranslate(LastD.getCause()));
			if (Mob != null)
				Log.rep("M:" + Mob);
			Log.rep("P1:" + e.getEntity().getUniqueId().toString());
			if (player != null)
				Log.rep("P2:" + player.toString());
			Log.rep("XP:" + XPLoast);
			if (Weapon != null)
				Log.rep("W:" + Weapon);
			if (Enchantments != null)
				Log.rep("E:" + Enchantments.toString());

		} else {
			if (e.getEntity().getKiller() != null) {
				player = e.getEntity().getKiller().getUniqueId();
			}

			A = new DeathMessage(DP, NameSpaceMappings.DMTranslate(e.getEntity().getLastDamageCause().getCause()), null,
					e.getEntity().getUniqueId(), player, XPLoast, null, null);
		}

		if (A != null) {

			BaseData.getPluginbase().getOutput().deathMessage(A);
			// Output
		}
	}

	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		BufferedImage image = null;
		try {
			// Guild g = ServerData.getServer().getGuild();
			String urlstring = BaseData.getPluginbase().getIconURL();
			if (urlstring == null)
				return;

			URL url = new URL(urlstring);
			image = ImageIO.read(url);
			Image newResizedImage = image.getScaledInstance(64, 64, Image.SCALE_SMOOTH);

			event.setServerIcon(Bukkit.getServer().loadServerIcon(toBufferedImage(newResizedImage)));
		} catch (Exception e) {
		
		}
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

}
