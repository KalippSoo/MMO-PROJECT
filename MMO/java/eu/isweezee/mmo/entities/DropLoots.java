package eu.isweezee.mmo.entities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import eu.isweezee.mmo.enums.GameItem;

public class DropLoots {

	Map<Double, ItemStack> loots = new HashMap<>();
	double value = 0;
	
	public DropLoots() {
		this.add(GameItem.NOTHING);
	}
	
	public void add(GameItem item) {
		loots.put(item.getDropChance(), item.getItem().get());
	}
	
	public ItemStack drop() {
		
		for (double d : loots.keySet()) {
			this.value += d;
		}
		
		double random = Math.random()*value+1, previous = 0;
		for (double d : loots.keySet()) {
			previous += d;
			if (random <= previous) {
				return loots.get(d);
			}
		}
		return null;
	}
	
	//damage * (1+strenght/100) * (1+critdamage/100)
}