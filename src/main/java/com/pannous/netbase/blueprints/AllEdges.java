package com.pannous.netbase.blueprints;

import com.tinkerpop.blueprints.Edge;

import java.util.Iterator;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 04/02/14
 * Time: 15:20
 */
public class AllEdges implements Iterable<Edge>, Iterator<Edge> {

    private int current=1;
    private int count;

    @Override
    public Iterator<Edge> iterator() {
        count = Netbase.statementCount();
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
        StatementStruct statement = Netbase.getStatement(current);
        if(statement!=null) return new Statement(statement);
        }
        return null;
    }

    @Override
    public void remove() {
        Netbase.deleteStatement(current);
    }
}
