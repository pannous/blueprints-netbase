package com.tinkerpop.blueprints.impls.netbase;

import com.sun.jna.Pointer;
import com.tinkerpop.blueprints.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tinkerpop.blueprints.impls.netbase.Netbase.*;

/**
 * @author Pannous (http://Pannous.com)
 */
public class NetbaseCoreTest extends BaseTest {
    private static final Logger logger = Logger.getLogger(NetbaseCoreTest.class.getName());

//    @Test
    public void testNetbaseCore() throws Exception {
//        logger.setUseParentHandlers(false);// Results in silence !?!
        logger.setLevel(Level.ALL);

        this.stopWatch();
        assertEquals(12345, Netbase.test2());
        logger.info("LOADING NETBASE " + (int) this.stopWatch() + " ms");

        Pointer result = Netbase.execute("frau");// < 2 sec ++
        Pointer firstNode;
        firstNode = result.getPointer(0);// Pointer[] -> Pointer[0] 'hack' but getPointer(1) NOT OK !
//        new NodeStruct(firstNode);
//        int nodeId = firstNode.getInt(0);
////        assertEquals(nodeId, 61069);
//        assertEquals(nodeId, 258623);

        Pointer[] pointerArray = result.getPointerArray(0);
//        int[] intArray = result.getIntArray(0, 100);
//        assertEquals(intArray[0],258623);
        Debugger.info(pointerArray.length);
        click();

//        firstNode = pointerArray[1];
//        nodeId = firstNode.getInt(0);
////        assertEquals(nodeId, 61069);
//        assertEquals(nodeId, 234256);
//        Pointer p= getAbstract("frau");
        assertEquals("frau", Netbase.getName(61069));
        Object id = new Node("hi").getId();

        assertEquals((int) id, Netbase.nodeCount() - 1);
        assertEquals(Netbase.getName((int) id), "hi");

//        assertEquals(getAbstract("frau").getInt(0),61069);
//        assertEquals(getAbstract("frau"),61069);
//        NodeStruct frau = getAbstract("frau");
//        assertEquals(frau.id,61069);
//        NodeStruct
//        }
        printTestPerformance("NetbaseCore ms:", this.stopWatch());
    }

    private void click() {
        logger.info("[INFO] click " + (int) this.stopWatch() + " ms");
    }

    public void testNetbaseEdges() {
        NodeStruct frau = getAbstract("frau");
        Node node = new Node(frau);
        Iterable<Edge> edges = node.getEdges(Direction.BOTH, "*");
        Statement next = (Statement) edges.iterator().next();
//        assertEquals(next.getLabel(),"instance");
        assertEquals(next.getLabel(),"Instanz");
        assertEquals(next.getVertex(Direction.OUT),frau);
        assertEquals(next.getVertex(Direction.OUT).getId(),61069);
        assertEquals(next.getVertex(Direction.IN).getId(),258623);
        int count = count(edges);
        assertEquals(count,frau.statementCount-1);// -1 wegen next ^^
//        frau.getEdgeIterator();
    }
    public void testNetbaseEdges2() {
        NodeStruct frau = getAbstract("frau");
        Node node = new Node(frau);
        Iterable<Edge> edges = node.getEdges(Direction.BOTH, Relation.instance);
        assertEquals(count(edges), frau.statementCount);// -1 wegen next ^^
        edges = node.getEdges(Direction.BOTH, "dsffdsa");
        assertEquals(count(edges), 0);
    }

    public void testNetbaseEdges3() {
        NodeStruct frau = getAbstract("test123");
        Node node = new Node(frau);
        Node fog = getThe("fog");
        assertNotNull(fog);
        node.addEdge(Relation.member, fog);
        assertTrue(node.hasMember(fog));
        Iterable<Edge> edges = node.getEdges(Direction.BOTH);
        int count = count(edges);
        assertEquals(count, node.statementCount());
        assertEquals(count, 1);
    }


    public void testNetbaseCorePerformance() throws Exception {
        this.stopWatch();
        setLabel(get(61069), "frau");
        for (int i = 0; i < 10000; i++) assertEquals("frau", new Node(61069).fetchName());// 100 times slower
        printTestPerformance(" new Node(61069).getName()", this.stopWatch());
        for (int i = 0; i < 10000; i++) assertEquals("frau", Netbase.getName(61069));// 20 times slower
        printTestPerformance("Netbase.getName(61069)", this.stopWatch());
        Node node = new Node(61069);
        for (int i = 0; i < 10000; i++)
            assertEquals("frau", node.getName());// cached >> 20 times faster (time vs space)
        printTestPerformance(" node.getName()", this.stopWatch());

    }
}
