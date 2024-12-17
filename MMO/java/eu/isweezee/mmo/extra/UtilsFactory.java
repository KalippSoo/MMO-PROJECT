package eu.isweezee.mmo.extra;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.data.PlayerData;
import eu.isweezee.mmo.entities.DropLoots;
import eu.isweezee.mmo.entities.entities.Void2;
import eu.isweezee.mmo.entities.spawnSystem.Spawn;
import eu.isweezee.mmo.enums.ClazzType;
import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.enums.Rarity;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.WorldServer;

public class UtilsFactory {

	//STATS
	public static final double combattant = 3;
	public static final double combattantCrit = 1.5;
	
	public static String color(String args) {
		return ChatColor.translateAlternateColorCodes('&', args);
	}
	
	public static List<String> color(List<String> args) {
		List<String> lines = new ArrayList<>();
		for (String line : args) {
			lines.add(color(line));
		}
		return lines;
	}
	
	public static String stripColor(String args) {
		return ChatColor.stripColor(args);
	}
	
	public static ItemStack buildItem(Material mat, int amount, String name, String... lines) {
		
		ItemStack item = new ItemStack(mat, amount == 0 ? 1 : amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name == null ? null : color(name));
		meta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
		List<String> tempList = new ArrayList<>();
		for (String line : lines) {
			tempList.add(color(line));
		}
		meta.setLore(tempList);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack buildItem(ItemStack copy, String... lines) {
		
		ItemStack item = new ItemStack(copy.getType(), copy.getAmount());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(copy.getItemMeta().getDisplayName() == null ? null : color(copy.getItemMeta().getDisplayName()));
		meta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
		List<String> tempList = new ArrayList<>();
		for (String line : lines) {
			tempList.add(color(line));
		}
		meta.setLore(tempList);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static Class<?> getNMSClass(String str) {
		try {
			return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + str);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
//	private static int c(String pattern, String[] args) {
//		
//		int value = 0;
//		
//		for (String line : args) {
//			if (line.trim().startsWith(pattern)) {
//				int i = Integer.parseInt(line.substring(4));
//				return i;
//			}
//		}
//		
//		return value;
//	}
	
//	public static ItemStack tranformItemFromMongoDB(int id) {
//		
//		MongoCollection<Document> items = MMO.items;
//		
//		Document found = items.find(new Document("id", id)).first();
//		
//		if (found != null) {
//			
//			Document stats = (Document) found.get("stats");
//			
//			String[] jsonExtract = stripColor(stats.toJson().replace("{", "").replace("\"", "").replace("}", "").trim()).split(",");
//			
//			Material mat = Material.valueOf((String) NotNull(stats, "m"));
//			String name = (String) NotNull(stats, "n") == null ? "" : (String) NotNull(stats, "n");
//			
//			return new ItemCreator(mat, name, c("r", jsonExtract), c("d", jsonExtract), c("s", jsonExtract), c("C", jsonExtract), c("D", jsonExtract), c("h", jsonExtract), c("a", jsonExtract)).get();
//			
//		}
//		else {
//			System.err.println("L'id de cette 'item' n'est pas reconnue par la base de donnée !");
//		}
//		return null;
//		
//		
//	}
	
//	private static Object NotNull(Document document, String path) {
//		
//		if (document.get(path) != null) {
//			return document.get(path);
//		}else {
//			System.out.println("Can't found the data on " + document.toString() + " (via the path" + path + ")");
//			
//		}
//		
//		return null;
//	}

	public static void InventorySet(Player player) {

		PlayerData data = MMO.dataStorage.get(player.getUniqueId());
		
		player.getInventory().clear();
		
		int[] magicItems = new int[] {4, 5, 6, 7, 8};
		
		for (int i = 0; i < magicItems.length; i++) {
			player.getInventory().setItem(magicItems[i], buildItem(Material.GRAY_STAINED_GLASS_PANE, 1, 
				"&7Emplacement de sort &e" + (i+1) + "&7 (vide)",
				"",
				"&e&lClique droit pour modifier le sort"));
		}
		
		player.getInventory().setItem(9, buildItem(Material.SADDLE, 1, "&aTes montures"));
		player.getInventory().setItem(10, buildItem(Material.EMERALD, 1, 
				"&aTon profil",
				"&7Niveau: &9&l" + data.getLevel(),
				"&7Classe: &e&l" + data.getClazzType().name(),
				"",
				"&7Point de vie: &c&l" + data.getHealth(),
				"&7Force: &6&l" + data.getStrenght(),
				"&7Chances critique &c&l" + data.getCritChance(),
				"&7Dégâts critique &c&l+" + data.getCritDamage(),
				"",
				"&7Cliquez ici pour avoir",
				"&7un meilleur visuel sur vos,",
				"&7équipement, runes etc..."));
		player.getInventory().setItem(18, buildItem(Material.RED_CARPET, 1, "&aCollection"));
		player.getInventory().setItem(27, buildItem(Material.TOTEM_OF_UNDYING, 1, "&aSac d'artefacts"));
		player.getInventory().setItem(28, buildItem(Material.CHEST, 1, "&aSac"));

		ClazzType.setupClass(player);
	}
	
	public static PlayerData applyData(Player player) {
		
		DocumentRelated.createOrGetCollectionForPlayer(player);
		PlayerData data = DocumentRelated.putMongoDBIntoJavaObjectPlayer(player);
		UtilsFactory.InventorySet(player);
		
		MMO.dataStorage.get(player.getUniqueId()).setInventory(player, (Document) ((Document)MMO.players.find(new Document("uuid", player.getUniqueId().toString())).first().get("profile")).get("inventory"));
		return data;
	}
	
	
	public static Entity Spawn(Class<? extends EntityInsentient> clazz, Location location) {
		
		try {
			Entity instance = clazz.getConstructor(Location.class).newInstance(location);
			WorldServer worldServer = ((CraftWorld)location.getWorld()).getHandle();
			worldServer.addEntity(instance, SpawnReason.CUSTOM);
			return instance;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void DamageIndictor(Player player, Location loc, double damage, double offset) {
		DamageIndictor(player, loc, damage, false, offset);
	}
	
	public static void DamageIndictor(Player player, Location loc, double damage, boolean isCrit, double offset) {
		
		ArmorStand stand = player.getWorld().spawn(loc.clone().add(getRandomOffset(offset), 1.25 + getRandomOffset(offset), getRandomOffset(offset)), ArmorStand.class);
		stand.setGravity(false);
		stand.setInvisible(true);
		stand.setMarker(true);
		stand.setCustomNameVisible(true);
		stand.setCustomName(UtilsFactory.color(!isCrit ? "&c" + damage : "&c&l★" + damage + "★"));
		
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object connection = handle.getClass().getField("playerConnection").get(handle);
			
			//Packet
			Object packet = getNMSClass("PacketPlayOutSpawnEntity").getConstructor(Entity.class).newInstance(((CraftEntity)stand).getHandle());
			Object removePacket = getNMSClass("PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[] {((CraftEntity)stand).getEntityId()});
			connection.getClass().getMethod("sendPacket", Packet.class).invoke(connection, packet);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					try {
						connection.getClass().getMethod("sendPacket", Packet.class).invoke(connection, removePacket);
						stand.remove();
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cancel();
				}
			}.runTaskLater(MMO.getPlugin(MMO.class), 30);
			
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double getRandomOffset(double offset) {
		
		double random = (Math.random()) * offset;
		if (Math.random() >= .5) random *= -1;
		return random;
	}

	public static void playerJoinServer(Player player) {
		if (applyData(player) == null)
			player.kickPlayer(UtilsFactory.color("&c&lDATA ERROR\n&fIt seems like your data wasn't load properly\n\nPlease rejoin !"));
		
		player.sendTitle(color("&aWelcome " + player.getName()), color("&7Have a good time in here"), 5, 30, 5);
	}

	public static PlayerData getPlayerData(UUID uniqueId) {
		
		return MMO.dataStorage.get(uniqueId);
	}
	
	public static void Kick(Player player, int errorId) {
		
		/*
		 * Code 45 : Null data profile (most of time a rejoin will fixed the problem)
		 * 
		 * 
		 */
		
		player.kickPlayer(color("&cAn error has occured\n\n&fYou have been kicked from a unexpected event &7(error id: " + errorId + "\n&fPlease rejoin !"));
	}

	public static void onEnable(MMO mmo) {
		
		new Spawn(Void2.class, 5, 5, 40);
		
	}
	
	
	public static void dropDeathLoot(DamageSource damagesource, int i, boolean flag, DropLoots dropLoots) {
		if (dropLoots.loots.isEmpty())return;
		Entity entity = damagesource.getEntity();
		if (entity instanceof EntityPlayer) {
			Player player = (Player) entity.getBukkitEntity();
			ItemStack itemStack = dropLoots.drop();
			if (itemStack == null)return;
			itemStack = itemStack.clone();
			itemStack.setAmount(1);
			ItemCreator creator = GameItem.getItemCreatorFromItemStack(itemStack);
			if (creator == null)return;
			if (GameItem.getIdFromItem(itemStack) == -1)return;
			MMO.dataStorage.get(player.getUniqueId()).getSack().getInventory().addItem(itemStack);
			
			player.sendMessage(UtilsFactory.color(Rarity.getOrdinal(creator.getRarity()).getStr() + " DROP! &fYou have dropped x" + itemStack.getAmount() + " " + itemStack.getItemMeta().getDisplayName()));
			
			
		}
	}
	
}








