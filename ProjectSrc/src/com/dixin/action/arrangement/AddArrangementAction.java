/**
 * 
 */
package com.dixin.action.arrangement;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.ArrangementHelper;
import com.dixin.business.impl.OrderdetailHelper;
import com.dixin.hibernate.Arrangement;
import com.dixin.hibernate.Orderdetail;

/**
 * Add Action For JDO Arrangement
 * 
 * @author Luo
 * 
 */
public class AddArrangementAction extends GenericAddAction<Arrangement> {
	public AddArrangementAction() {
		super(Arrangement.class, ArrangementHelper.class);
		uniqueKeys = new String[] { "", "id", "orderDate", "orderNo",
				"employee" };
	}

	private OrderdetailHelper _odhelper;

	OrderdetailHelper getOdhelper() {
		return _odhelper == null ? (_odhelper = new OrderdetailHelper())
				: _odhelper;
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String[] ids = request.getParameterValues("detailId");
		OrderdetailHelper odh = getOdhelper();
		Date update = new Date();
		for (String id : ids) {
			Arrangement ar = new Arrangement();
			Orderdetail od = odh.findById(Integer.valueOf(id));
			int amount = od.getReservedCount();
			if (amount <= 0) {
				throw new ActionException("订单[" + od.getOrder().getOrderNo()
						+ "]的一个明细不可以送货。");
			}
			ar.setQuantity(amount);
			ar.setOrderdetail(od);
			ar.setComment(od.getComment());
			ar.setDeliverDate(od.getDeliverDate());
			ar.setIsFinished(0);
			ar.setQueueTime(od.getDeliverDate());
			ar.setLastUpdate(update);
			this.getHelper().merge(ar);
		}
		return true;
	}
}
