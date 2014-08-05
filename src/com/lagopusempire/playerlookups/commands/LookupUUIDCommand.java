package com.lagopusempire.playerlookups.commands;

import com.lagopusempire.playerlookups.Clipboard;
import com.lagopusempire.playerlookups.PlayerInfo;
import com.lagopusempire.playerlookups.PlayerLookups;
import com.lagopusempire.playerlookups.utils.Formatter;
import com.lagopusempire.playerlookups.utils.IpUtils;
import com.lagopusempire.playerlookups.utils.ListPrinter;
import com.lagopusempire.playerlookups.utils.MetadataUtils;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.lagopusempire.playerlookups.Permissions.CAN_LOOKUP_UUIDS;

/**
 * Player gives a uuid and gets the ips and names the uuid has been seen using
 *
 * @author MrZoraman
 */
public class LookupUUIDCommand extends PlCommandBase
{
    /*
     /lookup uuid(s) [ip] - gets a list of uuids an ip has used
     /lookup uuid(s) [name] - gets a list of uuids a name has used
     */

    private final MetadataUtils metadata;

    public LookupUUIDCommand(PlayerLookups plugin)
    {
        super(plugin);
        this.metadata = new MetadataUtils(plugin);
    }

    @Override
    public boolean execute(final CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
    {
        if (CAN_LOOKUP_UUIDS.verify(sender))
        {
            if (args.length < 1)
            {
                sender.sendMessage(new Formatter(plugin.getMessages().getString("not-enough-args"))
                        .colorize()
                        .toString());

                printUsage(sender);
                return true;
            }
            else
            {
                if (IpUtils.isIpAddress(args[0]))
                {
                    lookupUUIDSfromIp(sender, args[0]);
                }
                else
                {
                    lookupUUIDSfromName(sender, args[0]);
                }
            }
        }
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
                        Formatter header = new Formatter(plugin.getMessages().getString("uuids-from-ip-result-header"))
                                .setIp(ip)
                                .colorize();

                        Formatter messagePart = new Formatter(plugin.getMessages().getString("uuids-from-ip-result"))
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
                final List<PlayerInfo> uuids = plugin.getUniqueIdsAndDatesFromName(name);
                final UUID currentNameOwner = plugin.getCurrentUniqueIdUsingName(name);

                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //SYNC
                        Formatter header = new Formatter(plugin.getMessages().getString("uuids-from-name-result-header"))
                                .setName(name)
                                .colorize();

                        Formatter messagePart = new Formatter(plugin.getMessages().getString("uuids-from-name-result"))
                                .colorize();

                        Formatter currentUserMessage = new Formatter(plugin.getMessages().getString("uuids-from-name-current"))
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
                                String uuid = uuids.get(ii).uuid.toString();
                                Date date = uuids.get(ii).date;

                                sender.sendMessage(messagePart.dup()
                                        .setUUID(uuid)
                                        .setNumber(ii)
                                        .setDate(date)
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

                        if (currentNameOwner == null)
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

    @Override
    public void printUsage(CommandSender sender)
    {
        sender.sendMessage(new Formatter(plugin.getMessages().getString("lookup-uuid-from-ip-command-usage"))
                .colorize()
                .toString());

        sender.sendMessage(new Formatter(plugin.getMessages().getString("lookup-uuid-from-name-command-usage"))
                .colorize()
                .toString());
    }
}
