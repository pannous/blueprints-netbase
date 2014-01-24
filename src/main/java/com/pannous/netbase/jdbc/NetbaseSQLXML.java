package com.pannous.netbase.jdbc;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 24/01/14
 * Time: 11:08
 */
public class NetbaseSQLXML implements SQLXML {
    @Override
    public void free() throws SQLException {

    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        return null;
    }

    @Override
    public OutputStream setBinaryStream() throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        return null;
    }

    @Override
    public Writer setCharacterStream() throws SQLException {
        return null;
    }

    @Override
    public String getString() throws SQLException {
        return null;
    }

    @Override
    public void setString(String value) throws SQLException {

    }

    @Override
    public <T extends Source> T getSource(Class<T> sourceClass) throws SQLException {
        return null;
    }

    @Override
    public <T extends Result> T setResult(Class<T> resultClass) throws SQLException {
        return null;
    }
}
