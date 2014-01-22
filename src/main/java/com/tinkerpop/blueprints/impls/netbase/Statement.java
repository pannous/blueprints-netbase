package com.tinkerpop.blueprints.impls.netbase;


import com.sun.jna.Structure;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.ExceptionFactory;
import com.tinkerpop.blueprints.util.StringFactory;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.tinkerpop.blueprints.impls.netbase.Netbase.addStatement4;
import static com.tinkerpop.blueprints.impls.netbase.Netbase.get;
import static com.tinkerpop.blueprints.impls.netbase.Netbase.getName;

/**
 * @author Pannous (http://Pannous.com)
 */
public class Statement implements Edge { // extends Structure


    private Node reification;
    private int subject;
    int predicate;
    private int object;

    private Node _subject;
    private Node _predicate;
    private Node _object;
    private NetbaseGraph graph;
    private Object id;

    public Statement() {
//        setFieldOrder(new String[]{"context","subject","predicate","object", "nextSubjectStatement", "nextPredicateStatement", "nextObjectStatement"});
        //    REORDER NEEDS NEW INDEX ON ALL SERVERS +JAVA!
//        setFieldOrder(new String[]{"context","nextSubjectStatement", "nextPredicateStatement", "nextObjectStatement","subject","predicate","object" });
    }

    public Statement(Vertex outVertex, Node predicate, Vertex inVertex) {
        graph = predicate.graph;
        _subject = (Node) outVertex;
        _predicate = predicate;
        _object = (Node) inVertex;
    }


    public Statement(final NetbaseGraph graph) {
//        super(graph);
    }

    public Statement(Node subject, Node predicate, Node object) {
//        super(subject.graph);
        _subject = subject;
        _predicate = predicate;
        _object = object;
    }

    public Statement(Edge subject, Edge predicate, Edge object) {
//        super(NetbaseGraph.me());
        try {
            this.subject = (int) subject.getId();
            this.predicate = (int) predicate.getId();
            this.object = (int) object.getId();
        } catch (Exception e) {
            Debugger.warn(e);
            _subject = new Node(subject);
            _predicate = new Node(predicate);
            _object = new Node(object);
        }
//        addStatement4(0, this.subject, this.predicate, this.object,false); UNLESS LOADED!
    }

    public Statement(StatementStruct statementStruct) {
        subject = statementStruct.subject;
        predicate = statementStruct.predicate;
        object = statementStruct.object;
        id = Netbase.getStatementId(statementStruct);// double, expensive
    }

    @Override
    public Vertex getVertex(Direction direction) {
        if (direction == Direction.OUT) return Subject();
        if (direction == Direction.IN) return Object();
        else
            throw ExceptionFactory.bothIsNotSupported();
    }

    @Override
    public String getLabel() {
//        return Subject().toString()+"->"+Predicate().toString()+"->"+Object();
        return getName(predicate);
    }

    protected Node Subject() {
        if (_subject == null) _subject = new Node(subject);
        return _subject;
    }

    private Node Predicate() {
        if (_predicate == null) _predicate = new Node(predicate);
        return _predicate;
    }

    public Node Object() {
        if (_object == null) _object = new Node(object);
        return _object;
    }


    @Override
    public boolean equals(final Object o) {
        if(this.getId()==null) return false;
        if (o == this) return true;
        if (o instanceof StatementStruct) {
            StatementStruct s = (StatementStruct) o;
            if ((s.subject == subject && s.predicate == predicate && s.object == object))
                return true;
        }
        if (o instanceof Statement) {
            Statement s = (Statement) o;
            if (id.equals(s.getId()))
                return true;
            if ((s.subject == subject && s.predicate == predicate && s.object == object))
                return true;
            if ((s.Subject().equals(Subject()) && s.Predicate().equals(Predicate())) && s.Object().equals(Object()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    public String toString() {
        return ""+id+"\t"+ subject + "->" + predicate + "->" + object+" ";
//        return getLabel();// StringFactory.edgeString(this);
    }

    public Node getReification() {
        if (reification == null) reification = new Node(graph,"reification of "+id);
        return reification;
    }

    public void setReification(Node reification) {
        this.reification = reification;
    }

    @Override
    public <T> T getProperty(String key) {
        return getReification().getProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return getReification().getPropertyKeys();
    }

    @Override
    public void setProperty(String key, Object value) {
        if(value==null)throw new IllegalArgumentException("EMPTY value not allowed as property");
        getReification().addEdge(key, new Node(graph, value));
    }

    @Override
    public <T> T removeProperty(String key) {
        return getReification().removeProperty(key);
    }

    @Override
    public void remove() {
        Netbase.deleteStatement((int)id);
    }

    @Override
    public Object getId() {
        if(id!=null)
        return id;
        else throw new RuntimeException("ID NULL");
    }

    public Statement setId(Object id) {// Maybe do something with it !!?!
        this.id = id;
        return this;
    }
}
