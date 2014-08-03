package com.lagopusempire.playerlookups.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author MrZoraman
 */
public class MySqlInteracter
{

    MySqlInteracter(final Connection conn, String query) throws SQLException
    {
        if (conn == null)
        {
            throw new NullPointerException("Connection cannot be null!");
        }

        if (conn.isClosed())
        {
            throw new IllegalStateException("Connection must be open!");
        }

        if (query == null)
        {
            throw new NullPointerException("Query cannot be null!");
        }

        this.pst = conn.prepareStatement(query);

        this.index = 1;
    }

    private final PreparedStatement pst;

    public void executeUpdate() throws SQLException
    {
        pst.executeUpdate();
    }

    public ResultSet executeReader() throws SQLException
    {
        return pst.executeQuery();
    }

    private int index;

    public MySqlInteracter setInt(int value) throws SQLException
    {
        pst.setInt(index++, value);
        return this;
    }

    public MySqlInteracter setString(String value) throws SQLException
    {
        pst.setString(index++, value);
        return this;
    }

    public MySqlInteracter setDouble(double value) throws SQLException
    {
        pst.setDouble(index++, value);
        return this;
    }

    public MySqlInteracter setBoolean(boolean value) throws SQLException
    {
        pst.setBoolean(index++, value);
        return this;
    }

    public MySqlInteracter setObject(Object value) throws SQLException
    {
        pst.setObject(index++, value);
        return this;
    }
}
