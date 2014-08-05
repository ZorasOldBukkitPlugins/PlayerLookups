
package com.lagopusempire.playerlookups.commands;

import com.lagopusempire.playerlookups.PlayerLookups;
import com.lagopusempire.playerlookups.utils.Formatter;
import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.CSBukkitCommand;
import org.bukkit.command.CommandSender;

/**
 *
 * @author MrZoraman
 */
public abstract class PlCommandBase implements CSBukkitCommand
{
    protected final PlayerLookups plugin;
    
    public PlCommandBase(PlayerLookups plugin)
    {
        this.plugin = plugin;
    }
    
    public abstract void printUsage(CommandSender sender);
    
    protected boolean noPermissions(CommandSender sender)
    {
        sender.sendMessage(new Formatter(plugin.getConfig().getString("strings.no-permissions"))
                .colorize()
                .toString());
        return true;
    }
}
