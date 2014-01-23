package com.tinkerpop.blueprints.impls.netbase;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * * Copyright 2013 Pannous GmbH
 * * <p/>
 * * User: info@pannous.com / me
 * * Date: 21/01/14
 * * Time: 17:36
 */
public class Netbase {
    static {
        Native.setProtected(true);
//        Native.setPreserveLastError(true);
//        Native.setPreserveLastError(false);// throw LastErrorException!
        Native.register("Netbase");// libNetbase.so | libNetbase.dylib
        Netbase.init();
//        Native.getLastError()  LastErrorException
//        NativeLibrary.getGlobalVariableAddress(java.lang.String symbolName) !
    }

    //    public static native int atol(String s); OK!!
    public static native int test2();

    public static native void init();

    public static Node get(int id){
//        if(!testing&& id<1000) return Relation.byId(id);
//        int i = getNodeI(id).getInt(0);
        return getNode(id);
//        return getNodeS(id);
//        return getNode(id).load();
    }

//        public static native Node getAbstract(String name);
    public static native Node getAbstract(String name);
    public static native Node getThe(String name);
//    public static native Node getNew(String name,Node kind);
    public static native Node getNew(String name);// C Default parameters do NOT work with Jna !!!
//    public static Node getNew(String name) {
//        return getNew(name, Relation.Object);
//    }
//    public static native Pointer getAbstract(String name);

    //    public static native Pointer getAbstract(String name);// just id!
//    public static native int getAbstract(String name);// just id!
//    public static native  String query2(String s, int limit);
    public static native Pointer execute(String s);

    public static native String getName(int id);

//    public static native long setLabel(Node node, String label);
    public static native void setName(int node, String label);

    public static native int statementCount();

    public static native int nodeCount();

    public static native int nextId();


    public static native boolean hasNode(String property);

    public static native Node getNode(int id);
//    public static Node getNode(int id) {
//        return getNodeS(id);
//        return new Node(id);
//    };
    public static native int getId(String name);
//    public static native Pointer getNodeP(int id);

//    public static native NodeStruct getNodeS(int id);
//    public static native Node getNodeS(int id);

    public static native void save(Node node);
//    public static native void save(NodeStruct node);

    //    public static Node getNode(int i) {
//        return new Node(Netbase.getNode(i));
//    }
    public static native void showNode(int id);

//    public static native StatementStruct nextStatement(Node n, StatementStruct current);//,bool stopAtInstances=false);
    public static native StatementStruct nextStatement(int node,StatementStruct current);//,bool stopAtInstances=false);

//    public static native Node has(Node n, Node m);

    public static native StatementStruct addStatement4(int contextId, int subjectId, int predicateId, int objectId, boolean checkNodes);

    // Swiss Army knife
//    public static native StatementStruct findStatement(Node subject, Node predicate, Node object, int recurse, boolean semantic, boolean symmetric, boolean semanticPredicate, boolean matchName);
    public static native StatementStruct findStatement(int subject, int predicate, int object, int recurse, boolean semantic, boolean symmetric, boolean semanticPredicate, boolean matchName);
//    public static native int has(int n, int m);

    public static native void deleteNode(int id);

    public static native void deleteStatement(int id);

//    public static native NodeStruct value(String name, double v, Node unit);
    public static native int valueId(String name, double v, int unit);


//    public static Node getThe(String value) {
//        return getAbstract("" + value);
////       return get(getAbstract("" + value).id);
//    }

//    public static <T> T getValue(int nodeId) {
////        if(Double.class.isAssignableFrom(T.class))return 0;
//        try {
//            return (T) getNode(nodeId).value.getTypedValue(Double.class);
//        } catch (Exception x){}
//        try {
//            return (T) getNode(nodeId).value.getTypedValue(String.class);
//        } catch (Exception x){}
//        try {
//            return (T) getNode(nodeId).value.getTypedValue(Node.class);
//        } catch (Exception x){}
//        try {
//            return (T) getNode(nodeId).value.getTypedValue(StatementStruct.class);
//        } catch (Exception x){}
//        throw new RuntimeException("Unknown value type "+nodeId+" " +getNode(nodeId).value);
//    }

    public static native StatementStruct getStatement(int id);

    public static native int getStatementId(StatementStruct struct);/// ugly long pointer

//    public static native Node add(String key, int kind,int context);
    public static native void setKind(int id,int kind);

    public static native void showStatement(int id);

    public static native Node add(String s,int kind);
//    public static native void showStatement(Statement s);

//    public static native int addNode(String hi);


//    public static native  String query2(String s, int limit);
//    public static native String getLabel(int i);
//    public static native boolean setLabel(int id, String label);
//    public static native Pointer getStatement(int i);
}
