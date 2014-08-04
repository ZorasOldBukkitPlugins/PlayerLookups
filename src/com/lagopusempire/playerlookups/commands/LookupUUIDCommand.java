package com.lagopusempire.playerlookups.commands;

import com.lagopusempire.playerlookups.Clipboard;
import com.lagopusempire.playerlookups.Permissions;
import com.lagopusempire.playerlookups.PlayerLookups;
import com.lagopusempire.playerlookups.utils.Formatter;
import com.lagopusempire.playerlookups.utils.IpUtils;
import com.lagopusempire.playerlookups.utils.ListPrinter;
import com.lagopusempire.playerlookups.utils.MetadataUtils;
import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.CSBukkitCommand;
import java.util.List;
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
            sender.sendMessage(new Formatter(plugin.getConfig().getString("strings.not-enough-args"))
                    .colorize()
                    .toString());

            sender.sendMessage(new Formatter(plugin.getConfig().getString("strings.lookup-uuid-command-usage"))
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
        sender.sendMessage(new Formatter(plugin.getConfig().getString("strings.no-permissions"))
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
                final List<UUID> uuids = plugin.getUniqueIdsFromIp(ip);

                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //SYNC
                        Formatter header = new Formatter(plugin.getConfig().getString("strings.uuids-from-ip-result-header"))
                                .setIp(ip)
                                .colorize();

                        Formatter messagePart = new Formatter(plugin.getConfig().getString("strings.uuids-from-ip-result"))
                                .colorize();

                        Player player = null;
                        if (sender instanceof Player)
                        {
                            player = (Player) sender;
                        }

                        if (player == null)
                        {
                            sender.sendMessage(header.toString());

                            Clipboard consoleClipboard = new Clipboard(null, plugin);

                            for (int ii = 0; ii < uuids.size(); ii++)
                            {
                                String uuid = uuids.get(ii).toString();

                                sender.sendMessage(messagePart.dup()
                                        .setUUID(uuid)
                                        .setNumber(ii)
                                        .toString());

                                consoleClipboard.setUUID(ii, uuid);
                            }
                        }
                        else
                        {
                            metadata.setMetadata(player, "lookup_pages", uuids);
                            Clipboard clipboard = new Clipboard(player, plugin);

                            player.sendMessage(header.toString());

                            for (int ii = 0; ii < ListPrinter.PAGE_LENGTH(); ii++)
                            {
                                if (uuids.size() == ii)
                                {
                                    break;
                                }

                                String uuid = uuids.get(ii).toString();

                                player.sendMessage(messagePart.dup()
                                        .setUUID(uuid)
                                        .setNumber(ii)
                                        .toString());

                                clipboard.setUUID(ii, uuid);
                            }
                        }
                    }
                });
            }
        });
    }

    //ASYNC
    private void lookupUUIDSfromName(final CommandSender sender, final String name)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                //ASYNC
                final List<UUID> uuids = plugin.getUniqueIdsFromName(name);
                final UUID currentNameOwner = plugin.getCurrentUniqueIdUsingName(name);

                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //SYNC
                        Formatter header = new Formatter(plugin.getConfig().getString("strings.uuids-from-name-result-header"))
                                .setName(name)
                                .colorize();

                        Formatter messagePart = new Formatter(plugin.getConfig().getString("strings.uuids-from-name-result"))
                                .colorize();

                        Formatter currentUserMessage = new Formatter(plugin.getConfig().getString("strings.uuids-from-name-current"))
                                .colorize();

                        Player player = null;
                        if (sender instanceof Player)
                        {
                            player = (Player) sender;
                        }
                        
                        Clipboard clipboard;

                        if (player == null)
                        {
                            sender.sendMessage(header.toString());

                            clipboard = new Clipboard(null, plugin);

                            for (int ii = 0; ii < uuids.size(); ii++)
                            {
                                String uuid = uuids.get(ii).toString();

                                sender.sendMessage(messagePart.dup()
                                        .setUUID(uuid)
                                        .setNumber(ii)
                                        .toString());

                                clipboard.setName(ii, uuid);
                            }
                        }
                        else
                        {
                            metadata.setMetadata(player, "lookup_pages", uuids);
                            clipboard = new Clipboard(player, plugin);

                            player.sendMessage(header.toString());

                            for (int ii = 0; ii < ListPrinter.PAGE_LENGTH(); ii++)
                            {
                                if (uuids.size() == ii)
                                {
                                    break;
                                }

                                String uuid = uuids.get(ii).toString();

                                player.sendMessage(messagePart.dup()
                                        .setUUID(uuid)
                                        .setNumber(ii)
                                        .toString());

                                clipboard.setName(ii, uuid);
                            }
                        }
                        
                        if(currentNameOwner == null)
                        {
                            sender.sendMessage(currentUserMessage
                                    .setUUID("none")
                                    .setNumber("X")
                                    .toString());
                        }
                        else
                        {
                            clipboard.setUUID(uuids.size(), currentNameOwner.toString());

                            sender.sendMessage(currentUserMessage
                                    .setUUID(currentNameOwner.toString())
                                    .setNumber(uuids.size())
                                    .toString());
                        }
                    }
                });
            }
        });
    }
}
