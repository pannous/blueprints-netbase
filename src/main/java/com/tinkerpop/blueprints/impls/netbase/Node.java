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

import java.text.DateFormat;
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
        nodeStruct.id=id;// !?!
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
    public Set<String> getPropertyKeys() {
        Set<String> list = new ListOrderedSet();
        for (Statement statement : getStatements()) {
            list.add(Netbase.getName(statement.predicate));
        }
        return list;
    }

    @Override
    public void setProperty(String key, Object value) {
        if (key == null || key.equals("")) throw new IllegalArgumentException("EMPTY ID not allowed as property");
        if (value == null || value.equals(""))
            throw new IllegalArgumentException("EMPTY value not allowed as property");
        if (key.equals("id")) throw new IllegalArgumentException("id key Not allowed as property");
        if(value instanceof HashMap){
            HashMap map = (HashMap) value;
            Node hashmap = new Node("hashmap");
            addStatement4(0, this.id,getAbstract(key).id, hashmap.id, false);
            for (Object k : map.keySet())
                addStatement4(0, hashmap.id, getAbstract(""+k).id,getValueNode(map.get(k)).id, false);
// OR FORGET KEY:addStatement4(0, this.id, getAbstract(""+k).id,getValueNode(map.get(k)).id, false);
        }
        if(value.getClass().isArray()){
            Object[] objects = (Object[]) value;
            NodeStruct arrayKey = new Node(key).getStruct();// works ONLY with findStatement 1->semantic
            Netbase.setKind(arrayKey.id,Relation._array);
            for(Object o: objects) {
                addStatement4(0, this.id, arrayKey.id,getValueNode(o).id, false);
            }
        }
        else if(value instanceof Iterable) {
//            NodeStruct arrayKey = Netbase.add(key, Relation._array,0);
            NodeStruct arrayKey = new Node(key).getStruct();// works ONLY with findStatement 1->semantic
            Netbase.setKind(arrayKey.id,Relation._array);
            Debugger.info(arrayKey);
            Debugger.info(arrayKey.kind);
            for (Object o : (Iterable) value) {
                StatementStruct ok = addStatement4(0, this.id, arrayKey.id, getValueNode(o).id, false);
            }
            Debugger.info(getProperty(key));
        }
        else
            addStatement4(0, this.id, getAbstract(key).id, getValueNode(value).id, false);
    }


    @Override
    public <T> T getProperty(String key) {
//        StatementStruct statement = findStatement(this.getStruct(), getAbstract(key), ANY, 0, false, false, false, true);
        StatementStruct statement = findStatement(this.getStruct(), getAbstract(key), ANY, 1, true, true, true, true);
        if (statement == null) return null;
        if(getNode(statement.predicate).kind==Relation._array) return getProperties(key);
        return getValue(statement.getObject());
    }

    //    public <T> T[] getPropertiesA(String key) {
    public <T> T getProperties(String key) {
        ArrayList list = new ArrayList();
        for (Statement statement : getStatements()) {
            if(key.equals(Netbase.getName(statement.predicate)))
                list.add(Netbase.getName(statement.object));
//                list.add(Netbase.getValue(statement.object));
        }
        try {
            return (T) list;
        } catch (Exception e) {
            return (T) list.toArray();
        }
    }


    Node getValueNode(Object value){
        if(value instanceof String) return getThe((String) value);
        if(value instanceof Integer) return new Node(Netbase.value("" + value, (double)(int)value, getNode(Relation.number)));
        if(value instanceof Long) return new Node(Netbase.value("" + value, (double)(long)value, getNode(Relation.number)));
        if(value instanceof Float) return new Node(Netbase.value("" + value, (double)(float)value, getNode(Relation.number)));
        if(value instanceof Number) return new Node(Netbase.value("" + value, (double)value, getNode(Relation.number)));
        if(value instanceof Boolean) return getThe("" + value);//.setKind(Relation.number);// todo
        if(value instanceof java.util.Date) return new Node(Netbase.value("" + value, (double)((Date) value).getTime(),getNode( Relation.date)));
//        if(value instanceof java.util.Date /* ETC!@@! */) throw new IllegalArgumentException("not yet supportsSerializableObjectProperty");
//        if(value instanceof ArrayList) throw new RuntimeException("Should have iterated over ArrayList before");
//        if(value instanceof Iterable)throw new RuntimeException("Should have iterated over ArrayList before");
        throw new IllegalArgumentException("not yet supportsSerializableObjectProperty");
    }


    @Override
    public <T> T removeProperty(String key) {
        StatementStruct statement;
        T r = null;
        while (true) {
            statement = findStatement(this.getStruct(), getAbstract(key), ANY, 0, false, false, false, true);
            if (statement == null) return r;// 'break'
            Node object = statement.getObject();
            if(r ==null)// keep first Just for testAddingRemovingEdgeProperties
            r = getValue(object);
            int id1 = statement.getId();
            Debugger.info(id1);
            Netbase.deleteStatement(id1);
            Netbase.showNode(statement.subject);
        }
    }

    private <T> T getValue(Node object) {
        try {
            return (T) DateFormat.getDateInstance().parse(object.getName());
        } catch (Exception e) {}
        try {
            return (T) DateFormat.getDateTimeInstance().parse(object.getName());
        } catch (Exception e) {}
        try {
            return (T) (Double) Double.parseDouble(object.getName());
        } catch (Exception e) {
        }
        try {
            return (T) (Integer) Integer.parseInt(object.getName());
        } catch (Exception e) {
        }
        return (T) object.getName();
    }

    @Override
    public void remove() {
        graph.removeVertex(this);
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

//    public void setValue(Object object) {
//        Netbase.setValue(id, object);
//    }

//    private void getStruct() {
//    }
}
