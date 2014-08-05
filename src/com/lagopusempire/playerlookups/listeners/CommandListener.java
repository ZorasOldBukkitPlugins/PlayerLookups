
package com.lagopusempire.playerlookups.listeners;

import com.lagopusempire.playerlookups.CommandInfoInjector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class CommandListener implements Listener
{
    private final CommandInfoInjector injector;
    
    public CommandListener(JavaPlugin plugin)
    {
        injector = new CommandInfoInjector(plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPLayerCommand(PlayerCommandPreprocessEvent event)
    {
        String command = injector.inject(event.getPlayer(), event.getMessage());
        event.setMessage(command);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onConsoleCommand(ServerCommandEvent event)
    {
        String command = injector.inject(null, event.getCommand());
        event.setCommand(command);
    }
}
