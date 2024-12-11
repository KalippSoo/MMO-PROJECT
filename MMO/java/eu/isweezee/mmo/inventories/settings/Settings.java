package eu.isweezee.mmo.inventories.settings;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import eu.isweezee.mmo.extra.UtilsFactory;
import eu.isweezee.mmo.inventories.Menu;

public class Settings extends Menu{

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Settings";
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 36;
	}

	@Override
	public void handle(InventoryClickEvent e) {
		
		switch (e.getCurrentItem().getType()) {
		case NAME_TAG:
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void setItems() {
		
		this.getInventory().setItem(10, UtilsFactory.buildItem(Material.WITHER_SKELETON_SKULL, 1, "Combat's settings"));
		this.getInventory().setItem(12, UtilsFactory.buildItem(Material.NAME_TAG, 1, "Notification's settings"));
		
	}

}
