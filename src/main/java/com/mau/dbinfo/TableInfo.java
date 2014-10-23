package com.mau.dbinfo;

import java.util.Collection;
import java.util.Map;

public class TableInfo {


	private String tableCat;
	private String tableSchema;
	private String tableName;
	private String tableType;
	private String tableRemarks;
	private Map<String, ColumnInfo> columns;

	public TableInfo() {

	}

	public TableInfo(String tableCat, String tableSchema, String tableName,
			String tableType, String tableRemarks) {
		this.tableCat = tableCat;
		this.tableSchema = tableSchema;
		this.tableName = tableName;
		this.tableType = tableType;
		this.tableRemarks = tableRemarks;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}

	public String getTableCat() {
		return tableCat;
	}

	public String getTableSchema() {
		return tableSchema;
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

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getTableRemarks() {
		return tableRemarks;
	}

	public void setTableRemarks(String tableRemarks) {
		this.tableRemarks = tableRemarks;
	}

	public Map<String, ColumnInfo> getAllColumns() {
		return this.columns;
	}
	
	public Collection<ColumnInfo> getColumnsList() {
		return this.columns.values();
	}

	public void setAllColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if(getTableCat() != null) {
			stringBuffer.append(getTableCat() + " ");
		}
		stringBuffer.append(getTableSchema());
		stringBuffer.append(" " + getTableName());
		stringBuffer.append(" " + getTableType());
		for (ColumnInfo columnInfo : getAllColumns().values()) {
			stringBuffer.append("\n     " + columnInfo);
		}
		return stringBuffer.toString();
	}

}