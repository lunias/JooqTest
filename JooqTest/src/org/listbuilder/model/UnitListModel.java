package org.listbuilder.model;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import org.jooq.Result;
import org.jooq.h2.generated.tables.records.UnitRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.listbuilder.common.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jooq.h2.generated.Tables.*;

public enum UnitListModel {

	INSTANCE;

	private final Logger LOG = LoggerFactory.getLogger(UnitListModel.class);

	Task<Void> queryTask = null;
	public BooleanProperty queryActive = new SimpleBooleanProperty(false);

	public ObservableList<Unit> unitList = FXCollections
			.<Unit> observableArrayList();

	private UnitListModel() {
		unitList.setAll(FXCollections.<Unit> observableArrayList());
	}

	public void unitSearchByName(final String searchTerm) {
		if (queryTask == null) {

			queryTask = new Task<Void>() {
				@Override
				protected Void call() {
					Connection conn = null;
					try {

						conn = Database.getConnection();
						Factory db = new H2Factory(conn);

						Result<UnitRecord> searchResult;

						if (searchTerm.isEmpty()) {
							searchResult = db.selectFrom(UNIT).fetch();
						} else {
							searchResult = db
									.selectFrom(UNIT)
									.where(UNIT.NAME.like("%" + searchTerm
											+ "%")).fetch();
						}
						
						// simulate long query
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						ObservableList<Unit> resultUnits = FXCollections
								.<Unit> observableArrayList();

						for (UnitRecord unit : searchResult) {
							resultUnits.add(new Unit(unit));
						}
						unitList.setAll(resultUnits);

					} catch (SQLException sqle) {
						LOG.error(
								"Could not check database; connection failed",
								sqle);
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException sqle) {
								LOG.warn("Could not close database connection",
										sqle);
							}
						}
						queryTask = null;
						updateActivityState();
						//updateActivityState();
					}
					return null;
				}
			};

			updateActivityState();

			Thread queryThread = new Thread(queryTask);
			queryThread.setDaemon(true);
			queryThread.start();
		} else {
			queryTask.cancel();
		}

	}

	private void updateActivityState() {
		boolean state = queryTask != null;
		System.out.println(state);
		queryActive.set(state);
	}

}
