package com.pannous.netbase.blueprints.remote;

import com.pannous.netbase.blueprints.*;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 05/02/14
 * Time: 09:47
 */
public class RemoteNode extends Node {//implements Vertex {//
    List<RemoteStatement> statements = new ArrayList<RemoteStatement>();
    public boolean loaded = false;
//    private final int id;
//    private final String name;
//    private final RemoteNetbaseGraph graph;

    public RemoteNode(int id, String name, RemoteNetbaseGraph adapter) {
        super(-1);
        super.setAutoRead(false);
        super.setAutoWrite(false);
        super.setAutoSynch(false);
        this.id = id;
        this.name = name;
        this.graph = adapter;
    }

    @Override
    public String getName() {
        if (name == null) name = graph.getName(id);
        return name;
    }

    @Override
    public Edge addEdge(String label, Vertex vertex) {
        try {
//            if (id > 0 && id < graph.nodeCount())
//                return "" + id;
//            else
//                return name;// the? new?
//            vertex.save()
            String s = ""+vertex.getId();// toString();
            Node[] nodes = graph.query("learn " + this.id + " " + label + " " + s);
            return nodes[0].getStatements().iterator().next();// FIRST / LAST ??
        } catch (Exception e) {
            Debugger.error(e);
            return null;
        }
    }

    @Override
    public void addProperty(String key, Object value) {
        try {
            Node[] nodes = graph.query("learn " + this.id + " " + key + " " + value);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    @Override
    public void delete() {
        try {
            graph.query("delete " + id);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        return new StatementIterable(statements);
    }

    @Override
    protected List getFieldOrder() {
        return new ArrayList();// don't sync
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public <T> T getProperty(String key) {
//        StatementStruct statement = findStatement(id, keyId, Relation.ANY, 0, false, false, false, true);
        if (statements != null && statements.size() > 0) {
            for (Edge statement : statements) {
                if (statement.getLabel().equalsIgnoreCase(key)) {
                    RemoteNode vertex = (RemoteNode) statement.getVertex(Direction.IN);
                    if (vertex.kind == Relation.list) return getPropertyList(key);
                    if (vertex.kind == Relation.array) return getPropertyArray(key, new ArrayList<T>());
                    return (T) vertex;
                }
            }
        }
        try {
            return (T) graph.query(id + "." + key)[0];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T, U> T getPropertyArray(String key, ArrayList<U> list) {
        if (statements != null && statements.size() > 0) {
            for (Edge statement : statements) {
                if (statement.getLabel().equalsIgnoreCase(key)) list.add((U) statement.getVertex(Direction.OUT));
            }
        }
        try {
            return (T) graph.query(id + "." + key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<String> getPropertyKeys() {
        Set<String> all = new HashSet<String>();
        if (statements != null && statements.size() > 0)
            for (Edge statement : statements) all.add(statement.getLabel());
        return all;
    }

    @Override
    public void setProperty(String s, Object o) {
        addProperty(s,o);
    }

    @Override
    public <T> T getPropertyList(String key) {
        return getPropertyArray(key, new ArrayList<Object>());
    }

    @Override
    public Iterable<Statement> getStatements() {
        return new StatementIterable(statements);
    }
//
//    @Override
//    int getValueNode(Object value) {
//    return graph.getNew(""+value).id;// todo!!
////        throw new NotImplementedException("remote getValueNode");
//    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        return super.getVertices(direction, labels);
    }

    @Override
    public VertexQuery query() {
        return new DefaultVertexQuery(this);
    }


    @Override
    public boolean hasMember(Node fog) {
        try {
            return graph.query(id + "." + fog.id).length > 0;
        } catch (Exception e) {
            Debugger.error(e);
            return false;
        }
    }

    @Override
    public Node load() {
        try {
            return graph.query("" + id)[0];
        } catch (Exception e) {
            Debugger.error(e);
        }
        loaded = false;
        return this;// Better than nothing
    }

//    @Override
//    public VertexQuery query() {
//        return super.query();
//    }

    @Override
    public void remove() {
        delete();
    }

    @Override
    public <T> T removeProperty(String key) {
        if (statements != null && statements.size() > 0) {
            for (Edge statement : statements) {
                if (statement.getLabel().equalsIgnoreCase(key)) {
                    T vertex = (T) statement.getVertex(Direction.OUT);
                    statement.remove();
                    return vertex;
                }
            }
        }
        try {
//            return (T) graph.query(delete id + "." + key)[0];
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Node setName(String label) {
        name = label;
        try {
//            if(!name.equals(label))
//            graph.query("label " + id + " " + label);
        } catch (Exception e) {
            Debugger.error(e);
        }
        return this;
    }

//    @Override
//    public void setProperty(String key, Object value) {
//        try {
//            if (value instanceof Array) {
//                Object[] array = (Object[]) value;
//                for (int i = 0; i < array.length; i++) {
//                    graph.query(id + "." + key + "=" + array[i]);
//                }
//            } else
//                graph.query(id + "." + key + "=" + value);
//        } catch (Exception e) {
//            Debugger.error(e);
//        }
//    }

    @Override
    public void show() {
        graph.showNode(id);
    }

    @Override
    public String toString() {
        return "(" + id + ") " + name;
    }
}
