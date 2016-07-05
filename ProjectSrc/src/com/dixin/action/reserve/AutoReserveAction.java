package com.dixin.action.reserve;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.JsonAction;
import com.dixin.business.impl.ReserveHelper;
import com.dixin.hibernate.Order;

public class AutoReserveAction extends JsonAction {

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		ReserveHelper helper = new ReserveHelper();
		List<Order> r = helper.reserveAll();
		result.accumulate("totalCount", r.size());
		return true;
	}

	public String getLocalizedName() {
		return "自动留货";
	}
}
