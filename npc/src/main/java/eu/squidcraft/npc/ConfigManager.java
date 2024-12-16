package eu.squidcraft.npc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

    private NPCPlugin main;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private String name;
    public String type;

    public ConfigManager(NPCPlugin main, String name, String type) {
        this.main = main;
        this.name = name;
        this.type = type;
        saveDefaultConfig();
    }

    public void reloadConfig(){

        if (this.configFile == null)
            this.configFile = new File(main.getDataFolder(), name + type);

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.main.getResource(name + type);

        if (defaultStream != null){

            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(){

        if (this.dataConfig == null)
            reloadConfig();

        return this.dataConfig;

    }
    

    public void saveConfig(){

        if (this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            main.getLogger().log(Level.SEVERE, "La sauvegarde du ficher de config \" " + this.configFile + "\" a échoué !" + e);
        }

    }

    public void saveDefaultConfig(){

        if (this.configFile == null){
            this.configFile = new File(main.getDataFolder(), name + type);
        }
        if (!this.configFile.exists()){
            this.main.saveResource(name + type, false);
        }


    }
    public String getName() {
    	return name + type;
    }
    
    public File getFile() {
    	return configFile;
    }
    
    public static void createFolder(NPCPlugin main, String name) {
        File folder = new File(main.getDataFolder(), name);
        if (!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

















