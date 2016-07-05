/**
 * 
 */
package com.dixin.action.order;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.OrderHelper;
import com.dixin.hibernate.Order;

/**
 * Delete Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class DeleteOrderAction extends GenericDeleteAction<Order> {
	public DeleteOrderAction() {
		super(Order.class, OrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
