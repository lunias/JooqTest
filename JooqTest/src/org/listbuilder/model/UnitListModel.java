package org.listbuilder.model;

import static org.jooq.h2.generated.Tables.TYPE;
import static org.jooq.h2.generated.Tables.UNIT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectQuery;
import org.jooq.SimpleSelectQuery;
import org.jooq.TableField;
import org.jooq.h2.generated.tables.records.UnitRecord;
import org.jooq.impl.Factory;
import org.jooq.util.h2.H2Factory;
import org.listbuilder.common.Database;
import org.listbuilder.common.UnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum UnitListModel {

	INSTANCE;

	private final Logger LOG = LoggerFactory.getLogger(UnitListModel.class);

	Task<Void> queryTask = null;
	public BooleanProperty queryActive = new SimpleBooleanProperty(false);

	public StringProperty searchColumns = new SimpleStringProperty(
			"Search on NAME");

	private ObservableList<Unit> unitList = FXCollections
			.<Unit> observableArrayList();

	private Map<TableField<UnitRecord, ?>, Boolean> searchMap = new HashMap<TableField<UnitRecord, ?>, Boolean>();

	private UnitListModel() {
		searchMap.put(UNIT.NAME, true);
	}

	public ObservableList<Unit> getUnitList() {
		return unitList;
	}

	public void toggleSearchOnColumn(TableField<UnitRecord, ?> field) {
		if (searchMap.containsKey(field)) {
			searchMap.put(field, !searchMap.get(field));
		} else {
			searchMap.put(field, true);
		}
		updateSearchColumns();
	}

	public void updateSearchColumns() {
		String sReturn = "Search on ";
		for (Entry<TableField<UnitRecord, ?>, Boolean> entry : searchMap
				.entrySet()) {
			if (entry.getValue() == true) {
				sReturn += entry.getKey().getName() + ", ";
			}
		}
		searchColumns.set(sReturn.substring(0, sReturn.length() - 2));
	}		
	
	public void unitSearch(final String searchTerm) {
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

							SimpleSelectQuery<UnitRecord> select = db
									.selectQuery(UNIT);

							for (Entry<TableField<UnitRecord, ?>, Boolean> entry : searchMap
									.entrySet()) {
								if (entry.getValue() == true) {

									switch (entry.getKey().getName()) {
									case "TYPE":
										System.out.println(searchTerm);
										System.out.println(UnitType.REVERSE_MAP);
										select.addConditions(
												Operator.OR,
												entry.getKey().likeIgnoreCase(
														"%" + UnitType.REVERSE_MAP.get(searchTerm) + "%"));
										break;
									default:
										select.addConditions(
												Operator.OR,
												entry.getKey().likeIgnoreCase(
														"%" + searchTerm + "%"));
									}

								}
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

// 	public void unitSearchGeneric(final String searchTerm) {
// 		if (queryTask == null) {
// 			queryTask = new Task<Void>() {
// 				Connection conn = null;
// 
// 				@Override
// 				protected Void call() {
// 					try {
// 						conn = Database.getConnection();
// 						Factory db = new H2Factory(conn);
// 
// 						Result<? extends Record> searchResult;					
// 						
// 						SelectOnConditionStep partialQuery = db.select(UNIT.NAME, UNIT.POINT)
// 								.from(UNIT).join(TYPE)
// 								.on(TYPE.ID.equal(UNIT.TYPE));
// 
// 						if (searchTerm.isEmpty()) {
// 							searchResult = partialQuery.fetch();
// 						} else {																				
// 							for (Entry<TableField<UnitRecord, ?>, Boolean> entry : searchMap
// 									.entrySet()) {
// 								if (entry.getValue() == true) {
// 									partialQuery.;
// 									select.addConditions(
// 											Operator.OR,
// 											entry.getKey().likeIgnoreCase(
// 													"%" + searchTerm + "%"));
// 
// 								}
// 							}
// 
// 							searchResult = select.fetch();
// 						}
// 
// 						ObservableList<Unit> resultUnits = FXCollections
// 								.<Unit> observableArrayList();
// 
// 						for (Record unit : searchResult) {
// 							System.out.println(unit);
// 						}
// 						unitList.setAll(resultUnits);
// 
// 					} catch (SQLException sqle) {
// 						LOG.error(
// 								"Could not check database; connection failed",
// 								sqle);
// 					} finally {
// 						closeConnection();
// 						queryTask = null;
// 						Platform.runLater(new Runnable() {
// 							@Override
// 							public void run() {
// 								updateActivityState();
// 							}
// 						});
// 					}
// 					return null;
// 				}
// 
// 				@Override
// 				protected void cancelled() {
// 					super.cancelled();
// 					closeConnection();
// 				}
// 
// 				private void closeConnection() {
// 					if (conn != null) {
// 						try {
// 							conn.close();
// 						} catch (SQLException sqle) {
// 							LOG.warn("Could not close database connection",
// 									sqle);
// 						}
// 					}
// 				}
// 			};
// 
// 			updateActivityState();
// 
// 			Thread queryThread = new Thread(queryTask);
// 			queryThread.setDaemon(true);
// 			queryThread.start();
// 		} else {
// 			queryTask.cancel();
// 		}
// 
// 	}

	private void updateActivityState() {
		queryActive.set(queryTask != null);
	}

}
