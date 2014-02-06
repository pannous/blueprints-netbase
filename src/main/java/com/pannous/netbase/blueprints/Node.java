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

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.Logger;


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
    private Logger logger = Logger.getLogger("Netbase");
    protected NetbaseGraph graph = LocalNetbaseGraph.me();


    @Override
    protected List getFieldOrder() {
//        JdbcRowSet

        return Arrays.asList(new String[]{"id", "name", "kind"});//, "statementCount", "firstStatement", "lastStatement" ,"value"
    }

    public Node(Pointer pointer) {
        super(pointer);
    }

    public Node(int id) {// AVOID!! more expensive than get(id) !!!
        if (id == -1) {
            setAutoRead(false);
            setAutoWrite(false);
            setAutoSynch(false);
        } else
            this.id = id;
//        load();
    }


    public Node(NetbaseGraph netbaseGraph, Object id) {// id can be string here !!!
//        graph = netbaseGraph;
        if (id != null && id instanceof Integer) this.id = (Integer) id;
        else {
            this.id = graph.nextId();
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
//        id = graph.nextId();
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

    public Node setName(String s) {
        if (s == null) return this;// or clear?
        if (s.equals(name)) return this;
//        name= Netbase.setLabel(this, s);
        graph.setName(id, s);
        name = s;
        return this;
    }

    public Node(Edge edge) {// reified statement!
        Object edgeId = edge.getId();
        id = (Integer) edgeId;
        setName(edge.getLabel());

//        statement = new Statement(Netbase.getStatement(id));
//        Netbase.setLabel(id,edge.getLabel());
        Set<String> propertyKeys = edge.getPropertyKeys();
        for (String propertyKey : propertyKeys) {
            addEdge(propertyKey, graph.getThe("" + edge.getProperty(propertyKey)));// todo: doeuble value etc
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

    private LocalNetbaseGraph graph() {
        if (graph == null) return LocalNetbaseGraph.me();
        return (LocalNetbaseGraph) graph;
    }

    public Edge addEdge(final String label, final Vertex vertex) {
        if (label == null || label.equals("")) throw new IllegalArgumentException("EMPTY ID not allowed as property");
        if (label.equals(StringFactory.LABEL)) throw new IllegalArgumentException("LABEL not allowed as property");
        if (label.equals(StringFactory.ID)) throw new IllegalArgumentException("ID not allowed as property");
        if (vertex == null || vertex.toString().equals(""))
            throw new IllegalArgumentException("EMPTY value not allowed as property");
        Node node = new Node(graph(), vertex.getId());
        Node the = graph.getThe(label);
        return graph().addStatement(id, the.id, node.id);
    }

    public VertexQuery query() {
        return new DefaultVertexQuery(this);
    }

    public boolean equals(final Object object) {
        if (object == this) return true;
        if (object instanceof Node) {
            Node node = (Node) object;
            return node.id == id;
        }
        if (object instanceof Vertex) return ((Vertex) object).getId().equals(id);
        return false;
//        return object instanceof Node && ((Node) object).getId().equals(this.getId());
    }

    public String toString() {
        return StringFactory.vertexString(this);// wah! blueprints wtf
    }


    @Override
    public Set<String> getPropertyKeys() {
        Set<String> list = new ListOrderedSet();
        for (Statement statement : getStatements()) {
            if (statement.object == Relation.reification) continue;
            if (statement.predicate == Relation.instance) continue;
            String propertyKey = graph.getName(statement.predicate);
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

        deleteProperty(key);// or merge ! blah.friends={1:a,2:b,3:c}

        if (value instanceof HashMap) {
            HashMap map = (HashMap) value;
//            Node hashmap = graph.getNew("hashmap");
            Node hashmap = graph.getNew(key);
            graph.setKind(hashmap.id, Relation.map);
            graph.addStatement(this.id, hashmap.id, hashmap.id);// :( wegen findStatement(.,KEY,*)
//            graph.addStatement(this.id, Relation.map, hashmap.id);
//            graph.addStatement(this.id, Relation.has, hashmap.id);
            for (Object k : map.keySet())
                graph.addStatement(hashmap.id, graph.getAbstract("" + k).id, getValueNode(map.get(k)));
// OR FORGET KEY:graph.addStatement( this.id, graph.getAbstract(""+k).id,getValueNode(map.get(k)).id, false);
        } else if (value.getClass().isArray()) {
            addAll(key, value);
        } else if (value instanceof Iterable) {
//            Node arrayKey = graph.getNew(key,Relation.Array);
            Node arrayKey = graph.getNew(key);
            graph.setKind(arrayKey.id, Relation.list);
            for (Object o : (Iterable) value)
                graph.addStatement(this.id, arrayKey.id, getValueNode(o));
        } else {
            int id1 = graph.getId(key);
            int valueNode = getValueNode(value);
            graph.addStatement(this.id, id1, valueNode);
        }
    }


    private <T> void addAll(String key, T values) {
        Node arrayKey = graph.getNew(key);
        graph.setKind(arrayKey.id, Relation.array);
        if (values instanceof int[])
            for (int i : (int[]) values)
                graph.addStatement(this.id, arrayKey.id, getValueNode(i));
        else if (values instanceof long[])
            for (long i : (long[]) values)
                graph.addStatement(this.id, arrayKey.id, getValueNode(i));
        else if (values instanceof double[])
            for (double i : (double[]) values)
                graph.addStatement(this.id, arrayKey.id, getValueNode(i));
        else if (values instanceof float[])
            for (float i : (float[]) values)
                graph.addStatement(this.id, arrayKey.id, getValueNode(i));
        else if (values instanceof boolean[])
            for (boolean i : (boolean[]) values)
                graph.addStatement(this.id, arrayKey.id, getValueNode(i));
        else for (Object o : (Object[]) values)
                graph.addStatement(this.id, arrayKey.id, getValueNode(o));
//        for (Object o : Arrays.asList(value)) {
//            graph.addStatement( this.id, arrayKey.id, getValueNode(o), false);
//        }
    }


    private void deleteProperty(String key) {
//        show();
        int keyId = graph.getId(key);
//        logger.info(key+" -> "+keyId);
        Statement statement = graph.findStatement(id, keyId, Relation.ANY, 0, false, false, false, true);
        if (statement == null) return;
        Node predicate = graph.getNode(statement.predicate);
        Node object = graph.getNode(statement.object);
        object.delete();
        show();
    }

    @Override
    public <T> T getProperty(String key) {
//        show();
        int keyId = graph.getId(key);
        Statement statement = graph.findStatement(id, keyId, Relation.ANY, 0, false, false, false, true);
        if (statement == null) return null;
        Node predicate = graph.getNode(statement.predicate);
        Node object = graph.getNode(statement.object);
        if (predicate.kind == Relation.list) return getPropertyList(key);
        if (predicate.kind == Relation.array) return getPropertyArray(key, new ArrayList<T>());
        if (predicate.kind == Relation.bytes || object.kind == Relation.bytes) return getSerialized(object);
        if (predicate.kind == Relation.map || object.kind == Relation.map) return getPropertyMap(object);
//        statement.show();
        return getValue(statement.Object());
    }


    private <T> T getPropertyMap(Node key) {
        HashMap map = new HashMap();
        for (Statement statement : key.getStatements()) {
            Node predicate = statement.Predicate();
            Node object = statement.Object();
            if (statement.subject == key.id && statement.predicate != key.id && statement.object != key.id)
                map.put(predicate.getName(), getValue(object));
        }
        return (T) map;
    }

    public <T, U> T getPropertyArray(String key, ArrayList<U> list) {
        for (Statement statement : getStatements()) {
            if (key.equals(graph.getName(statement.predicate))) {
                Object value = getValue(statement.Object());
                list.add((U) value);
            }
        }
        if (list.size() == 0) {
            logger.warning("EMPTY property array! " + key);
            return (T) list.toArray();
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

    public <T> T getPropertyList(String key) {
        ArrayList list = new ArrayList();
        for (Statement statement : getStatements()) {
            if (key.equals(graph.getName(statement.predicate)))
                list.add(graph.getName(statement.object));
        }
        Collections.reverse(list);
        return (T) list;
    }


    int getValueNode(Object value) {
        if (value instanceof Node) return ((Node) value).id;
        if (value instanceof String) return graph.getId((String) value);
//        if (value instanceof String) return graph.getNew((String) value).id;// zickzack!
        if (value instanceof Integer)
            return graph.valueId("" + value, (double) (Integer) value, Relation.integer);
        if (value instanceof Long)
            return graph.valueId("" + value, (double) (Long) value, Relation.integer);// long
        if (value instanceof Float)
            return graph.valueId("" + value, (double) (Float) value, Relation.number);
        if (value instanceof Number)
            return graph.valueId("" + value, (Double) value, Relation.number);
        if (value instanceof Boolean)
            return ((Boolean) value).booleanValue() == true ? Relation._true : Relation._false;
        if (value instanceof java.util.Date)
            return graph.valueId("" + value, (double) ((Date) value).getTime(), Relation.date);
//        if(value instanceof java.util.Date /* ETC!@@! */) throw new IllegalArgumentException("not yet supportsSerializableObjectProperty");
        if (value instanceof ArrayList) throw new RuntimeException("Should have iterated over ArrayList before");
        if (value instanceof Iterable) throw new RuntimeException("Should have iterated over ArrayList before");
        if (value instanceof Serializable) return serialized((Serializable) value);
        throw new IllegalArgumentException("not yet supportsSerializableObjectProperty " + value + " -> " + value.getClass());
    }


    private <T> T getSerialized(Node object) {
        try {
            int size = object.getProperty("size");
            byte[] byteArray= graph().getData(object.id,size);
//            if(byteArray[0]==-84&&byteArray[1]==-19) Java signature
            ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
            ObjectInput input = new ObjectInputStream(stream);
            return (T) input.readObject();
        } catch (Exception e) {
            Debugger.error(e);
        }
        return null;
    }

    private int serialized(Serializable value) {
        int dataId = 0;
        try {
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            ObjectOutput outputStream = new ObjectOutputStream(s);
            outputStream.writeObject(value);
            byte[] bytes = s.toByteArray();
            for (int i = 0; i < bytes.length; i++) {
                System.out.print(bytes[i]+",");
            }
            dataId = graph().valueId("byte[]", bytes.length, Relation.bytes);
            System.out.println(new String(bytes));
            graph().addStatement(dataId, graph().getAbstract("size").id, getValueNode(bytes.length));
            logger.info("------------------------");
            graph.save(dataId, bytes);
        } catch (IOException e) {
            Debugger.error(e);
        }
        return dataId;
    }


    @Override
    public <T> T removeProperty(String key) {
        Statement statement;
        T r = null;
        while (true) {
            statement = graph.findStatement(id, graph.getAbstract(key).id, Relation.ANY, 1, true, true, true, true);
            if (statement == null) return r;// 'break'
            Node object = statement.Object();
            if (r == null)// keep first Just for testAddingRemovingEdgeProperties
                r = getValue(object);
            int id1 = (Integer) statement.getId();
            Debugger.info(id1);
            graph.deleteStatement(id1);
            graph.showNode(statement.subject);
        }
    }

    private <T> T getValue(Node object) {
//        object.show();
        Debugger.info("KIND " + Relation.name(object.kind));
        String value = object.getName();
        if(value==null) return null;
        try {
            if (object.kind == Relation.date)
                return (T) DateFormat.getDateTimeInstance().parse(value);
        } catch (Exception e) {
        }
        if (value.equalsIgnoreCase("true")||value.equalsIgnoreCase("Wahr")) {// //object.kind == Relation.bool)
            return (T) (Boolean) true;
        }
        if (value.equalsIgnoreCase("false")||value.equalsIgnoreCase("Falsch")) {// //object.kind == Relation.bool)
            return (T) (Boolean) false;
        }
        try {
            if (object.kind == Relation.date)
                return (T) DateFormat.getDateInstance().parse(value);
        } catch (Exception e) {
        }
        try {
            if (object.kind == Relation.integer)// and long
                return (T) (Integer) Integer.parseInt(value);
        } catch (Exception e) {
        }
        try {
            if (object.kind == Relation.number)
                return (T) (Double) Double.parseDouble(value);
        } catch (Exception e) { // can't cast double to int :(
        }
        if (object.kind == Relation.node) {
            Integer id = Integer.parseInt(value);
            try {
                return (T) graph.getNode(id);
            } catch (Exception e) {
            }
            try {
                return (T) graph.getNode(id);
            } catch (Exception e) {
            }
        }
        if (object.kind == Relation.statement) {
            Integer id = Integer.parseInt(value);
            try {
                return (T) graph.getStatement(id);
            } catch (Exception e) {
            }
        }
        return (T) value;
    }

    @Override
    public void remove() {
        ((LocalNetbaseGraph) graph()).nodes.remove(this);
        graph().deleteNode(id);
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
        if (name == null) name = graph.getName(id);
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
        Statement has = graph.findStatement(id, Relation.ANY, fog.id, 0, false, false, false, false);
        graph.showNode(this.id);
        return has != null;
    }

    public void show() {
        graph.showNode(id);
    }

    public void delete() {
        if (id > 0 && id < graph().nodeCount()) {
            Debugger.trace("deleteNode " + id);
            graph().deleteNode(id);
        }
    }

    public Node load() {
        super.autoRead();
        return this;
    }

    public void addProperty(String key, Object value) {
        setProperty(key, value);
    }

    public <T> T getValue() {
        return (T) toString();
    }

    public void renameAll(String newName) {
        graph.renameAll(id, newName);
        name=newName;
    }

//    public void setValue(Object object) {
//        Netbase.setValue(id, object);
//    }

//    private void getStruct() {
//    }
}
