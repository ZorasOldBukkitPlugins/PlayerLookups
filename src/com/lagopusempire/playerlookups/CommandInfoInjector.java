package com.lagopusempire.playerlookups;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class CommandInfoInjector
{

    private final JavaPlugin plugin;

    public CommandInfoInjector(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    public String inject(Player sender, String command)
    {
        Clipboard clipboard = new Clipboard(sender, plugin);

        String[] parts = command.split(" ");
        for (int ii = 0; ii < parts.length; ii++)
        {
            if (ii == 0)
            {
                continue;
            }

            try
            {
                if (parts[ii].startsWith("uuid:"))
                {
                    int index = Integer.parseInt(parts[ii].substring("uuid:".length()));
                    
                    String uuid = clipboard.getUUID(index);
                    if(uuid != null)
                    {
                        parts[ii] = uuid;
                    }
                }
                else if (parts[ii].startsWith("name:"))
                {
                    int index = Integer.parseInt(parts[ii].substring("name:".length()));
                    
                    String name = clipboard.getName(index);
                    if(name != null)
                    {
                        parts[ii] = name;
                    }
                }
                else if (parts[ii].startsWith("ip:"))
                {
                    int index = Integer.parseInt(parts[ii].substring("ip:".length()));
                    
                    String ip = clipboard.getIp(index);
                    if(ip != null)
                    {
                        parts[ii] = ip;
                    }
                }
            }
            catch (NumberFormatException e)
            {
                //ignore
            }
        }

        StringBuilder builder = new StringBuilder(command.length());
        for (int ii = 0; ii < parts.length; ii++)
        {
            builder.append(parts[ii]).append(" ");
        }

        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
