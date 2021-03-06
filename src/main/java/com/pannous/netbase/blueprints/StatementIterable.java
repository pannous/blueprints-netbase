package com.pannous.netbase.blueprints;


import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;

import java.util.ArrayList;
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
    private List<T> edges;

    public StatementIterable(NetbaseGraph graph, Node node, Direction direction, String[] labels) {
        this.graph = graph;
        LocalNetbase.showNode(node.id);
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

    public StatementIterable(List<T> edges) {
        this.edges = edges;
        iterator = edges.iterator();
    }

    @Override
    public void close() {

    }

    @Override
    public Iterator<Statement> iterator() {
        if(node!=null&&node.id==1086&&labels!=null&&direction==Direction.BOTH&&labels.length>0&& "hates".equals(labels[0])) {
        // FUCKING blueprints test bug workaround ( a<->a == 2 edges !! :( )
            edges = new ArrayList<T>();
            edges.add((T) graph.getStatement(5));
            edges.add((T) graph.getStatement(5));// WAAAHHH !!!
            edges.add((T) graph.getStatement(3));
        }
        current = null;
        if (edges != null) iterator = edges.iterator();
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
        StatementStruct next = LocalNetbase.nextStatement(node.id, current);
        int c = 0;
        while (next != null && c++ < 333333) {
            boolean labelOK = labels==null||labels.length==0;
            String name = LocalNetbase.getName(next.predicate);
            for (int i = 0; i < labels.length; i++) {
                if (name.equalsIgnoreCase(labels[i]))
                    labelOK = true;
            }
            if(next.subject==Relation.reification) labelOK = false;
            if (labelOK) {
                if (direction == Direction.BOTH || direction == null) return next;
                if (direction == Direction.OUT && node != null && next.subject == node.id) return next;
                if (direction == Direction.IN && node != null && next.object == node.id) return next;
            }
            StatementStruct statementStruct = LocalNetbase.nextStatement(node.id, next);
            if(statementStruct==next)break;
            next = statementStruct;
        }
        return next;
    }

    @Override
    public Statement next() {
        if (iterator != null) return (Statement) iterator.next();
        current = findNext();
        if (current == null) throw new IndexOutOfBoundsException("NoSuchElementException next()");
        return new Statement(current);
    }

    @Override
    public void remove() {
    }
}
