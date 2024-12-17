package eu.isweezee.mmo.data.collection;

public class Collection {

	private SubCollection[] collections;
	
	public Collection() {
		collections = new SubCollection[] 
				{
						new SubCollection(0),
						new SubCollection(1)
						};
	}

	public SubCollection[] getCollections() {
		return collections;
	}
	
}