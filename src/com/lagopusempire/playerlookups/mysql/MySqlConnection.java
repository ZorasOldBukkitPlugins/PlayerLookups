package com.lagopusempire.playerlookups.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * 
 * @author MrZoraman
 */
public class MySqlConnection
{

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    public static String getDriverClassName()
    {
        return DRIVER_CLASS;
    }

    public static boolean isDriverInstalled()
    {
        try
        {
            Class.forName(DRIVER_CLASS);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private final MySqlCreds creds;
    private final Logger logger;
    private final String databaseName;

    private Connection conn;

    public MySqlConnection(final Logger logger, final MySqlCreds creds)
    {
        this.creds = creds;
        this.logger = logger;
        this.databaseName = creds.getDatabaseName();
    }

    public MySqlInteracter query(String query) throws SQLException
    {
        return new MySqlInteracter(connect(), query);
    }

    /**
     * Refreshes and retrieves a mysql connection. An attempt to establish a
     * connection will be made, and return null if a connection cannot be made
     *
     * @return The {@link java.sql.Connection connection} if the connection was
     * successful or there is already a connection, false if the connection
     * cannot be established
     */
    private Connection connect()
    {
        try
        {
            if (conn == null || conn.isClosed() || !conn.isValid(1))
            {
                conn = DriverManager.getConnection(creds.toUrl(), creds.getUser(), creds.getPassword());
                logger.info("Successfully established connection with mysql database.");
            }

            return conn;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void close()
    {
        try
        {
            if (conn != null && !conn.isClosed())
            {
                conn.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String getDatabaseName()
    {
        return databaseName;
    }
}
