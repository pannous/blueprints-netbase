package com.tinkerpop.blueprints.impls.netbase;

import com.sun.jna.Pointer;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.StringFactory;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.collections.set.PredicatedSet;

import java.util.*;

import static com.tinkerpop.blueprints.impls.netbase.Netbase.*;

/**
 * @author Pannous (http://Pannous.com)
 */
public class Node implements Vertex {


    private static final NodeStruct ANY;
    public NetbaseGraph graph;
    int id;
    private Pointer pointer;
    String name = null;//cache?
    private int kind;

    static {
        ANY = getNode(Relation.ANY);
    }

    public Node(final NetbaseGraph graph) {
        this.graph = graph;
    }

    public Node(int id) {
        this.id = id;
    }


    public Node(NetbaseGraph netbaseGraph, Object id) {
        graph = netbaseGraph;
        if (id != null && id instanceof Integer) this.id = (int) id;
        else {
            this.id = nextId();
            if (id != null)// wtf ???
                setLabel(id.toString());
        }
    }

    public Node(String hi) {
//        id = Netbase.addNode(hi);
        id = nextId();
        setLabel(hi);
    }

    public Node(NodeStruct struct) {
        id = struct.id;
        name = struct.name;
        kind = struct.kind;
//        struct.firstStatement
//                struct.statementCount
    }


    private void setLabel(String s) {
        if (pointer == null) pointer = Netbase.get(id);
        Netbase.setLabel(pointer, s);// zickzack!!
    }

    public Node(Edge edge) {// reified statement!
        Object edgeId = edge.getId();
        id = (int) edgeId;
        pointer = Netbase.get(id);
//        statement = new Statement(Netbase.getStatement(id));
//        Netbase.setLabel(id,edge.getLabel());
        Set<String> propertyKeys = edge.getPropertyKeys();
        for (String propertyKey : propertyKeys) {
            addEdge(propertyKey, getThe("" + edge.getProperty(propertyKey)));// todo: doeuble value etc
        }
    }


    public Node(Pointer pointer) {
        id = pointer.getInt(0);
        this.pointer = pointer;
    }


    public Iterable<Edge> getEdges(final com.tinkerpop.blueprints.Direction direction, final String... labels) {
            return new StatementIterable(graph(), this, direction, labels);
    }

    protected NodeStruct getStruct() {
        NodeStruct nodeStruct = Netbase.getNode(id);
        return nodeStruct;
    }

    public Iterable<Vertex> getVertices(final com.tinkerpop.blueprints.Direction direction, final String... labels) {
            return new NodeIterable(graph(), this, direction, labels);
    }

    private NetbaseGraph graph() {
        if (graph == null) return NetbaseGraph.me();
        return graph;
    }

    public Edge addEdge(final String label, final Vertex vertex) {
        return graph().addEdge(null, this, vertex, label);
    }

    public VertexQuery query() {
        return new DefaultVertexQuery(this);
    }

    public boolean equals(final Object object) {
        if (object == this) return true;
        if (object instanceof NodeStruct) return ((NodeStruct) object).id == id;
        if (object instanceof Node) return ((Node) object).id == id;
        return false;
//        return object instanceof Node && ((Node) object).getId().equals(this.getId());
    }

    public String toString() {
        return StringFactory.vertexString(this);
    }

    @Override
    public <T> T getProperty(String key) {
        Node object = findStatement(this.getStruct(), getAbstract(key), ANY, 0, false, false, false, true).getObject();
//        if(object instanceof T) return (T) object;
        try {
            return (T) (Integer) Integer.parseInt(object.getName());
        } catch (Exception e) {
            return (T) object.getName();
        }
    }

    @Override
    public Set<String> getPropertyKeys() {
        Set<String> list = new ListOrderedSet();
        for (Statement statement : getStatements()) {
            list.add(Netbase.getName(statement.predicate));
        }
        return list;
    }

    @Override
    public void setProperty(String key, Object value) {
        if(key==null|| key.equals("")) throw new IllegalArgumentException("EMPTY ID not allowed as property");
        if(key.equals("id")) throw new IllegalArgumentException("id key Not allowed as property");
        addStatement4(0, this.id, getAbstract(key).id, getThe("" + value).id, false);
    }

    @Override
    public <T> T removeProperty(String key) {
        StatementStruct statement;
        T r = null;
        while (true) {
            statement = findStatement(this.getStruct(), getAbstract(key), ANY, 0, false, false, false, true);
            if (statement == null) return r;// 'break'
            Node object = statement.getObject();
            r = getValue(object);
            int id1 = statement.getId();
            Debugger.info(id1);
            Netbase.deleteStatement(id1);
            Netbase.showNode(statement.subject);
        }
    }

    private <T> T getValue(Node object) {
        try {
            return (T) (Integer) Integer.parseInt(object.getName());
        } catch (Exception e) {
            return (T) object.getName();
        }
    }

    @Override
    public void remove() {
        Netbase.deleteNode(id);
    }

    @Override
    public Object getId() {
        return id;
    }

    public Iterable<Statement> getStatements() {
        return new StatementIterable(graph(), this, Direction.BOTH, null);
    }

    public String fetchName() {
//        if(pointer==null) pointer = Netbase.getPointer(id);
//        return name;
        return Netbase.getName(id);// cache?
    }

    public String getName() {
        if (name == null) name = fetchName();
//        if(pointer==null) pointer = Netbase.getPointer(id);
        return name;
//        return Netbase.getName(id);// cache?
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int statementCount() {
        return getNode(id).statementCount;
    }

    public boolean hasMember(Node fog) {
//        NodeStruct has = Netbase.has(this.getStruct(), fog.getStruct());
        StatementStruct has = findStatement(this.getStruct(), getNode(Relation.ANY), fog.getStruct(), 0, false, false, false, false);
        showNode(this.id);
        return has != null;
    }

    private void show() {
        Netbase.showNode(id);
    }

    public void delete() {
        Netbase.deleteNode(id);
    }

//    private void getStruct() {
//    }
}
