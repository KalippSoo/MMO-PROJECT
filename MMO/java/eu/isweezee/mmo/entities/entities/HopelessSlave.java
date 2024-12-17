	package eu.isweezee.mmo.entities.entities;

import org.bukkit.Location;

import eu.isweezee.mmo.entities.DropLoots;
import eu.isweezee.mmo.entities.EntityType;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.SoundEffects;

public class HopelessSlave extends EntityType{

	public HopelessSlave(Location location) {
		super(EntityTypes.ZOMBIE, location, 4);
	}

	@Override
	public DropLoots setDropLoots() {
		
		DropLoots loots = new DropLoots();
		loots.add(1);
		return loots;
	}
	
	@Override
	public int WhatsTheId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String GimmeMyName() {
		// TODO Auto-generated method stub
		return "&7Hopeless Slave";
	}

	@Override
	public void initPathfinderMethod() {

		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
	}

	@Override
	public double health() {
		// TODO Auto-generated method stub
		return 100;
	}
	
	@Override
	public boolean attackEntity(Entity entity) {
		super.attackEntity(entity);
		this.playSound(SoundEffects.AMBIENT_CAVE, 1.0F, 1.0F);
		this.world.broadcastEntityEffect(entity, (byte) 30);
		return true;
	}

	@Override
	public double follow() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public double howMuchExpIDrop() {
		// TODO Auto-generated method stub
		return 3;
	}
	
}
