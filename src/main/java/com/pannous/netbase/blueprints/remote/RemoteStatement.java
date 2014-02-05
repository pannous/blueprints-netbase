package com.pannous.netbase.blueprints.remote;


import com.pannous.netbase.blueprints.Debugger;
import com.pannous.netbase.blueprints.Node;
import com.pannous.netbase.blueprints.Statement;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 05/02/14
 * Time: 11:34
 */
public class RemoteStatement extends Statement {
    private final RemoteNetbaseGraph netbaseClient;
    String subjectName;
    String predicateName;
    String objectName;

    public RemoteStatement(int id, RemoteNetbaseGraph netbaseClient) {
        super();
//        super.setAutoRead(false);
//        super.setAutoWrite(false);
//        super.setAutoSynch(false);
        this.id = id;
        this.netbaseClient = netbaseClient;
    }

    private Node get(int subject, String name) {
        return netbaseClient.getNode(subject, name);
    }

    Node Subject() {
        if (_subject == null) _subject = get(subject, subjectName);
        return _subject;
    }

    Node Predicate() {
        if (_predicate == null) _predicate = get(predicate, predicateName);
        return _predicate;
    }

    public Node Object() {
        if (_object == null) _object = get(object, objectName);
        return _object;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public void remove() {
        try {
            netbaseClient.query("delete $" + id);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    public String show() {
        String x;
        if (subjectName != null)
            x= id + "\t" + subjectName + "\t" + predicateName + "\t" + objectName + "\t" + subject + "->" + predicate + "->" + object;
        else
            x= id + "\t" + subject + "\t" + predicate + "\t" + object;
        return x;
    }
}
