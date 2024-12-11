package eu.isweezee.mmo.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.enums.ClazzType;
import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.ItemCreator.Reforge;
import eu.isweezee.mmo.extra.UtilsFactory;

public class PlayerEquipmentUpdateManager {
	
	public static void run() {
		
		new BukkitRunnable() {
			
			Map<Player, ItemStack[]> lastArmors = new HashMap<>();
			
			@Override
			public void run() {
				
				for (Player players : Bukkit.getOnlinePlayers()) {
					
					ItemStack[] armors = players.getInventory().getArmorContents();
					if (lastArmors.get(players) == armors)continue;
					lastArmors.put(players, armors);
					PlayerData data = MMO.dataStorage.get(players.getUniqueId());
					if (data == null)continue;
					StatsModifier mod = data.getStatsModifier();
					ItemStack holdWeapon = players.getInventory().getItemInMainHand();
					double damage = getValue(holdWeapon, armors, "damage") + 3;
					double strenght = getValue(holdWeapon, armors, "strenght") + data.getStrenght() + (data.getClazzType() == ClazzType.WARRIOR && data.getLevel() < 5 ? 20 : (UtilsFactory.combattant * data.getLevel()));													
					double critDamage = getValue(holdWeapon, armors, "critdamage") + data.getCritDamage() + (data.getClazzType() == ClazzType.WARRIOR && data.getLevel() < 5 ? 20 : (UtilsFactory.combattantCrit * data.getLevel()));
					double critChance = getValue(holdWeapon, armors, "critchance") + data.getCritChance();
					
					mod.setModifiedDamage(damage);
					mod.setModifiedStrenght(strenght);
					mod.setModifiedCritDamage(critDamage);
					mod.setModifiedCritChance(critChance);
					
					players.getInventory().setItem(10, UtilsFactory.buildItem(Material.EMERALD, 1, 
							"&aYour profile",
							"&7Level: &9&l" + data.getLevel(),
							"&7Class: &e&l" + data.getClazzType().name(),
							"",
							"&7Damage: &c&l" + mod.getModifiedDamage(),
							"&7Health: &a&l" + data.getHealth(),
							"&7Strenght: &6&l" + mod.getModifiedStrenght(),
							"&7Crit Chance &c&l" + mod.getModifiedCritChance(),
							"&7Crit Damage &c&l+" + mod.getModifiedCritDamage(),
							"",
							"&e&lClick here to open your settings."));
				}
				
			}
		}.runTaskTimer(MMO.getPlugin(MMO.class), 0, 20);
		
	}
	
	private static int getValue(ItemStack item, ItemStack[] parts, String fieldName) {
		ItemCreator itemCreator = GameItem.getItemCreatorFromItemStack(item);
		int value = 0;
		try {
			if (itemCreator != null) {
				Reforge reforge = Reforge.getReforgeByName(item.getItemMeta().getDisplayName().split(" ")[0]);
				if (reforge != null && reforge != Reforge.NONE)
					value += reforge.getValue(fieldName);
				value += (int) new PropertyDescriptor(fieldName, ItemCreator.class).getReadMethod().invoke(itemCreator);
			}
			for (ItemStack part : parts) {
				itemCreator = GameItem.getItemCreatorFromItemStack(part);
				if (itemCreator == null)continue;
				value += (int) new PropertyDescriptor(fieldName, ItemCreator.class).getReadMethod().invoke(itemCreator);
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;
	}
	
}










