package com.pannous.netbase.blueprints;

import com.pannous.util.Internet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 05/02/14
 * Time: 09:45
 */
public class NetbaseClient {
    private final String host;
    private boolean local = true;
    private final HashMap<Integer, Node> cache;
    private final HashMap<Integer, Statement> statementCache;

    public NetbaseClient(String host) {
        this.host = host;
        cache = new HashMap<Integer, Node>();
        statementCache = new HashMap<Integer, Statement>();
        local = (host == null || host.contains("localhost") || host.contains(".0.0."));
//        Properties info;
//        new NetbaseConnection(host, info);
    }

    public Node[] query(String query) throws Exception {
        query.replaceAll("^/", "");
        if (local) Netbase.doExecute(query);
        String json = Internet.download(host + "/json/verbose/" + query);
//        String json = Internet.download(host + "/json/all/" + query);// Ignore filters
        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray("results");// xml.getJSONArray("entity");
        Node[] nodes = new Node[results.length()];
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            Node node = loadNode(result);
            nodes[i] = node;
        }
        return nodes;
    }

    public void execute(String query) {
        query.replaceAll("^/", "");
        if (local) Netbase.doExecute(query);
        try {
            String json = Internet.download(host + "/json/verbose/" + query);
        } catch (Exception e) {
            Debugger.error(e);
        }
    }

    private Node loadNode(JSONObject result) throws JSONException {
        RemoteNode node = (RemoteNode) getNode(result.getInt("id"), result.getString("name"));
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

    private Statement loadStatement(JSONObject statement) throws JSONException {
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

    public Node getNode(int id, String name) {
        if (local) return Netbase.getNode(id).setName(name);
        if (cache.containsKey(id)) return cache.get(id).setName(name);
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

}
