package net.porwit;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit test for ExpiringMap
 */
public class ExpiringMapTest
{
    @Test
    public void simplePutTest() {
	    ExpiringMap<String, String> map = new ExpiringMap<>(1000);
	    String value = "testValue1";
	    String ret = map.put("testKey1", value, 5000);
	    assertTrue("simple put works", ret == null);
    }

    @Test
    public void replacementPutTest() {
	    ExpiringMap<String, String> map = new ExpiringMap<>(1000);
	    String value = "testValue1";
	    String ret = map.put("testKey1", value, 5000);
	    ret = map.put("testKey1", value, 5000);
	    assertEquals("replacement works", value, ret);
    }
    
    @Test
    public void simpleGetTest() {
    	ExpiringMap<String, String> map = new ExpiringMap<>(1000);
    	String value = "testValue1";
    	String ret = map.put("testKey1", value, 5000);
    	
    	ret = map.get("testKey1");
    	assertNotNull("ret is not null", ret);
    	assertEquals("valid return", value, ret);
    }
    
    @Test
    public void failedGetTest() {
    	ExpiringMap<String, String> map = new ExpiringMap<>(1000);
    	String ret = map.get("testKey1");
    	assertNull("ret is null", ret);
    }
    
    @Test
    public void expiryGetSuccessTest() throws InterruptedException {
    	ExpiringMap<String, String> map = new ExpiringMap<>(1000);
    	long timeout = 1000;
    	String value = "testValue1";
    	String ret = map.put("testKey1", value, timeout);
    	Thread.sleep(timeout + 1);
    	ret = map.get("testKey1");
    	assertNull("ret is null", ret);
    }
    
    @Test
    public void synchronousCleanupTest() throws InterruptedException {
    	ExpiringMap<String, String> map = new ExpiringMap<>(1000);
    	long timeout = 500;
    	map.put("testKey1", "testValue1", timeout);
    	map.put("testKey2", "testValue2", timeout);
    	map.put("testKey3", "testValue3", timeout);
    	timeout = 5000;
    	map.put("testKey4", "testValue4", timeout);
    	map.put("testKey5", "testValue5", timeout);
    	map.put("testKey6", "testValue6", timeout);
    	assertEquals("map has six elements", 6, map.size());
    	Thread.sleep(500+1);
    	map.cleanup();
    	assertEquals("map has three elements", 3, map.size());
    	Thread.sleep(timeout + 1);
    	map.cleanup();
    	assertEquals("map has zero elements", 0, map.size());
    }
    
    @Test
    public void emptyMapCleanupTest() {
    	ExpiringMap<String, String> map = new ExpiringMap<>(1000);
    	map.cleanup();
    	assertEquals("map has zero elements", 0, map.size());
    }
    
    @Test
    public void timerBasedCleanupTest() throws InterruptedException {
    	ExpiringMap<String, String> map = new ExpiringMap<>(1000);
    	long timeout = 500;
    	map.put("testKey1", "testValue1", timeout);
    	map.put("testKey2", "testValue2", timeout);
    	map.put("testKey3", "testValue3", timeout);
    	timeout = 5000;
    	map.put("testKey4", "testValue4", timeout);
    	map.put("testKey5", "testValue5", timeout);
    	map.put("testKey6", "testValue6", timeout);
    	assertEquals("map has six elements", 6, map.size());
    	Thread.sleep(2000+1);
    	assertEquals("map has three elements", 3, map.size());
    	Thread.sleep(timeout + 1);
    	assertEquals("map has zero elements", 0, map.size());
    }
}
