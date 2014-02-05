package com.pannous.netbase.blueprints;


import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.ExceptionFactory;

import java.util.Set;
import static com.pannous.netbase.blueprints.LocalNetbase.*;

/**
 * @author Pannous (http://Pannous.com)
 */
public class Statement implements Edge { // extends Structure


    private Node reification;
    public int subject;
    public int predicate;
    public int object;

    protected Node _subject;
    protected Node _predicate;
    protected Node _object;
    private NetbaseGraph graph;
    public int id;

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
            this.subject = (Integer) subject.getId();
            this.predicate = (Integer) predicate.getId();
            this.object = (Integer) object.getId();
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
        id = LocalNetbase.getStatementId(statementStruct);// double, expensive
    }

    public Statement(NetbaseGraph graph, int subject, int predicate, int object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    @Override
    public Vertex getVertex(Direction direction) {
        if (direction == Direction.OUT) return Subject();
        if (direction == Direction.IN) return Object();
        else throw ExceptionFactory.bothIsNotSupported();// LOL tinkerpop --
    }

    @Override
    public String getLabel() {
//        return Subject().toString()+"->"+Predicate().toString()+"->"+Object();
        return Predicate().getName();// getName(predicate);
    }

    Node Subject() {
        if (_subject == null) _subject = get(subject);
        return _subject;
    }

    Node Predicate() {
        if (_predicate == null) _predicate = get(predicate);
        return _predicate;
    }

    public Node Object() {
        if (_object == null) _object = get(object);
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
            Object id1 = s.getId();
            if (id1!=null && id1.equals(id))
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
        if (reification == null) reification = getAbstract("reification of "+id);
        reification = get(valueId("reification of " + id, (double) id, Relation.reification));
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
        getReification().setProperty(key,value);
    }

    @Override
    public <T> T removeProperty(String key) {
        return getReification().removeProperty(key);
    }

    @Override
    public void remove() {
        LocalNetbase.deleteStatement((int) id);
    }

    @Override
    public Object getId() {
        return id;
    }

//    @Override
//    public Object getId() {
//        if(id!=null)
//        return id;
//        else throw new RuntimeException("ID NULL");
//    }
//
//    public Statement setId(Object id) {// Maybe do something with it !!?!
//        this.id = id;
//        return this;
//    }

    public String show() {
//        if (subjectName != null)
//            return id + "\t" + subjectName + "\t" + predicateName + "\t" + objectName + "\t" + subject + "\t" + predicate + "\t" + object;
//        else
            return id + "\t" + subject + "\t" + predicate + "\t" + object;
    }

    public int getObject() {
        return object;
    }
}
