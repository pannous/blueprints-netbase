package com.tinkerpop.blueprints.impls.netbase;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.util.StringFactory;

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
    private static ArrayList<Node> nodes = new ArrayList<>();
    private static List<Statement> edges = new ArrayList<>();

//    private static final Relation ANY =(Relation) getNode(1);

    private static NetbaseGraph me;
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
            if (id instanceof Integer)
                node = get((int) id);
            else if (hasNode("" + id))
                node = getNode("" + id);
            else {
                node = Netbase.add("" + id,Relation._abstract);
//                node = get(nextId());
//                node.setName(id.toString());
            }
        } else{
//            node = get(nextId());
            node = Netbase.add(""+(nextId()+1),Relation._abstract);// lolhack
        }
        nodes.add(node);
        return node;
    }


    public Vertex getVertex(Object id) {
        if (id == null) throw new IllegalArgumentException("getVertex id Must not be null");
        if (id instanceof Integer) return get((int) id);
        try {// for Blueprint !!
            int i = Integer.parseInt("" + id);
            if (i > 10000)// wth?
                return new Node(i);
        } catch (NumberFormatException e) {
        }
        if (!Netbase.hasNode(id.toString())) return null;
        return Netbase.getAbstract("" + id);
    }

    public void removeVertex(Vertex vertex) {
        nodes.remove(vertex);
        if(vertex.getId() instanceof Integer)
            Netbase.deleteNode((int) vertex.getId());
        else
            Netbase.deleteNode(getId("" + vertex.getId()));
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
        StatementStruct s = Netbase.addStatement4(0, subject.id, predicate.id, object.id, false);
        if (s == null) throw new RuntimeException("addStatement4 Unsuccessful!");
        Statement statement = new Statement(s);
        if (id != null){
            if(id instanceof Integer)
            statement.id = (int) id;// Only on the jave side
            statement.id = Integer.parseInt(""+id);
        }
        edges.add(statement);
        return statement;
    }

    private Node getNode(Vertex vertex) {
        if(vertex instanceof Node) return (Node) vertex;
        return getAbstract("" + vertex.getId());
    }

    private Node getNode(String label) {
        return getAbstract(label);
    }

    public Edge getEdge(Object id) {
        if (id == null) throw new IllegalArgumentException("getEdge id Must not be null");
        if (!(id instanceof Integer)) return null;// BAD Requirement
        return new Statement(Netbase.getStatement((int) id));
    }

    public void removeEdge(Edge edge) {
        Netbase.deleteStatement((int) edge.getId());
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

    @Override
    public String toString() {
        return "NetbaseGraph".toLowerCase() + "_" + super.toString();
    }
}
