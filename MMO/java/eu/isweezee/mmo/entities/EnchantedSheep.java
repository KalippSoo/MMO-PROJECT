	package eu.isweezee.mmo.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Location;

import eu.isweezee.mmo.enums.GameItem;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.IEntityAngerable;
import net.minecraft.server.v1_16_R3.IntRange;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.SoundEffects;
import net.minecraft.server.v1_16_R3.TimeRange;

public class EnchantedSheep extends EntityType implements IEntityAngerable{

	private UUID angerTarget = null;
	private static final DataWatcherObject<Integer> bp;
	private static final IntRange bq;
	
	static {
		bp = DataWatcher.a(EnchantedSheep.class, DataWatcherRegistry.b);
		bq = TimeRange.a(20, 39);
	}
	public EnchantedSheep(Location location) {
		super(EntityTypes.PIGLIN, location, 1);
	}

	@Override
	public DropLoots setDropLoots() {
		
		DropLoots loots = new DropLoots();
		loots.add(GameItem.FIRE_SPHERE);
		loots.add(GameItem.DARK_SPHERE);
		
		return loots;
	}
	
	
	@Override
	public int WhatsTheId() {
		// TODO Auto-generated method stub
		return 4547;
	}

	@Override
	public String GimmeMyName() {
		// TODO Auto-generated method stub
		return "&7Slave";
	}

	@Override
	public void initPathfinderMethod() {

		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
	}

	@Override
	public double health() {
		// TODO Auto-generated method stub
		return 50;
	}
	
	@Override
	public boolean attackEntity(Entity entity) {
		super.attackEntity(entity);
		this.playSound(SoundEffects.AMBIENT_CAVE, 1.0F, 1.0F);
		this.world.broadcastEntityEffect(entity, (byte) 30);
		return true;
	}
	
	@Override
	public int getAnger() {
		return (Integer) this.datawatcher.get(bp);
	}
	
	@Override
	public void setAnger(int i) {
		this.datawatcher.set(bp, i);
	}

	@Override
	public @Nullable UUID getAngerTarget() {
		// TODO Auto-generated method stub
		return angerTarget;
	}

	@Override
	public void setAngerTarget(@Nullable UUID var1) {
		this.angerTarget = var1;
	}

	@Override
	public void anger() {
		this.setAnger(bq.a(this.random));
		
	}

	@Override
	public double follow() {
		// TODO Auto-generated method stub
		return 15;
	}
	
}
