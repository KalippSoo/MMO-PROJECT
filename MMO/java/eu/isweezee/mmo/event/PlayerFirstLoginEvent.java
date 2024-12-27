package eu.isweezee.mmo.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.isweezee.mmo.data.PlayerData;

public class PlayerFirstLoginEvent extends Event{

	private final static HandlerList HANDLERS = new HandlerList();
	
	private Player player;
	private PlayerData data;
	
	public PlayerFirstLoginEvent(Player player, PlayerData data) {
		
		this.player = player;
		this.data = data;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList GetHandlerList() {
		return HANDLERS;
	}

	public Player getPlayer() {
		return player;
	}

	public PlayerData getData() {
		return data;
	}

}
