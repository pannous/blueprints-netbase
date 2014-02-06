package com.tinkerpop.blueprints.impls.netbase;

import com.pannous.netbase.blueprints.LocalNetbaseGraph;
import com.pannous.netbase.blueprints.NetbaseGraph;
import com.pannous.netbase.blueprints.Node;
import com.pannous.netbase.blueprints.Statement;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import java.util.Iterator;

/**
 * @author Pannous (http://Pannous.com)
 */
public class NetbaseBenchmarkTestSuite extends com.tinkerpop.blueprints.TestSuite {

    private static final int TOTAL_RUNS = 10;


    public void testNetbaseRaw() throws Exception {
        LocalNetbaseGraph.testing = true;
        double totalTime = 0.0d;
        Graph graph = graphTest.generateGraph();
        GraphMLReader.inputGraph(graph, GraphMLReader.class.getResourceAsStream("graph-example-2.xml"));
        graph.shutdown();

        for (int i = 0; i < TOTAL_RUNS; i++) {
            int counter = 0;
            this.stopWatch();
//            for (final Node node : ) {
            Iterator<Vertex> iterator = graph.getVertices().iterator();
            while (iterator.hasNext()) {
                Node node = (Node) iterator.next();
                counter++;
                for (final Statement relationship : node.getStatements()) {
                    counter++;
                    final Node node2 = relationship.Object();
                    counter++;
                    for (final Statement relationship2 : node2.getStatements()) {
                        counter++;
                        final Node node3 = relationship2.Object();
                        counter++;
                        for (final Statement relationship3 : node3.getStatements()) {
                            counter++;
                            relationship3.Object();
                            counter++;
                        }
                    }
                }
            }
            double currentTime = this.stopWatch();
            totalTime = totalTime + currentTime;
//            BaseTest.printPerformance(graph.toString(), counter, "netbase raw elements touched", currentTime);
            graph.shutdown();
        }
//        BaseTest.printPerformance("NetbaseRaw", 1, "netbase Raw experiment average", totalTime / (double) TOTAL_RUNS);
    }


    public void testNetbaseGraph() throws Exception {
        double totalTime = 0.0d;
        Graph graph = graphTest.generateGraph();
        GraphMLReader.inputGraph(graph, GraphMLReader.class.getResourceAsStream("graph-example-2.xml"));
        graph.shutdown();

        for (int i = 0; i < TOTAL_RUNS; i++) {
            graph = graphTest.generateGraph();
            this.stopWatch();
            int counter = 0;
            for (final Vertex vertex : graph.getVertices()) {
                counter++;
                for (final Edge edge : vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT)) {
                    counter++;
                    final Vertex vertex2 = edge.getVertex(com.tinkerpop.blueprints.Direction.IN);
                    counter++;
                    for (final Edge edge2 : vertex2.getEdges(com.tinkerpop.blueprints.Direction.OUT)) {
                        counter++;
                        final Vertex vertex3 = edge2.getVertex(com.tinkerpop.blueprints.Direction.IN);
                        counter++;
                        for (final Edge edge3 : vertex3.getEdges(com.tinkerpop.blueprints.Direction.OUT)) {
                            counter++;
                            edge3.getVertex(com.tinkerpop.blueprints.Direction.OUT);
                            counter++;
                        }
                    }
                }
            }
            double currentTime = this.stopWatch();
            totalTime = totalTime + currentTime;
            BaseTest.printPerformance(graph.toString(), counter, "NetbaseGraph elements touched", currentTime);
            graph.shutdown();
        }
        BaseTest.printPerformance("NetbaseGraph", 1, "NetbaseGraph experiment average", totalTime / (double) TOTAL_RUNS);
    }


}
