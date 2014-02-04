package com.pannous.netbase.jdbc;

import com.pannous.netbase.blueprints.Node;
import com.pannous.netbase.blueprints.Debugger;

import javax.sql.rowset.RowSetMetaDataImpl;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 24/01/14
 * Time: 11:27
 */
public class NetbaseResultSet implements ResultSet {
    private final String sql;
    public final Node[] nodes;
    private int current = -1;
    private HashMap<Integer, String> map = new HashMap();
    private Statement statement;

    public NetbaseResultSet(String sql, Node[] nodes) {
        this.sql = sql;
        this.nodes = nodes;
    }


    private <T> T getProperty(int column, Class<T> type) {
        return nodes[current].getProperty(map.get(column));
    }
    private <T> T getProperty(int column) {
        return nodes[current].getProperty(map.get(column));
    }
    private <T> T getProperty(String column) {
        return nodes[current].getProperty(column);
    }
    private <T> T getProperty(String column, Class<T> type) {
        return nodes[current].getProperty(column);
    }

//    public NetbaseResultSet(String sql) {
//    }

    @Override
    public boolean next() throws SQLException {
        return !wasNull() && current < nodes.length;
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public boolean wasNull() throws SQLException {
        return nodes != null && nodes.length > 0;
    }

    @Override
    public String getString(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public boolean getBoolean(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public byte getByte(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public short getShort(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public int getInt(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public long getLong(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public float getFloat(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public double getDouble(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public BigDecimal getBigDecimal(int column, int scale) throws SQLException {
        return getProperty(column);
    }

    @Override
    public byte[] getBytes(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Date getDate(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Time getTime(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Timestamp getTimestamp(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public InputStream getAsciiStream(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public InputStream getUnicodeStream(int column) throws SQLException {
        return new StringBufferInputStream(getProperty(column).toString());
    }

    @Override
    public InputStream getBinaryStream(int column) throws SQLException {
        return new StringBufferInputStream(getProperty(column).toString());
    }

    @Override
    public String getString(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public boolean getBoolean(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public byte getByte(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public short getShort(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public int getInt(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public long getLong(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public float getFloat(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public double getDouble(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public BigDecimal getBigDecimal(String column, int scale) throws SQLException {
        return getProperty(column);
    }

    @Override
    public byte[] getBytes(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Date getDate(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Time getTime(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Timestamp getTimestamp(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public InputStream getAsciiStream(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public InputStream getUnicodeStream(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public InputStream getBinaryStream(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;//
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public String getCursorName() throws SQLException {
        return sql + "@@" + current;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        RowSetMetaDataImpl rowSetMetaData = new RowSetMetaDataImpl();
        rowSetMetaData.setColumnCount(nodes.length);
        return rowSetMetaData;
    }

    @Override
    public Object getObject(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Object getObject(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public int findColumn(String column) throws SQLException {
        for (Integer integer : map.keySet()) {
            if (map.get(integer).equals(column)) return integer;
        }
        throw new NoSuchFieldError(column + " missing in mapping for " + sql);
//        return -1;
    }

    @Override
    public Reader getCharacterStream(int column) throws SQLException {
        return new StringReader(getString(column));
    }

    @Override
    public Reader getCharacterStream(String column) throws SQLException {
        return new StringReader(getString(column));
    }

    @Override
    public BigDecimal getBigDecimal(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public BigDecimal getBigDecimal(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return current < 0;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return current >= nodes.length;
    }

    @Override
    public boolean isFirst() throws SQLException {
        return current == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
        return current == nodes.length - 1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        current = -1;
    }

    @Override
    public void afterLast() throws SQLException {
        current = nodes.length;
    }

    @Override
    public boolean first() throws SQLException {
        current = 0;
        return !wasNull();
    }

    @Override
    public boolean last() throws SQLException {
        current = nodes.length;
        return !wasNull();
    }

    @Override
    public int getRow() throws SQLException {
        return current;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        return true;
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        return false;
    }

    @Override
    public boolean previous() throws SQLException {
        current--;
        return !isBeforeFirst();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return ResultSet.FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
    }

    @Override
    public int getFetchSize() throws SQLException {
        return 1;
    }

    @Override
    public int getType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return true;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return true;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return true;
    }

    @Override
    public void updateNull(int column) throws SQLException {
        setProperty(column, null);
    }

    private <T> void setProperty(int column, T o) {
        setProperty(column, o);
    }

    private <T> void setProperty(String column, T o) {
        setProperty(column, o);
    }

    @Override
    public void updateBoolean(int column, boolean x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateByte(int column, byte x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateShort(int column, short x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateInt(int column, int x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateLong(int column, long x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateFloat(int column, float x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateDouble(int column, double x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateBigDecimal(int column, BigDecimal x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateString(int column, String x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateBytes(int column, byte[] x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateDate(int column, Date x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateTime(int column, Time x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateTimestamp(int column, Timestamp x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateAsciiStream(int column, InputStream x, int length) throws SQLException {
        setProperty(column, read(x, length));
    }

    private String read(InputStream x, long length) throws SQLException {
        try {
            return read(new InputStreamReader(x), length);
//            return new String(IOUtils.readFully(x, length, true));//    , "UTF-8")
        } catch (Exception e) {
            Debugger.error(e);
            throw new SQLException(e);
        }
    }

    private String read(Reader x, long length) throws SQLException {
        try {
            BufferedReader br = new BufferedReader(x);
            if(length>0){
            char[] buffer = new char[(int) length];// 4GB max lol
            StringBuilder out = new StringBuilder();
            for(;;) {
                int rsz = x.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();}
            String read = br.readLine();
            StringBuffer sb=new StringBuffer();
            while(read != null) {
                sb.append(read);
                read =br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            Debugger.error(e);
            throw new SQLException(e);
        } finally {
            try {
                x.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void updateBinaryStream(int column, InputStream x, int length) throws SQLException {
        setProperty(column, read(x, length));
    }

    @Override
    public void updateCharacterStream(int column, Reader x, int length) throws SQLException {
        setProperty(column, read(x, length));
    }


    @Override
    public void updateObject(int column, Object x, int length) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateObject(int column, Object x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateNull(String column) throws SQLException {
        setProperty(column,null);
    }

    @Override
    public void updateBoolean(String column, boolean x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateByte(String column, byte x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateShort(String column, short x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateInt(String column, int x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateLong(String column, long x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateFloat(String column, float x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateDouble(String column, double x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateBigDecimal(String column, BigDecimal x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateString(String column, String x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateBytes(String column, byte[] x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateDate(String column, Date x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateTime(String column, Time x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateTimestamp(String column, Timestamp x) throws SQLException {
        setProperty(column,x);
    }

    @Override
    public void updateAsciiStream(String column, InputStream x, int length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateBinaryStream(String column, InputStream x, int length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateCharacterStream(String column, Reader x, int length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateObject(String column, Object x, int length) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void updateObject(String column, Object x) throws SQLException {
        setProperty(column, x);
    }

    @Override
    public void insertRow() throws SQLException {

    }

    @Override
    public void updateRow() throws SQLException {

    }

    @Override
    public void deleteRow() throws SQLException {
        nodes[current].remove();
        // keep??
    }

    @Override
    public void refreshRow() throws SQLException {
        nodes[current]=nodes[current].load();
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException("cancelRowUpdates Not possible because of missing transaction functionality");
    }

    @Override
    public void moveToInsertRow() throws SQLException {

    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        if(current<0)current=0;
    }

    @Override
    public Statement getStatement() throws SQLException {
        return statement;
    }

    @Override
    public Object getObject(int column, Map<String, Class<?>> map) throws SQLException {
        return nodes[column]; // todo
    }

    @Override
    public Ref getRef(int column) throws SQLException {
        return null;// new  nodes[column]; // todo
    }

    @Override
    public Blob getBlob(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Clob getClob(int column) throws SQLException {
        return getProperty(column);// ,new Class<Clob>() use
    }

    @Override
    public Array getArray(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Object getObject(String column, Map<String, Class<?>> map) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Ref getRef(String column) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Clob getClob(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Array getArray(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Date getDate(int column, Calendar cal) throws SQLException {
        java.util.Date date = (java.util.Date) getProperty(column);
        cal.setTime(date);
        return new Date(cal.getTimeInMillis());
    }

    @Override
    public Date getDate(String column, Calendar cal) throws SQLException {
        java.util.Date date = (java.util.Date) getProperty(column);
        cal.setTime(date);
        return new Date(cal.getTimeInMillis());
    }

    @Override
    public Time getTime(int column, Calendar cal) throws SQLException {
        java.util.Date date = (java.util.Date) getProperty(column);
        cal.setTime(date);return new Time(cal.getTimeInMillis());
    }

    @Override
    public Time getTime(String column, Calendar cal) throws SQLException {
        java.util.Date date = (java.util.Date) getProperty(column);
        cal.setTime(date);
        return new Time(cal.getTimeInMillis());
    }

    @Override
    public Timestamp getTimestamp(int column, Calendar cal) throws SQLException {
        java.util.Date date = (java.util.Date) getProperty(column);
        cal.setTime(date);return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    public Timestamp getTimestamp(String column, Calendar cal) throws SQLException {
        java.util.Date date = (java.util.Date) getProperty(column);
        cal.setTime(date);return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    public URL getURL(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public URL getURL(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public void updateRef(int column, Ref x) throws SQLException {
        setProperty(column, x.getObject());
    }

    @Override
    public void updateRef(String column, Ref x) throws SQLException {
        setProperty(column, x.getObject());
    }

    @Override
    public void updateBlob(int column, Blob x) throws SQLException {
        setProperty(column,read(x.getBinaryStream(),x.length()));
    }

    @Override
    public void updateBlob(String column, Blob x) throws SQLException {
        setProperty(column,read(x.getBinaryStream(),x.length()));
    }

    @Override
    public void updateClob(int column, Clob x) throws SQLException {
        setProperty(column,read(x.getCharacterStream(),x.length()));
    }

    @Override
    public void updateClob(String column, Clob x) throws SQLException {
        setProperty(column,read(x.getCharacterStream(),x.length()));
    }

    @Override
    public void updateArray(int column, Array x) throws SQLException {
                                            setProperty(column,x);
    }

    @Override
    public void updateArray(String column, Array x) throws SQLException {

    }

    @Override
    public RowId getRowId(final int column) throws SQLException {
        return new RowId() {
            @Override
            public byte[] getBytes() {
                return map.get(column).getBytes();
            }
        };
    }

    @Override
    public RowId getRowId(final String column) throws SQLException {
        return new RowId() {
            @Override
            public byte[] getBytes() {
                return column.getBytes();
            }
        };
    }

    @Override
    public void updateRowId(int column, RowId x) throws SQLException {

    }

    @Override
    public void updateRowId(String column, RowId x) throws SQLException {

    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void updateNString(int column, String nString) throws SQLException {
        setProperty(column,nString);

    }

    @Override
    public void updateNString(String column, String nString) throws SQLException {
        setProperty(column,nString);
    }

    @Override
    public void updateNClob(int column, NClob x) throws SQLException {
        setProperty(column,read(x.getCharacterStream(),-1));
    }

    @Override
    public void updateNClob(String column, NClob x) throws SQLException {
        setProperty(column,read(x.getCharacterStream(),-1));
    }

    @Override
    public NClob getNClob(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public NClob getNClob(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public SQLXML getSQLXML(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public SQLXML getSQLXML(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public void updateSQLXML(int column, SQLXML xmlObject) throws SQLException {
        setProperty(column, xmlObject);
    }

    @Override
    public void updateSQLXML(String column, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("updateSQLXML Coming soon!!");
    }

    @Override
    public String getNString(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public String getNString(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Reader getNCharacterStream(int column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public Reader getNCharacterStream(String column) throws SQLException {
        return getProperty(column);
    }

    @Override
    public void updateNCharacterStream(int column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateNCharacterStream(String column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateAsciiStream(int column, InputStream x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateBinaryStream(int column, InputStream x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateCharacterStream(int column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateAsciiStream(String column, InputStream x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateBinaryStream(String column, InputStream x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateCharacterStream(String column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateBlob(int column, InputStream x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateBlob(String column, InputStream x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateClob(int column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateClob(String column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateNClob(int column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateNClob(String column, Reader x, long length) throws SQLException {
        setProperty(column,read(x,length));
    }

    @Override
    public void updateNCharacterStream(int column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateNCharacterStream(String column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateAsciiStream(int column, InputStream x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateBinaryStream(int column, InputStream x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateCharacterStream(int column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateAsciiStream(String column, InputStream x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateBinaryStream(String column, InputStream x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateCharacterStream(String column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateBlob(int column, InputStream x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateBlob(String column, InputStream x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateClob(int column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateClob(String column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateNClob(int column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public void updateNClob(String column, Reader x) throws SQLException {
        setProperty(column,read(x,-1));
    }

    @Override
    public <T> T getObject(int column, Class<T> type) throws SQLException {
        return getProperty(column,type);
    }

    @Override
    public <T> T getObject(String column, Class<T> type) throws SQLException {
        return getProperty(column,type);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return (T) this;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
