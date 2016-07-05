package com.dixin.action.databackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.dixin.action.ActionException;

public class ExcelReader implements Progressive {
	private static Logger log = Logger.getLogger(ExcelReader.class);
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private NumberFormat numberFormat = NumberFormat.getNumberInstance();
	{
		numberFormat.setGroupingUsed(false);
		numberFormat.setMinimumFractionDigits(0);
		numberFormat.setMinimumIntegerDigits(1);
	}
	private Connection connection;
	private String[] tables = { "customer",//
			"action",//
			"menu",//
			"role",//
			"menu_role",//
			"user",//
			"user_role",

			"product",//
			"shop",//
			"storehouse",//
			"repertory",//
			"employee",//

			"customer",//
			"corder",//
			"orderdetail",//
			"refund",//
			"payment",//

			"factoryorder",//
			"factoryorderdetail",//
			"arrivement",//
			"arrivedetail",//
			"overflow",//

			"available",//
			"reserved",//

			"delivery",//
			"deliverydetail"//

	};
	// Logger logger = Logger.getLogger(ExcelWriter.class);
	File file;

	ExcelReader(File f) {
		this.file = f;
	}

	ExcelReader(Connection conn, File f) {
		this.connection = conn;
		this.file = f;
	}

	public void readFromExcel() throws SQLException, IOException {
		readFromExcel(file);
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void readFromExcel(File f) throws SQLException, IOException {
		totalTables = 0;
		tableCount = 0;
		totalRows = 0;
		rowCount = 0;
		finished = false;
		currentTable = "";
		totalTables = tables.length;
		connection.setAutoCommit(false);
		try {
			HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(f));
			for (String tname : tables) {
				tableCount++;
				currentTable = tname;
				log.info("Read sheet for table " + currentTable);
				HSSFSheet sheet = book.getSheet(tname);
				if (sheet == null) {
					log.info("No sheet for table " + currentTable);
					continue;
				}
				readSheet(sheet, currentTable);
			}
			connection.commit();
		} catch (Exception ex) {
			connection.rollback();
			log.error("恢复数据时发生错误：", ex);
			throw new ActionException("恢复数据时发生错误：" + this.getProgress() + ":"
					+ ex.getMessage(), ex);
		} finally {
			connection.close();
		}
		finished = true;
	}

	int totalTables = 0;
	int tableCount = 0;
	int totalRows = 0;
	int rowCount = 0;
	boolean finished = false;
	String currentTable = "";
	Throwable exception;

	public synchronized Progress getProgress() {
		return new Progress(totalTables, tableCount, totalRows, rowCount,
				finished, currentTable, exception);
	}

	public synchronized void readSheet(HSSFSheet sheet, String tableName)
			throws SQLException {
		rowCount = 1;
		totalRows = sheet.getLastRowNum();
		HSSFRow row = sheet.getRow(rowCount);
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("select * from " + tableName);
		ResultSetMetaData meta = rs.getMetaData();
		int colCount = meta.getColumnCount();
		int[] colTypes = new int[colCount + 1];
		for (int i = 1; i <= colCount; i++) {
			colTypes[i] = meta.getColumnType(i);
		}
		rs.close();
		stmt.close();

		StringBuffer sql = new StringBuffer("insert into " + tableName
				+ " values(");
		for (int i = 1; i < colCount; i++) {
			sql.append("?,");
		}
		sql.append("?)");

		PreparedStatement pstmt = connection.prepareStatement(sql.toString());

		for (; rowCount <= totalRows; rowCount++) {
			row = sheet.getRow(rowCount);
			for (short i = 1, col = 0; i <= colCount; i++, col++) {
				HSSFCell cell = row.getCell(col);
				if (cell == null) {
					pstmt.setNull(i, colTypes[i]);
					continue;
				}
				switch (colTypes[i]) {
				case Types.BOOLEAN:
					pstmt.setBoolean(i, Boolean.valueOf(getCellValue(cell)));
					break;
				case Types.BIT:
				case Types.TINYINT:
				case Types.SMALLINT:
				case Types.INTEGER:
					pstmt.setInt(i, Double.valueOf(getCellValue(cell))
							.intValue());
					break;
				case Types.BIGINT:
					pstmt.setLong(i, Double.valueOf(getCellValue(cell))
							.longValue());
					break;
				case Types.FLOAT:
				case Types.REAL:
				case Types.DOUBLE:
				case Types.NUMERIC:
				case Types.DECIMAL:
					pstmt.setDouble(i, Double.valueOf(getCellValue(cell)));
					break;
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
					pstmt.setString(i, getCellValue(cell));
					break;
				case Types.DATE:
				case Types.TIME:
				case Types.TIMESTAMP:
					try {
						pstmt.setDate(i, new java.sql.Date(dateFormat.parse(
								getCellValue(cell)).getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
						throw new ActionException("日期格式错误", e);
					}
					break;
				default:
					throw new ActionException("不支持的数据类型");
				}
			}
			try {
				pstmt.executeUpdate();
			} catch (SQLException ex) {
				ex.printStackTrace();
				if (ex.getMessage().indexOf("Duplicate entry") > 0) {

				} else {
					throw ex;
				}
			}
		}
	}

	public String getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case HSSFCell.CELL_TYPE_ERROR:
			throw new ActionException("错误的单元格");
		case HSSFCell.CELL_TYPE_FORMULA:
			throw new ActionException("不支持公式！");
		case HSSFCell.CELL_TYPE_NUMERIC:
			return numberFormat.format(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().getString();
		}
		throw new ActionException("不支持的单元格类型");
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	public void run() {
		try {
			this.readFromExcel();
		} catch (Exception e) {
			this.exception = e;
			e.printStackTrace();
		}
	}
}
