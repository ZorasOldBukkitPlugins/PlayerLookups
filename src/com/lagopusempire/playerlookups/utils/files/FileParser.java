package com.lagopusempire.playerlookups.utils.files;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This parses a resource file within the jar file. It will take all of the
 * contents and put it on one line. It caches the results so the file is only
 * read through once per run.
 * 
 * <b>NOT THREAD SAFE!</b>
 *
 * @author MrZoraman
 *
 */
public class FileParser
{
    private final Map<String, String> queries;
    private final JavaPlugin plugin;

    /**
     * Constructor
     *
     * @param plugin The plugin that the resource is located under. This is
     * important because this class uses bukkit's getResorce() method.
     */
    public FileParser(JavaPlugin plugin)
    {
        queries = new HashMap<String, String>();
        this.plugin = plugin;
    }

    /**
     * Gets the contents of a file. A string will be returned with all of the
     * contents on a single line. Newlines will be lost.
     *
     * @param resourcePath The absolute path from the base project directory.
     * File extention should be included.
     * @return The contents of the file
     */
    public String getContents(String resourcePath)
    {
        if (queries.containsKey(resourcePath))
        {
            return queries.get(resourcePath);
        }
        else
        {            
            InputStream stream = plugin.getResource(resourcePath);
            if (stream == null)
            {
                plugin.getLogger().log(Level.SEVERE, "Failed to find resource " + resourcePath + " in plugin " + plugin.getDescription().getName() + "!");
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

            queries.put(resourcePath, query);

            return query;
        }
    }
}
