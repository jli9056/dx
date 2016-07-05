package com.dixin.action.databackup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import com.dixin.action.CurrentUser;
import com.dixin.action.JsonAction;

public class DataExportAction extends JsonAction {
	private DataSource dataSource;
	static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String fileName = request.getParameter("filename");
		if ("undefined".equalsIgnoreCase(fileName)
				|| "null".equalsIgnoreCase(fileName)) {
			fileName = null;
		}
		String tmpDir = System.getProperty("java.io.tmpdir");
		Progress p = null;
		Progressive run;
		if (fileName != null) {
			run = (Progressive) request.getSession().getAttribute(fileName);
			p = run.getProgress();
		} else {
			fileName = CurrentUser.getCurrentUser().getUserName()
					+ df.format(new Date());
			run = new MySQLDump(new File(tmpDir, fileName));

			new Thread(run).start();

			request.getSession().setAttribute(fileName, run);
			p = run.getProgress();
		}
		p.setFileName(fileName);
		result.element("data", p);
		return true;
	}

	public String getLocalizedName() {
		return "备份数据库数据";
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
