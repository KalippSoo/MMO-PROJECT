package eu.squidcraft.npc.hologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import eu.squidcraft.npc.NPCPlugin;
import eu.squidcraft.npc.Reflection;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;

public class HologramManager {
	
	public List<Hologram> holograms = new ArrayList<>();
	public Map<Hologram, List<EntityArmorStand>> entities = new HashMap<>();
	
	NPCPlugin plugin;
	
	public HologramManager(NPCPlugin plugin) {
		this.plugin = plugin;
	}
	
	public List<EntityArmorStand> packetLines(Player player, Hologram hologram){
		
		List<EntityArmorStand> armorStand = new ArrayList<>();
		
		for (String text : hologram.getLines()) {
			
			if (text == null || text.equals("")) {
				hologram.getLocation().add(0, .35, 0);
				continue;
			}
			EntityArmorStand stand = new EntityArmorStand(((CraftWorld)player.getWorld()).getHandle(), hologram.getLocation().getX(), hologram.getLocation().getY(), hologram.getLocation().getZ());
			((ArmorStand)stand.getBukkitEntity()).setMarker(true);
			stand.setCustomNameVisible(true);
			stand.setNoGravity(true);
			stand.setInvisible(true);
			
			stand.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', text)));
			
			try {
				
				Reflection.sendPacket(player, new PacketPlayOutSpawnEntityLiving(stand));
				Reflection.sendPacket(player, new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
				
			} catch (Exception e) {}
			
			stand.setCustomNameVisible(true);
			
			entities.put(hologram, armorStand);
			armorStand.add(stand);
			hologram.getLocation().add(0, .35, 0);
		}
		
		hologram.setLocation(hologram.realLocation.clone());
		return armorStand;
		
	}
	
	public void check() {
		
		if (Bukkit.getOnlinePlayers().isEmpty())return;

		List<Hologram> removal = new ArrayList<>();
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			for (Hologram hologram : holograms) {
				
				if (hologram == null || hologram.getLines().isEmpty()) {
					removal.add(hologram);
					continue;
				}
				if (hologram.getDistance() == -1)continue;
				if (hologram.getLocation().getWorld().getName() != players.getWorld().getName())continue;
				
				hologram.setPlayer(players);
				
				double currentDistance = calculateDistanceBetweenPoints(players.getLocation().getBlockX(), players.getLocation().getBlockZ(), hologram.getLocation().getBlockX(), hologram.getLocation().getBlockZ());
				
				if (!hologram.getIsActive().containsKey(players)) {
					hologram.getIsActive().put(players, false);
				}
				
				if (currentDistance <= hologram.getDistance()) {
					if (!hologram.getIsActive().get(players)) {
						hologram.addHologram();
						hologram.getIsActive().put(players, true);
						continue;
					}
					if (hologram.isUpdate()) {
						hologram.removeHologram();
						hologram.addHologram();
						continue;
					}
					
				}
				if (currentDistance > hologram.getDistance() && hologram.getIsActive().get(players)) {
					hologram.removeHologram();
					hologram.getIsActive().put(players, false);
				}
				hologram.setPlayer(null);
			}
			
		}
		this.holograms.removeAll(removal);
		
	}
	
	private double calculateDistanceBetweenPoints(
			  double x1, 
			  double y1, 
			  double x2, 
			  double y2) {       
			    return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
			}
}
