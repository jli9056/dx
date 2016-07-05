/**
 * 
 */
package com.dixin.action.reserve;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.JsonAction;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.ReserveHelper;
import com.dixin.hibernate.Order;

/**
 * @author Luo
 * 
 */
public class UnreservedOrdersAction extends JsonAction {

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		ReserveHelper helper = new ReserveHelper();
		IPagedResult<Order> ip = helper.findUnreservedOrders();
		List<Order> r = ip.getResult(0, ip.count());
		result.accumulate("totalCount", r.size());
		result.accumulate("data", r);
		return true;
	}

	public String getLocalizedName() {
		return "未留货订单列表";
	}
}
