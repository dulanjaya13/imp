/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.chatManagement;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
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
public class ChatsManagerTest {
    
    public ChatsManagerTest() {
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
     * Test of createChat method, of class ChatsManager.
     */
    @Test
    public void testCreateChat() {
        System.out.println("createChat");
        String userID = "";
        ChatsManager instance = new ChatsManager();
        Chat expResult = null;
        Chat result = instance.createChat(userID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChatManager method, of class ChatsManager.
     */
    @Test
    public void testGetChatManager() {
        System.out.println("getChatManager");
        ChatsManager instance = new ChatsManager();
        ChatManager expResult = null;
        ChatManager result = instance.getChatManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addFriends method, of class ChatsManager.
     */
    @Test
    public void testAddFriends() {
        System.out.println("addFriends");
        String friend = "";
        ChatsManager instance = new ChatsManager();
        instance.addFriends(friend);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createChatRoomOpen method, of class ChatsManager.
     */
    @Test
    public void testCreateChatRoomOpen() throws Exception {
        System.out.println("createChatRoomOpen");
        String conversationname = "";
        ChatsManager instance = new ChatsManager();
        instance.createChatRoomOpen(conversationname);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createChatRoomClosed method, of class ChatsManager.
     */
    @Test
    public void testCreateChatRoomClosed() throws Exception {
        System.out.println("createChatRoomClosed");
        String conversationname = "";
        String password = "";
        ChatsManager instance = new ChatsManager();
        instance.createChatRoomClosed(conversationname, password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of joinRoom method, of class ChatsManager.
     */
    @Test
    public void testJoinRoom() throws Exception {
        System.out.println("joinRoom");
        String conversationname = "";
        ChatsManager instance = new ChatsManager();
        MultiUserChat expResult = null;
        MultiUserChat result = instance.joinRoom(conversationname);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of joinPrivateRoom method, of class ChatsManager.
     */
    @Test
    public void testJoinPrivateRoom() throws Exception {
        System.out.println("joinPrivateRoom");
        String conversationname = "";
        String password = "";
        ChatsManager instance = new ChatsManager();
        MultiUserChat expResult = null;
        MultiUserChat result = instance.joinPrivateRoom(conversationname, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedConversation method, of class ChatsManager.
     */
    @Test
    public void testGetSelectedConversation() {
        System.out.println("getSelectedConversation");
        String expResult = "";
        String result = ChatsManager.getSelectedConversation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSelectedConversation method, of class ChatsManager.
     */
    @Test
    public void testSetSelectedConversation() {
        System.out.println("setSelectedConversation");
        String selectedConversation = "";
        ChatsManager.setSelectedConversation(selectedConversation);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMuc method, of class ChatsManager.
     */
    @Test
    public void testGetMuc() {
        System.out.println("getMuc");
        ChatsManager instance = new ChatsManager();
        MultiUserChat expResult = null;
        MultiUserChat result = instance.getMuc();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
