package com.dixin.action.databackup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelWriter implements Progressive {

	private Connection connection;

	// Logger logger = Logger.getLogger(ExcelWriter.class);
	File file;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	ExcelWriter(File f) {
		this.file = f;
	}

	ExcelWriter(Connection conn, File f) {
		this.connection = conn;
		this.file = f;
	}

	public void writeToExcel() throws SQLException, IOException {
		writeToExcel(file);
	}

	public void writeToExcel(File f) throws SQLException, IOException {
		totalTables = 0;
		tableCount = 0;
		totalRows = 0;
		rowCount = 0;
		finished = false;
		currentTable = "";
		OutputStream out = new FileOutputStream(f);
		writeWorkbook().write(out);
		out.flush();
		out.close();
		finished = true;
		connection.close();
	}

	int totalTables = 0;
	int tableCount = 0;
	int totalRows = 0;
	int rowCount = 0;
	boolean finished = false;
	String currentTable = "";
	Throwable exception;

	public Progress getProgress() {
		return new Progress(totalTables, tableCount, totalRows, rowCount,
				finished, currentTable, exception);
	}

	public HSSFWorkbook writeWorkbook() throws SQLException {
		HSSFWorkbook book = new HSSFWorkbook();
		List<String> tableNames = getTableNames();
		// logger.debug("All Tables to Export:" + tableNames);
		totalTables = tableNames.size();
		for (String t : tableNames) {
			tableCount++;
			// logger.debug("Export Table: " + t);
			currentTable = t;
			writeSheet(book.createSheet(t), t);
			// logger.debug("Export Table finished: " + t);
		}
		return book;
	}

	public void writeSheet(HSSFSheet sheet, String tableName)
			throws SQLException {
		rowCount = 0;
		HSSFRow row = sheet.createRow(rowCount++);
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("select count(*) from " + tableName);
		if (rs.next()) {
			totalRows = rs.getInt(1);
		}
		rs.close();

		rs = stmt.executeQuery("select * from " + tableName);
		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		for (short i = 1, col = 0; i <= count; i++, col++) {
			HSSFCell cell = row.createCell(col);
			cell.setCellValue(new HSSFRichTextString(meta.getColumnName(i)));
		}
		while (rs.next()) {
			row = sheet.createRow(rowCount++);
			for (short i = 1, col = 0; i <= count; i++, col++) {
				HSSFCell cell = row.createCell(col);
				switch (meta.getColumnType(i)) {
				case Types.BOOLEAN:
					cell.setCellValue(rs.getBoolean(i));
					break;
				case Types.BIT:
				case Types.TINYINT:
				case Types.SMALLINT:
				case Types.INTEGER:
				case Types.BIGINT:
				case Types.FLOAT:
				case Types.REAL:
				case Types.DOUBLE:
				case Types.NUMERIC:
				case Types.DECIMAL:
					cell.setCellValue(rs.getInt(i));
					break;
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
					cell.setCellValue(new HSSFRichTextString(rs.getString(i)));
					break;
				case Types.DATE:
				case Types.TIME:
				case Types.TIMESTAMP:
					cell.setCellValue(new HSSFRichTextString(dateFormat
							.format(rs.getTimestamp(i))));
					break;
				default:
				}

			}
		}
	}

	public List<String> getTableNames() throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt
				.executeQuery("select table_name from INFORMATION_SCHEMA.TABLES where table_schema ='dixin'");
		List<String> ret = new ArrayList<String>();
		while (rs.next()) {
			ret.add(rs.getString(1));
		}
		rs.close();
		stmt.close();
		return ret;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void run() {
		try {
			this.writeToExcel();
		} catch (Exception e) {
			this.exception = e;
			e.printStackTrace();
		}
	}
}
