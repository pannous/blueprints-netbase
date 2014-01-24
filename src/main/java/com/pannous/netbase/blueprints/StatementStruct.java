package com.pannous.netbase.blueprints;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 21/01/14
 * Time: 22:29
 */
public class StatementStruct extends Structure {
    //public int id; // implicit
    public int context; //#ifdef useContext

    public int subject; // implicit!! Subject
    public int predicate;
    public int object;

    public int nextSubjectStatement;
    public int nextPredicateStatement;
    public int nextObjectStatement;

    @Override
    protected List getFieldOrder() {
//        return Arrays.asList(new String[]{"context", "nextSubjectStatement", "nextPredicateStatement", "nextObjectStatement", "subject", "predicate", "object"}); // Changed ORDER!!
        return Arrays.asList(new String[]{"context", "subject", "predicate", "object","nextSubjectStatement", "nextPredicateStatement", "nextObjectStatement"});
    }

    public Node getObject() {
        return Netbase.getNode(object);
    }

    public int getId() {
        return Netbase.getStatementId(this);// ugly
    }

    public void show() {
        Netbase.showStatement(getId());
    }
}
