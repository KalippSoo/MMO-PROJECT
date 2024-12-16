package eu.squidcraft.npc.hologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import eu.squidcraft.npc.NPCPlugin;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class Hologram {

	private Location location;
	public Location realLocation;
	private boolean update;
	private List<String> lines = new ArrayList<>();
	private List<EntityArmorStand> entities = new ArrayList<>();
	List<EntityArmorStand> removal = new ArrayList<>();
	private double distance;
	private Map<Player, Boolean> isActive = new HashMap<>();
	final HologramManager manager;
	Player player;

	public Hologram() {
		this.manager = NPCPlugin.getPlugin(NPCPlugin.class).getHologramManager();
	}
	
	public Hologram(Location location, boolean update, List<String> lines, double distance) {
		this.manager = NPCPlugin.getPlugin(NPCPlugin.class).getHologramManager();
		if (lines == null) {
			this.delete();
		}
		this.realLocation = location;
		this.location = realLocation.clone();
		this.lines = lines;
		this.update = update;
		this.distance = distance;
	}
	
	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	
	public Hologram clone() {
		
		Hologram hologram = new Hologram();
		
		if (this.lines != null) {
			hologram.setLines(new ArrayList<>(lines));
		}
		if (hologram.location != null) {
			hologram.setLocation(location);
		}
		hologram.setUpdate(update);
		
		return hologram;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Map<Player, Boolean> getIsActive() {
		return isActive;
	}

	public void setIsActive(Map<Player, Boolean> isActive) {
		this.isActive = isActive;
	}

	public List<EntityArmorStand> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityArmorStand> entities) {
		this.entities = entities;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void addHologram() {

		entities.addAll(manager.packetLines(player, this));
	}
	
	public void removeHologram() {
		
		for (EntityArmorStand displays : entities) {
			
			try {
				PlayerConnection connection = ((CraftPlayer)this.getPlayer()).getHandle().playerConnection;
				connection.sendPacket(new PacketPlayOutEntityDestroy(displays.getId()));
				if (lines.contains(displays.getCustomName().getText())) {
					removal.add(displays);
				}
			} catch (Exception e) {}
		}
		entities.removeAll(removal);
		removal.clear();
	}
	
	public void create() {
		this.manager.holograms.add(this);

	}
	
	public String toString() {
		return "{Hologram=lines(" + lines.size() + "),update(" + isUpdate() + "),distance(" + distance + ")}";
	}
	
	public void delete() {
		
		for (EntityArmorStand entities : this.entities) {

			for (Player player : isActive.keySet()) {
				if (isActive.get(player)) {
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entities.getId()));
				}
				
			}
		}
		
		this.manager.holograms.remove(this);
	}
}












