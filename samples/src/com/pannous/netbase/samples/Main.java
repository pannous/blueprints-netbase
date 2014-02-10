package com.pannous.netbase.samples;

import com.pannous.netbase.blueprints.*;
import com.pannous.netbase.blueprints.remote.RemoteNetbaseGraph;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

import java.util.logging.Logger;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 04/02/14
 * Time: 12:20
 */
public class Main {

    private static final Logger logger = Logger.getLogger(NetbaseGraph.class.getName());

    public static void main(String[] args) {
        try {
            remoteGraphSample(args);
//            remoteGraphSample2(args);
        } catch (Exception e) {
            Debugger.error(e);
        }
        try {
            // also see NetbaseCoreTest
//            localGraphSample(args);
//            localGraphSampleTinkerPop();
        } catch (Exception e) {
            Debugger.error(e);
        }
    }


    private static void localGraphSample(String[] args) {
        // passing most of tinkerpop specific standards
        // todo : their query builder vs netbase query language

        LocalNetbaseGraph graph = new LocalNetbaseGraph();
        graph.query("help");

//        graph.clear();

//        logger.info(System.getProperty("user.dir"));
        if(graph.nodeCount()<10000)
            graph.execute("import ./import/cities1000.txt");


        Node gehren = graph.getThe("Gehren");// cities1000
        Node city = gehren.getType();
        city.show();
        city.renameAll("city");
        gehren.show();

        int population = gehren.getProperty("population");
        logger.info("population in Gehren: "+population);

        graph.execute("quiet!");
//        Node node = graph.queryNode("city where population=3703");
        Node node = graph.queryNode("all city with latitude=50.65");
        logger.info("latitude is indeed: " + node.getProperty("latitude"));

//        Node node = graph.queryNode("Population of Gehren");
//        Node node = graph.queryNode("all city with countrycode=AD");// limit 10
//        logger.info("A city in Zimbabwe: "+node.toString());
    }

    private static void localGraphSampleTinkerPop() {
        // now with compatible super classes:
        Graph graph = new LocalNetbaseGraph();
        Vertex gehren = graph.getVertex("Gehren");
        Integer population = gehren.getProperty("population");
        logger.info("population in Gehren via TinkerPop api: "+population);
    }


    private static void remoteGraphSample(String[] args) throws Exception {
        // remote works ok too, but not passing tinkerpop specific standard yet
//        RemoteNetbaseGraph client = new RemoteNetbaseGraph("http://de.netbase.pannous.com:81");
        RemoteNetbaseGraph client = new RemoteNetbaseGraph("http://localhost:81");// local TCP instead of JNA connection
        client.clearView("T-Online");
        client.showView("T-Online");
        client.excludeProperties("Registrierung");
        client.includeProperty("T-Online", "Eigentümer");
//        client.includeProperty("T-Online", "Url");
        client.learn("T-Online Typ Firma");
        client.includeProperty("Firma", "Url");
        client.includeProperty("Firma", "Sprachen");
//        client.learn("T-Online Typ Unternehmen");
//        client.includeProperty("Unternehmen", "Url");
//        client.includeProperty("Unternehmen", "Sprachen");
//        client.includeProperty("T-Online", "4668573");//  wiki image
//        client.showAll("T-Online");
//        client.showNode(164861);
//        client.showAll("Unternehmen");
        client.showView("T-Online");

//        client.showView("Unternehmen");
        try {
            Node node = client.queryNode("T-Online");//Filtered with view
//            Node node = client.fetchNode("T-Online");// Complete
//            node.removeProperty("Typ");
            node.show();
            Node type = node.getType();
            if(type!=null){
            type.load();
//            type.show();
            }
        } catch (Exception e) {
            Debugger.error(e);
        }
    }
    private static void remoteGraphSample2(String[] args) throws Exception {
        // remote works ok too, but not passing tinkerpop specific standard yet
        RemoteNetbaseGraph client = new RemoteNetbaseGraph("http://de.netbase.pannous.com:81");
//        client.excludeProperties("Registrierung");
        client.includeProperty("Gehren", "Breitengrad");
        client.includeProperty("City", "Längengrad");
        client.includeProperty("City", "Bevölkerung");
//        client.includeProperty("T-Online", "4668573");//  wiki image
//        client.clearView("T-Online");
        client.showView("Gehren");
        try {
            Node[] nodes = client.query("Gehren");
            nodes[0].show();
        } catch (Exception e) {
            Debugger.error(e);
        }
    }



}
