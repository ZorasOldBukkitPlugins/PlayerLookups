
package test.java.com.lagopusempire.playerlookups.utils;

import com.lagopusempire.playerlookups.utils.IpUtils;
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
public class IpUtilsTest
{
    
    public IpUtilsTest()
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
     * Test of isIpAddress method, of class IpUtils.
     */
    @Test
    public void testIsIpAddress()
    {
        assertTrue(IpUtils.isIpAddress("192.168.1.1"));
        assertTrue(IpUtils.isIpAddress("127.0.0.1"));
        assertTrue(IpUtils.isIpAddress("255.255.255.255"));
        assertTrue(IpUtils.isIpAddress("1.1.1.1"));
        assertFalse(IpUtils.isIpAddress("999.999.999.999"));
        assertFalse(IpUtils.isIpAddress("12.4235.24"));
        assertFalse(IpUtils.isIpAddress("12.123.134"));
        assertFalse(IpUtils.isIpAddress("123.123.123.123.123"));
        assertFalse(IpUtils.isIpAddress("green eggs and ham"));
        assertFalse(IpUtils.isIpAddress(null));
    }
    
}
