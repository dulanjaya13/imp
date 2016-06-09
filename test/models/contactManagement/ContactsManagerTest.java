/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.contactManagement;

import java.util.Collection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.RosterPacket;
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
public class ContactsManagerTest {
    
    public ContactsManagerTest() {
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
     * Test of getContacts method, of class ContactsManager.
     */
    @Test
    public void testGetContacts() {
        System.out.println("getContacts");
        ContactsManager instance = new ContactsManager();
        Collection<RosterEntry> expResult = null;
        Collection<RosterEntry> result = instance.getContacts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPresence method, of class ContactsManager.
     */
    @Test
    public void testGetPresence() {
        System.out.println("getPresence");
        String user = "";
        ContactsManager instance = new ContactsManager();
        String expResult = "";
        String result = instance.getPresence(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAvailabilityforSharing method, of class ContactsManager.
     */
    @Test
    public void testGetAvailabilityforSharing() {
        System.out.println("getAvailabilityforSharing");
        String user = "";
        ContactsManager instance = new ContactsManager();
        boolean expResult = false;
        boolean result = instance.getAvailabilityforSharing(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAvailabilityforCalling method, of class ContactsManager.
     */
    @Test
    public void testGetAvailabilityforCalling() {
        System.out.println("getAvailabilityforCalling");
        String user = "";
        ContactsManager instance = new ContactsManager();
        boolean expResult = false;
        boolean result = instance.getAvailabilityforCalling(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addContact method, of class ContactsManager.
     */
    @Test
    public void testAddContact() {
        System.out.println("addContact");
        String userName = "";
        ContactsManager instance = new ContactsManager();
        instance.addContact(userName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeFriend method, of class ContactsManager.
     */
    @Test
    public void testRemoveFriend() {
        System.out.println("removeFriend");
        String userName = "";
        ContactsManager instance = new ContactsManager();
        instance.removeFriend(userName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFriendRequest method, of class ContactsManager.
     */
    @Test
    public void testGetFriendRequest() {
        System.out.println("getFriendRequest");
        ContactsManager instance = new ContactsManager();
        instance.getFriendRequest();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoster method, of class ContactsManager.
     */
    @Test
    public void testGetRoster() {
        System.out.println("getRoster");
        ContactsManager instance = new ContactsManager();
        Roster expResult = null;
        Roster result = instance.getRoster();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserType method, of class ContactsManager.
     */
    @Test
    public void testGetUserType() {
        System.out.println("getUserType");
        String user = "";
        ContactsManager instance = new ContactsManager();
        RosterPacket.ItemType expResult = null;
        RosterPacket.ItemType result = instance.getUserType(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of acceptFriend method, of class ContactsManager.
     */
    @Test
    public void testAcceptFriend() {
        System.out.println("acceptFriend");
        String user = "";
        ContactsManager instance = new ContactsManager();
        instance.acceptFriend(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
