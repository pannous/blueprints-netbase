package com.pannous.netbase.jdbc;


import com.pannous.netbase.blueprints.Netbase;
import com.pannous.netbase.blueprints.Node;
import com.tinkerpop.blueprints.BaseTest;

import java.sql.*;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 24/01/14
 * Time: 11:18
 */
public class NetbaseSqlTest extends BaseTest {
    public void testSql() throws SQLException {
        NetbaseConnection connection = (NetbaseConnection) new NetbaseDriver().connect(null, null);
        NetbaseStatement statement = (NetbaseStatement) connection.createStatement();
        boolean execute = statement.execute("select * from opposite");
        if(!execute) throw new SQLException("NetbaseStatement execution ERROR");
//        PreparedStatement statement = connection.prepareStatement("select * from opposite");
//        boolean execute = statement.execute();
        NetbaseResultSet resultSet = (NetbaseResultSet) statement.getResultSet();
        resultSet.next();
        String subject = resultSet.getString("subject");
        resultSet.next();
        String name= resultSet.getString("name");
//        resultSet.getString("name");
    }
    public void testNetbaseQuery(){
        String sql="select * from dog";
//        String sql="select Oberklasse from dog";
        Node[] nodes = Netbase.doExecute(sql);
        assertTrue(nodes.length>2);
        assertTrue(nodes[0].name.contains("Gattung")||nodes[0].name.contains("dog")||nodes[0].name.contains("Dog"));
        Netbase.showNodes(nodes);
    }
    public void testQueryField(){
        String sql="select Klasse from dog where Doenges";
//        String sql="select Oberklasse from dog";
        Node[] nodes = Netbase.doExecute(sql);
        assertNotNull(nodes);
        assertTrue("nodes.length==1",nodes.length==1);
        String name = nodes[0].name;
        assertTrue(name.equals("Conant"));
        Netbase.showNodes(nodes);
    }
}
