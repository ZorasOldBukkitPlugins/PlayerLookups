
package com.lagopusempire.playerlookups;

import org.bukkit.command.CommandSender;

/**
 *
 * @author MrZoraman
 */
public enum Permissions
{
    CAN_LOOKUP_UUIDS("playerlookups.uuids"),
    CAN_LOOKUP_NAMES("playerlookups.names"),
    CAN_LOOKUP_IPS("playerlookups.ips");
    
    private final String node;
    
    private Permissions(String node)
    {
        this.node = node;
    }

    public boolean verify(CommandSender sender)
    {
        return (sender.isOp() || sender.hasPermission(node));
    }
}
