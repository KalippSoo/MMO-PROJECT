package eu.isweezee.mmo.extra;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.isweezee.mmo.enums.ItemUse;
import eu.isweezee.mmo.enums.Rarity;

public class ItemCreator{
	
	private Stats stats;
	private String name;
	private byte rarity;
	private String[] lines;
	private Reforge reforge;
	
	public ItemCreator(String name, Stats stats, byte rarity, String... lines) {
		this(name, Reforge.NONE, stats, rarity, lines);
	}
	
	public ItemCreator(String name, Reforge reforge, Stats stats, byte rarity, String... lines) {
	
		this.stats = stats;
		this.reforge = reforge;
		this.name = name;
		this.rarity = rarity;
		this.lines = lines;
		
		List<String> structure = new ArrayList<>();
		
		structure.add("&7Damage: &c");
		structure.add("&7Strenght: &c");
		structure.add("&7Crit Chance: &e");
		structure.add("&7Crit Damage: &e");
		structure.add("&7Health: &a");
		structure.add("&7Armour: &a");
		
		ItemStack tempItem = new ItemStack(stats.mat);
		ItemMeta meta = tempItem.getItemMeta();
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON);
		meta.setDisplayName(UtilsFactory.color(Rarity.getOrdinal(rarity).getRarityColor() + reforge.getName() + name));
		List<String> lores = new ArrayList<>();
		for (Field fields : this.stats.getClass().getDeclaredFields()) {
			if (fields.getType().getName() != "int")continue;
			try {
				int i = fields.getInt(this.stats);
				if (i > 0) {
					lores.add(UtilsFactory.color(structure.get(0) + i));
				}
				structure.remove(0);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (String line : lines) {
			lores.add(UtilsFactory.color(line));
		}
		
		if (reforge.getId() >= 0) {
			lores.add("");
			lores.add(UtilsFactory.color("&7This item is using a reforge"));
			lores.addAll(UtilsFactory.color(reforge.getLines()));
		}
		
		lores.add("");
		if (stats.use == ItemUse.SPELL) {
			lores.add(UtilsFactory.color("&e&lClick here to choose a spell !"));
		}else {
			String str = !(stats.use == ItemUse.ARMOR || stats.use == ItemUse.WEAPON || stats.use == ItemUse.ARTEFACTS) ? "" : " " + stats.use.name();
			lores.add(UtilsFactory.color(Rarity.getOrdinal(rarity).getStr() + str));
		}
		meta.setLore(lores);
		tempItem.setItemMeta(meta);
		
		stats.item = tempItem;
		
	}
	
	public ItemCreator clone() {
		
		return new ItemCreator(name, getReforge(), stats, rarity, lines);
		
	}
	
	public ItemStack get() {
		return stats.item;
	}

	public Material getMat() {
		return stats.mat;
	}

	public ItemUse getUse() {
		return stats.use;
	}

	public int getDamage() {
		return stats.damage;
	}
	
	public void setDamage(int value) {
		this.stats.damage = value;
	}

	public int getStrenght() {
		return stats.strenght;
	}

	public void setStrenght(int value) {
		this.stats.strenght = value;
	}
	
	public int getCritchance() {
		return stats.critchance;
	}
	
	public void setCritchance(int value) {
		this.stats.critchance = value;
	}

	public int getCritdamage() {
		return stats.critdamage;
	}
	
	public void setCritdamage(int value) {
		this.stats.critdamage = value;
	}
	
	public int getHeatlh() {
		return stats.heatlh;
	}
	
	public void setHealth(int value) {
		this.stats.heatlh = value;
	}

	public int getArmor() {
		return stats.armor;
	}
	
	public void setArmor(int value) {
		this.stats.armor = value;
	}
	
	public Stats getStats() {
		return stats;
	}
	
	public String getName() {
		return name;
	}
	
	public int getRarity() {
		return rarity;
	}
	
	public Reforge getReforge() {
		return reforge;
	}
	
	public void setReforge(Reforge reforge) {
		this.reforge = reforge;
	}
	
	public String[] getLines() {
		return lines;
	}
	
	public static class Stats{
		
		Material mat;
		ItemUse use;
		
		int 
		damage,
		strenght,
		critchance,
		critdamage,
		heatlh,
		armor;

		public ItemStack item;
		
		public Stats(Material mat, ItemUse use, int damage, int strenght, int critchance, int critdamage, int heatlh,
				int armor) {
			super();
			this.mat = mat;
			this.use = use;
			this.damage = damage;
			this.strenght = strenght;
			this.critchance = critchance;
			this.critdamage = critdamage;
			this.heatlh = heatlh;
			this.armor = armor;
		}
		
	}
	
	public static enum Reforge{
		
		NONE(-1, "", Arrays.asList()),
		BLOSSOM(0, "Bitch ", Arrays.asList("&bBitch &7grant you &e210 &7crit damage !")),
		GODDESS(1, "Godlike ", Arrays.asList("&bGodlike &7grant you:", "&e250 &7crit damage !", "&e75 &7damage !", "&e75 &7strenght !")),
		;
		
		private int id;
		private String name;
		private List<String> lines;
		
		private Reforge(int id, String name, List<String> lines) {
			this.id = id;
			this.name = name;
			this.lines = lines;
		}
		
		public static Reforge getReforgeByID(int id) {
			for (Reforge reforges : values()) {
				if (reforges.getId() == id)
					return reforges;
			}
			return null;
		}
		
		public int getId() {
			return id;
		}

		public double getValue(String field) {
			
			switch (field) {
			case "damage":
				if (this == Reforge.GODDESS)return 75;
				break;
			case "strenght":
				if (this == Reforge.GODDESS)return 75;
				break;
			case "critdamage":
				if (this == Reforge.GODDESS)return 250;
				if (this == Reforge.BLOSSOM)return 120;
			case "critchance":
				break;
			default:
				break;
			}
			return 0;
		}

		public String getName() {
			return name;
		}

		public List<String> getLines() {
			return lines;
		}

		public static Reforge getReforgeByName(String string) {
			
			String targetName = UtilsFactory.stripColor(string);

			for (Reforge reforges : values()) {
				
				if (reforges.getName().trim().toLowerCase().equals(targetName.toLowerCase())) {
					return reforges;
				}
			}
			
			return null;
		}
	}
}












