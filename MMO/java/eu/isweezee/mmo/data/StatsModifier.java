package eu.isweezee.mmo.data;

public class StatsModifier {
	
	private double modifiedDamage;
	private double modifiedHealth;
	private double modifiedArmor;
	private double modifiedStrenght;
	private double modifiedCritChance;
	private double modifiedCritDamage;
	
	public StatsModifier(double modifiedHealth, double modifiedArmor, double modifiedStrenght, double modifiedCritChance,
			double modifiedCritDamage) {
		super();
		this.modifiedHealth = modifiedHealth;
		this.modifiedArmor = modifiedArmor;
		this.modifiedStrenght = modifiedStrenght;
		this.modifiedCritChance = modifiedCritChance;
		this.modifiedCritDamage = modifiedCritDamage;
	}
	public StatsModifier() {
		// TODO Auto-generated constructor stub
	}
	public double getDamage() {
		return 3 + (modifiedDamage*(1+modifiedStrenght)/100);
	}
	public double getDamageWithCrit() {
		return getDamage() + (modifiedDamage*(1+modifiedStrenght)/100*(1+modifiedCritDamage)/100);
	}

	public void setModifiedHealth(double modifiedHealth) {
		this.modifiedHealth = modifiedHealth;
	}

	public void setModifiedArmor(double modifiedArmor) {
		this.modifiedArmor = modifiedArmor;
	}

	public void setModifiedStrenght(double strenght) {
		this.modifiedStrenght = strenght;
	}

	public void setModifiedCritChance(double modifiedCritChance) {
		this.modifiedCritChance = modifiedCritChance;
	}

	public void setModifiedCritDamage(double modifiedCritDamage) {
		this.modifiedCritDamage = modifiedCritDamage;
	}

	public double getModifiedHealth() {
		return modifiedHealth;
	}

	public double getModifiedArmor() {
		return modifiedArmor;
	}

	public double getModifiedStrenght() {
		return modifiedStrenght;
	}

	public double getModifiedCritChance() {
		return modifiedCritChance;
	}

	public double getModifiedCritDamage() {
		return modifiedCritDamage;
	}

	public double getModifiedDamage() {
		return modifiedDamage;
	}

	public void setModifiedDamage(double modifiedDamage) {
		this.modifiedDamage = modifiedDamage;
	}
	
	
	
}
