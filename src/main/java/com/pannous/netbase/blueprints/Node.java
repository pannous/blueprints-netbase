package com.pannous.netbase.blueprints;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.StringFactory;
import org.apache.commons.collections.set.ListOrderedSet;

import java.text.DateFormat;
import java.util.*;

import static com.pannous.netbase.blueprints.Netbase.*;

/**
 * @author Pannous (http://Pannous.com)
 */
public class Node extends Structure implements Vertex {// extends Structure makes get(id) 3* slower, but no further lookups!

    public int id;
    //    public long name;// offset
    public String name;// yay Finally a private property make senses for me ! ;}
    public int kind;

//    public int statementCount; //implicit, can be replaced with iterator
//    public int firstStatement;
//    public int lastStatement;// remove
//    public Value value; // for statements, numbers  WASTE!!! remove

    private Pointer pointer;
    protected NetbaseGraph graph;

    //    @Override
    protected List getFieldOrder() {
//        JdbcRowSet

        return Arrays.asList(new String[]{"id", "name", "kind"});//, "statementCount", "firstStatement", "lastStatement" ,"value"
    }

    public Node(Pointer pointer) {
        super(pointer);
    }

    public Node(int id) {// AVOID!! more expensive than get(id) !!!
//        setAutoRead(false);
//        setAutoWrite(false);
//        setAutoSynch(false);
        this.id = id;
//        load();
    }


    public Node(NetbaseGraph netbaseGraph, Object id) {// id can be string here !!!
//        graph = netbaseGraph;
        if (id != null && id instanceof Integer) this.id = (int) id;
        else {
            this.id = nextId();
            if (id != null)// wtf ???
                setName(id.toString());
        }
//        save();
    }

//    private void load() {// what for?
//        /*this=*/ Node copy= Netbase.get(id);
//        kind=copy.kind;
//        name=copy.name;
//    }


//    private void save() {
//          Netbase.save(this);
//    }

//    public Node(String hi) {
////        id = Netbase.addNode(hi);
//        id = nextId();
//        setName(hi);
//        save(this);
//    }
//
//    public Node(NodeSt struct) {
//        throw new
//        id = struct.id;
//        name = struct.name;
//        kind = struct.kind;
////        struct.firstStatement
////                struct.statementCount
//    }
//

    public void setName(String s) {
//        name=
//        Netbase.setLabel(this, s);
        Netbase.setName(id, s);
        name = s;
    }

    public Node(Edge edge) {// reified statement!
        Object edgeId = edge.getId();
        id = (int) edgeId;
        setName(edge.getLabel());

//        statement = new Statement(Netbase.getStatement(id));
//        Netbase.setLabel(id,edge.getLabel());
        Set<String> propertyKeys = edge.getPropertyKeys();
        for (String propertyKey : propertyKeys) {
            addEdge(propertyKey, getThe("" + edge.getProperty(propertyKey)));// todo: doeuble value etc
        }
    }


    public Iterable<Edge> getEdges(final com.tinkerpop.blueprints.Direction direction, final String... labels) {
        return new StatementIterable(graph(), this, direction, labels);
    }

//    protected Node getStruct() {
//        Node Node = Netbase.getNode(id);
//        Node.id = id;// !?!
//        return Node;
//    }

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
        if (object instanceof Node) return ((Node) object).id == id;
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
            if (statement.object == Relation.reification) continue;
            if (statement.predicate == Relation.instance) continue;
            String propertyKey = Netbase.getName(statement.predicate);
            list.add(propertyKey);
        }
        return list;
    }

    //public <T> void setProperty(String key, T value) {
//    @Override
    public void setProperty(String key, Object value) {
        if (key == null || key.equals("")) throw new IllegalArgumentException("EMPTY ID not allowed as property");
        if (key.equals(StringFactory.LABEL)) throw new IllegalArgumentException("LABEL not allowed as property");
        if (key.equals(StringFactory.ID)) throw new IllegalArgumentException("ID not allowed as property");
        if (value == null || value.equals(""))
            throw new IllegalArgumentException("EMPTY value not allowed as property");

        if (value instanceof HashMap) {
            HashMap map = (HashMap) value;
            Node hashmap = getNew("hashmap");
            addStatement4(0, this.id, getAbstract(key).id, hashmap.id, false);
            for (Object k : map.keySet())
                addStatement4(0, hashmap.id, getAbstract("" + k).id, getValueNode(map.get(k)), false);
// OR FORGET KEY:addStatement4(0, this.id, getAbstract(""+k).id,getValueNode(map.get(k)).id, false);
        }
        if (value.getClass().isArray()) {
            addAll(key, value);
        } else {
            if (value instanceof Iterable) {
//            Node arrayKey = getNew(key,Relation.Array);
                Node arrayKey = getNew(key);
                Netbase.setKind(arrayKey.id, Relation.list);
                for (Object o : (Iterable) value)
                    addStatement4(0, this.id, arrayKey.id, getValueNode(o), false);
            } else {
                int id1 = Netbase.getId(key);
                int valueNode = getValueNode(value);
                addStatement4(0, this.id, id1, valueNode, false);
            }
        }
    }

    private <T> void addAll(String key, T values) {
        Node arrayKey = getNew(key);
        Netbase.setKind(arrayKey.id, Relation.array);
        if (values instanceof int[])
            for (int i : (int[]) values)
                addStatement4(0, this.id, arrayKey.id, getValueNode(i), false);
        else if (values instanceof long[])
            for (long i : (long[]) values)
                addStatement4(0, this.id, arrayKey.id, getValueNode(i), false);
        else if (values instanceof double[])
            for (double i : (double[]) values)
                addStatement4(0, this.id, arrayKey.id, getValueNode(i), false);
        else if (values instanceof float[])
            for (float i : (float[]) values)
                addStatement4(0, this.id, arrayKey.id, getValueNode(i), false);
        else if (values instanceof boolean[])
            for (boolean i : (boolean[]) values)
                addStatement4(0, this.id, arrayKey.id, getValueNode(i), false);
        else for (Object o : (Object[]) values)
                addStatement4(0, this.id, arrayKey.id, getValueNode(o), false);
//        for (Object o : Arrays.asList(value)) {
//            addStatement4(0, this.id, arrayKey.id, getValueNode(o), false);
//        }
    }


    @Override
    public <T> T getProperty(String key) {
        show();
        int keyId = Netbase.getId(key);
        StatementStruct statement = findStatement(id, keyId, Relation.ANY, 0, false, false, false, true);
        if (statement == null) return null;
        if (getNode(statement.predicate).kind == Relation.list) return getProperties(key);
        if (getNode(statement.predicate).kind == Relation.array) return getPropertiesA(key, new ArrayList<T>());
        statement.show();
        return getValue(statement.getObject());
    }

    public <T, U> T getPropertiesA(String key, ArrayList<U> list) {
        for (Statement statement : getStatements()) {
            if (key.equals(Netbase.getName(statement.predicate))) {
                Object value = getValue(statement.Object());
                list.add((U) value);
            }
        }
        Collections.reverse(list);
        if (list.get(0) instanceof String)
            return (T) list.toArray(new String[0]);
        if (list.get(0) instanceof Integer)
            return (T) list.toArray(new Integer[0]);
        if (list.get(0) instanceof Long)
            return (T) list.toArray(new Long[0]);
        return (T) (U[]) list.toArray();//
    }

    public <T> T getProperties(String key) {
        ArrayList list = new ArrayList();
        for (Statement statement : getStatements()) {
            if (key.equals(Netbase.getName(statement.predicate)))
                list.add(Netbase.getName(statement.object));
        }
        Collections.reverse(list);
        return (T) list;
    }


    int getValueNode(Object value) {
        if (value instanceof Node) return ((Node) value).id;
        if (value instanceof String) return Netbase.getId((String) value);
//        if (value instanceof String) return getNew((String) value).id;// zickzack!
        if (value instanceof Integer)
            return Netbase.valueId("" + value, (double) (int) value, Relation.integer);
        if (value instanceof Long)
            return Netbase.valueId("" + value, (double) (long) value, Relation.integer);// long
        if (value instanceof Float)
            return Netbase.valueId("" + value, (double) (float) value, Relation.number);
        if (value instanceof Number)
            return Netbase.valueId("" + value, (double) value, Relation.number);
        if (value instanceof Boolean) return value == true ? Relation._true : Relation._false;
        if (value instanceof java.util.Date)
            return Netbase.valueId("" + value, (double) ((Date) value).getTime(), Relation.date);
//        if(value instanceof java.util.Date /* ETC!@@! */) throw new IllegalArgumentException("not yet supportsSerializableObjectProperty");
        if (value instanceof ArrayList) throw new RuntimeException("Should have iterated over ArrayList before");
        if (value instanceof Iterable) throw new RuntimeException("Should have iterated over ArrayList before");
        throw new IllegalArgumentException("not yet supportsSerializableObjectProperty " + value + " -> " + value.getClass());
    }


    @Override
    public <T> T removeProperty(String key) {
        StatementStruct statement;
        T r = null;
        while (true) {
            statement = findStatement(id, getAbstract(key).id, Relation.ANY, 1, true, true, true, true);
            if (statement == null) return r;// 'break'
            Node object = statement.getObject();
            if (r == null)// keep first Just for testAddingRemovingEdgeProperties
                r = getValue(object);
            int id1 = statement.getId();
            Debugger.info(id1);
            Netbase.deleteStatement(id1);
            Netbase.showNode(statement.subject);
        }
    }

    private <T> T getValue(Node object) {
        object.show();
        Debugger.info("KIND " + Relation.name(object.kind));
        try {
            if (object.kind == Relation.date)
                return (T) DateFormat.getDateTimeInstance().parse(object.getName());
        } catch (Exception e) {
        }
        try {
            if (object.kind == Relation.date)
                return (T) DateFormat.getDateInstance().parse(object.getName());
        } catch (Exception e) {
        }
        try {
            if (object.kind == Relation.integer)// and long
                return (T) (Integer) Integer.parseInt(object.getName());
        } catch (Exception e) {
        }
        try {
            if (object.kind == Relation.number)
                return (T) (Double) Double.parseDouble(object.getName());
        } catch (Exception e) { // can't cast double to int :(
        }
        if (object.kind == Relation.node) {
            Integer id = Integer.parseInt(object.getName());
            try {
                return (T) getNode(id);
            } catch (Exception e) {
            }
            try {
                return (T) getNode(id);
            } catch (Exception e) {
            }
        }
        if (object.kind == Relation.statement) {
            Integer id = Integer.parseInt(object.getName());
            try {
                return (T) getStatement(id);
            } catch (Exception e) {
            }
            try {
                return (T) new Statement(getStatement(id));
            } catch (Exception e) {
            }
        }
        return (T) object.getName();
    }

    @Override
    public void remove() {
        graph().removeVertex(this);
    }

    @Override
    public Object getId() {// JUST FOR blueprint! ID==NAME
        if (name == null)
            return id;
        if (name.equals("" + id))
            return id;
        return name;
    }

    public Iterable<Statement> getStatements() {
        return new StatementIterable(graph(), this, Direction.BOTH, null);
    }

    //    public String getName() {
//        if (_name == null) _name = Netbase.getName(id);
//        return _name;
//    }
    public String getName() {
        if (name == null) name = Netbase.getName(id);
        return name;
    }

    @Override
    public int hashCode() {
        return id;
    }

//    public int statementCount() {
//        return getNode(id).statementCount;
//    }

    public boolean hasMember(Node fog) {
//        Node has = Netbase.has(this, fog);
        StatementStruct has = findStatement(id, Relation.ANY, fog.id, 0, false, false, false, false);

        showNode(this.id);
        return has != null;
    }

    public void show() {
        Netbase.showNode(id);
    }

    public void delete() {
//        Debugger.trace("deleteNode " + id);
        remove();
    }

    public Node load() {
        super.autoRead();
        return this;
    }

    public void addProperty(String key, Object value) {
        setProperty(key, value);
    }

//    public void setValue(Object object) {
//        Netbase.setValue(id, object);
//    }

//    private void getStruct() {
//    }
}