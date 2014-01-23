package com.tinkerpop.blueprints.impls.netbase;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 21/01/14
 * Time: 18:20
 */
public class NodeStructure extends Structure { // faster than NodeStruct
// twall.github.io/jna/4.0/javadoc/overview-summary.html#overview_description
    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[]{"id", "name"});//, "kind", "statementCount", "firstStatement", "lastStatement","value"});// ,"value"
    }

    public int id;
    public String name;
//    public int kind;
//    public int statementCount; //implicit, can be replaced with iterator
//    public int firstStatement;
//    public int lastStatement;// remove
//    public Value value; // for statements, numbers  WASTE!!! remove

//    Union.setType(java.lang.Class)

//    public volatile int refCount;
//    NodeStruct sub!
//    NodeStruct.ByReference haarrr!
    /*
 typedef struct Node {
//    class Node{
//    public:
 int id; //implicit -> redundant
 char* name; // see value for float etc
 int kind; // abstract,node,person,year, m^2     // via first slot? nah
 //int context; //implicit   | short context_id or int node_id
 //float rank;
 int statementCount; //implicit, can be replaced with iterator
 int firstStatement;
 int lastStatement;// remove
 Value value; // for statements, numbers  WASTE!!! remove

 // INDEX
 // Node* index;//nur #properties+1 Nits!!
 // class country{ population{property:0} capital{property:1} }
 // germany.index[0]=80Mio .index[1]=Berlin

}Node ;
  */

}
