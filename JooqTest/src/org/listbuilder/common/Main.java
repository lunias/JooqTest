package org.listbuilder.common;

import static org.jooq.h2.generated.Tables.UNIT;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.Result;
import org.jooq.h2.generated.tables.records.UnitRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static void main(String[] args) {
		
		final Logger LOG = LoggerFactory.getLogger(Main.class);
		
		if (!Database.isInitialized()) {
			Database.resetDatabase();
		}
		
		Connection conn = null;
		try {
			conn = Database.getConnection();
			Factory db = new H2Factory(conn);
			
			Result<UnitRecord> result = db.selectFrom(UNIT).fetch();
			
			for (UnitRecord unit : result) {
				System.out.println("Found unit: " + unit.getName());
			}
			
		} catch (SQLException sqle) {
			LOG.error("Could not get units; connection failed", sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOG.warn("Could not close database connection", sqle);
				}
			}
		}
		
		Database.dispose();

	}

}
