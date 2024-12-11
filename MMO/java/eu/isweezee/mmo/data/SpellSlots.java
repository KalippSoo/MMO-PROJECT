package eu.isweezee.mmo.data;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bukkit.entity.Player;

public class SpellSlots {

	private Map<Integer, Double> activeSpells = new HashMap<>();
	
	public void setupSpells(Document doc, Player player) {
		
		//code "1T154.5"
		
		for (String str : doc.getList("selection", String.class)) {
			
			String[] cleanStr = str.split("T");
			
			int slot = Integer.parseInt(cleanStr[0]);
			double id = Double.parseDouble(cleanStr[1]);
			
			if (id == 0)id = -1;
			
			activeSpells.put(slot, id);
			
		}
		setupInventory(player);
	}
	
	private void setupInventory(Player player) {
		
		
	}
	
	public void save(Document doc) {
		
		
		
	}

	public Map<Integer, Double> getActiveSpells(){
		return activeSpells;
	}
	
	
}
