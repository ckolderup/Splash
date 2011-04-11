package org.kolderup.casey.bukkitplugins;


import java.net.URLEncoder;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * Handle events for all Player related events
 * @author ckolderup
 */
public class SplashPlayerListener extends PlayerListener {
    private final Splash plugin;

    public SplashPlayerListener(final Splash plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
    	try { 
	    	String data =  URLEncoder.encode("playerName", "UTF-8") + "=" + URLEncoder.encode(event.getPlayer().getName(), "UTF-8"); 
	    	plugin.postRequest("PLAYER_QUIT", data);
    	} catch (Exception e) { }
    	super.onPlayerQuit(event);
    }
    
    //Insert Player related code here
    @Override
	public void onPlayerJoin(PlayerJoinEvent event) {
    	try {
	    	String data =  URLEncoder.encode("playerName", "UTF-8") + "=" + URLEncoder.encode(event.getPlayer().getName(), "UTF-8");
	    	plugin.postRequest("PLAYER_JOIN", data);
    	} catch (Exception e) { }
    	super.onPlayerJoin(event);
	}
}

