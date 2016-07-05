package com.dixin.action.clean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.JsonAction;
import com.dixin.util.DBUtil;

public class CleanDataAction extends JsonAction {

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Date start = null;
		Date end = null;
		try {
			start = dateFormat.parse(request.getParameter("startDate"));
			end = dateFormat.parse(request.getParameter("endDate"));
		} catch (Exception ex) {
			throw new ActionException("日期格式错误", ex);
		}
		if (start.after(end)) {
			throw new ActionException("起始日期必须在截止日期之前");
		}
		DBUtil.removeOrderData(start, end);
		return true;
	}

	public String getLocalizedName() {
		return "清理数据";
	}

}
