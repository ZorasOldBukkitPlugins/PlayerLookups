
package com.lagopusempire.playerlookups.listeners;

import com.lagopusempire.playerlookups.mysql.MySqlConnection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 *
 * @author MrZoraman
 */
public class PlayerLoginListener implements Listener
{
    private final MySqlConnection conn;
    
    public PlayerLoginListener(MySqlConnection conn)
    {
        this.conn = conn;
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event)
    {
        
    }
}
