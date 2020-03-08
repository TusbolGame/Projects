package com.ticketadvantage.services.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.ticketadvantage.services.util.ServerInfo;

public class DataSourceNoAutoManager {
	private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
//	private static final String DB_URL = "jdbc:postgresql://localhost:38293/tadatabase";
	private static final String DB_URL = "jdbc:postgresql://" + ServerInfo.getHost() + ":" + ServerInfo.getDbPort() + "/tadatabase";
    private static final String DB_USER = "scrapperuser";
    private static final String DB_PASSWORD = "3id39d";
    private static final int CONN_POOL_SIZE = 20;
    private BasicDataSource bds = new BasicDataSource();

    
    /**
     * 
     */
	private DataSourceNoAutoManager() {
		super();
        //Set database driver name
        bds.setDriverClassName(DRIVER_CLASS_NAME);
        bds.setUrl(DB_URL);
        bds.setUsername(DB_USER);
        bds.setPassword(DB_PASSWORD);
        bds.setInitialSize(CONN_POOL_SIZE);
	}

	/**
	 * 
	 * @author jmiller
	 *
	 */
    private static class DataSourceHolder {
        private static final DataSourceNoAutoManager INSTANCE = new DataSourceNoAutoManager();
    }

    /**
     * 
     * @return
     */
    public static DataSourceNoAutoManager getInstance() {
        return DataSourceHolder.INSTANCE;
    }

    /**
     * 
     * @return
     */
    public BasicDataSource getBds() {
        return bds;
    }

    /**
     * 
     * @param bds
     */
    public void setBds(BasicDataSource bds) {
        this.bds = bds;
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
    		final Connection conn = getInstance().bds.getConnection();
    		conn.setAutoCommit(false);
        return conn;
    }
}