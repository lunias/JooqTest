package org.listbuilder.model;

import org.jooq.h2.generated.tables.records.UnitRecord;

public class Unit {

	private String name;
	
	public Unit(UnitRecord record) {
		name = record.getName();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "Warmachine: " + name;
	}
}
