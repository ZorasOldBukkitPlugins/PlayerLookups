
package com.lagopusempire.playerlookups.utils;

/**
 *
 * @author MrZoraman
 */
public class IpUtils
{
    private IpUtils()
    {
    }
    
    public static boolean isIpAddress(String address)
    {
        if(address == null)
            return false;
        
        return address.matches("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
    }
}
