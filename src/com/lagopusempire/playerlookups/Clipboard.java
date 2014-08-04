package com.lagopusempire.playerlookups;

import com.lagopusempire.playerlookups.utils.MetadataUtils;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class Clipboard
{

    private static final Map<String, String> consoleValues = new HashMap<String, String>();

    private final MetadataUtils metadata;
    private final Player player;

    public Clipboard(Player player, JavaPlugin plugin)
    {
        this.metadata = new MetadataUtils(plugin);
        this.player = player;
    }

    public void setUUID(int index, String uuid)
    {
        if (player == null)
        {
            consoleValues.put("pl_uuid." + index, uuid);
        }
        else
        {
            metadata.setMetadata(player, "pl_uuid." + index, uuid);
        }
    }

    public void setName(int index, String name)
    {
        if (player == null)
        {
            consoleValues.put("pl_name." + index, name);
        }
        else
        {
            metadata.setMetadata(player, "pl_name." + index, name);
        }
    }

    public void setIp(int index, String ip)
    {
        if (player == null)
        {
            consoleValues.put("pl_ip." + index, ip);
        }
        metadata.setMetadata(player, "pl_ip." + index, ip);
    }
}
