/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.contacts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
public class ContactsViewControllerTest {
    
    public ContactsViewControllerTest() {
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
     * Test of handleSearchButtonAction method, of class ContactsViewController.
     */
    @Test
    public void testHandleSearchButtonAction() {
        System.out.println("handleSearchButtonAction");
        ActionEvent event = null;
        ContactsViewController instance = new ContactsViewController();
        instance.handleSearchButtonAction(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleAddButtonAction method, of class ContactsViewController.
     */
    @Test
    public void testHandleAddButtonAction() {
        System.out.println("handleAddButtonAction");
        ActionEvent event = null;
        ContactsViewController instance = new ContactsViewController();
        instance.handleAddButtonAction(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleRemoveButtonAction method, of class ContactsViewController.
     */
    @Test
    public void testHandleRemoveButtonAction() {
        System.out.println("handleRemoveButtonAction");
        ActionEvent event = null;
        ContactsViewController instance = new ContactsViewController();
        instance.handleRemoveButtonAction(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initialize method, of class ContactsViewController.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        URL url = null;
        ResourceBundle rb = null;
        ContactsViewController instance = new ContactsViewController();
        instance.initialize(url, rb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
