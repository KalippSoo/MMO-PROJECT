package eu.isweezee.mmo.extra;

import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import eu.isweezee.mmo.MMO;
import eu.isweezee.mmo.data.PlayerData;
import eu.isweezee.mmo.data.Sack;
import eu.isweezee.mmo.enums.ClazzType;

public class DocumentRelated {

	public static void createOrGetCollectionForPlayer(Player player) {
		Document playerData = new Document("uuid", player.getUniqueId().toString());
		Document found = MMO.players.find(playerData).first();
		
		if (found == null) {
			
			Document
			stats = new Document(),
			quests = new Document(),
			
			collection = new Document(),
			inventory = new Document(),
			
			slayers = new Document(),
			artefactSack = new Document(),
			selection = new Document(),
			
			sacks = new Document(),
			sack = new Document(),
			
			profile = new Document();
			
			stats.append("level", 0);
			stats.append("title", null);
			stats.append("class", ClazzType.NONE.name());
			stats.append("health", 25);
			stats.append("damage", 5);
			stats.append("critdamage", 10);
			stats.append("critchance", 0);
			stats.append("armor", 50);
			
			slayers.append("enchanted_cow", 0);
			
			artefactSack.append("size", 27);
			artefactSack.append("items", new ArrayList<>());
			
			sack.append("size", 54);
			sack.append("items", new ArrayList<>());
			
			for (int i = 1; i <= 6; i++) {
				selection.append(i+"", null);
			}
			
			sacks.append("sack", sack);
			sacks.append("artefacts", artefactSack);
			
			collection.append("slayers", slayers);
			
			quests.append("0", new Document("value", "0").append("done", false));
			
			profile.append("stats", stats);
			profile.append("quests", quests);
			profile.append("collections", collection);
			profile.append("sacks", sacks);
			profile.append("inventory", inventory);

			playerData.append("account-creation", new Date().toString());
			playerData.append("profile", profile);
			
			MMO.players.insertOne(playerData);
		}
	}
	
	public static PlayerData putMongoDBIntoJavaObjectPlayer(Player player) {
		
		Document found = MMO.players.find(new Document("uuid", player.getUniqueId().toString())).first();
		
		Document profile = (Document) found.get("profile");
		Document stats = (Document) profile.get("stats");
		//Document collections = (Document) profile.get("collections");
		
		Document sacks = (Document) profile.get("sacks");
		Document sack = (Document) sacks.get("sack");
		Document artefacts = (Document) sacks.get("artefacts");
		
		//Document magic = (Document) collections.get("magic");

		PlayerData data = new PlayerData(
				null,
				stats.getInteger("health"),
				stats.getInteger("armor"),
				stats.getInteger("damage"),
				stats.getString("title"),
				ClazzType.valueOf(stats.getString("class")),
				stats.getInteger("critdamage"),
				stats.getInteger("critchance"),
				stats.getInteger("level")
				);
		
		//data.getSpellSlots().setupSpells((Document) sacks.get("spells"), player);
		data.setSack(new Sack(sack, sack.getInteger("size", 9), "Sac personnel"));
		data.setArtefacts(new Sack(artefacts, artefacts.getInteger("size", 9), "Sac d'artefacts"));
		
		MMO.dataStorage.put(player.getUniqueId(), data);
		return data;
	}
	
	
	public static void saveJavaPlayerDataToMongoDB(Player player, Document found) {
		
		PlayerData data = MMO.dataStorage.get(player.getUniqueId());
		Inventory inv = player.getInventory();
		
		Document
		stats = new Document(),
		quests = new Document(),
		
		collection = new Document(),
		inventory = new Document(),
		
		slayers = new Document(),
		artefactSack = new Document(),
		//selection = new Document(),
		
		sacks = new Document(),
		sack = new Document(),
		
		profile = new Document();
		
		stats.append("level", data.getLevel());
		stats.append("title", data.getTitle() == null ? null : data.getTitle());
		stats.append("class", data.getClazzType() == null ? null : data.getClazzType().name());
		stats.append("health", data.getHealth());
		stats.append("damage", data.getStrenght());
		stats.append("critdamage", data.getCritDamage());
		stats.append("critchance", data.getCritChance());
		stats.append("armor", data.getArmor());
		
		slayers.append("enchanted_cow", 0);
		
		artefactSack.append("size", data.getArtefacts().getInventory().getSize());
		artefactSack.append("items", data.getArtefacts().saveInventory());
		
		sack.append("size", data.getSack().getInventory().getSize());
		sack.append("items", data.getSack().saveInventory());
		
		sacks.append("sack", sack);
		sacks.append("artefacts", artefactSack);
		
		collection.append("slayers", slayers);
		
		quests.append("0", new Document("value", "0").append("done", false));
		inventory.append("items", data.saveInventory(inv));
		
		profile.append("stats", stats);
		profile.append("quests", quests);
		profile.append("collections", collection);
		profile.append("sacks", sacks);
		profile.append("inventory", inventory);
		
		Document updateValue = new Document("profile", profile);
		Document updateOperation = new Document("$set", updateValue);
		MMO.players.updateOne(found, updateOperation);
		
		System.out.println("Data saved for " + player.getName() + "!");
		
	}
	
}