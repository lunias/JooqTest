package org.listbuilder.model;

import static org.jooq.h2.generated.Tables.UNIT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.jooq.FutureResult;
import org.jooq.Result;
import org.jooq.h2.generated.tables.records.UnitRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.listbuilder.common.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum UnitListModel {
	
	INSTANCE;

	private final Logger LOG = LoggerFactory.getLogger(UnitListModel.class);	
	
	public BooleanProperty queryActive = new SimpleBooleanProperty(false);	
	public ObservableList<Unit> unitList = FXCollections.<Unit>observableArrayList();	

	private FutureResult<UnitRecord> searchResult = null;
	
	private UnitListModel() {
		unitList.setAll(FXCollections.<Unit>observableArrayList());				
	}

	public void unitSearchByName(final String searchTerm) {		
		
		if (searchResult != null) {
			searchResult.cancel(true);
		}
		
		updateActivityState();
		
		Connection conn = null;		
		
		try {
			conn = Database.getConnection();			
			Factory db = new H2Factory(conn);
			
			// TODO Add ExecutorStatement for calling updateActivityState()
			if (searchTerm.isEmpty()) {
				searchResult = db.selectFrom(UNIT).fetchLater();
			} else {
				searchResult = db.selectFrom(UNIT).where(UNIT.NAME.like("%" + searchTerm + "%")).fetchLater();
			}
			
			Result<UnitRecord> result = null;
			try {
				result = searchResult.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ObservableList<Unit> resultUnits = FXCollections.<Unit>observableArrayList();
			
			for (UnitRecord unit : result) {
				resultUnits.add(new Unit(unit));
			}
			
			unitList.setAll(resultUnits);
			
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
			searchResult = null;
			updateActivityState();
		}
	}
	
	private void updateActivityState() {
		queryActive.set(searchResult != null && !searchResult.isDone());
	}
	
}
