package org.kolderup.casey.bukkitplugins;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author ckolderup
 */
public class SplashPlayerListener extends PlayerListener {
    private final Splash plugin;
    private final Map<String, String> urlsForEvents;

    public SplashPlayerListener(final Splash plugin, Map<String, String> urlsForEvents) {
        this.plugin = plugin;
        this.urlsForEvents = urlsForEvents;
    }

    //Insert Player related code here
    @Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		//HTTP POST	
		String urlString = urlsForEvents.get("PLAYER_LOGIN");
		
		try {
			String data =  URLEncoder.encode("playerName", "UTF-8") + "=" + URLEncoder.encode(event.getPlayer().getName(), "UTF-8");
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
		
		super.onPlayerLogin(event);
	}
}

