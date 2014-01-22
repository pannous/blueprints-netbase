package com.tinkerpop.blueprints.impls.netbase;

import com.sun.jna.Pointer;
import com.tinkerpop.blueprints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.tinkerpop.blueprints.impls.netbase.Netbase.*;

/**
 * A Blueprints implementation of the graph database netbase (http://netbase.org)
 *
 * @author Pannous (http://Pannous.com)
 */
public class NetbaseGraph<T extends Node> implements Graph {//} IndexableGraph implements TransactionalGraph, IndexableGraph, KeyIndexableGraph {//, MetaGraph<GraphDatabaseService>
    private static final Logger logger = Logger.getLogger(NetbaseGraph.class.getName());
    private ArrayList<Node> nodes = new ArrayList<>();
    private List<Statement> edges = new ArrayList<>();

//    private static final Relation ANY =(Relation) getNode(1);

    protected static Node getNode(int i) {
        return new Node(Netbase.getNode(i));
    }

    private static NetbaseGraph me;
//    public NetbaseGraph() {
////        System.loadLibrary("netbase");// java.library.path
//        System.load("/usr/lib/netbase.so");// java.library.path
//    }

    private static final Features FEATURES = new Features();

    static {
        FEATURES.supportsSerializableObjectProperty = false;// ?!
        FEATURES.supportsBooleanProperty = true;
        FEATURES.supportsDoubleProperty = true;
        FEATURES.supportsFloatProperty = true;
        FEATURES.supportsIntegerProperty = true;
        FEATURES.supportsPrimitiveArrayProperty = true;
        FEATURES.supportsUniformListProperty = true;
        FEATURES.supportsMixedListProperty = false;
        FEATURES.supportsLongProperty = true;
        FEATURES.supportsMapProperty = false;//
        FEATURES.supportsStringProperty = true;

        FEATURES.supportsDuplicateEdges = true;
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
        if (id!=null&& hasNode("" + id))
            node = getNode("" + id);
        else
            node = new Node(this, id);
        nodes.add(node);
        return node;
    }


    public Vertex getVertex(Object id) {
        if (id == null) throw new IllegalArgumentException("getVertex id Must not be null");
        if (id instanceof Integer) {
            return new Node((int) id);
        } else {
            if (!Netbase.hasNode(id.toString())) return null;
            return new Node(Netbase.getAbstract("" + id));
        }
    }

    public void removeVertex(Vertex vertex) {
        nodes.remove(vertex);
//        Netbase.delete(vertex.getId());
    }

    public Iterable<Vertex> getVertices() {
        return new NodeIterable((List<Node>) nodes.clone());
    }

    public Iterable<Vertex> getVertices(String key, Object value) {
        return getThe(key).getVertices(Direction.BOTH, "" + value);//?
    }

    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
        Node predicate = getNode(label);
        StatementStruct s= Netbase.addStatement4(0, (int) outVertex.getId(), predicate.id, (int) inVertex.getId(), false);
        if(s==null) throw new RuntimeException("addStatement4 Unsuccessful!");
        Statement statement = new Statement(s);
        if(id!=null)statement.setId(id);// Only on the jave side
        edges.add(statement);
        return statement;
    }

    private Node getNode(String label) {
        return new Node(getAbstract(label));
    }

    public Edge getEdge(Object id) {
        return new Statement(Netbase.getStatement((int) id));
    }

    public void removeEdge(Edge edge) {
        Netbase.deleteStatement((int)edge.getId());
    }

    public Iterable<Edge> getEdges() {
        return null;        // ALL ??
    }

    public Iterable getEdges(String key, Object value) {
//        ArrayList <Statement> relations=new ArrayList<>();
//        relations.add(new Statement(getEdge(ANY),getEdge(key),getEdge(value)));
        return new StatementIterable<Edge>(key, value);
    }

//    private Node getNode(String key) {
//        return new Node(Netbase.getAbstract(key));
//    }

    public GraphQuery query() {
        return new NetbaseGraphQuery(this);
    }

    public void shutdown() {
        Netbase.execute("exit");
    }

    public static NetbaseGraph me() {
        if (me == null) me = new NetbaseGraph();
        return me;
    }

}
