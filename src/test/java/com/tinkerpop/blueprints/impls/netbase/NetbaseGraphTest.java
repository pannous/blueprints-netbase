package com.tinkerpop.blueprints.impls.netbase;

import com.pannous.netbase.blueprints.LocalNetbase;
import com.pannous.netbase.blueprints.LocalNetbaseGraph;
import com.pannous.netbase.blueprints.NetbaseGraph;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.GraphTest;
import com.tinkerpop.blueprints.util.io.gml.GMLReaderTestSuite;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReaderTestSuite;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * @author Pannous (http://Pannous.com)
 */
public class NetbaseGraphTest extends GraphTest {

    private static final Logger logger = Logger.getLogger(NetbaseGraphTest.class.getName());

    /*public void testNetbaseBenchmarkTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new NetbaseBenchmarkTestSuite(this));
        printTestPerformance("NetbaseBenchmarkTestSuite", this.stopWatch());
    }*/

    public void testVertexTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new VertexTestSuite(this));
        printTestPerformance("VertexTestSuite", this.stopWatch());
    }

    public void testEdgeTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new EdgeTestSuite(this));
        printTestPerformance("EdgeTestSuite", this.stopWatch());
    }

    public void testGraphTestSuite() throws Exception {
        this.stopWatch();
        cleanup();
        doTestSuite(new GraphTestSuite(this));
        printTestPerformance("GraphTestSuite", this.stopWatch());
    }

    public void testVertexQueryTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new VertexQueryTestSuite(this));
        printTestPerformance("VertexQueryTestSuite", this.stopWatch());
    }

    public void testGraphQueryTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphQueryTestSuite(this));
        printTestPerformance("GraphQueryTestSuite", this.stopWatch());
    }

    public void testGraphSONReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphSONReaderTestSuite(this));
        printTestPerformance("GraphSONReaderTestSuite", this.stopWatch());
    }

    public void testGMLReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GMLReaderTestSuite(this));
        printTestPerformance("GMLReaderTestSuite", this.stopWatch());
    }

    public void testNetbaseGraphSpecificTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new NetbaseGraphSpecificTestSuite(this));
        printTestPerformance("NetbaseGraphSpecificTestSuite", this.stopWatch());
    }

//    public void testGraphMLReaderTestSuite() throws Exception {
//        this.stopWatch();
//        doTestSuite(new GraphMLReaderTestSuite(this));
//        printTestPerformance("GraphMLReaderTestSuite", this.stopWatch());
//    }

    public Graph generateGraph() {
        return generateGraph("default");// not good for tests!
    }

    public Graph generateGraph(final String name) {
        Graph graph = LocalNetbaseGraph.me();
        return graph;
    }

    public void doTestSuite(final TestSuite testSuite) throws Exception {
        String directory = this.getWorkingDirectory();
        deleteDirectory(new File(directory));
        for (Method method : testSuite.getClass().getDeclaredMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("test")) {
                System.out.println("Testing " + methodName + "...");
                cleanup();
                if (methodName.equals("testLegalVertexEdgeIterables")
                        || methodName.equals("testGettingEdgesAndVertices")
                        || methodName.equals("testVertexCentricLinking"))
                    logger.info("DEBUG " + methodName);
                method.invoke(testSuite);
                deleteDirectory(new File(directory));
            }
        }
    }

    private void cleanup() {
        LocalNetbase.getAbstract("location").delete();// danger!
        LocalNetbase.getAbstract("name").delete();// danger!
        LocalNetbase.getAbstract("marko").delete();// danger!
        LocalNetbase.getAbstract("a").delete();
        LocalNetbase.getAbstract("b").delete();
        LocalNetbase.getAbstract("c").delete();
        for (int i = 0; i < 1000; i++) LocalNetbase.getAbstract("" + i).delete();
        for (Object o : LocalNetbaseGraph.me().getVertices()) {
            LocalNetbase.getAbstract("" + o).delete();
            ((Vertex) o).remove();
        }
//        Netbase.getAbstract("a").delete();
    }

    private String getWorkingDirectory() {
        return this.computeTestDataRoot().getAbsolutePath();
    }


//    public void testKeyIndexableGraphTestSuite() throws Exception {
//        this.stopWatch();
//        doTestSuite(new KeyIndexableGraphTestSuite(this));
//        printTestPerformance("KeyIndexableGraphTestSuite", this.stopWatch());
//    }
//
//    public void testIndexableGraphTestSuite() throws Exception {
//        this.stopWatch();
//        doTestSuite(new IndexableGraphTestSuite(this));
//        printTestPerformance("IndexableGraphTestSuite", this.stopWatch());
//    }
//
//    public void testIndexTestSuite() throws Exception {
//        this.stopWatch();
//        doTestSuite(new IndexTestSuite(this));
//        printTestPerformance("IndexTestSuite", this.stopWatch());
//    }
//
//    public void testTransactionalGraphTestSuite() throws Exception {
//        this.stopWatch();
//        doTestSuite(new TransactionalGraphTestSuite(this));
//        printTestPerformance("TransactionalGraphTestSuite", this.stopWatch());
//    }

}
