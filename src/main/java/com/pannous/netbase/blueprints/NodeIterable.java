package com.pannous.netbase.blueprints;


import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import java.util.Iterator;
import java.util.List;
import static com.pannous.netbase.blueprints.Netbase.*;

/**
 * @author Pannous (http://Pannous.com)
 */
public class NodeIterable<T extends Vertex> implements CloseableIterable<Node>, Iterator<Node> {
    private  String[] labels=new String[]{};
    private  NetbaseGraph graph;
    private  Node node;
    private  Direction direction;
     Iterator<T> iterator;//
    List<Node> vertices;
    private Iterator<Statement> statements;
    private StatementStruct current;


    public NodeIterable(NetbaseGraph graph, Node node, Direction direction, String[] labels) {
        this.graph = graph;
        this.node=node;
        this.direction=direction;
        if(labels!=null)
        this.labels = labels;
        showNode(node.id);
        statements = node.getStatements().iterator();
    }

    public NodeIterable(List<Node> vertices) {
        this.vertices=vertices;
    }


    @Override
    public void close() {
    }

    @Override
    public Iterator<Node> iterator() {
        current = null;
        if(this.vertices!=null)return (Iterator<Node>) vertices.iterator();
        if (iterator != null) return (Iterator<Node>) iterator;
        return this;
    }

    @Override
    public boolean hasNext() {
        if(iterator!=null) return iterator.hasNext();
        StatementStruct next = findNext();
        return next != null;
    }

    private StatementStruct findNext() {
        StatementStruct next = Netbase.nextStatement(node.id, current);
        int c=0;
        while (next!=null && c++ <333333) {
            boolean labelOK = labels==null||labels.length==0;
            if(labels !=null)
            for (int i = 0; i < labels.length; i++) {
                String label = labels[i];
                if (Netbase.getName(next.predicate).equalsIgnoreCase(label))// expensive!
                    labelOK = true;
                if (Netbase.getName(next.object).equalsIgnoreCase(label))// expensive!
                    labelOK = true;// valueOK !?!
            }
            if (labelOK) {
                if (direction == Direction.BOTH || direction == null) return next;
                if (direction == Direction.OUT && next.subject == node.id) return next;
                if (direction == Direction.IN && next.object == node.id) return next;
            }
            next = Netbase.nextStatement(node.id, next);
        }
        return next;
    }


    @Override
    public Node next() {
        if(iterator!=null) return (Node)iterator.next();
        current = findNext();
        if(node.id==current.object) return get(current.subject);
        if(node.id==current.subject) return get(current.object);
//        if(node.id==current.predicate) return get(current.object);// b.c=x OR
        if(node.id==current.predicate) return get(current.subject);// x.b:c
        throw new RuntimeException("iterator met predicate");
    }

    @Override
    public void remove() {
    }
}
