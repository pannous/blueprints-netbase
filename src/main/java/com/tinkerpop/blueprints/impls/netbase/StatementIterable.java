package com.tinkerpop.blueprints.impls.netbase;


import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.Iterator;
import java.util.List;

/**
 * @author Pannous (http://Pannous.com)
 */
public class StatementIterable<T extends Edge> implements CloseableIterable<Statement>, Iterator<Statement>,Iterable<Statement> {
    private Direction direction;
    private Node node;
    private NetbaseGraph graph;
    private String[] labels;
    private Iterator<T> iterator;
    private StatementStruct current;
    private NodeStruct nodeS;

    public StatementIterable(NetbaseGraph graph, NodeStruct node, Direction direction, String[] labels) {
        this.graph = graph;
        this.nodeS=node;
//        this.node=node;
        this.direction=direction;
        this.labels = labels;
//        if(labels.length>0){
//              iterator() = =
//            new Node(node).getEdges()
//        }
    }

    public StatementIterable(String key, Object value) {

    }

    public StatementIterable(List<T> vertices) {
        iterator = vertices.iterator();
    }

    @Override
    public void close() {

    }

    @Override
    public Iterator<Statement> iterator() {
        if(iterator!=null)return (Iterator<Statement>) iterator;
        return this;
    }

    @Override
    public boolean hasNext() {
        if(iterator!=null) return iterator.hasNext();
        StatementStruct next = findNext();
        return next != null;
    }

    private StatementStruct findNext() {
        StatementStruct next = Netbase.nextStatement(nodeS, current);
        if(labels.length==0)return next;
        int c=0;
        while (next!=null && c++ <333333){
        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            if(Netbase.getName(next.predicate).equals(label)) return next;
        }next = Netbase.nextStatement(nodeS, next);
        }
        return next;
    }

    @Override
    public Statement next() {
        if(iterator!=null) return (Statement) iterator.next();
        current = findNext();
        return new Statement(current);
    }

    @Override
    public void remove() {
    }
}
