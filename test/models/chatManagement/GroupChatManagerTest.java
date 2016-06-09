/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.chatManagement;

import java.util.List;
import org.jivesoftware.smack.packet.Message;
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
public class GroupChatManagerTest {
    
    public GroupChatManagerTest() {
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
     * Test of getRoom method, of class GroupChatManager.
     */
    @Test
    public void testGetRoom() {
        System.out.println("getRoom");
        GroupChatManager instance = null;
        String expResult = "";
        String result = instance.getRoom();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMuc method, of class GroupChatManager.
     */
    @Test
    public void testGetMuc() {
        System.out.println("getMuc");
        GroupChatManager instance = null;
        MultiUserChat expResult = null;
        MultiUserChat result = instance.getMuc();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMsgList method, of class GroupChatManager.
     */
    @Test
    public void testGetMsgList() {
        System.out.println("getMsgList");
        GroupChatManager instance = null;
        List<Message> expResult = null;
        List<Message> result = instance.getMsgList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
