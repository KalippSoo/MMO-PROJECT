package eu.isweezee.mmo.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import eu.isweezee.mmo.extra.UtilsFactory;

public abstract class Menu implements InventoryHolder{
	
	private Inventory inventory;
	private Player player;
	
	public abstract String name();
	public abstract int size();
	public abstract void handle(InventoryClickEvent e);
	public abstract void setItems();
	
	public void dispose(Player player) {
		
		this.player = player;
		inventory = Bukkit.createInventory(this, size() == 0 ? 54 : size(), UtilsFactory.color(name()));
		setItems();
		player.openInventory(inventory);
		
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
