package com.tinkerpop.blueprints.impls.netbase;


import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import java.util.Iterator;
import java.util.List;

/**
 * @author Pannous (http://Pannous.com)
 */
public class NodeIterable<T extends Vertex> implements CloseableIterable<Node> {
    private  String[] labels;
    private  NetbaseGraph graph;
    private  Node node;
    private  Direction direction;
        Iterator<T> iterator;

    public NodeIterable(NetbaseGraph graph, Node node, Direction direction, String[] labels) {
        this.graph = graph;
        this.node=node;
        this.direction=direction;
        this.labels = labels;
    }

    public NodeIterable(List<Node> vertices) {
        iterator = (Iterator<T>) vertices.iterator();
    }

    public NodeIterable(String key, Object value) {

    }

    @Override
    public void close() {

    }

    @Override
    public Iterator<Node> iterator() {
        return (Iterator<Node>) iterator;
    }
}
