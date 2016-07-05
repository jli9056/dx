/**
 * 
 */
package com.dixin.action.reserve;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.DixinException;
import com.dixin.action.ActionException;
import com.dixin.action.GenericLoadAction;
import com.dixin.action.util.Messages;
import com.dixin.business.impl.OrderHelper;
import com.dixin.business.impl.OrderdetailHelper;
import com.dixin.business.impl.ReserveHelper;
import com.dixin.business.impl.StorehouseHelper;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Storehouse;

/**
 * @author Luo
 * 
 */
public class ReserveOrderAction extends GenericLoadAction<Order> {
	public ReserveOrderAction() {
		super(Order.class, OrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	public String getLocalizedName() {
		return "手动留货";
	}

	/*
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		ReserveHelper rvhelper = new ReserveHelper();
		String method = request.getParameter("method");
		if ("reserveDetail".equals(method)) {
			reserveDetail(request, result);
			return true;
		} else if ("unreserveDetail".equals(method)) {
			unreserveDetail(request, result);
			return true;
		} else if ("lockReserveDetail".equals(method)) {
			lockReserveDetail(request, result);
			return true;
		} else if ("unlockReserveDetail".equals(method)) {
			unlockReserveDetail(request, result);
			return true;
		} else if ("reserveMultiDetail".equals(method)) {
			reserveMultiDetail(request, result);
			return true;
		}

		String[] sid = request.getParameterValues("id");
		if (sid == null) {
			throw new ActionException("没有指定要留货的订单");
		}
		Map<String, String> list = new HashMap<String, String>();

		for (String s : sid) {
			try {
				Order t = getHelper().findById(Integer.parseInt(s));
				rvhelper.reserve(t);
			} catch (Exception ex) {
				ex.printStackTrace();
				list.put(s, Messages.getMessage(ex));
			}
		}
		result.accumulate("fails", list);
		return true;
	}

	public void reserveMultiDetail(HttpServletRequest request, JSONObject result) {
		String[] ids = request.getParameterValues("id");
		if (ids == null) {
			throw new ActionException("请选择至少一个订单明细");
		}
		int i = 0;
		try {
			OrderdetailHelper helper = new OrderdetailHelper();
			for (String sid : ids) {
				i++;
				int id = Integer.valueOf(sid);
				Orderdetail d = helper.findById(id);
				if (d == null) {
					throw new ActionException("所选订单第" + i
							+ "个明细不存在,请刷新后检查并重新操作");
				}
				new ReserveHelper().reserve(d);
			}
		} catch (NumberFormatException nfe) {
			throw new ActionException("输入有误，第" + i + "个订单明细号应该是整数");
		} catch (DixinException dex) {
			throw new ActionException(
					"给选中的第" + i + "条留货失败：" + dex.getMessage(), dex);
		}
	}

	public void reserveDetail(HttpServletRequest request, JSONObject result) {
		int detailId = 0;
		try {
			detailId = Integer.parseInt(request.getParameter("id"));
		} catch (Exception ex) {
			throw new ActionException("没有指定订单明细", ex);
		}
		int quantity = 0;
		try {
			quantity = Integer.parseInt(request.getParameter("quantity"));
		} catch (Exception ex) {
			throw new ActionException("请输正确的留货数量", ex);
		}
		Orderdetail d = new OrderdetailHelper().findById(detailId);
		Storehouse sh = new StorehouseHelper()//
				.findByProperty("name", //
						request.getParameter("storehouse"))//
				.getResult(0, 1)//
				.get(0);
		new ReserveHelper().reserve(d, sh, quantity);
	}

	public void lockReserveDetail(HttpServletRequest request, JSONObject result) {
		String[] ids = request.getParameterValues("id");
		try {
			for (String s : ids) {
				int detailId = 0;
				detailId = Integer.parseInt(s);
				new OrderdetailHelper().saveLockReserve(detailId);
			}
		} catch (DixinException dex) {
			throw dex;
		} catch (Exception ex) {
			throw new ActionException("发生错误，锁定可能没有全部完成，请刷新并检查锁定情况。", ex);
		}
	}

	public void unlockReserveDetail(HttpServletRequest request,
			JSONObject result) {
		String[] ids = request.getParameterValues("id");
		try {
			for (String s : ids) {
				int detailId = 0;
				detailId = Integer.parseInt(s);
				new OrderdetailHelper().saveUnlockReserve(detailId);
			}
		} catch (DixinException dex) {
			throw dex;
		} catch (Exception ex) {
			throw new ActionException("发生错误，解除锁定可能没有全部完成，请刷新并检查锁定情况。", ex);
		}

	}

	public void unreserveDetail(HttpServletRequest request, JSONObject result) {

		String[] ids = request.getParameterValues("id");
		try {
			ReserveHelper rhelper = new ReserveHelper();
			OrderdetailHelper odhelper = new OrderdetailHelper();
			for (String s : ids) {
				int detailId = 0;
				detailId = Integer.parseInt(s);
				Orderdetail d = odhelper.findById(detailId);
				rhelper.dereserve(d);
			}
		} catch (DixinException dex) {
			throw dex;
		} catch (Exception ex) {
			throw new ActionException("发生错误，取消留货可能没有全部完成，请刷新并检查留货情况。", ex);
		}
	}
}
