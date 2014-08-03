package com.lagopusempire.playerlookups.utils;

import java.util.UUID;

/**
 * This static class contains various convenience methods for manipulating UUIDS
 *
 * @author MrZoraman
 *
 */
public class UuidUtils
{

    /**
     * No constructor. Static class.
     */
    private UuidUtils()
    {
    }

    public static String removeDashes(UUID uuid)
    {
        return uuid.toString().replaceAll("-", "");
    }

    public static UUID fromDashlessString(String dashlessUuid)
    {
        int length = dashlessUuid.length();

        if (length != 32)
        {
            throw new IllegalStateException("UUID is not the right length! (lemme know 'bout this please)");
        }

        StringBuilder builder = new StringBuilder();
        builder.append(dashlessUuid.substring(0, 8)).append("-");
        builder.append(dashlessUuid.substring(8, 12)).append("-");
        builder.append(dashlessUuid.substring(12, 16)).append("-");
        builder.append(dashlessUuid.substring(16, 20)).append("-");
        builder.append(dashlessUuid.substring(20, length));

//		System.out.println(dashlessUuid.length());
        return UUID.fromString(builder.toString());
    }
}
