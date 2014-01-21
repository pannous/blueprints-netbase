package com.tinkerpop.blueprints.impls.netbase;

import com.tinkerpop.blueprints.TestSuite;
import com.tinkerpop.blueprints.impls.GraphTest;

/**
 * @author Pannous (http://Pannous.com)
 */
public class NetbaseGraphSpecificTestSuite extends TestSuite {

    public NetbaseGraphSpecificTestSuite() {
    }

    public NetbaseGraphSpecificTestSuite(final GraphTest graphTest) {
        super(graphTest);
    }

    public void testLongIdConversions() {
        String id1 = "100";  // good  100
        String id2 = "100.0"; // good 100
        String id3 = "100.1"; // good 100
        String id4 = "one"; // bad

        try {
            Double.valueOf(id1).longValue();
            assertTrue(true);
        } catch (NumberFormatException e) {
            assertFalse(true);
        }
        try {
            Double.valueOf(id2).longValue();
            assertTrue(true);
        } catch (NumberFormatException e) {
            assertFalse(true);
        }
        try {
            Double.valueOf(id3).longValue();
            assertTrue(true);
        } catch (NumberFormatException e) {
            assertFalse(true);
        }
        try {
            Double.valueOf(id4).longValue();
            assertTrue(false);
        } catch (NumberFormatException e) {
            assertFalse(false);
        }
    }

}
