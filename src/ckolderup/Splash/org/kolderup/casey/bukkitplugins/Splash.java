package org.kolderup.casey.bukkitplugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
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
    	
    	playerListener = new SplashPlayerListener(this);
    	entityListener = new SplashEntityListener(this);
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.ENTITY_DEATH, entityListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Monitor, this);

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
    
    public void postRequest(String eventKey, String data) {
    	//HTTP POST	
		String urlString = urlsForEvents.get(eventKey);
		
		try {
			
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();
			
			System.out.println("Sent update to" + urlString + ", response follows:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			
			writer.close();
			reader.close();
		}
		catch (Exception e) {
			System.out.println("Malformed HTTP request, request may or may not have exceeded.");
			System.out.println(e.getMessage());
		}
    }
}

