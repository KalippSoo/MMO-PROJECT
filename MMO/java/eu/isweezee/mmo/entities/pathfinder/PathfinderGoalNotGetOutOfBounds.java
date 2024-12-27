package eu.isweezee.mmo.entities.pathfinder;

import java.util.EnumSet;

import org.bukkit.Location;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.SoundEffects;

public class PathfinderGoalNotGetOutOfBounds extends PathfinderGoal{

	private final EntityCreature a; //Creature
	private final int range;
	private Location location;
		
	public PathfinderGoalNotGetOutOfBounds(EntityCreature a, int range) {
		this.a = a;
		this.range = range;
		this.a(EnumSet.of(Type.MOVE));
	}
		
	@Override
	public boolean a() {
			
		if (a == null)
			return false;
		else if (this.location == null) {
			Location loc = this.a.getBukkitEntity().getLocation().clone();
			this.location = (loc.getBlockX() != 0 && loc.getBlockZ() != 0) ? loc : null;
			return false;
		}
		else if (this.a.getBukkitEntity().getLocation().distance(this.location) < range)
			return false;
		
		this.a.playSound(SoundEffects.BLOCK_ANVIL_FALL, 1.0f, 1.0f);
		this.a.getBukkitEntity().teleport(this.location);
		
		return true;
	}

}
