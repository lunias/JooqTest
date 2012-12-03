package org.listbuilder.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum UnitTableModel {
	
	INSTANCE;
	
	private final Logger LOG = LoggerFactory.getLogger(UnitTableModel.class);
	
	private ObservableList<Unit> unitList = FXCollections.<Unit>observableArrayList();

	public ObservableList<Unit> getUnitList() {
		return unitList;
	}
	
	public void addUnit(Unit unit) {
		unitList.add(unit);
	}

}
