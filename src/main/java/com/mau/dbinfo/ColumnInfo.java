package com.mau.dbinfo;

import java.util.Collection;
import java.util.HashSet;

public class ColumnInfo {

	private String tableName;
	private String tableCat;
	private String tableSchema;
	private String columnName;
	private Integer position;
	private String columnType;
	private String columnDataType;
	private Integer columnDecimalDigits;
	private String columnDefault;
	private Integer columnSize;
	private Boolean isNullable = Boolean.FALSE;
	private Boolean isPrimaryKey = Boolean.FALSE;
	private String primaryKeyName;
	private Boolean isForeignKey = Boolean.FALSE;
	private String foreignKeyName;

	private Collection<String> referenceTo = new HashSet<String>();
	private Collection<String> exportedReferenceTo = new HashSet<String>();

	public ColumnInfo() {

	}

	public ColumnInfo(ColumnBuilder builder) {
		this.tableName = builder.tableName;
		this.tableCat = builder.tableCat;
		this.tableSchema = builder.tableSchema;
		this.columnName = builder.columnName;
		this.position = builder.position;
		this.columnType = builder.columnType;
		this.columnDataType = builder.columnDataType;
		this.columnDecimalDigits = builder.columnDecimalDigits;
		this.columnDefault = builder.columnDefault;
		this.columnSize = builder.columnSize;
		this.isNullable = builder.isNullable;
		this.isPrimaryKey = builder.isPrimaryKey;
		this.primaryKeyName = builder.primaryKeyName;
		this.isForeignKey = builder.isForeignKey;
		this.foreignKeyName = builder.foreignKeyName;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}

	public String getTableCat() {
		return tableCat;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public Integer getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}

	public boolean isForeignKey() {
		return isForeignKey;
	}

	public void setForeignKeyName(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
	}

	public String getForeignKeyName() {
		return foreignKeyName;
	}

	public void addReferenceTo(String referenceTo, boolean exportedOne) {
		if(exportedOne) {
			this.exportedReferenceTo.add(referenceTo);
		} else {
			this.referenceTo.add(referenceTo);
		}
		
	}

	public Collection<String> getReferenceTo() {
		return referenceTo;
	}
	
	public Collection<String> getExportedReferenceTo() {
		return exportedReferenceTo;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getDataType() {
		return columnDataType;
	}
	
	public void setDataType(String columnDataType) {
		this.columnDataType = columnDataType;
	}
	
	public Integer getDecimalDigits() {
		return columnDecimalDigits;
	}
	
	public void setDecimalDigits(Integer columnDecimalDigits) {
		this.columnDecimalDigits = columnDecimalDigits;
	}
	
	public String getDefault() {
		return columnDefault;
	}
	
	public void setDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}

	@Override
	public String toString() {
		
//		$column.getColumnName()#if( $column.isPrimaryKey() )(PK)#end#if( $column.isForeignKey() )(FK) $column.getReferenceTo() $column.getExportedReferenceTo()#end#if( !$column.isNullable() ) NOT NULL#end $column.getColumnType() 
		StringBuffer stringBuffer = new StringBuffer();
		
		stringBuffer.append(getPosition());
		stringBuffer.append("-" + getColumnName());
		if(isPrimaryKey()) {
			stringBuffer.append(" (PK)");
		}
		if(isForeignKey()) {
			stringBuffer.append(" (FK)");
			if(!getReferenceTo().isEmpty()) {
				stringBuffer.append(" importedKey => " + getReferenceTo());
			}
			if(!getExportedReferenceTo().isEmpty()) {
				stringBuffer.append(" exportedKey => " + getExportedReferenceTo());
			}
			
		}
		if(isNullable()) {
			stringBuffer.append(", NOT NULL");
		}
		stringBuffer.append(", type: " + getColumnType());
		stringBuffer.append(", dataType: " + getDataType());
		if(getDefault() != null) {
			stringBuffer.append(", default: " + getDefault());
		}
		if(getColumnSize() != null) {
			stringBuffer.append(", size: " + getColumnSize());
		}
		if(getDecimalDigits() != null) {
			stringBuffer.append(", decimals: " + getDecimalDigits());
		}
		return stringBuffer.toString();
	}

	

}