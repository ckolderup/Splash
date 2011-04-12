package org.kolderup.casey.bukkitplugins;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import java.net.URLEncoder;

/**
 * Splash entity listener
 * @author ckolderup
 */
public class SplashEntityListener extends EntityListener {
    private final Splash plugin;

    public SplashEntityListener(final Splash plugin) {
        this.plugin = plugin;
    }

    //put all Entity related code here
    @Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			try { 
		    	String data =  URLEncoder.encode("playerName", "UTF-8") + "=" + URLEncoder.encode(player.getName(), "UTF-8"); 
		    	plugin.postRequest("PLAYER_DEATH", data);
	    	} catch (Exception e) { }
		}
	}
}
