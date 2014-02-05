package com.pannous.netbase.blueprints.remote;

import com.pannous.netbase.blueprints.Node;
import com.pannous.netbase.blueprints.RemoteNetbaseGraph;
import com.tinkerpop.blueprints.Vertex;

import java.util.Iterator;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 04/02/14
 * Time: 15:20
 */
public class AllRemoteVertices implements Iterable<Vertex>, Iterator<Vertex> {

    private int current = 1;
    private int count;
    private final RemoteNetbaseGraph graph;

    public AllRemoteVertices(RemoteNetbaseGraph graph) {
        this.graph = graph;
    }

    @Override
    public Iterator<Vertex> iterator() {
        try {
            count = graph.nodeCount();
        } catch (Exception e) {
            count = graph.nodeCount;
        }
        return this;
    }

    @Override
    public boolean hasNext() {
        return current < count;
    }

    @Override
    public Vertex next() {
        while (hasNext()) {
            current++;
            Node node = graph.get(current);
            if (node == null) node = new Node(current);
            return node;
        }
        return null;
    }

    @Override
    public void remove() {
        graph.deleteNode(current);
    }
}
