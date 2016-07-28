package net.porwit;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for ExpiringMap
 */
public class ExpiringMapTest
{
    @Test
    public void simplePutTest()
    {
	    ExpiringMap<String, String> map = new ExpiringMap<>();
	    String value = "testValue1";
	    String ret = map.put("testKey1", value);
	    assertTrue("simple put works", ret == null);
    }

    @Test
    public void replacementPutTest()
    {
	    ExpiringMap<String, String> map = new ExpiringMap<>();
	    String value = "testValue1";
	    String ret = map.put("testKey1", value);
	    ret = map.put("testKey1", value);
	    assertEquals("replacement works", value.equals(ret));
    }
}
