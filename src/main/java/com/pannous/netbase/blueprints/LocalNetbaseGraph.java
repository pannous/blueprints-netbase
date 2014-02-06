package com.pannous.netbase.blueprints;

import com.sun.jna.Pointer;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.util.StringFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.pannous.netbase.blueprints.LocalNetbase.*;

/**
 * A Blueprints implementation of the graph database netbase (https://github.com/pannous/blueprints-netbase)
 *
 * @author Pannous (http://Pannous.com)
 */
public class LocalNetbaseGraph<T extends Node> implements NetbaseGraph, Graph {//} IndexableGraph implements TransactionalGraph, IndexableGraph, KeyIndexableGraph {//, MetaGraph<GraphDatabaseService>
    private static final Logger logger = Logger.getLogger(NetbaseGraph.class.getName());
    public static ArrayList<Node> nodes = new ArrayList();
//    public static Map<Integer,Node> cache=new HashMap<Integer, Node>(1000);
    public static List<Statement> edges = new ArrayList();
//    public static NetbaseCon

//    private static final Relation ANY =(Relation) getNode(1);

    private static LocalNetbaseGraph  me;
//    public NetbaseGraph() {
////        System.loadLibrary("netbase");// java.library.path
//        System.load("/usr/lib/netbase.so");// java.library.path
//    }

    private static final Features FEATURES = new Features();

    static {
        FEATURES.supportsSerializableObjectProperty = true;// false true Date AND ... ? false;// ?!
        FEATURES.supportsBooleanProperty = true;
        FEATURES.supportsDoubleProperty = true;
        FEATURES.supportsFloatProperty = true;
        FEATURES.supportsIntegerProperty = true;
        FEATURES.supportsPrimitiveArrayProperty = true;// testNetbaseArray true;// todo!! true;
        FEATURES.supportsUniformListProperty = true;// todo!! true;
        FEATURES.supportsMixedListProperty = true;
        FEATURES.supportsLongProperty = true;
        FEATURES.supportsMapProperty = true;//false
        FEATURES.supportsStringProperty = true;

        FEATURES.supportsDuplicateEdges = false;// true == bad?!
        FEATURES.supportsSelfLoops = true;
        FEATURES.isPersistent = true;
        FEATURES.isWrapper = false;
        FEATURES.supportsVertexIteration = true;
        FEATURES.ignoresSuppliedIds = false;
        FEATURES.supportsTransactions = false;

        FEATURES.supportsEdgeIteration = false;// true

        FEATURES.supportsIndices = false;// true?
        FEATURES.supportsEdgeIndex = false;
        FEATURES.supportsVertexIndex = false;
        FEATURES.supportsKeyIndices = false;// ?
        FEATURES.supportsVertexKeyIndex = false;// ?
        FEATURES.supportsEdgeKeyIndex = false;// ?

        FEATURES.supportsEdgeRetrieval = true;
        FEATURES.supportsEdgeProperties = true;// x ??
        FEATURES.supportsVertexProperties = true;// reification
        FEATURES.supportsThreadedTransactions = false;
    }


    public Features getFeatures() {
        return FEATURES;
    }

    public Vertex addVertex(Object id) {
        Node node;
        if (id != null) {
//            if (id instanceof Integer && (int)id>1000)
//                node = get((Integer) id);
//            else
            if (hasNode("" + id))
                node = getNode("" + id);
            else {
                node = LocalNetbase.getAbstract("" + id);
//                node = LocalNetbase.add("" + id, Relation._abstract);
                nodes.add(node);
//                node = get(nextId());
//                node.setName(id.toString());
            }
        } else {
//            node = get(nextId());
            node = LocalNetbase.add("" + (nextId() + 1), Relation._abstract);// lolhack
            nodes.add(node);
        }
//        cache.put(node.id, node);
        return node;
    }


    public Vertex getVertex(Object id) {
        if (id == null) throw new IllegalArgumentException("getVertex id Must not be null");
        if (id instanceof Integer) return get((Integer) id);
        try {// for Blueprint !!
            int i = Integer.parseInt("" + id);
            if (i >= 1000)// don't ID Blueprint's "1" test 'ids'
                return new Node(i);
//            else return get(i);// RELATIONS!
        } catch (NumberFormatException e) {
        }
        if (!LocalNetbase.hasNode(id.toString())) return null;
        return LocalNetbase.getAbstract("" + id);
    }

    public void removeVertex(Vertex vertex) {
        if(vertex==null)return;
        nodes.remove(vertex);
        if (vertex.getId() instanceof Integer)
            LocalNetbase.deleteNode((Integer) vertex.getId());
        else
            LocalNetbase.deleteNode(getId("" + vertex.getId()));
    }

    public Iterable<Vertex> getVertices() {
        return new NodeIterable((List<Node>) nodes.clone());
    }

    public Iterable<Vertex> getVertices(String key, Object value) {
//        return getThe(key).getVertices(Direction.VALUE, "" + value);//? TPDP!
//        return getThe(key).getVertices(Direction.BOTH, "" + value);//?
        return getAbstract(key).getVertices(Direction.BOTH, "" + value);//?
    }

    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
        if (label == null || label.equals(""))
            throw new IllegalArgumentException("EMPTY label not allowed as property");
        if (label.equals(StringFactory.LABEL))
            throw new IllegalArgumentException("label 'LABEL' not allowed as property");// why??
        if (label.equals(StringFactory.ID)) throw new IllegalArgumentException("label 'ID' not allowed as property");

//        if(value==null|| value.equals("")) throw new IllegalArgumentException("EMPTY value not allowed as property");
//        if(key.equals("id")) throw new IllegalArgumentException("id key Not allowed as property");
        Node predicate = getNode(label);
        Node subject = getNode(outVertex);
        Node object = getNode(inVertex);
        StatementStruct s = LocalNetbase.addStatement4(0, subject.id, predicate.id, object.id, false);
        if (s == null) throw new RuntimeException("addStatement4 Unsuccessful!");
        Statement statement = new Statement(s);
        if (id != null) {
            if (id instanceof Integer)
                statement.id = (Integer) id;// Only on the jave side
            statement.id = Integer.parseInt("" + id);
        }
        edges.add(statement);
        return statement;
    }

    private Node getNode(Vertex vertex) {
        if (vertex instanceof Node) return (Node) vertex;
        return getAbstract("" + vertex.getId());
    }

    private Node getNode(String label) {
        return getAbstract(label);
    }

    public Edge getEdge(Object id) {
        if (id == null) throw new IllegalArgumentException("getEdge id Must not be null");
        if (!(id instanceof Integer)) return null;// BAD Requirement
        return new Statement(LocalNetbase.getStatement((Integer) id));
    }

    public void removeEdge(Edge edge) {
        LocalNetbase.deleteStatement((Integer) edge.getId());
    }

    public Iterable<Edge> getEdges() {
        return new AllEdges();        // ALL ??
    }

    public Iterable getEdges(String key, Object value) {
//        ArrayList <Statement> relations=new ArrayList<>();
//        relations.add(new Statement(getEdge(ANY),getEdge(key),getEdge(value)));
//        Netbase.doExecute()
//        return new StatementIterable<Edge>(key, value);
        return getNode(key).getEdges(Direction.BOTH, "" + value);
    }

//    private Node getNode(String key) {
//        return new Node(Netbase.getAbstract(key));
//    }

    public GraphQuery query() {
        return new NetbaseGraphQuery(this);
    }

    public void shutdown() {
        LocalNetbase.doExecute("exit");
    }

    public static LocalNetbaseGraph me() {
        if (me == null) me = new LocalNetbaseGraph();
        return me;
    }

    @Override
    public String toString() {
        return "LocalNetbaseGraph".toLowerCase() + "_" + super.toString();
    }

    public void addStatement4(int contextId, int subjectId, int predicateId, int objectId, boolean checkNodes) {
        LocalNetbase.addStatement4(contextId, subjectId, predicateId, objectId, checkNodes);
    }

    public void setKind(int id, int kind) {
        LocalNetbase.setKind(id, kind);
    }

    public Statement addStatement(int subjectId, int predicateId, int objectId) {
        return new Statement(LocalNetbase.addStatement4(0, subjectId, predicateId, objectId, false));
    }

    public int nextId() {
        return LocalNetbase.nextId();
    }

    public Node getThe(String name) {
        return LocalNetbase.getThe(name);
    }

    public Node getNew(String name) {
        return LocalNetbase.getThe(name);
    }

    public Node getAbstract(String name) {
        return LocalNetbase.getAbstract(name);
    }

    public Node getNode(int id) {
        return LocalNetbase.getNode(id);
    }

    public Statement getStatement(Integer id) {
        return new Statement(LocalNetbase.getStatement(id));
    }

    @Override
    public void deleteNode(int id) {
//        nodes.remove(getNode(id));
        LocalNetbase.deleteNode(id);
    }

    @Override
    public int statementCount() {
        return LocalNetbase.statementCount();
    }

    public void showNode(int id) {
        LocalNetbase.showNode(id);
    }

    public void setName(int id, String s) {
        LocalNetbase.setName(id, s);
    }

    public String getName(int predicate) {
        return LocalNetbase.getName(predicate);
    }

    public int getId(String key) {
        return LocalNetbase.getId(key);
    }

    public Statement findStatement(int subject, int predicate, int object, int recurse, boolean semantic, boolean symmetric, boolean semanticPredicate, boolean matchName) {
        StatementStruct statement = LocalNetbase.findStatement(subject, predicate, object, recurse, semantic, symmetric, semanticPredicate, matchName);
        if(statement==null) return null;
        return new Statement(statement);
    }

    public int valueId(String name, double value, int unitId) {
        return LocalNetbase.valueId(name, value, unitId);
    }

    public void deleteStatement(int id) {
        LocalNetbase.deleteStatement(id);
    }

    public int nodeCount() {
        return LocalNetbase.nodeCount();
    }

    @Override
    public Node[] query(String s) {
        return LocalNetbase.doExecute(s);
    }

    @Override
    public void save(int id, byte[] bytes) {
        boolean copy = true;// false;
//        new Memory(bytes.length).setByte()
//        new Buffer()
        LocalNetbase.saveData(id, bytes, bytes.length, copy);

    }

    @Override
    public Value getValue(int id) {
        return LocalNetbase.getValue(id);
    }
    @Override
    public byte[] getData(int id, int size){
//        byte[] byteArray  = LocalNetbase.getData(id);
//        byte[] byteArray = pointer.getByteArray(0, size);
        Pointer pointer = LocalNetbase.getData(id);
        logger.info(pointer.dump(0,size));
        logger.info(pointer.toString());
        byte[] byteArray = pointer.getByteArray(0, size);
        String test = new String(byteArray);
        System.out.println(test);
        return byteArray;
    }
}
