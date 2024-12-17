package eu.isweezee.mmo.inventories.collection;

import org.bukkit.event.inventory.InventoryClickEvent;

import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.inventories.Menu;

public class CollectionMenu extends Menu{

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Collection";
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 54;
	}

	@Override
	public void handle(InventoryClickEvent e) {
		
		
	}

	@Override
	public void setItems() {
		this.getInventory().setItem(10, GameItem.getItemById(1, 1).get().clone());
		
	}
	
}
