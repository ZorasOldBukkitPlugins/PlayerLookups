package com.lagopusempire.playerlookups.utils.files;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This parses a resource file within the jar file. It will take all of the
 * contents and put it on one line. It caches the results so the file is only
 * read through once per run.
 *
 * @author MrZoraman
 *
 */
public class FileParser
{

    private static final Map<String, String> queries = new ConcurrentHashMap<String, String>();
    
    /**
     * static class
     */
    private FileParser()
    {
    }

    public static String getContents(String path, Class clazz)
    {
        if(queries.containsKey(path))
        {
            return queries.get(path);
        }
        
        ClassLoader cl = clazz.getClassLoader();
        URL url = cl.getResource(path);
        InputStream stream = null;

        try
        {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            stream = conn.getInputStream();
        }
        catch (Exception e)
        {
            return null;
        }

        if (stream == null)
        {
            return null;
        }

        Scanner scan = new Scanner(stream);

        StringBuilder queryBuilder = new StringBuilder();
        while (scan.hasNext())
        {
            queryBuilder.append(scan.nextLine());
        }

        scan.close();

        String query = queryBuilder.toString();
        
        queries.putIfAbsent(path, query);

        return query;
    }
}
