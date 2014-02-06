package com.pannous.netbase.blueprints;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 05/02/14
 * Time: 18:25
 */
public interface NetbaseGraph{//} extends Graph {
    int nextId();

    void setName(int id, String s);

    Node getThe(String s);

    String getName(int predicate);

    Node getNew(String hashmap);

    Node getAbstract(String key);

    Statement addStatement(int subject, int predicate, int object);

    void setKind(int id, int list);

    int getId(String key);

    Statement findStatement(int id, int keyId, int any, int i, boolean b, boolean b1, boolean b2, boolean b3);

    Node getNode(int id);

    int valueId(String s, double value, int integer);

    void deleteStatement(int id1);

    void showNode(int subject);

    Statement getStatement(Integer id);

    void deleteNode(int id);

    int statementCount();

    int nodeCount();

    Node[] query(String s) throws Exception;

    void save(int id, byte[] bytes);

    Value getValue(int id);

    byte[] getData(int id, int size);

    void renameAll(int id, String newName);
}
