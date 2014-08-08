package com.lagopusempire.playerlookups.commands;

import com.lagopusempire.playerlookups.Clipboard;
import com.lagopusempire.playerlookups.MojangServerResult;
import com.lagopusempire.playerlookups.Permissions;
import com.lagopusempire.playerlookups.PlayerInfo;
import com.lagopusempire.playerlookups.PlayerLookups;
import com.lagopusempire.playerlookups.utils.Formatter;
import com.lagopusempire.playerlookups.utils.ListPrinter;
import com.lagopusempire.playerlookups.utils.MetadataUtils;
import com.lagopusempire.playerlookups.utils.UuidUtils;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class LookupNameCommand extends PlCommandBase
{

    private final MetadataUtils metadata;

    public LookupNameCommand(PlayerLookups plugin)
    {
        super(plugin);
        this.metadata = new MetadataUtils(plugin);
    }

    @Override
    public boolean execute(final CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
    {
        if (!Permissions.CAN_LOOKUP_NAMES.verify(sender))
        {
            return noPermissions(sender);
        }

        if (args.length < 1)
        {
            sender.sendMessage(new Formatter(plugin.getMessages().getString("not-enough-args"))
                    .colorize()
                    .toString());

            printUsage(sender);
            return true;
        }

        final String uuidString = args[0];

        if (!UuidUtils.isUUID(uuidString))
        {
            sender.sendMessage(new Formatter(plugin.getMessages().getString("must-be-uuid"))
                    .colorize()
                    .toString());
            printUsage(sender);

            System.out.println("(got: " + uuidString + ")");
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                //ASYNC
                UUID uuid = UUID.fromString(uuidString);
                final List<PlayerInfo> names = plugin.getNames(uuid);
                final MojangServerResult result = plugin.getCurrentNameUsingUUID(uuid);

                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //SYNC                        
                        Formatter header = new Formatter(plugin.getMessages().getString("names-from-uuid-result-header"))
                                .setUUID(uuidString)
                                .colorize();

                        Formatter messagePart = new Formatter(plugin.getMessages().getString("names-from-uuid-result"))
                                .colorize();

                        Formatter currentUserMessage = new Formatter(plugin.getMessages().getString("names-from-uuid-current"))
                                .colorize();

                        Player player = null;
                        if (sender instanceof Player)
                        {
                            player = (Player) sender;
                        }

                        Clipboard clipboard = new Clipboard(player, plugin);

                        sender.sendMessage(header.toString());

                        for (int ii = 0; ii < names.size(); ii++)
                        {
                            if (player == null && ii > ListPrinter.PAGE_LENGTH())
                            {
                                break;
                            }

                            String name = names.get(ii).name;

                            sender.sendMessage(messagePart.dup()
                                    .setName(name)
                                    .setNumber(ii)
                                    .toString());

                            clipboard.setName(ii, name);
                        }

                        if (player != null)
                        {
                            metadata.setMetadata(player, "lookup_pages", names);
                        }

                        if (result.failed)
                        {
                            String message = new Formatter(plugin.getMessages().getString("external-server-failure"))
                                    .colorize()
                                    .toString();

                            sender.sendMessage(message);
                        }
                        else
                        {
                            if (result.name == null)
                            {
                                sender.sendMessage(currentUserMessage
                                        .setName("none")
                                        .setNumber("X")
                                        .toString());
                            }
                            else
                            {
                                clipboard.setName(names.size(), result.name);
                                sender.sendMessage(currentUserMessage
                                        .setName(result.name)
                                        .setNumber(names.size())
                                        .toString());
                            }
                        }
                    }
                });
            }
        });

        return true;
    }

    @Override
    public void printUsage(CommandSender sender)
    {
        sender.sendMessage(new Formatter(plugin.getMessages().getString("names-from-uuid-command-usage"))
                .colorize()
                .toString());
    }
}
