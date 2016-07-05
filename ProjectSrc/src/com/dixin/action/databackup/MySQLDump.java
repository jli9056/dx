package com.dixin.action.databackup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.dbcp.BasicDataSource;

import com.dixin.AppContextFactory;
import com.dixin.DixinException;

public class MySQLDump implements Progressive {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		File f = File.createTempFile("sqld", ".data");
		System.out.println(new MySQLDump().dump(f));
		System.out.println("Finished:" + f.getAbsolutePath());
		new MySQLSource().source(f, true, true);
	}

	static String dumpTablePrefix = "-- Dumping data for table `";
	private String currentTable;
	private int tableCount = 0;
	private int totalTables = 33;
	private int totalRows;
	private int rowCount;
	private boolean finished;
	private Throwable exception;

	private Thread thread;
	private File file;

	public MySQLDump() {
	}

	public MySQLDump(File f) {
		this.file = f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.action.databackup.Progressive#getProgress()
	 */
	public Progress getProgress() {
		Progress p = new Progress(totalTables, tableCount, totalRows, rowCount,
				finished, currentTable, exception);
		return p;
	}

	public String getCurrentTable() {
		return currentTable;
	}

	public int getTableCount() {
		return tableCount;
	}

	public int getTotalTables() {
		return totalTables;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Thread getThread() {
		return thread;
	}

	public int dump() {
		return dump(file);
	}

	private void resetProgress() {
		currentTable = "";
		tableCount = 0;
		totalTables = 33;
		totalRows = 0;
		rowCount = 0;
		finished = false;
	}

	public int dump(File f) {
		try {
			resetProgress();
			// SCHEMA
			BasicDataSource ds = (BasicDataSource) AppContextFactory
					.getAppContext().getBean("ds");
			Process p = Runtime.getRuntime().exec(//
					"mysqldump -uroot -p" + ds.getPassword()// 
							+ " -d dixin ");// no data
			BufferedReader reader = new BufferedReader(new InputStreamReader(p
					.getInputStream(), "utf-8"));
			ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(f));
			zip.putNextEntry(new ZipEntry("SCHEMA"));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(zip,
					"utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(dumpTablePrefix)) {
					currentTable = line.substring(dumpTablePrefix.length(),
							line.length() - 1);
					tableCount++;
					System.out.println("正在备份数据表" + currentTable);
				}
				out.println(line);
			}
			out.flush();
			zip.closeEntry();
			// DATA
			p = Runtime.getRuntime().exec(//
					"mysqldump -uroot -p" + ds.getPassword()// 
							+ " -c -t dixin ");// NO Schema
			reader = new BufferedReader(new InputStreamReader(p
					.getInputStream(), "utf-8"));
			zip.putNextEntry(new ZipEntry("DATA"));
			out = new PrintWriter(new OutputStreamWriter(zip, "utf-8"));
			line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(dumpTablePrefix)) {
					currentTable = line.substring(dumpTablePrefix.length(),
							line.length() - 1);
					tableCount++;
					System.out.println("正在备份数据表" + currentTable);
				}
				out.println(line);
			}
			out.flush();
			out.close();
			finished = true;
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			this.exception = e;
			finished = true;
			throw new DixinException("备份数据发生错误:" + e);
		}

	}

	public void run() {
		this.dump();
	}
}
