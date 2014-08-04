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
    
    private static final String PLAYER_STRING = "%player%";
    private static final String SENDER_STRING = "%sender%";
    private static final String RECIEVER_STRING = "%reciever%";
    private static final String MESSAGE_STRING = "%message%";
    
    private String message;
    
    public Formatter(String message)
    {
        this.message = message;
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
    
    public Formatter setPlayer(String name)
    {
        this.message = message.replaceAll(PLAYER_STRING, name);
        return this;
    }
    
    public Formatter setSender(String name)
    {
        this.message = message.replaceAll(SENDER_STRING, name);
        return this;
    }
    
    public Formatter setReciever(String name)
    {
        this.message = message.replaceAll(RECIEVER_STRING, name);
        return this;
    }
    
    public Formatter setMessage(String message)
    {
        this.message = this.message.replaceAll(MESSAGE_STRING, message);
        return this;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
