package com.mau.dbinfo.services;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mau.dbinfo.ColumnBuilder;
import com.mau.dbinfo.ColumnInfo;
import com.mau.dbinfo.TableInfo;

public class DBInfoService {
	
	private final transient Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<String> getSchemas(Connection connection) throws SQLException {
		ArrayList<String> schemas = new ArrayList<String>(); 
		DatabaseMetaData dbmd = connection.getMetaData();
		ResultSet res = dbmd.getSchemas();
		while (res.next()) {
//			String tableCat = res.getString("TABLE_CAT");
			String tableSchema = res.getString("TABLE_SCHEM");
			schemas.add(tableSchema);
		}
		res.close();
		return schemas;
	}

	public Map<String, TableInfo> getSchemaTables(Connection connection,
			String schema) throws SQLException {
		return getInfoBySchemaAndTable(connection, schema, null, false, false, false);
	}
	
	public Map<String, TableInfo> getSchemaTables(Connection connection,
			String schema, Boolean infoAboutColumns, Boolean infoAboutExportedKeys, Boolean infoAboutImportedKeys) throws SQLException {
		return getInfoBySchemaAndTable(connection, schema, null, infoAboutColumns, infoAboutExportedKeys, infoAboutImportedKeys);
	}
	
	public Map<String, TableInfo> getInfoBySchemaAndTables(Connection connection, String schema, Boolean infoAboutColumns, Boolean infoAboutExportedKeys, Boolean infoAboutImportedKeys, String... tables) throws SQLException {
		LinkedHashMap<String, TableInfo> map = new LinkedHashMap<String, TableInfo>();
		for (int i = 0; i < tables.length; i++) {
			String table = tables[i];
			map.putAll(getInfoBySchemaAndTable(connection, schema, table, infoAboutColumns, infoAboutExportedKeys, infoAboutImportedKeys));
		}
		return map;
	}
	
	public TableInfo getTableInfo(Connection connection, String schema, String table, Boolean infoAboutColumns, Boolean infoAboutExportedKeys, Boolean infoAboutImportedKeys) throws SQLException {
		return getTablesInfo(connection, schema, table, infoAboutColumns, infoAboutExportedKeys, infoAboutImportedKeys).get(table);
	}
	
	public Map<String, TableInfo> getTablesInfo(
			Connection connection, String schema, String table, Boolean infoAboutColumns, Boolean infoAboutExportedKeys, Boolean infoAboutImportedKeys)
			throws SQLException {
		
		long lStartTime = System.currentTimeMillis();
		
		LinkedHashMap<String, TableInfo> map = new LinkedHashMap<String, TableInfo>();
		
		DatabaseMetaData dbmd = connection.getMetaData();

		ResultSet res = null;
		res = dbmd.getTables(null, schema, table, new String[] { "TABLE" });
		int size = 0;

		while (res.next()) {
			size++;
		}
		res.close();
		res = dbmd.getTables(null, schema, table, new String[] { "TABLE" });

		int count = 0;
		while (res.next()) {
			String tableCat = res.getString("TABLE_CAT");
			String tableSchema = res.getString("TABLE_SCHEM");
			String tableName = res.getString("TABLE_NAME");
			String tableType = res.getString("TABLE_TYPE");
			String tableRemarks = res.getString("REMARKS");
			String prefix = "## [" + ++count + "/" + size + "]";
			if(size == 1) {
				prefix = "## ";
			}
			getLogger().info(
					prefix + " Getting info from " + tableSchema + "." + tableName);
			TableInfo tableInfo = new TableInfo(tableCat, tableSchema,
					tableName, tableType, tableRemarks);
			
			map.put(tableInfo.getTableName(), tableInfo);
			
			if(infoAboutColumns) {
				inspect(connection, tableInfo, infoAboutExportedKeys, infoAboutImportedKeys);
			}
			
		}
		res.close();

		long lEndTime = System.currentTimeMillis();
		long difference = lEndTime - lStartTime;
		getLogger()
				.debug("\t### Database inspection elapsed milliseconds: "
						+ difference);
		return map;
	}

	public Map<String, TableInfo> getInfoBySchemaAndTable(
			Connection connection, String schema, String table, Boolean infoAboutColumns, Boolean infoAboutExportedKeys, Boolean infoAboutImportedKeys)
			throws SQLException {

//		DBInfo dbInfo = new DBInfo();
//		dbInfo.setDatabaseName(connection.getMetaData().getURL());
//		dbInfo.setSchema(schema);
//		dbInfo.setDate(new Date());
		
		return getTablesInfo(connection, schema, table, infoAboutColumns, infoAboutExportedKeys, infoAboutImportedKeys);
	}
	
	
	protected void inspect(Connection connection, TableInfo tableInfo, Boolean infoAboutExportedKeys, Boolean infoAboutImportedKeys) throws SQLException {
		tableInfo.setAllColumns(getInfoAboutAllColumns(connection, tableInfo));
		long lStartTime = System.currentTimeMillis();
		searchPrimaryKeys(connection, tableInfo);
		if(infoAboutImportedKeys) {
			searchImportedKeys(connection, tableInfo);
		}
		if(infoAboutExportedKeys) {
			searchExportedKeys(connection, tableInfo);
		}
		long lEndTime = System.currentTimeMillis();
		long difference = lEndTime - lStartTime;
		getLogger().debug(
				"\t### Table inspection elapsed milliseconds: " + difference);
	}

	protected void searchImportedKeys(Connection connection, TableInfo tableInfo)
			throws SQLException {
		searchKeys(connection, tableInfo, false);
	}

	protected void searchExportedKeys(Connection connection, TableInfo tableInfo)
			throws SQLException {
		searchKeys(connection, tableInfo, true);
	}

	protected void searchKeys(Connection connection, TableInfo tableInfo, boolean exportedOne)
			throws SQLException {
		ResultSet r = exportedOne ? connection.getMetaData().getExportedKeys(
				tableInfo.getTableCat(), tableInfo.getTableSchema(), tableInfo.getTableName()) : connection
				.getMetaData().getImportedKeys(tableInfo.getTableCat(), tableInfo.getTableSchema(),
						tableInfo.getTableName());
		String kind = exportedOne ? "exported" : "imported";
		getLogger().debug("\t### getting " + kind + " keys ###");
		while (r.next()) {
			// String tableCat = r.getString("PKTABLE_CAT");
			String tableSchema = exportedOne ? r.getString("FKTABLE_SCHEM") : r
					.getString("PKTABLE_SCHEM");
			String tableName = exportedOne ? r.getString("FKTABLE_NAME") : r
					.getString("PKTABLE_NAME");
			String columnName = exportedOne ? r.getString("FKCOLUMN_NAME") : r
					.getString("PKCOLUMN_NAME");
			// String fkTableCat = r.getString("FKTABLE_CAT");
			// String fkTableSchema = r.getString("FKTABLE_SCHEM");
			// String fkTableName = r.getString("FKTABLE_NAME");
			// String fkColumnName = r.getString("FKCOLUMN_NAME");
			// short keySeq = r.getShort("KEY_SEQ");
			// short updateRule = r.getShort("UPDATE_RULE");
			// short deleteRule = r.getShort("DELETE_RULE");
			String keyName = exportedOne ? r.getString("FK_NAME") : r
					.getString("PK_NAME");
			// String pkName = r.getString("PK_NAME");
			// search and update
			ColumnInfo columnInfo = searchColumnInfoByName(tableInfo, exportedOne ? r
					.getString("PKCOLUMN_NAME") : r.getString("FKCOLUMN_NAME"));
			if (columnInfo != null) {
				columnInfo.setForeignKey(true);
				columnInfo.setForeignKeyName(keyName);
				String referenceTo = tableSchema + "." + tableName + "."
						+ columnName;
				columnInfo.addReferenceTo(referenceTo, exportedOne);
				getLogger().debug(
						"\t\t- " + kind + " key: "
								+ columnInfo.getForeignKeyName() + " "
								+ columnInfo.getColumnName() + " reference to "
								+ referenceTo);
			}
		}
		r.close();
	}

	protected void searchPrimaryKeys(Connection connection, TableInfo tableInfo) throws SQLException {
		ResultSet r = connection.getMetaData().getPrimaryKeys(tableInfo.getTableCat(),
				tableInfo.getTableSchema(), tableInfo.getTableName());
		getLogger().debug("\t### getting primary keys ###");
		while (r.next()) {
			String columnName = r.getString("COLUMN_NAME");
			String primarykey = r.getString("PK_NAME");
			if (columnName != null) {
				ColumnInfo columnInfo = searchColumnInfoByName(tableInfo, columnName);
				if (columnInfo != null) {
					columnInfo.setPrimaryKey(true);
					columnInfo.setPrimaryKeyName(primarykey);
					getLogger().debug(
							"\t\t- primary key: "
									+ columnInfo.getPrimaryKeyName() + " "
									+ columnInfo.getColumnName() + " "
									+ columnInfo.getColumnType() + " "
									+ columnInfo.getColumnSize());
				}
			}
		}
		r.close();
	}

	protected ColumnInfo searchColumnInfoByName(TableInfo tableInfo, String columnName) {
		for (Iterator<String> iterator = tableInfo.getAllColumns().keySet().iterator(); iterator
				.hasNext();) {
			String cName = (String) iterator.next();
			if (cName.equalsIgnoreCase(columnName)) {
				return tableInfo.getAllColumns().get(columnName);
			}
		}
		return null;
	}

	protected Map<String, ColumnInfo> getInfoAboutAllColumns(
			Connection connection, TableInfo tableInfo) throws SQLException {
		LinkedHashMap<String, ColumnInfo> map = new LinkedHashMap<String, ColumnInfo>();
		ResultSet r = connection.getMetaData().getColumns(tableInfo.getTableCat(),
				tableInfo.getTableSchema(), tableInfo.getTableName(), null);
		getLogger().debug("\t### getting columns ###");
		while (r.next()) { 
			String tableCat = r.getString("TABLE_CAT");
			String columnSchema = r.getString("TABLE_SCHEM");
			String columnTableName = r.getString("TABLE_NAME");
			String columnName = r.getString("COLUMN_NAME");
			String columnType = r.getString("TYPE_NAME");
			String columnSize = r.getString("COLUMN_SIZE");
			String columnDataType = r.getString("DATA_TYPE");
			String columnDecimalDigits = r.getString("DECIMAL_DIGITS");
			String columnDefault = r.getString("COLUMN_DEF");
			String columnNullable = r.getString("NULLABLE");
			int position = r.getInt("ORDINAL_POSITION");
			ColumnInfo columnInfo = new ColumnBuilder().
					setTableCat(tableCat).
					setTableSchema(columnSchema).
					setTableName(columnTableName).
					setPosition(position).
					setColumnName(columnName).
					setColumnType(columnType).
					setColumnSize(columnSize).
					setColumnDataType(columnDataType).
					setColumnDecimalDigits(columnDecimalDigits).
					setColumnDefault(columnDefault).
					setIsNullable(columnNullable).
					build();
			getLogger().debug(
					"\t\t- column: (position-"+columnInfo.getPosition()+") " + columnInfo.getColumnName() + " "
							+ columnInfo.getColumnType() + " "
							+ columnInfo.getColumnSize());
			map.put(columnInfo.getColumnName(), columnInfo);
		}
		r.close();
		return map;
	}

	public Logger getLogger() {
		return logger;
	}

}
