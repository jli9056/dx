package com.dixin.action.databackup;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.JsonAction;

public class DataImportAction extends JsonAction {
	private DataSource dataSource;
	static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		log.debug("getting progress");
		Progressive run = (Progressive) request.getSession().getAttribute(
				"MySQLSource");
		Progress p = run.getProgress();
		if (p.getException() != null) {
			throw new ActionException("恢复数据失败", p.getException());
		}
		result.element("data", p);
		return true;
	}

	public String getLocalizedName() {
		return "恢复数据";
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
