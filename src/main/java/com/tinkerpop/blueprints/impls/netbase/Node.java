package com.tinkerpop.blueprints.impls.netbase;

import com.sun.jna.Pointer;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.StringFactory;

import java.util.Arrays;
import java.util.Set;

import static com.tinkerpop.blueprints.impls.netbase.Netbase.*;

/**
 * @author Pannous (http://Pannous.com)
 */
public class Node implements Vertex {



    public NetbaseGraph graph;
    int id;
    private Pointer pointer;
    String name=null;//cache?
    private int kind;

    public Node(final NetbaseGraph graph) {
        this.graph = graph;
    }

    public Node(int id) {
        this.id = id;
    }


    public Node(NetbaseGraph netbaseGraph, Object id) {
        graph=netbaseGraph;
        try {
            if(id instanceof Number) this.id = (int) id;
            else this.id = Integer.parseInt("" + id);
        } catch (NumberFormatException e) {
            this.id=nextId();
            if(id!=null)// wtf ???
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
        pointer=Netbase.get(id);
//        statement = new Statement(Netbase.getStatement(id));
//        Netbase.setLabel(id,edge.getLabel());
        Set<String> propertyKeys = edge.getPropertyKeys();
        for (String propertyKey : propertyKeys) {
            addEdge(propertyKey, Node.get(edge.getProperty(propertyKey)));
        }
    }

    private static Node get(Object property) {
        return null;
    }


    public Node(Pointer pointer) {
        id = pointer.getInt(0);
        this.pointer = pointer;
    }



    public Iterable<Edge> getEdges(final com.tinkerpop.blueprints.Direction direction, final String... labels) {

        if (direction.equals(com.tinkerpop.blueprints.Direction.OUT))
            return new StatementIterable(graph(), getStruct(), Direction.OUTGOING, labels);
        else if (direction.equals(com.tinkerpop.blueprints.Direction.IN))
            return new StatementIterable(graph(), getStruct(), Direction.INCOMING, labels);
        return new StatementIterable(graph(), getStruct(), Direction.BOTH, labels);
//        else
//            return new MultiIterable(Arrays.asList(
//                    new StatementIterable(graph(),getStruct( this), Direction.OUTGOING, labels),
//                    new StatementIterable(graph(), getStruct(this), Direction.INCOMING, labels)));
    }

    private NodeStruct getStruct() {
        NodeStruct nodeStruct = Netbase.getNode(id);
        return nodeStruct;
    }

    public Iterable<Vertex> getVertices(final com.tinkerpop.blueprints.Direction direction, final String... labels) {
        if (direction.equals(com.tinkerpop.blueprints.Direction.OUT))
            return new NodeIterable(graph(), this, Direction.OUTGOING, labels);
        else if (direction.equals(com.tinkerpop.blueprints.Direction.IN))
            return new NodeIterable(graph(), this, Direction.INCOMING, labels);
        else
            return new MultiIterable(Arrays.asList(new NodeIterable(graph(), this, Direction.OUTGOING, labels), new NodeIterable(graph(), this, Direction.INCOMING, labels)));
    }

    private NetbaseGraph graph() {
        if(graph==null) return NetbaseGraph.me();
        return graph;
    }

    public Edge addEdge(final String label, final Vertex vertex) {
        return graph().addEdge(null, this, vertex, label);
    }

    public VertexQuery query() {
        return new DefaultVertexQuery(this);
    }

    public boolean equals(final Object object) {
        if(object==this) return true;
        if(object instanceof NodeStruct) return ((NodeStruct) object).id == id;
        if(object instanceof Node) return ((Node)object).id == id;
        return false;
//        return object instanceof Node && ((Node) object).getId().equals(this.getId());
    }

    public String toString() {
        return StringFactory.vertexString(this);
    }

    public Node getRawVertex() {
        return this;
    }

    public Set<String> getRelations() {
        return null;
    }

    @Override
    public <T> T getProperty(String key) {
        return null;
    }

    @Override
    public Set<String> getPropertyKeys() {
        return null;
    }

    @Override
    public void setProperty(String key, Object value) {

    }

    @Override
    public <T> T removeProperty(String key) {
        return null;
    }

    @Override
    public void remove() {

    }

    @Override
    public Object getId() {
        return id;
    }

    public Iterable<? extends Statement> getStatements(Direction outgoing) {
        return null;
    }

    public String fetchName() {
//        if(pointer==null) pointer = Netbase.getPointer(id);
//        return name;
        return Netbase.getName(id);// cache?
    }

    public String getName() {
        if(name==null) name=fetchName();
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
        StatementStruct has = findStatement(this.getStruct(), getNode(Relation.ANY), fog.getStruct(), 0, false,false,false,false);
        showNode(this.id);
        return has != null;
    }

    private void show(int id) {
    }

//    private void getStruct() {
//    }
}
