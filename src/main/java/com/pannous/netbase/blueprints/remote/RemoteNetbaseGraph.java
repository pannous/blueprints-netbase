package com.pannous.netbase.blueprints.remote;

import com.pannous.netbase.blueprints.*;
import com.pannous.netbase.blueprints.remote.AllRemoteEdges;
import com.pannous.netbase.blueprints.remote.AllRemoteVertices;
import com.pannous.util.Internet;
import com.tinkerpop.blueprints.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 05/02/14
 * Time: 09:45
 */
public class RemoteNetbaseGraph implements Graph ,NetbaseGraph {
    private final String host;
    private boolean local = true;
    private final HashMap<Integer, RemoteNode> cache = new HashMap<Integer, RemoteNode>();
    private final HashMap<Integer, RemoteStatement> statementCache = new HashMap<Integer, RemoteStatement>();
    public int nodeCount;
    public int statementCount;

    public RemoteNetbaseGraph(String host) throws Exception {
        if(!host.startsWith("http")) host = "http://" + host;
        this.host = host;
        local = (host == null || host.contains("localhost") || host.contains(".0.0."));
        nodeCount = nodeCount();
        statementCount = statementCount();
//        Properties info;
//        new NetbaseConnection(host, info);
    }


    public RemoteNode getNode(String name) {
        try {
            Node[] query = query("/short/" + name);
            if(query.length==0) return getNode(-1, name);
            return (RemoteNode) query[0];
        } catch (Exception e) {
            Debugger.error(e);
        }
        return getNode(-1, name);
    }

    public Node[] query(String query) throws Exception {
        if(query.startsWith("/")) query = query.substring(1);
//        if (local) LocalNetbase.doExecute(query);
        String json = Internet.download(host + "/json/verbose/" + query);
//        String json = Internet.download(host + "/json/all/" + query);// Ignore filters
        JSONObject root = null;
        try {
            root = new JSONObject(json);
        } catch (JSONException e) {
            Debugger.trace("ERROR in JSON >>"+json+"<<");
            throw e;
        }
        JSONArray results = root.getJSONArray("results");// xml.getJSONArray("entity");
        RemoteNode[] nodes = new RemoteNode[results.length()];
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            RemoteNode node = loadNode(result);
            nodes[i] = node;
        }
        return nodes;
    }

    @Override
    public void save(int id, byte[] bytes) {

    }

    @Override
    public Value getValue(int id) {
        return null;
    }

    @Override
    public byte[] getData(int id, int size) {
        return new byte[0];
    }

    @Override
    public void renameAll(int id, String newName) {
        execute("label "+id+" "+newName);
    }

    public void execute(String query) {
        query.replaceAll("^/", "");
//        if (local) LocalNetbase.doExecute(query);
        try {
            String json = Internet.download(host + "/json/verbose/" + query);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    private RemoteNode loadNode(JSONObject result) throws JSONException {
        RemoteNode node = (RemoteNode) getNode(result.getInt("id"), result.getString("name"));
        if(result.has("kind"))
            node.kind = result.getInt("kind");
        if (result.getInt("statementCount") > 0 && !result.has("statements"))
            return node;
        JSONArray statements = result.getJSONArray("statements");
        for (int j = 0; j < statements.length(); j++) {
            JSONObject statement = statements.getJSONObject(j);
            node.statements.add(loadStatement(statement));
        }
        node.loaded = true;
        return node;
    }

    private RemoteStatement loadStatement(JSONObject statement) throws JSONException {
        int id = statement.getInt("id");
//        if(statementCache.containsKey(id))return statementCache.get(id); // don't cache!
        RemoteStatement remoteStatement = new RemoteStatement(id, this);
        remoteStatement.subject = statement.getInt("sid");
        remoteStatement.predicate = statement.getInt("pid");
        remoteStatement.object = statement.getInt("oid");
        remoteStatement.subjectName = statement.getString("subject");
        remoteStatement.predicateName = statement.getString("predicate");
        remoteStatement.objectName = statement.getString("object");
//        statementCache.put(id, remoteStatement);
        return remoteStatement;
    }

    public RemoteNode getNode(int id, String name) {
//        if (local) return LocalNetbase.getNode(id).setName(name);
        if (cache.containsKey(id)) return (RemoteNode) cache.get(id).setName(name);
        if(id==-1)id= nodeCount++;
        RemoteNode remoteNode = new RemoteNode(id, name, this);
        cache.put(id, remoteNode);
        return remoteNode;
    }

    public String getName(int id) {
        try {
            return query("/short/" + id)[0].name;
        } catch (Exception e) {
            Debugger.error(e);
            return null;
        }
    }

    @Override
    public Node getNew(String name) {
        return new RemoteNode(nodeCount++, name, this);
    }

    @Override
    public Node getAbstract(String key) {
        return queryNode("a " + key);
    }

    public RemoteNode queryNode(String s) {
        try {
            return (RemoteNode) query(s)[0];
        } catch (Exception e) {
            return new RemoteNode(-1, s, this);
        }
    }

    @Override
    public Statement addStatement(int subject, int predicate, int object) {
        execute("learn "+subject+" "+predicate+" "+object);
        try {
            return getStatement(statementCount() - 1);
        } catch (Exception e) {
            return new Statement(this, subject, predicate, object);
        }
    }

    @Override
    public void setKind(int id, int kind) {
        execute(id+".kind="+kind);
    }

    @Override
    public int getId(String key) {
        return getNode(key).id;
    }

    @Override
    public Statement findStatement(int id, int keyId, int any, int i, boolean b, boolean b1, boolean b2, boolean b3) {
        return null;
    }

    @Override
    public Node getNode(int id) {
        return getNode(id,null);
    }

    @Override
    public int valueId(String s, double value, int integer) {
        return new RemoteNode(nodeCount++,s,this).id;// TOODOO!!
    }

    @Override
    public void deleteStatement(int id1) {
        execute("delete $" + id1);
    }

    @Override
    public void showNode(int subject) {
//        get(subject).show();
    }

    @Override
    public Statement getStatement(Integer id) {
        return null;
//        return new NotImplementedException();
//        return queryStatement(id);
    }



    public void deleteNode(int current) {
        execute("delete " + current);
//        get(current).remove();
    }

    public void excludeProperties(String nodeOrRelation) {
        try {
            execute("exclude " + nodeOrRelation);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    public void excludeProperty(String node, String property) {
        try {
            execute("exclude " + node + " " + property);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    public void includeProperty(String node, String property) {
        execute("include " + node + " " + property);
    }

    public void includeProperties(String node) {
        try {
            execute("include " + node);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }


    public void showAll(String name) {
        try {
            Node[] nodes = query("all/" + name);
            for (Node excluded : nodes)
                excluded.show();
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    public void showView(String node) {
        try {
            Node[] excludeds = query("showview/" + node);
            for (Node excluded : excludeds)
                excluded.show();
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    public void clearView(String node) {
        try {
            Node[] excludeds = query("excluded/" + node);
            for (Node excluded : excludeds) {
                for (Statement s : excluded.getStatements()) {
                    s.show();
                    s.remove();
                }
            }
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    @Override
    public Features getFeatures() {
        return null;
    }

    @Override
    public Vertex addVertex(Object o) {
        return null;
    }

    @Override
    public Vertex getVertex(Object o) {
        try {
//            if(o instanceof Integer)
            return getNode(Integer.parseInt(o.toString()), null);
        } catch (NumberFormatException e) {
            return getNode(-1, o.toString());
        }
    }

    @Override
    public void removeVertex(Vertex vertex) {
        Node node = getNode((Integer) vertex.getId(),null);// edge.getLabel());
        node.remove();
    }

    @Override
    public Iterable<Vertex> getVertices() {
        return new AllRemoteVertices(this);
    }

    @Override
    public Iterable<Vertex> getVertices(String s, Object o) {
        try {
            return new StatementIterable(queryNode(s + "." + o).statements);
        } catch (Exception e) {
            Debugger.error(e);
            return null;
        }
    }

    @Override
    public Edge addEdge(Object o, Vertex vertex, Vertex vertex2, String s) {
        return null;
    }

    @Override
    public Edge getEdge(Object o) {
        Statement statement = getStatement((Integer) o);
        return statement;
    }

    @Override
    public void removeEdge(Edge edge) {
        Statement statement = getStatement((Integer) edge.getId());
        statement.remove();
    }

    private Statement getStatement(int id) {
        try {
            return queryNode("$" + id).statements.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Iterable<Edge> getEdges() {
        return new AllRemoteEdges(this);
    }

    @Override
    public Iterable<Edge> getEdges(String s, Object o) {
        try {
            return new StatementIterable(queryNode(s + "." + o).statements);
        } catch (Exception e) {
            Debugger.error(e);
            return null;
        }
    }

    @Override
    public GraphQuery query() {
        return new NetbaseGraphQuery(this);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public int nextId() {
        return 0;
    }

    @Override
    public void setName(int id, String s) {

    }

    public RemoteNode getThe(String name) {
        return getNode("the " + name);
    }

    public int statementCount()  {
        try {
            return Integer.parseInt(Internet.download(host+"/statement count"));
        } catch (Exception e) {
            return statementCount;
        }
    }

    public int nodeCount()  {
        try {
            return Integer.parseInt(Internet.download(host+"/node count"));
        } catch (Exception e) {
            return nodeCount;
        }
    }

    public Node get(int current) {
        return getNode(current, null);
    }

    public void learn(String statement) {
        execute("learn "+statement);
    }

}
