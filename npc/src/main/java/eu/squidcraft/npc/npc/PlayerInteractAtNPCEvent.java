package eu.squidcraft.npc.npc;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractAtNPCEvent extends Event implements Cancellable{

	private final NoPlayerCharacter npc;
	private final Player player;
	private final UUID uuidNPC;
	
    private static final HandlerList HANDLERS = new HandlerList();
	
	public PlayerInteractAtNPCEvent(NoPlayerCharacter npc, Player player, UUID uuidNPC) {
		this.npc = npc;
		this.player = player;
		this.uuidNPC = uuidNPC;
	}
	
	public NoPlayerCharacter getNpc() {
		return npc;
	}
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public UUID getUUID() {
    	return uuidNPC;
    }
    
	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCancelled(boolean var1) {
		// TODO Auto-generated method stub
		
	}

}
