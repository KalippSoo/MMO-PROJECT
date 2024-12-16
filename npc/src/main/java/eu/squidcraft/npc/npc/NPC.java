package eu.squidcraft.npc.npc;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class NPC{
	
	private static Map<UUID, NoPlayerCharacter> NPC = new HashMap<>();
	public static int distanceViewNPC = 5;
	
	static List<String> npcName = new ArrayList<>();
	
	public static void loadNPCs(FileConfiguration file) {
		
		if (file.getConfigurationSection("npc").getKeys(false).isEmpty())return;
		
		file.getConfigurationSection("npc").getKeys(false).forEach(uuids ->{
			
			String path = "npc." + uuids;
			String name = getAlphaNumericString(16);
			List<String> lines = file.getStringList(path + ".info.name");
			
			//Skin
			String signature = file.getString(path + ".skin.signature"), texture = file.getString(path + ".skin.texture");
			
			//Location
			path += ".location.";
			double x = file.getDouble(path + "x"), y = file.getDouble(path + "y"), z = file.getDouble(path + "z");
			float yaw = (float) file.getDouble(path + "yaw"), pitch = (float) file.getDouble(path + "pitch");
			
			World w = Bukkit.getWorld(file.getString(path + "world"));
			
			NoPlayerCharacter npc = new NoPlayerCharacter(w, name, lines, x, y, z, yaw, pitch, signature, texture);
			
			NPC.put(UUID.fromString(uuids), npc);
		});
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			PacketReader reader = new PacketReader();
			reader.inject(players);
			
		}
	}
	
	public static void distanceNPC() { 
		
		for (Player players: Bukkit.getOnlinePlayers()) {
			
			for (NoPlayerCharacter npcs : NPC.values()) {
				
				if (npcs.getLoaded().contains(players)) {
					if (players.getLocation().getWorld().getName() != npcs.getNpc().getBukkitEntity().getLocation().getWorld().getName()) {
						npcs.getLoaded().remove(players);
						npcs.remove(players);
						continue;
					}
				}
				
				int distance = (int) calculateDistanceBetweenPoints(players.getLocation().getBlockX(), players.getLocation().getBlockZ(), npcs.getNpc().getBukkitEntity().getLocation().getBlockX(), npcs.getNpc().getBukkitEntity().getLocation().getBlockZ());
				
				if (distance <= distanceViewNPC) {
					if (!npcs.getLoaded().contains(players)) {
						npcs.getLoaded().add(players);
						npcs.add(players);
						continue;
					}
				}
				if (distance > distanceViewNPC) {
					//spawnNPC
					if (npcs.getLoaded().contains(players)) {
						npcs.getLoaded().remove(players);
						npcs.remove(players);
						continue;
					}
				}
			}
		}
	}
	public static double calculateDistanceBetweenPoints(
			  double x1, 
			  double y1, 
			  double x2, 
			  double y2) {
			    return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
			}
	
	public static void removeNPCPacket() {
		
		for (NoPlayerCharacter npc : NPC.values()) {
			
			npc.getHologram().delete();
			
			for (Player players : Bukkit.getOnlinePlayers()) {
			
				PlayerConnection connection = ((CraftPlayer)players).getHandle().playerConnection;
			
				PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(npc.getNpc().getId());
			
				connection.sendPacket(destroy);
			
				PacketReader reader = new PacketReader();
				reader.uninject(players);
			}
		
		}
		NPC.clear();
	}

	public static Map<UUID, NoPlayerCharacter> getNpcs() {
		return NPC;
	}
	
	public static String[] getSkin(Player player, String name) {
		
		try {
			
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			
			URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(url2.openStream());
			JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();
			return new String[] {texture, signature};
		} catch (Exception e) {
			
			EntityPlayer p = ((CraftPlayer)player).getHandle();
			GameProfile profile = p.getProfile();
			Property property = profile.getProperties().get("textures").iterator().next();
			String texture = property.getValue();
			String signature = property.getSignature();
			return new String[] {texture, signature};
		}
		
	}
	
	private static String getAlphaNumericString(int size) {
		char[] str = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		
		String finalString = "";
		
		for (int i = 0; i < size; i++) {
			
			char c = str[ThreadLocalRandom.current().nextInt(str.length)];
			
			finalString = finalString + ((Math.random() > .5)  ? String.valueOf(c).toUpperCase() : String.valueOf(c));
		}
		
		return finalString;
	}
	
}











