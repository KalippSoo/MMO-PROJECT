package eu.isweezee.mmo.entities;

import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.entities.pathfinder.PathfinderGoalNotGetOutOfBounds;
import eu.isweezee.mmo.enums.GameItem;
import eu.isweezee.mmo.enums.Rarity;
import eu.isweezee.mmo.extra.ItemCreator;
import eu.isweezee.mmo.extra.UtilsFactory;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityChicken;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStroll;

public abstract class EntityType extends EntityCreature{

	public ArmorStand stand;
	EntityTypes<? extends EntityInsentient> entityTypes;
	public abstract DropLoots setDropLoots();
	public abstract int WhatsTheId();
	public abstract String GimmeMyName();
	public abstract void initPathfinderMethod();
	public abstract double health();
	public abstract double follow();
	public abstract double howMuchExpIDrop();
	
	public String[] entityState = {"&c[M] ", "&6[S] "};
	
	Location location;
	
	protected EntityType(EntityTypes<? extends EntityCreature> entitytypes, Location location, double damage) {
		super(entitytypes, ((CraftWorld)location.getWorld()).getHandle());
		this.location = location;
		
		this.setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
		this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(health());
		this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(damage);
		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(follow());
		this.setHealth((float) health());
		
		stand = location.getWorld().spawn(location, ArmorStand.class, type ->{
			type.setGravity(false);
			type.setInvisible(true);
			type.setMarker(true);
			type.setCustomNameVisible(true);
			type.setCustomName(UtilsFactory.color(GimmeMyName()));
		});
		
		this.drops.clear();
	}
	
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalNotGetOutOfBounds(this, 20));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, EntityHuman.class));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityChicken>(this, EntityChicken.class, false));
		initPathfinderMethod();
	}
	
	@Override
	protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
		if (setDropLoots().loots.isEmpty())return;
		Entity entity = damagesource.getEntity();
		if (entity instanceof EntityPlayer) {
			Player player = (Player) entity.getBukkitEntity();
			ItemStack itemStack = setDropLoots().drop();
			if (itemStack == null)return;
			itemStack = itemStack.clone();
			itemStack.setAmount(1);
			ItemCreator creator = GameItem.getItemCreatorFromItemStack(itemStack);
			if (creator == null)return;
			if (GameItem.getIdFromItem(itemStack) == -1)return;
			MMO.dataStorage.get(player.getUniqueId()).getSack().getInventory().addItem(itemStack);
			if (creator.getRarity() > 0) {
				player.sendMessage(UtilsFactory.color(Rarity.getOrdinal(creator.getRarity()).getStr() + " DROP! &fYou have dropped x" + itemStack.getAmount() + " " + itemStack.getItemMeta().getDisplayName()));
			}
			
		}
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
		DecimalFormat format = new DecimalFormat("###.###,##");
		return " &a" + format.format(this.getHealth()) + "&f/&c" + health();
	}
	
}


