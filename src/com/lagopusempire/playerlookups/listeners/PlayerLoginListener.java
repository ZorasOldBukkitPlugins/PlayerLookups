package com.lagopusempire.playerlookups.listeners;

import com.lagopusempire.playerlookups.mysql.MySqlConnection;
import com.lagopusempire.playerlookups.utils.files.FileParser;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

/**
 *
 * @author MrZoraman
 */
public class PlayerLoginListener implements Listener
{

    private final MySqlConnection conn;
    private final FileParser parser;

    public PlayerLoginListener(FileParser parser, MySqlConnection conn)
    {
        this.conn = conn;
        this.parser = parser;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event)
    {
        final UUID uuid = event.getUniqueId();
        final String name = event.getName();
        final String ip = event.getAddress().toString().substring(1);

        String query = parser.getContents("queries/call-add_player-procedure.sql");
        try
        {
            conn.query(query)
                    .setString(uuid.toString())
                    .setString(name)
                    .setString(ip)
                    .executeUpdate();
        }
        catch (Exception e)
        {
            event.disallow(Result.KICK_OTHER, "Internal server error :(");
            e.printStackTrace();
        }
    }
}
