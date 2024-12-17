package eu.isweezee.mmo.data.collection;

import org.bukkit.entity.Player;

public class SubCollection {

	private int kills = 0;
	private int expPerKill = 0;
	private int entityId = -1;
	
	public SubCollection(int id) {
		this.entityId = id;
	}
	
	public SubCollection(int kills, int expPerKill) {
		super();
		this.kills = kills;
		this.expPerKill = expPerKill;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public void addKills(int kills) {
		this.kills += kills;
	}

	public int getExpPerKill() {
		return expPerKill;
	}
	
	public int addExpPerKill(Player player) {
		player.giveExp(expPerKill);
		return expPerKill;
	}

	public void setExpPerKill(int expPerKill) {
		this.expPerKill = expPerKill;
	}
	public int getEntityId() {
		return entityId;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	
}
