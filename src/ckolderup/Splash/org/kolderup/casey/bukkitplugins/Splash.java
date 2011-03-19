package org.kolderup.casey.bukkitplugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

/**
 * Splash for Bukkit
 *
 * @author ckolderup
 */
public class Splash extends JavaPlugin {
    private SplashPlayerListener playerListener = null; 
    private SplashEntityListener entityListener = null;
    private Map<String, String> urlsForEvents = null;
    public void onEnable() {
        // Load URLs from YAML file
    	urlsForEvents = loadUrls("plugins/Splash/config.yml");
    	
    	playerListener = new SplashPlayerListener(this, urlsForEvents);
    	entityListener = new SplashEntityListener(this, urlsForEvents);
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.ENTITY_DEATH, entityListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_LOGIN, playerListener, Priority.Monitor, this);

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        System.out.println("Unloading Splash...");
    }

    private Map<String, String> loadUrls(String configPath) {
    	File configFile = new File(configPath);
    	DumperOptions options = new DumperOptions();
    	options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    	Yaml yaml = new Yaml(options);
    	Object res = null;
		if (configFile.exists() && configFile.canRead()) {
			try {
				Reader rdr = new FileReader(configFile);
				res = yaml.load(rdr);
				rdr.close();
			} catch (FileNotFoundException ex) {
				System.err.println(ex.getMessage());
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}

		return (Map<String, String>)res;
    }
}

