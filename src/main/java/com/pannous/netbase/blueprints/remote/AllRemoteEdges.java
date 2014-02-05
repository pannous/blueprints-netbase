package com.pannous.netbase.blueprints.remote;

import com.pannous.netbase.blueprints.*;
import com.tinkerpop.blueprints.Edge;

import java.util.Iterator;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 04/02/14
 * Time: 15:20
 */
public class AllRemoteEdges implements Iterable<Edge>, Iterator<Edge> {

    private int current=1;
    private int count;
    private final RemoteNetbaseGraph graph;

    public AllRemoteEdges(RemoteNetbaseGraph graph) {
       this.graph = graph;
   }

    @Override
    public Iterator<Edge> iterator() {
        try {
            count = graph.statementCount();
        } catch (Exception e) {
            count=graph.statementCount;
        }
        return this;
    }

    @Override
    public boolean hasNext() {
        return current<count;
    }

    @Override
    public Edge next() {
        while (hasNext()){
        current++;
        StatementStruct statement = LocalNetbase.getStatement(current);
        if(statement!=null) return new Statement(statement);
        }
        return null;
    }

    @Override
    public void remove() {
        LocalNetbase.deleteStatement(current);
    }
}
