/**
 * 
 */
package com.dixin.action.order;

import com.dixin.action.GenericRestoreAction;
import com.dixin.business.impl.OrderHelper;
import com.dixin.hibernate.Order;

/**
 * Restore Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class RestoreOrderAction extends GenericRestoreAction<Order> {
	public RestoreOrderAction() {
		super(Order.class, OrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
