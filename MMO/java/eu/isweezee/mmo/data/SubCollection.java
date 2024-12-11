package eu.isweezee.mmo.data;

public class SubCollection {

	private int kills;
	private int expPerKill;
	
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

	public int getExpPerKill() {
		return expPerKill;
	}

	public void setExpPerKill(int expPerKill) {
		this.expPerKill = expPerKill;
	}
	
}
