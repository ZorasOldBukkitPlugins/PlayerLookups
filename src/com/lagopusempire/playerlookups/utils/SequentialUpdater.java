package com.lagopusempire.playerlookups.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates -something- in a step-by-step manner. This is intended for things
 * such as MySql schema updates that must be executed in a certain order.
 *
 * @author MrZoraman
 *
 */
public class SequentialUpdater
{

    private final List<IUpdateTask> steps;

    private boolean failed;

    /**
     * Constructor
     */
    public SequentialUpdater()
    {
        this.steps = new ArrayList<IUpdateTask>();
        this.failed = false;
    }

    /**
     * Adds a step to the sequence. Steps must be added in order from first to
     * last.
     *
     * @param step The runnable with code to perform the desired update
     */
    public void addUpdateStep(IUpdateTask step)
    {
        steps.add(step);
    }

    /**
     * Runs the updater
     *
     * @param version The version the config number is at.
     * @return The version of the latest successful update
     */
    public int update(int version)
    {
        for (int ii = version; ii < steps.size(); ii++)
        {
            try
            {
                steps.get(ii).runUpdate();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.failed = true;
                //return 1 before the update so that the next one (the one that failed) will be executed again on next update
                if (ii - 1 < 0)
                {
                    return 0;
                }
                else
                {
                    return ii - 1;
                }
            }
        }

        return steps.size();
    }

    /**
     * Checks if something failed during the update or not
     *
     * @return True if the update failed somewhere, false if all went ok
     */
    public boolean failed()
    {
        return failed;
    }
}
