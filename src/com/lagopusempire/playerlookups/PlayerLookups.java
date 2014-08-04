package com.lagopusempire.playerlookups;

import com.lagopusempire.playerlookups.commands.LookupUUIDCommand;
import com.lagopusempire.playerlookups.evilmidget38.NameFetcher;
import com.lagopusempire.playerlookups.evilmidget38.UUIDFetcher;
import com.lagopusempire.playerlookups.listeners.PlayerLoginListener;
import com.lagopusempire.playerlookups.mysql.MySqlConnection;
import com.lagopusempire.playerlookups.mysql.MySqlCreds;
import com.lagopusempire.playerlookups.utils.IUpdateTask;
import com.lagopusempire.playerlookups.utils.SequentialUpdater;
import com.lagopusempire.playerlookups.utils.files.FileParser;
import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.BukkitCommandSystem;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.Listener;
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

        cs.registerCommand("lookup uuid", new LookupUUIDCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(connection), this);
    }
    
    /**
     * See {@link #getUniqueIdsFromIp(java.lang.String)}
     * @param addr The address to check
     * @return A set of ips that have used the given address
     */
    public Set<UUID> getUniqueIdsFromIp(InetAddress addr)
    {
        return getUniqueIdsFromIp(addr.toString().substring(1));
    }
    
    /**
     * Gets a set of unique ids the server has seen a certain ip use. <b>THIS
     * IS A BLOCKING THREAD!</b>
     * @param ip The ip address to check
     * @return  A set (order not important) of UUIDS that have used the given ip address
     */
    public Set<UUID> getUniqueIdsFromIp(String ip)
    {
        final String query = FileParser.getContents("queries/get-uuids-from-ip.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(ip)
                    .executeReader();

            final Set<UUID> ids = new HashSet<UUID>();

            while (result.next())
            {
                ids.add(UUID.fromString(result.getString(1)));
            }

            result.close();

            return ids;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Uses evilmidget38's uuidfetcher to get a uuid given a player name. <b>THIS
     * IS A BLOCKING THREAD!</b>
     * @param name The name of the player
     * @return The player's uuid
     */
    public UUID getCurrentUniqueIdUsingName(String name)
    {
        final UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(name));
        try
        {
            return fetcher.call().get(name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Uses evilmidget38's namefetcher to get a name given a uuid. <b>THIS
     * IS A BLOCKING THREAD!</b>
     * @param uuid The uud to lookup
     * @return The current name the uuid is using
     */
    public String getCurrentNameUsingUUID(UUID uuid)
    {
        final NameFetcher fetcher = new NameFetcher(Arrays.asList(uuid));
        try
        {
            return fetcher.call().get(uuid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a set of unique ids the server has seen use a certain name use. <b>THIS
     * IS A BLOCKING THREAD!</b>
     *
     * @param name The name to check
     * @return A set (order not important) of UUIDS that have used the given
     * name
     */
    public Set<UUID> getUniqueIdsFromName(String name)
    {
        final String query = FileParser.getContents("queries/get-uuids-from-name.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(name)
                    .executeReader();

            final Set<UUID> ids = new HashSet<UUID>();

            while (result.next())
            {
                ids.add(UUID.fromString(result.getString(1)));
            }

            result.close();

            return ids;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a set of ip addresses that the server has seen a uuid use. <b>THIS
     * IS A BLOCKING THREAD!</b>
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
