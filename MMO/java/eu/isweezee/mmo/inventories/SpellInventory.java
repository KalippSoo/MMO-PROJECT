package eu.isweezee.mmo.inventories;

import org.bukkit.event.inventory.InventoryClickEvent;

import eu.isweezee.mmo.enums.GameItem;

public class SpellInventory extends Menu{

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Tes sorts";
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 18;
	}

	@Override
	public void setItems() {
		for (int i = 154; i <= 160; i+= 1) {
			System.out.println(i);
			getInventory().addItem(GameItem.getItemById(i, 1).get());
		}
	}

	@Override
	public void handle(InventoryClickEvent e) {
		
		switch (e.getCurrentItem().getType()) {
		case PACKED_ICE:
			
			break;
		case ENDER_EYE:
			
			break;

		default:
			break;
		}
		
	}

}
