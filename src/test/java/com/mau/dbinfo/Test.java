package com.mau.dbinfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import com.mau.dbinfo.services.DBInfoService;

public class Test {
	
	@org.junit.Test
	public void test() throws ClassNotFoundException, SQLException {
		DBInfoService dbInfoService = new DBInfoService();
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		String url = "";
		String user = "";
		String password = "";
		
		Connection connection  = DriverManager.getConnection(url, user, password);
		
		String schema = user.toUpperCase();
		Boolean infoAboutColumns = true;
		Boolean infoAboutExportedKeys = true;
		Boolean infoAboutImportedKeys  = true;
//		Collection<TableInfo> tables = dbInfoService.getSchemaTables(connection, schema, infoAboutColumns, infoAboutExportedKeys, infoAboutImportedKeys).values();
		Collection<TableInfo> tables = dbInfoService.getInfoBySchemaAndTables(connection, schema, infoAboutColumns, infoAboutExportedKeys, infoAboutImportedKeys, "TABLE1", "TABLE2").values();
		for (TableInfo tableInfo : tables) {
			System.out.println(tableInfo);
		}
	}

}
