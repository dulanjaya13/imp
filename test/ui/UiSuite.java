/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

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
@Suite.SuiteClasses({ui.contacts.ContactsSuite.class, ui.conversation.ConversationSuite.class, ui.login.LoginSuite.class, ui.main.MainSuite.class, ui.call.CallSuite.class, ui.enhancement.EnhancementSuite.class, ui.about.AboutSuite.class, ui.account.AccountSuite.class, ui.register.RegisterSuite.class})
public class UiSuite {

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
