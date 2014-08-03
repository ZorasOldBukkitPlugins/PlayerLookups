
package com.lagopusempire.playerlookups.commands;

import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.CSBukkitCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class LookupUUIDCommand implements CSBukkitCommand
{
    @Override
    public boolean execute(CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
    {
        sender.sendMessage("hi!");
        return true;
    }
}
