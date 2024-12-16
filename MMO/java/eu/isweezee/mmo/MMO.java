package eu.isweezee.mmo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import eu.isweezee.mmo.data.PlayerData;
import eu.isweezee.mmo.data.PlayerEquipmentUpdateManager;
import eu.isweezee.mmo.entities.EnchantedSheep;
import eu.isweezee.mmo.entities.spawnSystem.Spawn;
import eu.isweezee.mmo.listeners.PlayerListeners;
import eu.squidcraft.npc.NPCPlugin;

public class MMO extends JavaPlugin{
	
	private NPCPlugin npcApi;
	
	public static Map<UUID, PlayerData> dataStorage = new HashMap<>();
	public static Map<Player, Long> abilityCooldown = new HashMap<>();
	
	static String uri = "mongodb+srv://bouchardphilippe08:HhQ9zRWCEGpx2k0V@squidcraft.ujl1cfx.mongodb.net/?retryWrites=true&w=majority";
	static MongoDatabase mongoDatabase = MongoClients.create(uri).getDatabase("Minecraft");
	
	//Collections
	public static MongoCollection<Document> items = mongoDatabase.getCollection("items");
	public static MongoCollection<Document> players = mongoDatabase.getCollection("players");

	@Override
	public void onEnable() {

		npcApi = (NPCPlugin) this.getServer().getPluginManager().getPlugin("NPCPlugin");
		if (npcApi != null) {
			System.out.println("La connection a l'api NPCPlugin a été un succès !");
		}
		
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
		
		PlayerEquipmentUpdateManager.run();
		new Spawn(EnchantedSheep.class, 5, 5, 40);
	}

	public NPCPlugin getNpcApi() {
		return npcApi;
	}
	
}	