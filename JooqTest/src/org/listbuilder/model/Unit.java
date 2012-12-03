package org.listbuilder.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.jooq.h2.generated.tables.records.UnitRecord;

public class Unit {
	
	private StringProperty name = new SimpleStringProperty();
	private IntegerProperty pointValue = new SimpleIntegerProperty();
	
	public Unit(UnitRecord record) {
		setName(record.getName());
		setPointValue(record.getPoint());
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	public String getName() {
		return name.get();
	}
	public StringProperty getNameProperty() {
		return name;
	}
	
	
	public void setPointValue(int pointValue) {
		this.pointValue.set(pointValue);
	}
	public int getPointValue() {
		return pointValue.get();
	}
	public IntegerProperty getIntegerProperty() {
		return pointValue;
	}
	
	@Override
	public String toString() {
		return "Warmachine: " + name;
	}
}
