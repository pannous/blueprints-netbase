package com.tinkerpop.blueprints.impls.netbase;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 21/01/14
 * Time: 17:16
 */
public class Debugger {
    public static final Logger logger = Logger.getLogger("Netbase");// 'global'
    public static void info(Object whattt) {
        logger.setLevel(Level.ALL);
        logger.info(""+whattt);
    }

    public static void error(Exception e) {
        logger.log(Level.SEVERE,e.getMessage(),e);
    }
    public static void warn(Exception e) {
        logger.log(Level.WARNING,e.getMessage(),e);
    }

    public static void trace(String s) {
        logger.info(s);
    }
}
