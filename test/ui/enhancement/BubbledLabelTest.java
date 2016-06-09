/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.enhancement;

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
public class BubbledLabelTest {
    
    public BubbledLabelTest() {
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
     * Test of updateBounds method, of class BubbledLabel.
     */
    @Test
    public void testUpdateBounds() {
        System.out.println("updateBounds");
        BubbledLabel instance = new BubbledLabel();
        instance.updateBounds();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPading method, of class BubbledLabel.
     */
    @Test
    public void testGetPading() {
        System.out.println("getPading");
        BubbledLabel instance = new BubbledLabel();
        double expResult = 0.0;
        double result = instance.getPading();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPading method, of class BubbledLabel.
     */
    @Test
    public void testSetPading() {
        System.out.println("setPading");
        double pading = 0.0;
        BubbledLabel instance = new BubbledLabel();
        instance.setPading(pading);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBubbleSpec method, of class BubbledLabel.
     */
    @Test
    public void testGetBubbleSpec() {
        System.out.println("getBubbleSpec");
        BubbledLabel instance = new BubbledLabel();
        BubbleSpec expResult = null;
        BubbleSpec result = instance.getBubbleSpec();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBubbleSpec method, of class BubbledLabel.
     */
    @Test
    public void testSetBubbleSpec() {
        System.out.println("setBubbleSpec");
        BubbleSpec bubbleSpec = null;
        BubbledLabel instance = new BubbledLabel();
        instance.setBubbleSpec(bubbleSpec);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
