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
public class StatementIterable<T extends Edge> implements CloseableIterable<Statement>, Iterator<Statement>, Iterable<Statement> {
    private Direction direction;
    private Node node;
    private NetbaseGraph graph;
    private String[] labels=new String[]{};
    private Iterator<T> iterator;
    private StatementStruct current;
    private NodeStruct nodeS;
    private List<T> vertices;

    public StatementIterable(NetbaseGraph graph, Node node, Direction direction, String[] labels) {
        this.graph = graph;
        this.nodeS = node.getStruct();
        Netbase.showNode(node.id);
        this.node=node;
        this.direction = direction;

        if(labels!=null)
            this.labels = labels;
//        if(labels.length>0){
//              iterator() = =
//            new Node(node).getEdges()
//        }
    }

    public StatementIterable(String key, Object value) {

    }

    public StatementIterable(List<T> vertices) {
        this.vertices = vertices;
        iterator = vertices.iterator();
    }

    @Override
    public void close() {

    }

    @Override
    public Iterator<Statement> iterator() {
        current = null;
        if (vertices != null) iterator = vertices.iterator();
        if (iterator != null) return (Iterator<Statement>) iterator;
        return this;
    }

    @Override
    public boolean hasNext() {
        if (iterator != null) return iterator.hasNext();
        StatementStruct next = findNext();
        return next != null;
    }

    private StatementStruct findNext() {
        StatementStruct next = Netbase.nextStatement(nodeS, current);
        int c = 0;
        while (next != null && c++ < 333333) {
            boolean labelOK = labels==null||labels.length==0;
            for (int i = 0; i < labels.length; i++)
                if (Netbase.getName(next.predicate).equals(labels[i]))
                    labelOK = true;
            if (labelOK) {
                if (direction == Direction.BOTH || direction == null) return next;
                if (direction == Direction.OUT && node != null && next.subject == node.id) return next;
                if (direction == Direction.IN && node != null && next.object == node.id) return next;
            }
            next = Netbase.nextStatement(nodeS, next);
        }
        return next;
    }

    @Override
    public Statement next() {
        if (iterator != null) return (Statement) iterator.next();
        current = findNext();
        return new Statement(current);
    }

    @Override
    public void remove() {
    }
}
