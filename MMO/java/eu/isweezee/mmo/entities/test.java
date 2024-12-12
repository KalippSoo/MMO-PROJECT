package eu.isweezee.mmo.entities;

import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_16_R3.EntityCow;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalFollowParent;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_16_R3.PathfinderGoalWrapped;
import net.minecraft.server.v1_16_R3.WorldServer;

public class test extends EntityCow{

	public test(Location location) {
		super(EntityTypes.COW, ((CraftWorld)location.getWorld()).getHandle());
		this.setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
		this.setAggressive(true);
		System.out.println(this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getBaseValue());
		
		reset(targetSelector);
	}
	
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.25D));
		this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
		this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
	}

	public static void spawn(Location location) {
		test t = new test(location);
		WorldServer worldServer = ((CraftWorld)location.getWorld()).getHandle();
		worldServer.addEntity(t, SpawnReason.CUSTOM);
	}
	
	@SuppressWarnings("unchecked")
	private void reset(PathfinderGoalSelector p) {
		
		try {
			Field field = p.getClass().getDeclaredField("d");
			field.setAccessible(true);
			Set<PathfinderGoalWrapped> paths = (Set<PathfinderGoalWrapped>) field.get(p);
			field.setAccessible(false);
			paths.clear();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}















