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
        Netbase netbase = new Netbase();
        NetbaseGraph<Node> graph = new NetbaseGraph<>();
        Netbase.doExecute("help");
        Netbase.doExecute("console");
        String command = "";
        for (String arg : args) command += arg + " ";
        Netbase.doExecute(command);
    }
}
