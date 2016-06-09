/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Dulanjaya
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({models.transferable.TransferableSuite.class, models.fileManagement.FileManagementSuite.class, models.user.UserSuite.class, models.callManagement.CallManagementSuite.class, models.chatManagement.ChatManagementSuite.class, models.connectionManagement.ConnectionManagementSuite.class, models.contactManagement.ContactManagementSuite.class})
public class ModelsSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}