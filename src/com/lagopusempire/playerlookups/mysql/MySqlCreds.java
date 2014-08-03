package com.lagopusempire.playerlookups.mysql;

/**
 *
 * This class stores the credentials needed to connect to a
 * {@link com.mrz.dyndns.server.base.mysql.SimpleMySql SimpleMySql} database
 *
 * @author Zora
 */
public class MySqlCreds
{

    /**
     * The constructor
     *
     * @param host The mysql server host (like localhost)
     * @param port The port the server is on (most likely 3306)
     * @param database The database name
     * @param user The username
     * @param password The password for that user
     */
    public MySqlCreds(String host, int port, String database, String user, String password)
    {

        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public String toUrl()
    {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public String getUser()
    {
        return user;
    }

    protected String getPassword()
    {
        return password;
    }

    public String getDatabaseName()
    {
        return database;
    }

    @Override
    public String toString()
    {
        return "USER=" + user + " URL=" + toUrl();
    }
}
