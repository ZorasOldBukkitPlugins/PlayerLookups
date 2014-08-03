package com.lagopusempire.playerlookups;

import com.lagopusempire.playerlookups.commands.LookupUUIDCommand;
import com.lagopusempire.playerlookups.listeners.PlayerLoginListener;
import com.lagopusempire.playerlookups.mysql.MySqlConnection;
import com.lagopusempire.playerlookups.mysql.MySqlCreds;
import com.lagopusempire.playerlookups.utils.IUpdateTask;
import com.lagopusempire.playerlookups.utils.SequentialUpdater;
import com.lagopusempire.playerlookups.utils.files.FileParser;
import com.lagopusempire.playerlookups.zorascommandsystem.bukkitcompat.BukkitCommandSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MrZoraman
 */
public class PlayerLookups extends JavaPlugin
{
    private final FileParser parser;
    private final BukkitCommandSystem cs;

    public PlayerLookups()
    {
        super();
        this.parser = new FileParser(this);
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
        
        MySqlConnection connection = new MySqlConnection(getLogger(), creds);
        int schemaVersion = updateSchema(getConfig().getInt("mysql.schema-version", 0), connection);
        getConfig().set("mysql.schema-version", schemaVersion);
        saveConfig();
        
        cs.registerCommand("lookup uuid", new LookupUUIDCommand());
        
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(parser, connection), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
    {
        return true;
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
                String query = parser.getContents("queries/create-pl_uuids-table.sql");
                conn.query(query).executeUpdate();
                getLogger().info("pl_uuids table created successfully.");

                query = parser.getContents("queries/create-pl_names-table.sql");
                conn.query(query).executeUpdate();
                getLogger().info("pl_names table created successfully.");

                query = parser.getContents("queries/create-pl_ips-table.sql");
                conn.query(query).executeUpdate();
                getLogger().info("pl_ips table created successfully.");
                
                query = parser.getContents("queries/create-add_player-procedure.sql");
                conn.query(query).executeUpdate();
                getLogger().info("add_player procedure created successfully.");
            }
        });

        return schemaUpdater.update(currentVersion);
    }
}
