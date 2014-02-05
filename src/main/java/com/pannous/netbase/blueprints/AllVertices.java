package com.pannous.netbase.blueprints;

import com.tinkerpop.blueprints.Vertex;

import java.util.Iterator;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 04/02/14
 * Time: 15:20
 */
public class AllVertices implements Iterable<Vertex>, Iterator<Vertex> {

    private int current = 1;
    private int count;

    @Override
    public Iterator<Vertex> iterator() {
        count = LocalNetbase.nodeCount();
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
            Node node = LocalNetbase.get(current);
            if (node == null) node = new Node(current);
            return node;
        }
        return null;
    }

    @Override
    public void remove() {
        LocalNetbase.deleteNode(current);
    }
}
