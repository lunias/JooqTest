package org.listbuilder.common;

import static org.jooq.h2.generated.Tables.TYPE;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jooq.Result;
import org.jooq.h2.generated.tables.records.TypeRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitType {
	
	private static final Logger LOG = LoggerFactory.getLogger(UnitType.class);
	
	private static final Map<?, ?>[] MAPS = buildMaps();
	
	public static final Map<Integer, String> MAP = (Map<Integer, String>) MAPS[0];
	public static final Map<String, Integer> REVERSE_MAP = (Map<String, Integer>) MAPS[1];
	
	private static Map<?, ?>[] buildMaps() {
		Map<Integer, String> tmpMap = new HashMap<Integer, String>();
		Map<String, Integer> tmpRevMap = new HashMap<String, Integer>();
		
		Connection conn = null;
		try {
			conn = Database.getConnection();
			
			Factory db = new H2Factory(conn);
			
			Result<TypeRecord> searchResult = db.selectFrom(TYPE).fetch();
						
			for (TypeRecord type : searchResult) {
				tmpMap.put(type.getId(), type.getType());
				tmpRevMap.put(type.getType(), type.getId());
			}
			
		} catch (SQLException sqle) {
			LOG.error("Could not build unit type map; connection failed", sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOG.warn("Could not close database connection", sqle);
				}
			}
		}
		
		return new Map<?, ?>[] {Collections.unmodifiableMap(tmpMap), Collections.unmodifiableMap(tmpRevMap)};
	}	

}
