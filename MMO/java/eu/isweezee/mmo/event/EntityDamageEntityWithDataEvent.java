package eu.isweezee.mmo.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.isweezee.mmo.data.PlayerData;
import eu.isweezee.mmo.data.impletation.EntityData;
import net.minecraft.server.v1_16_R3.Entity;

public class EntityDamageEntityWithDataEvent extends Event{

	private static HandlerList HANDLERS = new HandlerList();
	
	private final Entity damager;
	private final Entity entity;
	
	private final EntityData entityData;
	private final PlayerData playerData;
	
	private final Player player;
	private final float damage;
	
	public EntityDamageEntityWithDataEvent(Entity damager, Entity entity, EntityData entityData, PlayerData playerData,
			Player player, float damage) {
		this.damager = damager;
		this.entity = entity;
		this.entityData = entityData;
		this.playerData = playerData;
		this.player = player;
		this.damage = damage;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public static HandlerList getHANDLERS() {
		return HANDLERS;
	}

	public Entity getDamager() {
		return damager;
	}

	public Entity getEntity() {
		return entity;
	}

	public EntityData getEntityData() {
		return entityData;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public Player getPlayer() {
		return player;
	}

	public float getDamage() {
		return damage;
	}
	
	
	
}
