package org.listbuilder.common;

import static org.jooq.h2.generated.Tables.UNIT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.Result;
import org.jooq.h2.generated.tables.records.UnitRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static void main(String[] args) {

		// database connection constants
		final String USERNAME = "sa";
		final String PASSWORD = "";
		final String URL = "jdbc:h2:~/test";

		// log instance
		final Logger LOG = LoggerFactory.getLogger(Main.class);

		// dynamically load the h2 database driver
		try {
			Class.forName("org.h2.Driver");

		} catch (ClassNotFoundException cnfe) {
			LOG.error("Could not find org.h2.Driver - Exiting");
			System.exit(0);
		}

		Connection conn = null;

		try {
			// get a connection to the database
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			LOG.info("Starting application...");

			// create a factory to return results
			Factory db = new H2Factory(conn);
			
			// query factory for result
			Result<UnitRecord> result = db.selectFrom(UNIT).fetch();
			
			// iterate over UnitRecord(s)
			for (UnitRecord unit : result) {
				String name = unit.getName();
				int str = unit.getStr();
				System.out.println("Name: " + name + " Strength: " + str);
			}

		} catch (SQLException sqle) {
			LOG.error("Caught SQLException: ", sqle);
			
		} finally {
			// make sure database connection is closed
			if (conn != null) {
				try {
					conn.close();
					
				} catch (SQLException sqle) {
					LOG.warn("Caught SQLException closing connection: ", sqle);
				}
			}
		}

	}

}
