package com.ticketadvantage.services.db;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jmiller
 *
 */
public class LOB implements AutoCloseable {
    private final Connection connection;
    private final List<Blob> blobs = new ArrayList<>();
    private final List<Clob> clobs = new ArrayList<>();
 
    /**
     * 
     * @param connection
     */
    public LOB(Connection connection) {
        this.connection = connection;
    }
 
    /**
     * 
     * @param bytes
     * @return
     * @throws SQLException
     */
    public final Blob blob(byte[] bytes) 
    throws SQLException {
        final Blob blob = connection.createBlob();
        blob.setBytes(1, bytes);
        blobs.add(blob);
        return blob;
    }
 
    /**
     * 
     * @param string
     * @return
     * @throws SQLException
     */
    public final Clob clob(String string) throws SQLException {
        final Clob clob = connection.createClob();
        clob.setString(1, string);
        clobs.add(clob);
        return clob;
    }
 
    /*
     * (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public final void close() throws Exception {
	    	for (int x = 0; x < blobs.size(); x++) {
	    		Blob blob = blobs.get(x);
	    		blob.free();
	    	}
	    	for (int x = 0; x < clobs.size(); x++) {
	    		Clob clob = clobs.get(x);
	    		clob.free();
	    	}
    }
}