/**
 * 
 */
package com.dixin.action.order;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.OrderHelper;
import com.dixin.hibernate.Order;

/**
 * Load Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class LoadOrderAction extends GenericLoadAction<Order> {
	public LoadOrderAction() {
		super(Order.class, OrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
