package com.lagopusempire.playerlookups;

import com.lagopusempire.playerlookups.commands.LookupUUIDCommand;
import com.lagopusempire.playerlookups.listeners.PlayerLoginListener;
import com.lagopusempire.playerlookups.mysql.MySqlConnection;
import com.lagopusempire.playerlookups.mysql.MySqlCreds;
import com.lagopusempire.playerlookups.utils.IUpdateTask;
import com.lagopusempire.playerlookups.utils.SequentialUpdater;
import com.lagopusempire.playerlookups.utils.files.FileParser;
import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.BukkitCommandSystem;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MrZoraman
 */
public class PlayerLookups extends JavaPlugin implements Listener
{

    private final BukkitCommandSystem cs;

    private MySqlConnection connection;

    public PlayerLookups()
    {
        super();
        this.cs = new BukkitCommandSystem(this);
    }

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();

        final MySqlCreds creds = new MySqlCreds(
                getConfig().getString("mysql.host"),
                getConfig().getInt("mysql.port"),
                getConfig().getString("mysql.database"),
                getConfig().getString("mysql.user"),
                getConfig().getString("mysql.password"));

        this.connection = new MySqlConnection(getLogger(), creds);
        int schemaVersion = updateSchema(getConfig().getInt("mysql.schema-version", 0), connection);
        getConfig().set("mysql.schema-version", schemaVersion);
        saveConfig();

        cs.registerCommand("lookup uuid", new LookupUUIDCommand());

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(connection), this);
        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event)
    {
//        List<PlayerInfoUnion> info = getIps(event.getUniqueId());
//        System.out.println("this player has gone under these ips:");
//        for (int ii = 0; ii < info.size(); ii++)
//        {
//            System.out.println(info.get(ii).ip);
//        }
    }

    /**
     * Gets a set of ip addresses that the server has seen a uuid use
     *
     * @param uuid The uuid to check
     * @return A set (order is not maintained) of ips that the server has seen a
     * uuid use
     */
    public Set<PlayerInfoUnion> getIps(UUID uuid)
    {
        final String query = FileParser.getContents("queries/get-ips-from-uuid.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(uuid.toString())
                    .executeReader();

            final Set<PlayerInfoUnion> ips = new HashSet<PlayerInfoUnion>();

            while (result.next())
            {
                PlayerInfoUnion info = new PlayerInfoUnion();
                info.uuid = uuid;
                info.ip = result.getString(1);
                ips.add(info);
            }

            result.close();

            return ips;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a list of names the server has seen a uuid go by. <b>THIS IS A
     * BLOCKING THREAD!</b>
     *
     * @param uuid The uuid to check
     * @return A list of PlayerInfoUnions, which will contain the date of the
     * uuid, the name and the date the name was last seen used. The names will
     * be in order from oldest to most recent.
     */
    public List<PlayerInfoUnion> getNames(UUID uuid)
    {
        final String query = FileParser.getContents("queries/get-names-from-uuid.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(uuid.toString())
                    .executeReader();

            final List<PlayerInfoUnion> names = new ArrayList<PlayerInfoUnion>();

            while (result.next())
            {
                PlayerInfoUnion info = new PlayerInfoUnion();
                info.uuid = uuid;
                info.name = result.getString(1);
                info.date = result.getDate(2);
                names.add(info);
            }

            result.close();

            return names;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private int updateSchema(int currentVersion, final MySqlConnection conn)
    {
        final SequentialUpdater schemaUpdater = new SequentialUpdater();

        //Initially create the tables
        schemaUpdater.addUpdateStep(new IUpdateTask()
        {
            @Override
            public void runUpdate() throws Exception
            {
                String query = FileParser.getContents("queries/create-pl_uuids-table.sql", PlayerLookups.class);
                conn.query(query).executeUpdate();
                getLogger().info("pl_uuids table created successfully.");

                query = FileParser.getContents("queries/create-pl_names-table.sql", PlayerLookups.class);
                conn.query(query).executeUpdate();
                getLogger().info("pl_names table created successfully.");

                query = FileParser.getContents("queries/create-pl_ips-table.sql", PlayerLookups.class);
                conn.query(query).executeUpdate();
                getLogger().info("pl_ips table created successfully.");

                query = FileParser.getContents("queries/create-add_player-procedure.sql", PlayerLookups.class);
                conn.query(query).executeUpdate();
                getLogger().info("add_player procedure created successfully.");
            }
        });

        return schemaUpdater.update(currentVersion);
    }
}
