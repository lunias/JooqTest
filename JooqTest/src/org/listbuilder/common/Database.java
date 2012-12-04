package org.listbuilder.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {

	private static final String DRIVER_NAME = "org.h2.Driver";

	private static final Logger LOG = LoggerFactory.getLogger(Database.class);

	static {
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException nfe) {
			LOG.error("Could not load database driver " + DRIVER_NAME, nfe);
		}
	}

	private static final String URL = "jdbc:h2:~/test";
	private static final String USERNAME = "sa";
	private static final String PASSWORD = "";

	private static JdbcConnectionPool connectionPool = JdbcConnectionPool
			.create(URL, USERNAME, PASSWORD);

	public static Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
	}
	
	public static boolean isInitialized() {
		Connection conn = null;
		try {
			conn = getConnection();			
			ResultSet rs = conn.getMetaData().getTables(null, null, "UNIT", null);
			return rs.first();
		} catch (SQLException sqle) {
			LOG.error("Could not check database; connection failed", sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOG.warn("Could not close database connection", sqle);
				}
			}
		}
		return true;
	}

	public static void resetDatabase() {
		Connection conn = null;
		try {
			conn = getConnection();
			try {
				LOG.debug("Executing init script!");
				RunScript.execute(conn, new FileReader("scripts/init.sql"));
			} catch (FileNotFoundException nfe) {
				LOG.error("Could not reset database; init script not found", nfe);
			}
		} catch (SQLException sqle) {
			LOG.error("Could not reset database; connection failed", sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOG.warn("Could not close database connection", sqle); 
				}
			}
		}
	}
	
	public static void dispose() {
		connectionPool.dispose();
	}	

}
