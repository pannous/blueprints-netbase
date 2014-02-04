package com.tinkerpop.blueprints.impls.netbase;

import com.pannous.netbase.blueprints.*;
import com.tinkerpop.blueprints.*;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.pannous.netbase.blueprints.Netbase.*;


/**
 * @author Pannous (http://Pannous.com)
 */
public class NetbaseCoreTest extends BaseTest {
    private static final Logger logger = Logger.getLogger(NetbaseCoreTest.class.getName());

    public NetbaseCoreTest() {
        super();
        logger.setLevel(Level.ALL);
//        Debugger.logger.setLevel(Level.ALL);
    }


    public void testNetbaseProperty(){;
        assertEquals(Relation.object,Relation.Object.id);
        assertEquals(get(Relation.object), Relation.Object);
        Node ar = getNew("arg1");
        int kind = ar.kind;
        assertEquals(Relation.object, kind);
        ar.setProperty("qa", 3);
        ar.setProperty("tab", "3");
        int q = ar.getProperty("qa");
        assertEquals(q,3);
        String t=ar.getProperty("tab").toString();
        assertEquals(t,"3");
    }

    public void testNetbaseArray() throws Exception {
        String[] arr = new String[]{"A", "B"};
        Node ar = getNew("a");
        ar.setProperty("t", arr);
        String[] t = ar.getProperty("t");
        logger.info(Arrays.toString(t));
        assertTrue("Arrays.equals(arr, t)", Arrays.equals(arr, t));
    }

    public void testNetbaseIntArray() throws Exception {
//        int[] arr = new int[]{1,2,3};
        Integer[] arr = new Integer[]{1,2,3};
        Node ar = getNew("a");
        ar.setProperty("t", arr);
        showNode(ar.id);
//        int[] t = ar.getProperty("t");
        Integer[] t = ar.getProperty("t");
        assertTrue("Arrays.equals(arr, t)",Arrays.equals(arr, t));
    }

    public void testNetbaseArrayList() throws Exception {
        logger.info("testNetbaseArrayList");
        ArrayList arrayList = new ArrayList();
        arrayList.add("A");
        arrayList.add("B");
        Node ar = getNew("a");// new Node("ar");
        ar.setProperty("q", 2);
        ar.setProperty("t", arrayList);
        showNode(ar.id);
//        ArrayList t = Arrays.asList((Object[])ar.getProperty("t"));
//        ArrayList t = new ArrayList(Arrays.asList((Object[]) ar.getProperty("t")));
        ArrayList t = ar.getProperty("t");
//        assertEquals(new HashSet(arrayList),new HashSet(t));
        assertEquals(arrayList, t);
    }

    private void click() {
        logger.info("[INFO] click " + (int) this.stopWatch() + " ms");
    }

    public void dont_testNetbaseEdges() {
        logger.info("testNetbaseEdges1");

        Node frau = getThe("freu");
        Node node = frau;// new Node(frau);
        frau.show();
        Iterable<Edge> edges = node.getEdges(Direction.BOTH, "*");
        Statement next = (Statement) edges.iterator().next();
//        assertEquals(next.getLabel(),"instance");
        assertEquals(next.getLabel(), "Instanz");
        assertEquals(next.getVertex(Direction.OUT), frau);
        assertEquals(next.getVertex(Direction.OUT).getId(), 61069);
        assertEquals(next.getVertex(Direction.IN).getId(), 258623);
        int count = count(edges);
//        assertEquals(count, frau.statementCount - 1);// -1 wegen next ^^
//        frau.getEdgeIterator();
    }

    public void testNetbaseEdges2() {
        logger.info("testNetbaseEdges2");

        Node frau = getThe("frau");
        Node node = frau;// new Node(frau);

        Iterable<Edge> edges = node.getEdges(Direction.BOTH, Relation.Instance.getName());
//        assertEquals(count(edges), frau.statementCount);// -1 wegen next ^^
        edges = node.getEdges(Direction.BOTH, "dsffdsa");
        assertEquals(count(edges), 0);
    }

    public void testNetbaseEdges3() {
        logger.info("testNetbaseEdges3");
//        Node frau = getThe("frau");

        try {
            System.out.println(ManagementFactory.getRuntimeMXBean().getName());
//            Process exec = Runtime.getRuntime().exec("");
//            ((java.lang.UNIXProcess)exec).
//            Thread.sleep(10000);//Wait for GDB
            System.out.println("done");
        } catch (Exception e) {
            Debugger.error(e);
        }
        Node frau = getNew("frau");
        Node node = frau;// new Node(frau);
        Node fog = getThe("fog");
        assertNotNull(fog);
        node.addEdge(Relation.Member.getName(), fog);
        assertTrue(node.hasMember(fog));
        Iterable<Edge> edges = node.getEdges(Direction.BOTH);
        int count = count(edges)-1;// -1 * Instance
//        assertEquals(count, node.statementCount());
        assertEquals(count, 1);
    }


    public void testNetbaseCorePerformance() throws Exception {
        logger.info("testNetbaseCorePerformance");
        this.stopWatch();
        Node node1 = get(61069);
        node1.addProperty("hi", 4);
        assertEquals(node1.id, 61069);
        showNode(61069);
        node1.show();
//        setLabel(node1, "freu");
        logger.info("OK");
        node1.setName("freu");
        logger.info(get(61069).toString(true));
        logger.info("OK2");
        logger.info(get(61069).getName());
        logger.info("OK2.1");
        logger.info(get(61069).toString(true));
        logger.info("OK2.2");
        logger.info(get(61069).getName());
        logger.info("OK2.3");
//        for (int i = 0; i < 10000; i++) assertEquals("freu", new Node(61069).getName());// 4000 times slower ??
//        printTestPerformance(" new Node(61069).getName()", this.stopWatch());
        for (int i = 0; i < 10000; i++) assertEquals("freu", get(61069).getName());// 400 times slower
        printTestPerformance(" get(61069).getName()", this.stopWatch());
        logger.info("OK3");

//        for (int i = 0; i < 10000; i++) assertEquals("freu", getNodeS(61069).name);// 150 times slower
//        printTestPerformance(" getNodeS(61069).name", this.stopWatch());
//        for (int i = 0; i < 10000; i++) assertEquals(null, getNodeI(61069).getString(4,false));// 100 times slower
//        printTestPerformance(" getNodeI(61069).name", this.stopWatch());
        for (int i = 0; i < 10000; i++) assertEquals("freu", Netbase.getName(61069));// 20 times slower
        printTestPerformance("Netbase.getName(61069)", this.stopWatch());
        for (int i = 0; i < 10000; i++)assertEquals("freu", node1.getName());// cached >> 20 times faster (time vs space)
        printTestPerformance(" node.getName()", this.stopWatch());
        for (int i = 0; i < 10000; i++)assertEquals("freu", node1.name);// cached >> 20 times faster (time vs space)
        printTestPerformance(" node.name", this.stopWatch());
//        setLabel(node1, "frau");
        node1.setName("frau");
    }
}
