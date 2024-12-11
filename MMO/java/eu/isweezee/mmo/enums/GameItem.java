package eu.isweezee.mmo.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.ItemCreator.Stats;

public enum GameItem{
	
	//Spells
	SLOWNESS(154, 0, new ItemCreator("&aGel", new Stats(Material.PACKED_ICE, ItemUse.SPELL, 0, 0, 0, 0, 0, 0), (byte)0, "&7Gel l'ennemit pendant 5 secondes !")),
	FEAR(155, 0, new ItemCreator("&aPeur", new Stats(Material.ENDER_EYE, ItemUse.SPELL, 0, 0, 0, 0, 0, 0), (byte)0, "&7Effraie l'ennemit pendant 5 secondes !")),
	INVISIBLE(156, 0, new ItemCreator("&aInvisibilité", new Stats(Material.SPLASH_POTION, ItemUse.SPELL, 0, 0, 0, 0, 0, 0), (byte)0)),

	//Items
	DRAGON_SPINE(5, .5, new ItemCreator("L'épine du dragon", new Stats(Material.GOLDEN_SWORD, ItemUse.WEAPON, 100, 100, 40, 75, 0, 0), (byte)3)),
	
	//Loots
	NOTHING(-1, 101, new ItemCreator("", new Stats(Material.TORCH, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)1)),
	
	BASIC_LOOT(0, 100, new ItemCreator("Unknown Substance", new Stats(Material.PHANTOM_MEMBRANE, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)1)),
	FIRE_SPHERE(1, 25, new ItemCreator("Fire Sphere", new Stats(Material.ORANGE_DYE, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)0)),
	DARK_SPHERE(2, 5, new ItemCreator("Dark Sphere", new Stats(Material.GRAY_DYE, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)1)),
	
	PIGLOW_LOOT_ITEM(.1, 10, new ItemCreator("Piglow's Organ", new Stats(Material.CACTUS, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)2)),
	
	//Weapons
	ALMOST_BROKEN_AXE(11, .05, new ItemCreator("Almost Broken Axe", new Stats(Material.WOODEN_AXE, ItemUse.WEAPON, 50, 25, 20, 40, 0, 0), (byte)2)),
	XhroAxe(10, 0.1, new ItemCreator("Xhro Divine", new Stats(Material.IRON_AXE, ItemUse.WEAPON, 150, 75, 50, 250, 0, 0), (byte)3)),
	
	//Armors
	PYROHELMET(14, 1, new ItemCreator("Pyro", new Stats(Material.LEATHER_HELMET, ItemUse.ARMOR, 150, 75, 50, 250, 0, 0), (byte)3)),
	PYROCHESTPLATE(14.1, 1, new ItemCreator("Pyro", new Stats(Material.LEATHER_CHESTPLATE, ItemUse.ARMOR, 150, 75, 50, 250, 0, 0), (byte)3)),
	PYROLEGGINGS(14.2, 1, new ItemCreator("Pyro", new Stats(Material.LEATHER_LEGGINGS, ItemUse.ARMOR, 150, 75, 50, 250, 0, 0), (byte)3)),
	PYROBOOTS(14.3, 1, new ItemCreator("Pyro", new Stats(Material.LEATHER_BOOTS, ItemUse.ARMOR, 150, 75, 50, 250, 0, 0), (byte)3)),
	;
	
	private double id;
	private ItemCreator item;
	private double dropChance;
	
	private GameItem(double id, double dropChance, ItemCreator item) {
		this.id = id;
		this.item = item;
		this.dropChance = dropChance;
	}
	
	public static ItemCreator getItemById(double id, int amount) {
		
		for (GameItem gameItems : GameItem.values()) {
			if (gameItems.id == id) {
				ItemStack item = gameItems.item.get();
				item.setAmount(amount);
				return gameItems.getItem();
			}
		}
		System.err.println("Attempt to get the item from id " + id + " (failed) !");
		return null;
	}
	
	public static double getIdFromItem(ItemStack item) {
		
		for (GameItem gameItems : GameItem.values()) {
			if (item.isSimilar(gameItems.getItem().get())) {
				return gameItems.getId();
			}
		}
		
		return -1;
		
	}
	public static ItemCreator getItemCreatorFromItemStack(ItemStack item) {
		
		if (item != null) {
			for (GameItem gameItems : GameItem.values()) {
				if (item.isSimilar(gameItems.getItem().get())) {
					return gameItems.item;
				}
			}
		}
		
		return null;
		
	}
	
	public static GameItem getGameItemFromItemStack(ItemStack item) {
		
		if (item != null) {
			for (GameItem gameItems : GameItem.values()) {
				if (item.isSimilar(gameItems.getItem().get())) {
					return gameItems;
				}
			}
		}
		
		return null;
		
	}
	
	public double getId() {
		return id;
	}

	public ItemCreator getItem() {
		return item;
	}

	public double getDropChance() {
		return dropChance;
	}
	
	
}

