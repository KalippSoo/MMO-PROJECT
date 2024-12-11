package eu.isweezee.mmo.listeners;

import org.bson.Document;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.data.PlayerData;
import eu.isweezee.mmo.entities.test;
import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.enums.ItemUse;
import eu.isweezee.mmo.extra.DocumentRelated;
import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.UtilsFactory;
import eu.isweezee.mmo.inventories.Menu;
import eu.isweezee.mmo.inventories.SpellInventory;

public class PlayerListeners implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		player.getInventory().clear();
		UtilsFactory.applyData(player);
		player.setWalkSpeed(0.175f);
	}
	
	@EventHandler
	public void onKill(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof Sheep) {
			test.spawn(e.getRightClicked().getLocation());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) return;
		Player player = (Player) e.getDamager();
		PlayerData data = MMO.dataStorage.get(player.getUniqueId());
		boolean ifCrit = (Math.random() * 101 < data.getStatsModifier().getModifiedCritChance());
		double damage = !ifCrit ? data.getStatsModifier().getDamage() : data.getStatsModifier().getDamageWithCrit();
		
		e.setDamage(damage);
		
		UtilsFactory.DamageIndictor(player, e.getEntity().getLocation(), damage, ifCrit, .3);
		
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
			if (itemName == "Your profile") {
				
			}
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
		
		Document playerData = new Document("uuid", player.getUniqueId().toString());
		Document found = MMO.players.find(playerData).first();
		
		DocumentRelated.saveJavaPlayerDataToMongoDB(player, found);
		MMO.dataStorage.remove(player.getUniqueId());
		
	}
	
}