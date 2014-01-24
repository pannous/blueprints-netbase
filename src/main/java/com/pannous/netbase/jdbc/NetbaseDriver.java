package com.pannous.netbase.jdbc;


import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 24/01/14
 * Time: 11:04
 */
public class NetbaseDriver implements Driver {
    Logger logger = Logger.getLogger("Netbase");

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return new NetbaseConnection(url,info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return true;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return logger;
    }
}
