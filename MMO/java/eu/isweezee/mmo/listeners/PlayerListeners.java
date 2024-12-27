package eu.isweezee.mmo.listeners;

import org.bson.Document;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.data.PlayerData;
import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.enums.ItemUse;
import eu.isweezee.mmo.extra.DocumentRelated;
import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.UtilsFactory;
import eu.isweezee.mmo.inventories.Menu;
import eu.isweezee.mmo.inventories.SpellInventory;
import eu.isweezee.mmo.inventories.settings.Settings;

public class PlayerListeners implements Listener{
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		player.getInventory().clear();
		UtilsFactory.playerJoinServer(player);
		player.setWalkSpeed(0.175f);
	}
	
	@EventHandler
	public void onFallDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player)
			if (e.getCause() == DamageCause.FALL)
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Player) {
			Player player = (Player) e.getDamager();
			PlayerData data = MMO.dataStorage.get(player.getUniqueId());
			boolean ifCrit = (Math.random() * 101 < data.getStatsModifier().getModifiedCritChance());
			double damage = !ifCrit ? data.getStatsModifier().getDamage() : data.getStatsModifier().getDamageWithCrit();
			
			e.setDamage(damage);
			UtilsFactory.DamageIndictor(player, e.getEntity().getLocation(), damage, ifCrit, .3);
			
			if (data.hits < 3) {
				player.setVelocity(e.getEntity().getLocation().toVector().subtract(player.getLocation().toVector()).multiply(.15D));
				data.hits ++;
			}
			if (data.hits == 3) {
				data.hits = 4;
				PlayerAttackPrototype(player, data, e.getDamage());
			}
			
		}
		
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			PlayerData data = MMO.dataStorage.get(player.getUniqueId());
			double damageReduction = UtilsFactory.damageReduction(data, null);
			e.setDamage((e.getDamage() * damageReduction)/100);
		}
		
	}
	
	private void PlayerAttackPrototype(Player player, PlayerData data, double damage) {
		
		Vector vector = new Vector(0, 2, 0);
		player.setVelocity(vector);
		
		new BukkitRunnable() {
			
			int timer = (40);
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if (timer == 35) {
					player.setVelocity(new Vector());
					player.setAllowFlight(true);
					player.setFlying(true);
					player.playSound(player.getLocation(), Sound.ENTITY_MAGMA_CUBE_JUMP, 1f, .5f);
				}
				if (timer > 1 && timer < 35) {
					player.getLineOfSight(null, 30).stream()
							.filter(block -> block.getType() != Material.AIR)
							.forEach(location -> {
								Location loc = location.getLocation().add(.5, 1, .5);
								player.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc, 1, 0, 0, 0, 0.001);
								player.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, 1f, .5f);
								for (Entity entities : loc.getChunk().getEntities()) {
									if (entities == player)continue;
									if (entities.isInvulnerable())continue;
									if (!(entities instanceof LivingEntity))continue;
									if (entities.getLocation().distance(loc) < 1.5) 
										((LivingEntity)entities).damage(damage, player);
										
								}
							});
				}
				
				if (timer == 0) {

					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1f, 1.5f);
					player.setAllowFlight(false);
					player.setFlying(false);
					player.setVelocity(player.getLocation().getDirection().multiply(1.0D));
					data.hits = 0;
					cancel();
				}
				timer--;
				
			}
		}.runTaskTimer(MMO.getPlugin(MMO.class), 0, 0);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (e.getItem() == null)return;
		if (!e.getAction().name().contains("RIGHT_CLICK"))return;
		if (!UtilsFactory.stripColor(e.getItem().getItemMeta().getDisplayName()).contains("Rune du"))return;
		PlayerData data = UtilsFactory.getPlayerData(e.getPlayer().getUniqueId());
		if (data == null)UtilsFactory.Kick(e.getPlayer(), 45);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE)return;
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)return;
		
		InventoryHolder holder = e.getInventory().getHolder();
		
		if (holder instanceof Menu) {
			Menu menu = (Menu) holder;
			menu.handle(e);
		}
		
		Player player = (Player) e.getWhoClicked();
		PlayerData data = MMO.dataStorage.get(player.getUniqueId());
		e.setCancelled(true);
		String itemName = UtilsFactory.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
		Material type = e.getCurrentItem().getType();
		
		switch (e.getCurrentItem().getType()) {
		case EMERALD:
			Settings settings = new Settings();
			settings.dispose(player);
			
			break;
		case TOTEM_OF_UNDYING: case CHEST:
			
			if (itemName.contains("Sac")) {
				if (type == Material.CHEST) {
					player.openInventory(data.getSack().getInventory());
				}
				if (type == Material.TOTEM_OF_UNDYING) {
					player.openInventory(data.getArtefacts().getInventory());
				}
			}
			break;
		case GRAY_STAINED_GLASS_PANE:
			SpellInventory inventory = new SpellInventory();
			inventory.dispose(player);
			
			break;
		default:
			break;
		}
		
		if (GameItem.getItemCreatorFromItemStack(e.getCurrentItem()) != null && e.getClickedInventory() == data.getSack().getInventory()) {
			ItemCreator itemCreator = GameItem.getItemCreatorFromItemStack(e.getCurrentItem());
			if (itemCreator.getUse() == ItemUse.WEAPON) {
				player.getInventory().setItem(0, itemCreator.get());
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		
		Player player = e.getPlayer();
		
		if (!MMO.dataStorage.containsKey(player.getUniqueId()))return;
		Document playerData = new Document("uuid", player.getUniqueId().toString());
		Document found = MMO.players.find(playerData).first();
		
		DocumentRelated.saveJavaPlayerDataToMongoDB(player, found);
		MMO.dataStorage.remove(player.getUniqueId());
		
	}
	
}
