package eu.isweezee.mmo.entities.spawnSystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.entities.EntityType;
import eu.isweezee.mmo.extra.UtilsFactory;
import net.minecraft.server.v1_16_R3.Entity;

public class Spawn {
	
	public List<Entity> entities = new ArrayList<>();
	
	public Spawn(Class<? extends EntityType> entityTarget, int mobCap, int size, int delay){
		
		World world = Bukkit.getWorld("world");
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				 List<Entity> removal = new ArrayList<>();
				for (Entity entity : entities) {
					if (!entity.getBukkitEntity().isValid() || entity.getBukkitEntity().isDead())removal.add(entity);
				}
				entities.removeAll(removal);
				
				int diff = mobCap - entities.size();
				if (diff <= 0)return;
				int spawnAmount = (int) Math.random() * (mobCap + 1), count = 0;
				while(count <= spawnAmount) {
					count++;
					int randX = getRandomWithNeg(size) + -6, randZ = getRandomWithNeg(size) + 41;
					Block block = world.getHighestBlockAt(randX, randZ);
					Location loc = block.getLocation().clone().add(0, 1, 0);
					
					entities.add(UtilsFactory.Spawn(entityTarget, loc));
				}
			}
		}.runTaskTimer(MMO.getPlugin(MMO.class), 0, delay);
		
	}
	
	private int getRandomWithNeg(int size) {
		
		int random = (int) (Math.random() * (size +1));
		if (Math.random() > .5) random *= -1;
		return random;
	}
}
