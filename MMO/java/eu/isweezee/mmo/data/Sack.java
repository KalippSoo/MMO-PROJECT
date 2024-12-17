package eu.isweezee.mmo.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.UtilsFactory;

//Field field = nmsItem.getClass().getDeclaredField("maxStackSize");
public class Sack {
	
	private Inventory inventory;
	Document document;
	
	public Sack(Document document, int slots, String name) {
		this.document = document;
		this.inventory = Bukkit.createInventory(null, 54, name);
		
		int[] pane = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
		for (int i : pane) {
			this.inventory.setItem(i, UtilsFactory.buildItem(Material.BROWN_STAINED_GLASS_PANE, 1, " "));
		}
		
		for (String string : document.getList("items", String.class)) {
			
			// Pattern "4.2T25"
			String[] pattern = string.split("T");
			double id = Double.parseDouble(pattern[0]);
			int amount = Integer.parseInt(pattern[1]);
			
			ItemCreator item = GameItem.getItemById(id, amount);
			if (item == null) continue;
			this.inventory.addItem(item.get());
		}
		
	}
	
	List<String> code = new ArrayList<>();
	
	public List<String> saveInventory() {
		
		code.clear();
		for (ItemStack items : this.inventory.getContents()) {
			
			if (items == null)continue;
			
			double id = GameItem.getIdFromItem(items);
			if (id == -1)continue;
			
			code.add(id+ "T" + items.getAmount());
			
		}
		return code;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	
}
