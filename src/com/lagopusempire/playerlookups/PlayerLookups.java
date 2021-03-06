package com.lagopusempire.playerlookups;

import com.lagopusempire.playerlookups.commands.HelpCommand;
import com.lagopusempire.playerlookups.commands.LookupNameCommand;
import com.lagopusempire.playerlookups.commands.LookupUUIDCommand;
import com.lagopusempire.playerlookups.commands.PlCommandBase;
import com.lagopusempire.playerlookups.evilmidget38.NameFetcher;
import com.lagopusempire.playerlookups.evilmidget38.UUIDFetcher;
import com.lagopusempire.playerlookups.listeners.CommandListener;
import com.lagopusempire.playerlookups.listeners.PlayerLoginListener;
import com.lagopusempire.playerlookups.mysql.MySqlConnection;
import com.lagopusempire.playerlookups.mysql.MySqlCreds;
import com.lagopusempire.playerlookups.utils.IUpdateTask;
import com.lagopusempire.playerlookups.utils.SequentialUpdater;
import com.lagopusempire.playerlookups.utils.files.ConfigAccessor;
import com.lagopusempire.playerlookups.utils.files.FileParser;
import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.BukkitCommandSystem;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MrZoraman
 */
public class PlayerLookups extends JavaPlugin implements Listener
{

    private final BukkitCommandSystem cs;

    private MySqlConnection connection;
    private ConfigAccessor messages;

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
        
        messages = new ConfigAccessor(this, "messages.yml");
        messages.getConfig().options().copyDefaults(true);
        messages.saveConfig();

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
        
        HelpCommand helpCmd = new HelpCommand(this);
        
        PlCommandBase cmd = new LookupUUIDCommand(this);
        helpCmd.addCommand(cmd);
        cs.registerCommand("lookup {uuid|uuids}", cmd);
        
        cmd = new LookupNameCommand(this);
        helpCmd.addCommand(cmd);
        cs.registerCommand("lookup {name|names}", cmd);
        
        cs.registerCommand("lookup {help|?}", helpCmd);
        cs.registerCommand("lookup", helpCmd);

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(connection), this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
    }
    
    public FileConfiguration getMessages()
    {
        return messages.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
    {
        System.out.println("Command recieved: " + cmd.getName());
        for (int ii = 0; ii < args.length; ii++)
        {
            System.out.print(" " + args[ii]);
        }

        return true;
    }

    /**
     * See {@link #getUniqueIdsFromIp(java.lang.String)}
     *
     * @param addr The address to check
     * @return A list of ips that have used the given address
     */
    public List<UUID> getUniqueIdsFromIp(InetAddress addr)
    {
        return getUniqueIdsFromIp(addr.toString().substring(1));
    }

    /**
     * Gets a list of unique ids the server has seen a certain ip use. <b>THIS
     * IS A BLOCKING THREAD!</b>
     *
     * @param ip The ip address to check
     * @return A list (order not important) of UUIDS that have used the given ip
     * address
     */
    public List<UUID> getUniqueIdsFromIp(String ip)
    {
        final String query = FileParser.getContents("queries/get-uuids-from-ip.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(ip)
                    .executeReader();

            final List<UUID> ids = new ArrayList<UUID>();

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
     * Uses evilmidget38's uuidfetcher to get a uuid given a player name.
     * <b>THIS IS A BLOCKING THREAD!</b>
     *
     * @param name The name of the player
     * @return An instance that contains the name, the uuid currently using that name and whether the request failed or not.
     */
    public MojangServerResult getCurrentUniqueIdUsingName(String name)
    {
        final UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(name));
        MojangServerResult result = new MojangServerResult();
        try
        {
            result.uuid = fetcher.call().get(name);
            result.name = name;
            return result;
        }
        catch (IOException e)
        {
            result.failed = true;
            getLogger().warning("Failed to retrieve uuid info from external server: " + e.getMessage());
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Uses evilmidget38's namefetcher to get a name given a uuid. <b>THIS IS A
     * BLOCKING THREAD!</b>
     *
     * @param uuid The uud to lookup
     * @return An instance that contains the uuid, the name currently using that uuid and whether the request failed or not.
     */
    public MojangServerResult getCurrentNameUsingUUID(UUID uuid)
    {
        final NameFetcher fetcher = new NameFetcher(Arrays.asList(uuid));
        MojangServerResult result = new MojangServerResult();
        try
        {
            result.name = fetcher.call().get(uuid);
            result.uuid = uuid;
            return result;
        }
        catch (IOException e)
        {
            result.failed = true;
            getLogger().warning("Failed to retrieve uuid info from external server: " + e.getMessage());
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a list of unique ids the server has seen use a certain name use.
     * <b>THIS IS A BLOCKING THREAD!</b>
     *
     * @param name The name to check
     * @return A list (order not important) of UUIDS that have used the given
     * name
     */
    public List<UUID> getUniqueIdsFromName(String name)
    {
        final String query = FileParser.getContents("queries/get-uuids-from-name.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(name)
                    .executeReader();

            final List<UUID> ids = new ArrayList<UUID>();

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
     * Gets a list of names and the date the server has last seen the name given
     * a uuid. <b>THIS IS A BLOCKING THREAD!</b>
     *
     * @param name The name to lookup
     * @return A list (oldest at index=0) of names and timestamps that the
     * server has seen a uuid use.
     */
    public List<PlayerInfo> getUniqueIdsAndDatesFromName(String name)
    {
        final String query = FileParser.getContents("queries/get-uuids-from-name.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(name)
                    .executeReader();

            final List<PlayerInfo> ids = new ArrayList<PlayerInfo>();

            while (result.next())
            {
                PlayerInfo info = new PlayerInfo();
                info.uuid = UUID.fromString(result.getString(1));
                info.date = result.getDate(2);

                ids.add(info);
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
     * Gets a list of ip addresses that the server has seen a uuid use. <b>THIS
     * IS A BLOCKING THREAD!</b>
     *
     * @param uuid The uuid to check
     * @return A list (order is not maintained) of ips that the server has seen
     * a uuid use
     */
    public List<PlayerInfo> getIps(UUID uuid)
    {
        final String query = FileParser.getContents("queries/get-ips-from-uuid.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(uuid.toString())
                    .executeReader();

            final List<PlayerInfo> ips = new ArrayList<PlayerInfo>();

            while (result.next())
            {
                PlayerInfo info = new PlayerInfo();
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
    public List<PlayerInfo> getNames(UUID uuid)
    {
        final String query = FileParser.getContents("queries/get-names-from-uuid.sql", getClass());
        try
        {
            ResultSet result = connection.query(query)
                    .setString(uuid.toString())
                    .executeReader();

            final List<PlayerInfo> names = new ArrayList<PlayerInfo>();

            while (result.next())
            {
                PlayerInfo info = new PlayerInfo();
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

                query = FileParser.getContents("queries/drop-pl_add_player-procedure.sql", PlayerLookups.class);
                conn.query(query).executeUpdate();

                query = FileParser.getContents("queries/create-pl_add_player-procedure.sql", PlayerLookups.class);
                conn.query(query).executeUpdate();
                getLogger().info("pl_add_player procedure created successfully.");
            }
        });

        return schemaUpdater.update(currentVersion);
    }
}
