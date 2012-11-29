/**
 * This class is generated by jOOQ
 */
package org.jooq.h2.generated.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.6.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class Type extends org.jooq.impl.UpdatableTableImpl<org.jooq.h2.generated.tables.records.TypeRecord> {

	private static final long serialVersionUID = 392529590;

	/**
	 * The singleton instance of PUBLIC.TYPE
	 */
	public static final org.jooq.h2.generated.tables.Type TYPE = new org.jooq.h2.generated.tables.Type();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.h2.generated.tables.records.TypeRecord> getRecordType() {
		return org.jooq.h2.generated.tables.records.TypeRecord.class;
	}

	/**
	 * The table column <code>PUBLIC.TYPE.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.h2.generated.tables.records.TypeRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The table column <code>PUBLIC.TYPE.TYPE</code>
	 */
	public final org.jooq.TableField<org.jooq.h2.generated.tables.records.TypeRecord, java.lang.String> TYPE_ = createField("TYPE", org.jooq.impl.SQLDataType.VARCHAR, this);

	public Type() {
		super("TYPE", org.jooq.h2.generated.Public.PUBLIC);
	}

	public Type(java.lang.String alias) {
		super(alias, org.jooq.h2.generated.Public.PUBLIC, org.jooq.h2.generated.tables.Type.TYPE);
	}

	@Override
	public org.jooq.Identity<org.jooq.h2.generated.tables.records.TypeRecord, java.lang.Integer> getIdentity() {
		return org.jooq.h2.generated.Keys.IDENTITY_TYPE;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.h2.generated.tables.records.TypeRecord> getMainKey() {
		return org.jooq.h2.generated.Keys.CONSTRAINT_27;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.h2.generated.tables.records.TypeRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.h2.generated.tables.records.TypeRecord>>asList(org.jooq.h2.generated.Keys.CONSTRAINT_27);
	}

	@Override
	public org.jooq.h2.generated.tables.Type as(java.lang.String alias) {
		return new org.jooq.h2.generated.tables.Type(alias);
	}
}
