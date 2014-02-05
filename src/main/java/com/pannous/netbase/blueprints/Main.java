package com.pannous.netbase.blueprints;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 04/02/14
 * Time: 12:20
 */
public class Main {
    public static void main(String[] args) {
        testRemoteGraph(args);
//        testLocalGraph(args);
    }

    private static void testRemoteGraph(String[] args) {
        NetbaseClient client = new NetbaseClient("http://de.netbase.pannous.com:81");
//        client.includeProperty("T-Online", "Url");
//        client.showView("T-Online");
        client.clearView("T-Online");
        client.excludeProperties("Registrierung");
        client.includeProperty("T-Online", "Eigentümer");
        client.includeProperty("T-Online", "Url");
        client.includeProperty("Unternehmen", "Sprache");
//        client.includeProperty("T-Online", "4668573");//  wiki image
//        client.clearView("T-Online");
        client.showView("T-Online");
        Debugger.info("-----------------------------");
        System.out.println("-----------------------------");
        try {
            Node[] nodes = client.query("T-Online");
            nodes[0].show();
        } catch (Exception e) {
            Debugger.error(e);
        }
    }


    private static void testLocalGraph(String[] args) {
//        Netbase netbase = new Netbase();
//        NetbaseGraph<Node> graph = new NetbaseGraph();
        Netbase.doExecute("help");
//        Netbase.doExecute("console");
        String command = "";
        for (String arg : args) command += arg + " ";
        Netbase.doExecute(command);
    }

}
