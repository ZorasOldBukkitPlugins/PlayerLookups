package com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author MrZoraman
 */
public interface CSBukkitCommand
{

    public boolean execute(CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args);
}
