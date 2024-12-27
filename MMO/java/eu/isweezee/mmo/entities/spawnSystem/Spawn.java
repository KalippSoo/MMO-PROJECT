package eu.isweezee.mmo.entities.spawnSystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.extra.UtilsFactory;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityInsentient;

public class Spawn {
	
	public List<Entity> entities = new ArrayList<>();
	
	public Spawn(Class<? extends EntityInsentient> entityTarget, int mobCap, int size, int delay){
		
		new BukkitRunnable() {
			
			World world = Bukkit.getWorld("spawn");
			
			@Override
			public void run() {
				if (world == null) {
					world = Bukkit.getWorld("spawn");
					return;
				}
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
					int randX = getRandomWithNeg(size), randZ = getRandomWithNeg(size);
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
