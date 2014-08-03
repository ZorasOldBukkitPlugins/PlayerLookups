package com.lagopusempire.playerlookups;

import com.lagopusempire.playerlookups.mysql.MySqlConnection;
import com.lagopusempire.playerlookups.mysql.MySqlCreds;
import com.lagopusempire.playerlookups.utils.IUpdateTask;
import com.lagopusempire.playerlookups.utils.SequentialUpdater;
import com.lagopusempire.playerlookups.utils.files.FileParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MrZoraman
 */
public class PlayerLookups extends JavaPlugin
{

    private final FileParser parser;

    public PlayerLookups()
    {
        super();
        this.parser = new FileParser(this);
    }

    @Override
    public void onEnable()
    {
        final MySqlCreds creds = new MySqlCreds(
                getConfig().getString("mysql.host"),
                getConfig().getInt("mysql.port"),
                getConfig().getString("mysql.database"),
                getConfig().getString("mysql.user"),
                getConfig().getString("mysql.password"));
        
        MySqlConnection connection = new MySqlConnection(getLogger(), creds);
        updateSchema(getConfig().getInt("mysql.schema-version", 0), connection);
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
            }
        });

        return schemaUpdater.update(currentVersion);
    }
}
