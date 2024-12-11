package eu.isweezee.mmo.enums;

import eu.isweezee.mmo.extra.UtilsFactory;

public enum Rarity {
	
	commun(0, "&f&lCOMMON"),
	uncommon(1, "&7&lUNCOMMON"),
	kindarare(2, "&e&lKINDA RARE"),
	discovery(3, "&c&lBIG DISCOVERY"),
	surprising(4, "&d&lSURPRISING")
	
	;
	
	private int rarity;
	private String str;
	private String stripColor;
	private String rarityColor;
	
	private Rarity(int rarity, String str) {
		this.rarity = rarity;
		this.str = str;
		this.stripColor = UtilsFactory.stripColor(str);
		this.rarityColor = str.substring(0, 4);
	}
	
	public static Rarity getOrdinal(int t) {
		for (Rarity r : values()) {
			if (r.getRarity() == t) {
				return r;
			}
		}
		return null;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getStripColor() {
		return stripColor;
	}

	public void setStripColor(String stripColor) {
		this.stripColor = stripColor;
	}

	public String getRarityColor() {
		return rarityColor;
	}
	
	
}
