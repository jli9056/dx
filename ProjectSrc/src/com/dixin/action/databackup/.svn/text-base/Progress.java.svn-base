/**
 * 
 */
package com.dixin.action.databackup;

import java.io.Serializable;

public class Progress implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int totalTables = 0;
	int tableCount = 0;
	int totalRows = 0;
	int rowCount = 0;
	boolean finished = false;
	String currentTable = "";
	Throwable exception;
	String fileName;

	public Progress(int totalTables, int tableCount, int totalRows,
			int rowCount, boolean finished, String currentTable, Throwable ex) {
		this.totalTables = totalTables;
		this.tableCount = tableCount;
		this.totalRows = totalRows;
		this.rowCount = rowCount;
		this.finished = finished;
		this.currentTable = currentTable;
		this.exception = ex;
	}

	public Throwable getException() {
		return exception;
	}

	public int getTotalTables() {
		return totalTables;
	}

	public void setTotalTables(int totalTables) {
		this.totalTables = totalTables;
	}

	public int getTableCount() {
		return tableCount;
	}

	public void setTableCount(int tableCount) {
		this.tableCount = tableCount;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRow) {
		this.totalRows = totalRow;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getCurrentTable() {
		return currentTable;
	}

	public void setCurrentTable(String currentTable) {
		this.currentTable = currentTable;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}