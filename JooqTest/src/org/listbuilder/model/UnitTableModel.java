package org.listbuilder.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum UnitTableModel {
	
	INSTANCE;
	
	private final Logger LOG = LoggerFactory.getLogger(UnitTableModel.class);
	
	private ObservableMap<String, Integer> unitMap = FXCollections.<String, Integer>observableHashMap();
	private ObservableList<Unit> unitList = FXCollections.<Unit>observableArrayList();

	public ObservableMap<String, Integer> getUnitMap() {
		return unitMap;
	}
	
	public ObservableList<Unit> getUnitList() {
		return unitList;
	}
	
	public void addUnit(Unit unit) {
		Integer count = unitMap.get(unit.getName());
		if (count == null) {
			unitMap.put(unit.getName(), 1);
			unitList.add(unit);
		} else {
			unitMap.put(unit.getName(), count + 1);
		}		
	}
	
	public int getUnitQuantityByName(String name) {
		Integer count = unitMap.get(name);
		if (count == null) {
			return 0;
		}
		return unitMap.get(name);
	}

}
