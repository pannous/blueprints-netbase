package com.tinkerpop.blueprints.impls.netbase;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 ** Copyright 2013 Pannous GmbH
 ** <p/>
 ** User: info@pannous.com / me
 ** Date: 21/01/14
 ** Time: 17:36
 **/
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

    public static native Pointer get(int i);
    public static native NodeStruct getAbstract(String name);
//    public static native Pointer getAbstract(String name);// just id!
//    public static native int getAbstract(String name);// just id!
//    public static native  String query2(String s, int limit);
    public static native Pointer execute(String s);
    public static native String getName(int id);

    public static native int statementCount();
    public static native int nodeCount();
    public static native int nextId();

    public static native void setLabel(Pointer pointer, String s);

    public static native boolean hasNode(String s);
    public static native NodeStruct getNode(int id);
    public static native void showNode(int id);
    public static native StatementStruct nextStatement(NodeStruct n, StatementStruct current);//,bool stopAtInstances=false);
    public static native NodeStruct has(NodeStruct n, NodeStruct m);
    public static native StatementStruct addStatement4(int contextId, int subjectId, int predicateId, int objectId, boolean checkNodes);
    // Swiss Army knife
    public static native StatementStruct findStatement(NodeStruct subject, NodeStruct predicate, NodeStruct object, int recurse, boolean semantic, boolean symmetric, boolean semanticPredicate, boolean matchName);
//    public static native int has(int n, int m);


    public static Node getThe(String name) {
        return new Node(getAbstract(name));
    }


//    public static native int addNode(String hi);


//    public static native  String query2(String s, int limit);
//    public static native String getLabel(int i);
//    public static native boolean setLabel(int id, String label);
//    public static native Pointer getStatement(int i);
}
