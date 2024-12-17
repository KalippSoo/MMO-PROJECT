package eu.isweezee.mmo.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.ItemCreator.Reforge;
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
	
	TENDERFLESH(1, 25, new ItemCreator("Tender Flesh", new Stats(Material.ROTTEN_FLESH, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)0)),
	DARK_SPHERE(1.1, 5, new ItemCreator("Dark Sphere", new Stats(Material.GRAY_DYE, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)1)),
	
	BONEPOWDER(2, 50, new ItemCreator("Tooth's bone", new Stats(Material.PHANTOM_MEMBRANE, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)0)),
	
	PIGLOW_LOOT_ITEM(.1, 10, new ItemCreator("Piglow's Organ", new Stats(Material.CACTUS, ItemUse.LOOTS, 0, 0, 0, 0, 0, 0), (byte)2)),
	
	//Weapons
	BROKENSWORD(9, .05, new ItemCreator("Broken Sword", new Stats(Material.STICK, ItemUse.WEAPON, 10, 10, 0, 30, 0, 0), (byte)0)),
	FOUNDONTHEGROUND(10, .05, new ItemCreator("Found on the Ground", Reforge.HOPELESS, new Stats(Material.IRON_AXE, ItemUse.WEAPON, 20, 20, 30, 40, 0, 0), (byte)1)),
	//EASTEREGG FOUND THIS ITEM VIA SECRET PNJ
	
	//Armors
	RECOVERYOFTHEGRASSLANDHELMET(14, 1, new ItemCreator("Grassland Helmet", new Stats(Material.CHAINMAIL_HELMET, ItemUse.ARMOR, 0, 0, 0, 0, 10, 0), (byte)0)),
	RECOVERYOFTHEGRASSLANDCHESTPLATE(14.1, 1, new ItemCreator("Grassland Chestplate", new Stats(Material.LEATHER_CHESTPLATE, ItemUse.ARMOR, 0, 0, 0, 0, 10, 0), (byte)0)),
	RECOVERYOFTHEGRASSLANDLEGGINGS(14.2, 1, new ItemCreator("Grassland Leggings", new Stats(Material.CHAINMAIL_LEGGINGS, ItemUse.ARMOR, 0, 0, 0, 0, 10, 0), (byte)0)),
	RECOVERYOFTHEGRASSLANDBOOTS(14.3, 1, new ItemCreator("Grassland Boots", new Stats(Material.LEATHER_BOOTS, ItemUse.ARMOR, 0, 0, 0, 0, 10, 0), (byte)0)),
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
	
	public static GameItem getItemById(double id) {
		
		for (GameItem gameItems : GameItem.values()) {
			if (gameItems.id == id) {
				return gameItems;
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

