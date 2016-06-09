/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.handler;

import java.sql.Connection;
import java.sql.ResultSet;
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
public class DBHandlerTest {
    
    public DBHandlerTest() {
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
     * Test of setData method, of class DBHandler.
     */
    @Test
    public void testSetData_3args() throws Exception {
        System.out.println("setData");
        Connection connection = null;
        String query = "";
        Object[] ob = null;
        int expResult = 0;
        int result = DBHandler.setData(connection, query, ob);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class DBHandler.
     */
    @Test
    public void testSetData_Connection_String() throws Exception {
        System.out.println("setData");
        Connection connection = null;
        String query = "";
        int expResult = 0;
        int result = DBHandler.setData(connection, query);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getData method, of class DBHandler.
     */
    @Test
    public void testGetData_3args() throws Exception {
        System.out.println("getData");
        Connection connection = null;
        String query = "";
        Object[] ob = null;
        ResultSet expResult = null;
        ResultSet result = DBHandler.getData(connection, query, ob);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getData method, of class DBHandler.
     */
    @Test
    public void testGetData_Connection_String() throws Exception {
        System.out.println("getData");
        Connection connection = null;
        String query = "";
        ResultSet expResult = null;
        ResultSet result = DBHandler.getData(connection, query);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
