package eu.isweezee.mmo.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.isweezee.mmo.enums.ClazzType;
import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.ItemCreator.Reforge;

public class PlayerData {

	private int damage;
	private int health;
	private int armor;
	private int strenght;
	private String title;
	private ClazzType clazzType;
	private Collection collection;
	private int critDamage;
	private int critChance;
	private int level;
	private StatsModifier statsModifier;
	private Sack sack;
	private Sack artefacts;
	private SpellSlots spellSlots;
	
	public PlayerData(Collection collection, int health, int armor, int strenght, String title, ClazzType clazzType, int critDamage,
			int critChance, int level) {
		
		this.health = health;
		this.armor = armor;
		this.strenght = strenght;
		this.title = title;
		this.clazzType = clazzType;
		this.critDamage = critDamage;
		this.critChance = critChance;
		this.level = level;
		this.collection = collection;
		this.statsModifier = new StatsModifier();
	}
	
	public List<String> saveInventory(Inventory inv){
		
		List<String> dataString = new ArrayList<>();
		int[] iTable = {0, 1};
		
		for (int i = 0; i<=iTable.length;i++) {
			ItemStack item = inv.getItem(i);
			if (item == null || item.getType() == Material.AIR)continue;
			GameItem gameItem = GameItem.getGameItemFromItemStack(item);
			ItemCreator itemCreator = gameItem.getItem();
			if (itemCreator == null)continue;
			dataString.add(i + "_" + gameItem.getId() + "_" + itemCreator.getReforge().getId());
		}
		
		return dataString;
	}
	
	public void setInventory(Player player, Document inventory) {
		List<String> inventoryItems = new ArrayList<>(inventory.getList("items", String.class));
		if (!inventoryItems.isEmpty()) {
			for (String dataStrings : inventoryItems) {
				String[] splitData = dataStrings.split("_");
				
				int slot = Integer.parseInt(splitData[0]);
				double itemID = Double.parseDouble(splitData[1]);
				int reforgeID = Integer.parseInt(splitData[2]);
				
				ItemCreator itemCreator = GameItem.getItemById(itemID, 1).clone();
				ItemCreator newInstance = null;
				if (reforgeID >= 0) {
					newInstance = new ItemCreator(itemCreator.getName(), Reforge.getReforgeByID(reforgeID), itemCreator.getStats(), (byte)itemCreator.getRarity(), itemCreator.getLines());
				}
				
				player.getInventory().setItem(slot, newInstance != null ? newInstance.get() : itemCreator.get());
			}
		}
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getStrenght() {
		return strenght;
	}

	public void setStrenght(int strenght) {
		this.strenght = strenght;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ClazzType getClazzType() {
		return clazzType;
	}

	public void setClazzType(ClazzType clazzType) {
		this.clazzType = clazzType;
	}

	public int getCritDamage() {
		return critDamage;
	}

	public void setCritDamage(int critDamage) {
		this.critDamage = critDamage;
	}

	public int getCritChance() {
		return critChance;
	}

	public void setCritChance(int critChance) {
		this.critChance = critChance;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Collection getCollection() {
		return collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	public StatsModifier getStatsModifier() {
		return statsModifier;
	}

	public Sack getSack() {
		return sack;
	}

	public void setSack(Sack sack) {
		this.sack = sack;
	}

	public Sack getArtefacts() {
		return artefacts;
	}

	public void setArtefacts(Sack artefacts) {
		this.artefacts = artefacts;
	}

	public SpellSlots getSpellSlots() {
		return spellSlots;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	
	
}
