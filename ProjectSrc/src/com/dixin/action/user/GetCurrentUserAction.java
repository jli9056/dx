package com.dixin.action.user;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.CurrentUser;
import com.dixin.action.JsonAction;
import com.dixin.hibernate.convert.JSONalizer;

public class GetCurrentUserAction extends JsonAction {

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		result.accumulate("data", JSONalizer.getInstance().toJSONString(
				CurrentUser.getCurrentUser()));
		return true;
	}

	public String getLocalizedName() {
		return "取当前用户信息";
	}

}
