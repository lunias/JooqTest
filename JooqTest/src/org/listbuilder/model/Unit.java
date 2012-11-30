package org.listbuilder.model;

import org.jooq.h2.generated.tables.records.UnitRecord;

public class Unit {

	private String name;
	private int pointValue;
	
	public Unit(UnitRecord record) {
		name = record.getName();
		pointValue = record.getPoint();
	}
	
	public String getName() {
		return name;
	}
	
	public int getPointValue() {
		return pointValue;
	}
	
	@Override
	public String toString() {
		return "Warmachine: " + name;
	}
}
