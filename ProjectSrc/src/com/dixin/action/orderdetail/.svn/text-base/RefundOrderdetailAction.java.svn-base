/**
 * 
 */
package com.dixin.action.orderdetail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.AbstractAction;
import com.dixin.action.ActionException;
import com.dixin.action.util.ResultUtil;
import com.dixin.business.DataException;
import com.dixin.business.impl.OrderHelper;
import com.dixin.business.impl.OrderdetailHelper;
import com.dixin.hibernate.Orderdetail;

/**
 * @author Jason
 * 
 */
public class RefundOrderdetailAction extends AbstractAction<Orderdetail> {

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public RefundOrderdetailAction() {
		super(Orderdetail.class, OrderdetailHelper.class);
	}

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String[] ids = request.getParameterValues("id");
		try {
			int refundCount = Integer.parseInt(request
					.getParameter("refundCount"));
			if (refundCount <= 0) {
				throw new ActionException("退货数量不能为零或负数！");
			}
			double refundAmount = Double.parseDouble(request
					.getParameter("refundAmount"));
			if (refundAmount < 0) {
				throw new ActionException("退款金额不能为负数！");
			}
			Date refundDate;
			refundDate = df.parse(request.getParameter("refundDate"));
			String refundMethod = request.getParameter("refundMethod");
			String comment = request.getParameter("comment");
			OrderHelper orderHelper = new OrderHelper();
			if (ids != null && ids.length > 0) {
				for (String id : ids) {
					try {
						Orderdetail od = getHelper().findById(
								Integer.parseInt(id));
						orderHelper
								.refundOrderdetail(od, refundCount,
										refundAmount, refundDate, refundMethod,
										comment);
					} catch (DataException e) {
						throw new ActionException("退单过程中发生错误！", e);
					}
				}
				return true;
			}
		} catch (ParseException e1) {
			result = ResultUtil.getFailureJSON(e1.toString());
			return false;
		}
		return false;
	}

	@Override
	public String getLocalizedName() {
		return "客户订单明细退货";
	}

}
