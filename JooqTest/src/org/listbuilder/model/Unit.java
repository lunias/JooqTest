package org.listbuilder.model;

import static org.jooq.h2.generated.Tables.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.jooq.Record;

public class Unit {
	
	private IntegerProperty quantity = new SimpleIntegerProperty(); 
	private StringProperty name = new SimpleStringProperty();
	private IntegerProperty pointValue = new SimpleIntegerProperty();
	private StringProperty type = new SimpleStringProperty();	
	
	public Unit(Record record) {
		String name = record.getValue(UNIT.NAME);
		
		setQuantity(UnitTableModel.INSTANCE.getUnitQuantityByName(name));		
		setName(name);
		setPointValue(record.getValue(UNIT.POINT));
		setType(record.getValue(TYPE.TYPE_));		
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity.set(quantity);
	}
	public Integer getQuantity() {
		return quantity.get();
	}
	public IntegerProperty getQuantityProperty() {
		return quantity;
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
	public IntegerProperty getPointValueProperty() {
		return pointValue;
	}
	
	public void setType(String type) {
		this.type.set(type);		
	}
	public String getType() {
		return type.get();
	}
	public StringProperty getTypeProperty() {
		return type;
	}
	
	@Override
	public String toString() {
		return "Warmachine: " + name;
	}
}
