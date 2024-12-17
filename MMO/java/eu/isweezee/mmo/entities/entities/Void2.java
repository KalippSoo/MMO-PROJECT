	package eu.isweezee.mmo.entities.entities;

import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;

import eu.isweezee.mmo.data.impletation.Type;
import eu.isweezee.mmo.entities.DropLoots;
import eu.isweezee.mmo.extra.UtilsFactory;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityWolf;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalOwnerHurtByTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalOwnerHurtTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R3.PathfinderGoalUniversalAngerReset;

public class Void2 extends EntityWolf implements Type{
	
	public ArmorStand stand;
	double health = 50;
	
	public Void2(Location l) {
		super(EntityTypes.WOLF, ((CraftWorld)l.getWorld()).getHandle());
		this.setLocation(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
		this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(health);
		this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(4.0D);
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(follow());
		this.setHealth((float) health);
		
		this.drops.clear();
		this.expToDrop = this.howMuchExpIDrop();
		
		stand = l.getWorld().spawn(l, ArmorStand.class, type ->{
			type.setGravity(false);
			type.setInvisible(true);
			type.setMarker(true);
			type.setCustomNameVisible(true);
			type.setCustomName(UtilsFactory.color(GimmeMyName()));
		});
	}
	
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(1, new PathfinderGoalFloat(this));
		this.goalSelector.a(4, new PathfinderGoalLeapAtTarget(this, 0.4F));
		this.goalSelector.a(5, new PathfinderGoalMeleeAttack(this, 1.0D, true));
		this.goalSelector.a(8, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
		this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
		this.targetSelector.a(3, (new PathfinderGoalHurtByTarget(this, new Class[0])).a(new Class[0]));
		this.targetSelector.a(7, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, false));
		this.targetSelector.a(8, new PathfinderGoalUniversalAngerReset<>(this, true));
	}
	
	@Override
	protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
		UtilsFactory.dropDeathLoot(damagesource, i, flag, setDropLoots());
	}
	
	int currentArmorStandTicks = 0, wait = 2;
	double height = getHeight();
	double lastHealth = 0;
	
	@Override
	public void tick() {
		super.tick();
		if (!dead) {
			
			this.ticksFarFromPlayer = 0;
			this.activatedTick = MinecraftServer.currentTick;
			
			((CraftArmorStand)stand).getHandle().activatedTick = MinecraftServer.currentTick;
			
			if (lastHealth != this.getHealth()) {
				
				stand.setCustomName(UtilsFactory.color(GimmeMyName() + healthMethod()));
				
				lastHealth = this.getHealth();
			}
			
			if (currentArmorStandTicks > 0) {
				currentArmorStandTicks--;
			}else {
				stand.teleport(getBukkitEntity().getLocation().clone().add(0, height+0.25, 0));
				currentArmorStandTicks = wait;
			}
		}else {

			stand.remove();
		}
	}
	private String healthMethod() {
		DecimalFormat format = new DecimalFormat("###,###.##");
		return " &a" + format.format(this.getHealth()) + "&f/&c" + health;
	}

	@Override
	public DropLoots setDropLoots() {
		
		DropLoots loots = new DropLoots();
		loots.add(2);
		return loots;
	}

	@Override
	public int WhatsTheId() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String GimmeMyName() {
		// TODO Auto-generated method stub
		return "&7&kLOL HELP MEE";
	}

	@Override
	public void initPathfinderMethod() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double follow() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int howMuchExpIDrop() {
		// TODO Auto-generated method stub
		return 4;
	}
	
	
	
}