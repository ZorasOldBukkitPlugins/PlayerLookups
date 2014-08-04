/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lagopusempire.playerlookups.utils;

import org.bukkit.ChatColor;

/**
 *
 * @author MrZoraman
 */
public class Formatter
{
    private static final char COLOR_CHAR = '&';
    
    private static final String NAME_STRING = "%name%";
    private static final String IP_STRING = "%ip%";
    private static final String UUID_STRING = "%uuid%";
    private static final String NUMBER_STRING = "%#%";
    
    private String message;
    
    public Formatter(String message)
    {
        this.message = message;
    }
    
    public Formatter dup()
    {
        return new Formatter(message);
    }
    
    public Formatter colorize()
    {
        this.message = ChatColor.translateAlternateColorCodes(COLOR_CHAR, message);
        return this;
    }
    
    public Formatter decolorize()
    {
        this.message = ChatColor.stripColor(message);
        return this;
    }
    
    public Formatter setName(String name)
    {
        this.message = message.replaceAll(NAME_STRING, name);
        return this;
    }
    
    public Formatter setIp(String ip)
    {
        this.message = message.replaceAll(IP_STRING, ip);
        return this;
    }
    
    public Formatter setUUID(String uuid)
    {
        this.message = message.replaceAll(UUID_STRING, uuid);
        return this;
    }
    
    public Formatter setNumber(int number)
    {
        this.message = this.message.replaceAll(NUMBER_STRING, String.valueOf(number));
        return this;
    }
    
    public Formatter setNumber(String number)
    {
        this.message = this.message.replaceAll(NUMBER_STRING, number);
        return this;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
