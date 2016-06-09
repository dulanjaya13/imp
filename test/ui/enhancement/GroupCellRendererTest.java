/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.enhancement;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.jivesoftware.smackx.muc.HostedRoom;
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
public class GroupCellRendererTest {
    
    public GroupCellRendererTest() {
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
     * Test of call method, of class GroupCellRenderer.
     */
    @Test
    public void testCall() {
        System.out.println("call");
        ListView<HostedRoom> param = null;
        GroupCellRenderer instance = new GroupCellRenderer();
        ListCell<HostedRoom> expResult = null;
        ListCell<HostedRoom> result = instance.call(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
