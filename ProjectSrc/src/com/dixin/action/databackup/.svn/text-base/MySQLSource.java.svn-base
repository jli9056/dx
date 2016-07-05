package com.dixin.action.databackup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.dbcp.BasicDataSource;

import com.dixin.AppContextFactory;
import com.dixin.DixinException;
import com.dixin.action.ActionException;

public class MySQLSource implements Progressive {

	public Progress getProgress() {
		Progress p = new Progress(totalTables, tableCount, totalRows, rowCount,
				finished, currentTable, exception);
		return p;
	}

	private String currentTable;
	private int tableCount = 0;
	private int totalTables = 33;
	private int totalRows;
	private int rowCount;
	private boolean finished;
	private Throwable exception;

	private Thread thread;
	private File file;

	public MySQLSource() {
	}

	private boolean schema = true;
	private boolean data = true;

	public MySQLSource(File f, boolean schema, boolean data) {
		this.file = f;
		this.schema = schema;
		this.data = data;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
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

	public int getTotalRows() {
		return totalRows;
	}

	public int getRowCount() {
		return rowCount;
	}

	public boolean isFinished() {
		return finished;
	}

	public Thread getThread() {
		return thread;
	}

	private static File unzip(File f, String entry) throws IOException {
		File tmp = File.createTempFile("sqlsourcetemp", "");
		tmp.deleteOnExit();
		OutputStream out = new FileOutputStream(tmp);
		ZipInputStream in = new ZipInputStream(new FileInputStream(f));
		ZipEntry zen = in.getNextEntry();
		while (zen != null) {
			if (entry.equalsIgnoreCase(zen.getName())) {
				byte[] buf = new byte[1024 * 40];
				int count = 0;
				while ((count = in.read(buf)) >= 0) {
					out.write(buf, 0, count);
				}
				out.flush();
				out.close();
				in.close();
				return tmp;
			}
			zen = in.getNextEntry();
		}
		throw new ActionException("上传的文件中没有找到需要的数据。");
	}

	static String dumpTablePrefix = "-- Dumping data for table `";

	public int source(File f, boolean schema, boolean data) {
		int ret = 0;
		if (schema) {
			ret = cmdSource(f, "SCHEMA");
		}
		if (data) {
			ret += cmdSource(f, "DATA");
		}
		return ret;
	}

	private int cmdSource(File f, String entry) {
		try {

			f = unzip(f, entry);
			BasicDataSource ds = (BasicDataSource) AppContextFactory
					.getAppContext().getBean("ds");
			Process p = Runtime.getRuntime().exec(
					"mysql -f -uroot -p" + ds.getPassword());
			final BufferedReader stderr = new BufferedReader(
					new InputStreamReader(p.getErrorStream()));
			PrintWriter stdin = new PrintWriter(p.getOutputStream());

			// print stdout
			new Thread() {
				public void run() {
					String line = null;
					try {
						while ((line = stderr.readLine()) != null) {
							System.err.println(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();

			// print err stream
			final BufferedReader stdout = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			new Thread() {
				public void run() {
					String line = null;
					try {
						while ((line = stdout.readLine()) != null) {
							System.out.println(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();

			// input command
			stdin.flush();
			stdin.println("use dixin");
			stdin.flush();

			stdin.println("source " + f.getAbsolutePath());
			stdin.flush();

			stdin.flush();
			stdin.println("quit");
			stdin.flush();

			while (true)
				try {
					int ret = p.exitValue();

					return ret;
				} catch (Exception ex) {
					try {
						Thread.sleep(100);
					} catch (Exception exx) {
					}
				}
		} catch (IOException e) {
			this.exception = e;
			e.printStackTrace();
			throw new DixinException("恢复数据发生错误:" + e);
		} finally {
			this.tableCount = this.totalTables;
			this.finished = true;
		}
	}

	public void source() {
		source(file, schema, data);
	}

	public void run() {
		source();
	}

}
