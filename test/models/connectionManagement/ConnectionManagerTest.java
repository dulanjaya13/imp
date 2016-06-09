/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.connectionManagement;

import javafx.scene.image.Image;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
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
public class ConnectionManagerTest {
    
    public ConnectionManagerTest() {
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
     * Test of getConnectionManager method, of class ConnectionManager.
     */
    @Test
    public void testGetConnectionManager() {
        System.out.println("getConnectionManager");
        ConnectionManager expResult = null;
        ConnectionManager result = ConnectionManager.getConnectionManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of connectToServer method, of class ConnectionManager.
     */
    @Test
    public void testConnectToServer() {
        System.out.println("connectToServer");
        ConnectionManager instance = null;
        instance.connectToServer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loginToServer method, of class ConnectionManager.
     */
    @Test
    public void testLoginToServer() throws Exception {
        System.out.println("loginToServer");
        String userName = "";
        String password = "";
        ConnectionManager instance = null;
        instance.loginToServer(userName, password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of logOutServer method, of class ConnectionManager.
     */
    @Test
    public void testLogOutServer() {
        System.out.println("logOutServer");
        ConnectionManager instance = null;
        instance.logOutServer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registerWithServer method, of class ConnectionManager.
     */
    @Test
    public void testRegisterWithServer() throws Exception {
        System.out.println("registerWithServer");
        String username = "";
        String password = "";
        ConnectionManager instance = null;
        instance.registerWithServer(username, password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMyUsername method, of class ConnectionManager.
     */
    @Test
    public void testGetMyUsername() {
        System.out.println("getMyUsername");
        ConnectionManager instance = null;
        String expResult = "";
        String result = instance.getMyUsername();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMyPresence method, of class ConnectionManager.
     */
    @Test
    public void testGetMyPresence() {
        System.out.println("getMyPresence");
        ConnectionManager instance = null;
        String expResult = "";
        String result = instance.getMyPresence();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMyPresence method, of class ConnectionManager.
     */
    @Test
    public void testSetMyPresence() {
        System.out.println("setMyPresence");
        ConnectionManager instance = null;
        instance.setMyPresence();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMyAbsence method, of class ConnectionManager.
     */
    @Test
    public void testSetMyAbsence() {
        System.out.println("setMyAbsence");
        ConnectionManager instance = null;
        instance.setMyAbsence();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMyProfileAvatar method, of class ConnectionManager.
     */
    @Test
    public void testGetMyProfileAvatar() throws Exception {
        System.out.println("getMyProfileAvatar");
        ConnectionManager instance = null;
        Image expResult = null;
        Image result = instance.getMyProfileAvatar();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoster method, of class ConnectionManager.
     */
    @Test
    public void testGetRoster() {
        System.out.println("getRoster");
        ConnectionManager instance = null;
        Roster expResult = null;
        Roster result = instance.getRoster();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChatManager method, of class ConnectionManager.
     */
    @Test
    public void testGetChatManager() {
        System.out.println("getChatManager");
        ConnectionManager instance = null;
        ChatManager expResult = null;
        ChatManager result = instance.getChatManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXMPPConnection method, of class ConnectionManager.
     */
    @Test
    public void testGetXMPPConnection() {
        System.out.println("getXMPPConnection");
        ConnectionManager instance = null;
        XMPPConnection expResult = null;
        XMPPConnection result = instance.getXMPPConnection();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addOfflineMessageListener method, of class ConnectionManager.
     */
    @Test
    public void testAddOfflineMessageListener() {
        System.out.println("addOfflineMessageListener");
        ConnectionManager instance = null;
        instance.addOfflineMessageListener();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeOfflinePacketListener method, of class ConnectionManager.
     */
    @Test
    public void testRemoveOfflinePacketListener() {
        System.out.println("removeOfflinePacketListener");
        ConnectionManager instance = null;
        instance.removeOfflinePacketListener();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
