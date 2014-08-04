package com.lagopusempire.playerlookups.commands;

import com.lagopusempire.playerlookups.Permissions;
import com.lagopusempire.playerlookups.PlayerLookups;
import com.lagopusempire.playerlookups.utils.Formatter;
import com.lagopusempire.playerlookups.utils.IpUtils;
import com.lagopusempire.playerlookups.utils.MetadataUtils;
import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.CSBukkitCommand;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Player gives a uuid and gets the ips and names the uuid has been seen using
 *
 * @author MrZoraman
 */
public class LookupUUIDCommand implements CSBukkitCommand
{
    /*
     /lookup uuid(s) [ip] - gets a list of uuids an ip has used
     /lookup uuid(s) [name] - gets a list of uuids a name has used
     */

    private final PlayerLookups plugin;
    private final MetadataUtils metadata;

    public LookupUUIDCommand(PlayerLookups plugin)
    {
        this.plugin = plugin;
        this.metadata = new MetadataUtils(plugin);
    }

    @Override
    public boolean execute(final CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
    {
        if (!Permissions.CAN_LOOKUP_IPS.verify(sender) && !Permissions.CAN_LOOKUP_NAMES.verify(sender))
        {
            return noPermissions(sender);
        }

        if (args.length < 1)
        {
            sender.sendMessage(new Formatter(plugin.getConfig().getString("not-enough-args"))
                    .colorize()
                    .toString());

            sender.sendMessage(new Formatter(plugin.getConfig().getString("lookup-uuid-command-usage"))
                    .colorize()
                    .toString());
            return true;
        }

        final String input = args[0];

        if (IpUtils.isIpAddress(input))
        {
            if (Permissions.CAN_LOOKUP_IPS.verify(sender))
            {
                lookupUUIDSfromIp(sender, input);
            }
            else
            {
                return noPermissions(sender);
            }
        }
        else
        {
            if (Permissions.CAN_LOOKUP_NAMES.verify(sender))
            {
                lookupUUIDSfromName(sender, input);
            }
            else
            {
                return noPermissions(sender);
            }
        }

        return true;
    }

    private boolean noPermissions(CommandSender sender)
    {
        sender.sendMessage(new Formatter(plugin.getConfig().getString("no-permissions"))
                .colorize()
                .toString());
        return true;
    }

    //ASYNC
    private void lookupUUIDSfromIp(final CommandSender sender, final String ip)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                //ASYNC
                final Set<UUID> uuids = plugin.getUniqueIdsFromIp(ip);

                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //SYNC
                        Formatter header = new Formatter(plugin.getConfig().getString("uuids-from-ip-result-header"))
                                .setIp(ip)
                                .colorize();
                        
                        Formatter messagePart = new Formatter(plugin.getConfig().getString("uuids-from-ip-result"))
                                .colorize();
                        
                        Player player = null;
                        if(sender instanceof Player)
                        {
                            player = (Player) sender;
                        }
                        
                        if(player == null)
                        {
                            //TODO: console clipboard
                            sender.sendMessage(header.decolorize().toString());
                            
                            for(UUID uuid : uuids)
                            {
                                sender.sendMessage(messagePart
                                        .setUUID(uuid.toString())
                                        .decolorize()
                                        .toString());
                            }
                        }
                        else
                        {
                            metadata.setMetadata(player, "lookup_pages", uuids);
                        }
                        //show page 1
                        
                    }
                });
            }
        });
    }

    //ASYNC
    private void lookupUUIDSfromName(CommandSender sender, String name)
    {

    }
}
