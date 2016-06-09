/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.fileManagement;

import java.io.File;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
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
public class FileManagerTest {
    
    public FileManagerTest() {
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
     * Test of sendFile method, of class FileManager.
     */
    @Test
    public void testSendFile() throws Exception {
        System.out.println("sendFile");
        File file = null;
        String sender = "";
        FileManager instance = new FileManager();
        instance.sendFile(file, sender);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFTM method, of class FileManager.
     */
    @Test
    public void testGetFTM() {
        System.out.println("getFTM");
        FileManager instance = new FileManager();
        FileTransferManager expResult = null;
        FileTransferManager result = instance.getFTM();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
