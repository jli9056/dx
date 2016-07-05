package com.dixin.action.payment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.OrderHelper;
import com.dixin.business.impl.PaymentHelper;
import com.dixin.hibernate.Order;

/**
 * use searchOrderAction instead
 * 
 * @author Luo
 * 
 */
@Deprecated
public class FindUnpayedOrderAction extends GenericSearchAction<Order> {

	public FindUnpayedOrderAction() {
		super(Order.class, OrderHelper.class);
	}

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String limit = request.getParameter("limit");
		String start = request.getParameter("start");

		if (limit == null) {
			limit = "20";
		}
		if (start == null) {
			start = "0";
		}
		int st = Integer.parseInt(start);
		int li = Integer.parseInt(limit);
		if (sort == null) {
			sort = "orderDate";
		}
		if (dir == null) {
			dir = "asc";
		}

		if (!sort.matches("[a-zA-Z]+")) {
			throw new ActionException("排序属性错误");
		}
		if (!dir.matches("(asc|desc|ASC|DESC)")) {
			throw new ActionException("排序方式错误");
		}

		List<Order> orders = new PaymentHelper().findUnpayedOrders(sort, dir,
				li, st);
		result.element("data", orders);
		return true;
	}

	public String getLocalizedName() {
		return "查找未付清款订单";
	}

}
