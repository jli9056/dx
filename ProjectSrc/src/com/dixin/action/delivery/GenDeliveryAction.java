/**
 * 
 */
package com.dixin.action.delivery;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.GenericLoadAction;

import com.dixin.business.impl.DeliveryHelper;
import com.dixin.business.impl.OrderdetailHelper;
import com.dixin.hibernate.Orderdetail;

/**
 * Add Action For JDO Delivery
 * 
 * @author Luo
 * 
 */
public class GenDeliveryAction extends GenericLoadAction<Orderdetail> {
	public GenDeliveryAction() {
		super(Orderdetail.class, OrderdetailHelper.class);
		uniqueKeys = new String[] { "", "id", "orderDate", "orderNo",
				"employee" };
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		DeliveryHelper helper = new DeliveryHelper();

		Date end = new Date();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 10);
		List<Orderdetail> orders = helper.findDeliverableOderdetail(null, end);
		result.element("data", orders);
		return true;
	}
}
