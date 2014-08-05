
package test.java.com.lagopusempire.playerlookups;

import com.lagopusempire.playerlookups.CommandInfoInjector;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author MrZoraman
 */
public class CommandInfoInjectorTest
{
    
    public CommandInfoInjectorTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of inject method, of class CommandInfoInjector.
     */
    @Test
    public void testInject()
    {
        System.out.println("inject");
        Player sender = null;
        CommandInfoInjector instance = new CommandInfoInjector(null);
    }
}
