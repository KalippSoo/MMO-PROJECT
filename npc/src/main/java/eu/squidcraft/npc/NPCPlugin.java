package eu.squidcraft.npc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import eu.squidcraft.npc.hologram.HologramManager;
import eu.squidcraft.npc.npc.NPC;

public class NPCPlugin extends JavaPlugin{
	
	private HologramManager hologramManager;
	private Map<String, String> lines = new HashMap<>();
	
	public ConfigManager npcs;
	
	@Override
	public void onEnable() {
		
		hologramManager = new HologramManager(this);
		npcs = new ConfigManager(this, "npcs", ".yml");
		
		NPC.loadNPCs(npcs.getConfig());
		
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
						
				NPC.distanceNPC();
				hologramManager.check();
			}
		}.runTaskTimer(this, 0, 20);
		
	}

	public HologramManager getHologramManager() {
		return hologramManager;
	}

	public Map<String, String> getLines() {
		return lines;
	}

}
