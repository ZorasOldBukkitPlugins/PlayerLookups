package com.lagopusempire.playerlookups.utils;

/**
 * Tries to run a body of code that may throw an exception
 *
 * @author MrZoraman
 *
 */
public interface IUpdateTask
{

    /**
     * Runs an update
     *
     * @throws Exception If something goes wrong. This method does not have to
     * have any exception-throwing code inside it, however.
     */
    public void runUpdate() throws Exception;
}
