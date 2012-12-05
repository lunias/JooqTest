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
	
	public void addUnit(Unit addedUnit) {
		String name = addedUnit.getName();
		Integer count = unitMap.get(name);
		
		if (count == null) {
			unitMap.put(name, 1);
			unitList.add(addedUnit);
		} else {
			count += 1;
			unitMap.put(name, count);
			// must update quantity of unitList object
			for (Unit localUnit : unitList) {
				if (localUnit.getName().equals(name)) {
					localUnit.setQuantity(count);
				}
			}
		}		
	}
	
	public void removeUnit(Unit unitToRemove) {
		String name = unitToRemove.getName();
		int count = getUnitQuantityByName(name) - 1;
		
		if (count > 0) {
			unitMap.put(name, count);			
			unitToRemove.setQuantity(count);
		} else {
			completeRemoveUnit(unitToRemove);
		}
	}
	
	public void completeRemoveUnit(Unit unitToRemove) {
		unitMap.remove(unitToRemove.getName());
		unitList.remove(unitToRemove);
		unitToRemove.setQuantity(1);
	}
	
	public void removeAllUnits() {
		for (Unit unit : unitList) {
			unit.setQuantity(1);
		}
		unitList.clear();
		unitMap.clear();
	}
	
	public int getUnitQuantityByName(String name) {
		Integer count = unitMap.get(name);
		if (count == null) {
			return 0;
		}
		return unitMap.get(name);
	}
	
	public void updatePointTotals() {
		int pointTotal = 0;
		int warjackPointTotal = 0;
		
		for (Unit unit : unitList) {
			if (unit.getPointValue() < 0) {
				warjackPointTotal += unit.getPointValue() * unitMap.get(unit.getName());
			} else {
				pointTotal += unit.getPointValue() * unitMap.get(unit.getName());	
			}
		}
	}

}
