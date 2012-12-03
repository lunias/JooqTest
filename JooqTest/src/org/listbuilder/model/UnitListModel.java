package org.listbuilder.model;

import static org.jooq.h2.generated.Tables.UNIT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import org.jooq.DataType;
import org.jooq.Operator;
import org.jooq.Result;
import org.jooq.SimpleSelectQuery;
import org.jooq.TableField;
import org.jooq.h2.generated.tables.records.UnitRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.listbuilder.common.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum UnitListModel {

	INSTANCE;

	private final Logger LOG = LoggerFactory.getLogger(UnitListModel.class);

	Task<Void> queryTask = null;
	public BooleanProperty queryActive = new SimpleBooleanProperty(false);

	private ObservableList<Unit> unitList = FXCollections
			.<Unit> observableArrayList();

	private List<TableField<UnitRecord, ?>> searchColumns = new ArrayList<TableField<UnitRecord, ?>>();

	public ObservableList<Unit> getUnitList() {
		return unitList;
	}

	public void setSearchColumns() {
		// searchColumns = 
	}
	
	public void unitSearchByName(final String searchTerm) {
		if (queryTask == null) {
			queryTask = new Task<Void>() {
				Connection conn = null;

				@Override
				protected Void call() {
					try {
						conn = Database.getConnection();
						Factory db = new H2Factory(conn);

						Result<UnitRecord> searchResult;

						if (searchTerm.isEmpty()) {
							searchResult = db.selectFrom(UNIT).fetch();
						} else {
							searchColumns.clear();
							searchColumns.add(UNIT.NAME);
							searchColumns.add(UNIT.POINT);
							SimpleSelectQuery<UnitRecord> select = db
									.selectQuery(UNIT);
							for (TableField<UnitRecord, ?> field : searchColumns) {
									select.addConditions(Operator.OR,
											field.like("%" + searchTerm + "%"));
							}

							searchResult = select.fetch();
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
						closeConnection();
						queryTask = null;
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								updateActivityState();
							}
						});
					}
					return null;
				}

				@Override
				protected void cancelled() {
					super.cancelled();
					closeConnection();
				}

				private void closeConnection() {
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException sqle) {
							LOG.warn("Could not close database connection",
									sqle);
						}
					}
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
		queryActive.set(queryTask != null);
	}

}
