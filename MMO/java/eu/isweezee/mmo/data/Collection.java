package eu.isweezee.mmo.data;

public class Collection {
	
	private Slayers slayers;
	
	public Collection() {
		super();
		this.slayers = new Slayers();
	}

	public Slayers getSlayers() {
		return slayers;
	}

	public void setSlayers(Slayers slayers) {
		this.slayers = slayers;
	}
	
}