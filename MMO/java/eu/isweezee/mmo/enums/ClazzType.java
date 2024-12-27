package eu.isweezee.mmo.enums;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.data.PlayerData;
import eu.isweezee.mmo.extra.UtilsFactory;

public enum ClazzType {

	NONE(null),
	MAGE(UtilsFactory.buildItem(Material.BLUE_ORCHID, 1, "&aRune du mage")),
	TANK(UtilsFactory.buildItem(Material.SEA_PICKLE, 1, "&aRune du tank")),
	WARRIOR(UtilsFactory.buildItem(Material.RED_TULIP, 1, "&aRune du combattant")),
	HEALER(UtilsFactory.buildItem(Material.PINK_TULIP, 1, "&aRune du soigneur"))
	;
	
	private ItemStack item;
	
	private ClazzType(ItemStack item) {
		this.item = item;
	}
	
	public static void setupClass(Player player) {
		PlayerData data = MMO.dataStorage.get(player.getUniqueId());
		if (data.getClazzType() == NONE) {
			player.getInventory().setItem(3, UtilsFactory.buildItem(Material.ORANGE_STAINED_GLASS_PANE, 1, "&aVous n'avez pas encore choisi une classe"));
			return;
		}
		if (data.getClazzType() == WARRIOR) {
			
			ItemStack item = data.getClazzType().getItem();
			player.getInventory().setItem(3, UtilsFactory.buildItem(item.clone(),
					"&e&lPassif",
					"&fGrant you &a+" + UtilsFactory.combattant + "(" + (data.getLevel() < 5 ? 20 : data.getLevel() * UtilsFactory.combattant) + ") &fstrenght",
					"&fGrant you &a+" + UtilsFactory.combattantCrit + "(" + (data.getLevel() < 5 ? 20 : data.getLevel() * UtilsFactory.combattantCrit) + ") &fcrit damage",
					"&fBased on your current level !",
					"&7(Grant you 20 strenght & crit damage if your level is lower than 5)"
					));
		}
	}

	public ItemStack getItem() {
		return item;
	}
	
}
