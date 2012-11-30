package org.listbuilder.model;

import static org.jooq.h2.generated.Tables.UNIT;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.jooq.Result;
import org.jooq.h2.generated.tables.records.UnitRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.listbuilder.common.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitListModel {

	public static UnitListModel instance = new UnitListModel();
	
	public ObservableList<Unit> allUnits = FXCollections.<Unit>observableArrayList();
	
	public static final Logger LOG = LoggerFactory.getLogger(UnitListModel.class);
	
	public UnitListModel() {
		allUnits.setAll(FXCollections.<Unit>observableArrayList());				
	}

	public void unitSearchByName(final String searchTerm) {		
		
		Connection conn = null;		
		
		try {
			conn = Database.getConnection();			
			Factory db = new H2Factory(conn);

			Result<UnitRecord> result;
			
			if (searchTerm.isEmpty()) {
				result = db.selectFrom(UNIT).fetch();
			} else {
				result = db.selectFrom(UNIT).where(UNIT.NAME.like("%" + searchTerm + "%")).fetch();
			}
			
			ObservableList<Unit> resultUnits = FXCollections.<Unit>observableArrayList();
			
			for (UnitRecord unit : result) {
				resultUnits.add(new Unit(unit));
			}
			
			allUnits.setAll(resultUnits);
			
		} catch (SQLException sqle) {
			LOG.error("Could not check database; connection failed", sqle);
		} finally {
			try {
				if (conn != null) {
					conn.close();	
				}
			} catch (SQLException sqle) {
				LOG.warn("Could not close database connection", sqle);
			}
		}
	}
	
}
