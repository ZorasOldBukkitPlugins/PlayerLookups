package com.lagopusempire.playerlookups.utils;

import java.util.List;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class MetadataUtils
{

    private final JavaPlugin plugin;

    public MetadataUtils(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void setMetadata(Metadatable object, String key, Object value)
    {
        object.setMetadata(key, new FixedMetadataValue(plugin, value));
    }

    public Object getMetadata(Metadatable object, String key)
    {
        List<MetadataValue> values = object.getMetadata(key);
        for (MetadataValue value : values)
        {
            if (value.getOwningPlugin() == plugin)
            {
                return value.value();
            }
        }

        return null;
    }

    public void removeMetadata(Metadatable object, String key)
    {
        object.removeMetadata(key, plugin);
    }

    public void setBoolean(Metadatable object, String key, boolean value)
    {
        if (value)
        {
            object.setMetadata(key, new FixedMetadataValue(plugin, new Object()));
        }
        else
        {
            if (object.hasMetadata(key))
            {
                object.removeMetadata(key, plugin);
            }
        }
    }

    public boolean getBoolean(Metadatable object, String key)
    {
        return object.hasMetadata(key);
    }
}
