package com.mau.dbinfo;

public class ColumnBuilder {

	protected String tableName;
	protected String tableCat;
	protected String tableSchema;
	protected String columnName;
	protected Integer position;
	protected String columnType;
	protected String columnDataType;
	protected Integer columnDecimalDigits;
	protected String columnDefault;
	protected Integer columnSize;
	protected Boolean isNullable = Boolean.FALSE;
	protected Boolean isPrimaryKey = Boolean.FALSE;
	protected String primaryKeyName;
	protected Boolean isForeignKey = Boolean.FALSE;
	protected String foreignKeyName;

	public ColumnInfo build() {
		return new ColumnInfo(this);
	}

	public ColumnBuilder setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public ColumnBuilder setTableCat(String tableCat) {
		this.tableCat = tableCat;
		return this;
	}

	public ColumnBuilder setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
		return this;
	}

	public ColumnBuilder setColumnName(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public ColumnBuilder setPosition(Integer position) {
		this.position = position;
		return this;
	}

	public ColumnBuilder setColumnType(String columnType) {
		this.columnType = columnType;
		return this;
	}

	public ColumnBuilder setColumnDataType(String columnDataType) {
		this.columnDataType = columnDataType;
		return this;
	}

	public ColumnBuilder setColumnDecimalDigits(Integer columnDecimalDigits) {
		this.columnDecimalDigits = columnDecimalDigits;
		return this;
	}

	public ColumnBuilder setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
		return this;
	}

	public ColumnBuilder setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
		return this;
	}

	public ColumnBuilder setIsNullable(Boolean isNullable) {
		this.isNullable = isNullable;
		return this;
	}

	public ColumnBuilder setIsPrimaryKey(Boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
		return this;
	}

	public ColumnBuilder setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
		return this;
	}

	public ColumnBuilder setIsForeignKey(Boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
		return this;
	}

	public ColumnBuilder setForeignKeyName(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
		return this;
	}

	public ColumnBuilder setColumnSize(String columnSize) {
		if(columnSize != null) {
			setColumnSize(Integer.parseInt(columnSize));
		}
		return this;
	}

	public ColumnBuilder setColumnDecimalDigits(String columnDecimalDigits) {
		if(columnDecimalDigits != null) {
			setColumnDecimalDigits(Integer.parseInt(columnDecimalDigits));
		}
		return this;
	}

	public ColumnBuilder setIsNullable(String columnNullable) {
		if(columnNullable != null) {
			setIsNullable(columnNullable.equals("1")? Boolean.TRUE : Boolean.FALSE);
		}
		return this;
	}

}
