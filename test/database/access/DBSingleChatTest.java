/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.access;

import java.sql.ResultSet;
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
public class DBSingleChatTest {
    
    public DBSingleChatTest() {
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
     * Test of saveTextMessage method, of class DBSingleChat.
     */
    @Test
    public void testSaveTextMessage() {
        System.out.println("saveTextMessage");
        String contactname = "";
        boolean isRecieved = false;
        String message = "";
        Timestamp time = null;
        DBSingleChat.saveTextMessage(contactname, isRecieved, message, time);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of savePictureMessage method, of class DBSingleChat.
     */
    @Test
    public void testSavePictureMessage() {
        System.out.println("savePictureMessage");
        String contactname = "";
        boolean isRecieved = false;
        String path = "";
        Timestamp time = null;
        DBSingleChat.savePictureMessage(contactname, isRecieved, path, time);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveFileMessage method, of class DBSingleChat.
     */
    @Test
    public void testSaveFileMessage() {
        System.out.println("saveFileMessage");
        String contactname = "";
        boolean isRecieved = false;
        String path = "";
        Timestamp time = null;
        DBSingleChat.saveFileMessage(contactname, isRecieved, path, time);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readMessages method, of class DBSingleChat.
     */
    @Test
    public void testReadMessages() {
        System.out.println("readMessages");
        String contactname = "";
        ResultSet expResult = null;
        ResultSet result = DBSingleChat.readMessages(contactname);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearHistory method, of class DBSingleChat.
     */
    @Test
    public void testClearHistory() {
        System.out.println("clearHistory");
        String contactname = "";
        DBSingleChat.clearHistory(contactname);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
