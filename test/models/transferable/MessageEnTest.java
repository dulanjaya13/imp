/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.transferable;

import java.sql.Timestamp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dulanjaya
 */
public class MessageEnTest {
    
    public MessageEnTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMessageContent method, of class MessageEn.
     */
    @Test
    public void testGetMessageContent() {
        System.out.println("getMessageContent");
        MessageEn instance = null;
        String expResult = "";
        String result = instance.getMessageContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIsReceived method, of class MessageEn.
     */
    @Test
    public void testGetIsReceived() {
        System.out.println("getIsReceived");
        MessageEn instance = null;
        Boolean expResult = null;
        Boolean result = instance.getIsReceived();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTime method, of class MessageEn.
     */
    @Test
    public void testGetTime() {
        System.out.println("getTime");
        MessageEn instance = null;
        Timestamp expResult = null;
        Timestamp result = instance.getTime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class MessageEn.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        MessageEn instance = null;
        String expResult = "";
        String result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
