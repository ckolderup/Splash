package org.kolderup.casey.bukkitplugins;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Splash entity listener
 * @author ckolderup
 */
public class SplashEntityListener extends EntityListener {
    private final Splash plugin;
    private final Map<String, String> urlsForEvents;

    public SplashEntityListener(final Splash plugin, final Map<String, String> urlsForEvents) {
        this.plugin = plugin;
        this.urlsForEvents = urlsForEvents;
    }

    //put all Entity related code here
    @Override
	public void onEntityDeath(EntityDeathEvent event) {
    	
		if (event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
					
			//HTTP POST	
			String urlString = urlsForEvents.get("PLAYER_DEATH");
			
			try {
				String data =  URLEncoder.encode("playerName", "UTF-8") + "=" + URLEncoder.encode(player.getName(), "UTF-8");
				URL url = new URL(urlString);
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
				writer.write(data);
				writer.flush();
				
				System.out.println("Sent player death to" + urlString + ", response follows:");
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				
				writer.close();
				reader.close();
			}
			catch (Exception e) {
				System.out.println("Malformed HTTP request, request may not have been sent to the remote server");
			}
			
		}
		
		super.onEntityDeath(event);
	}
}
