
package test.java.com.lagopusempire.playerlookups.utils;

import com.lagopusempire.playerlookups.utils.UuidUtils;
import java.util.UUID;
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
public class UuidUtilsTest
{
    
    public UuidUtilsTest()
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
     * Test of removeDashes method, of class UuidUtils.
     */
    @Test
    public void testRemoveDashes()
    {
        System.out.println("removeDashes");
        
        UUID uuid = UUID.fromString("d88ec9e3-8b46-466c-b82f-fa909b371d83");
        String expectedResult = ("d88ec9e38b46466cb82ffa909b371d83");
        assertEquals(expectedResult, UuidUtils.removeDashes(uuid));
    }

    /**
     * Test of fromDashlessString method, of class UuidUtils.
     */
    @Test
    public void testFromDashlessString()
    {
        System.out.println("fromDashlessString");
        
        String uuidString = "d88ec9e38b46466cb82ffa909b371d83";
        UUID uuid = UuidUtils.fromDashlessString(uuidString);
        assertEquals(uuid.toString(), "d88ec9e3-8b46-466c-b82f-fa909b371d83");
    }

    /**
     * Test of isUUID method, of class UuidUtils.
     */
    @Test
    public void testIsUUID()
    {
        System.out.println("isUUID");
        
        assertTrue("mrz", UuidUtils.isUUID("d88ec9e3-8b46-466c-b82f-fa909b371d83"));
        assertTrue("someone else", UuidUtils.isUUID("0d47a480-bb5c-4fc9-b18e-bdc331a63776"));
        assertFalse("green eggs and ham", UuidUtils.isUUID("green eggs and ham"));
        assertFalse("empty string", UuidUtils.isUUID(""));
        assertFalse("null", UuidUtils.isUUID(null));
        assertFalse("potato", UuidUtils.isUUID("hurnngngngnggngngoasdfonaiwejropsdjfjfdsaiop!"));
    }
    
}
