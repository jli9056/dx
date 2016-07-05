/**
 * 
 */
package com.dixin.action.order;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.OrderHelper;
import com.dixin.hibernate.Order;

/**
 * Search Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class SearchOrderAction extends GenericSearchAction<Order> {
	public SearchOrderAction() {
		super(Order.class, OrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected Criterion getXCriterion(String key, String value) {
		if ("hasRefund".equals(key) && "true".equals(value)) {
			return Restrictions.sqlRestriction("isValid=0 OR (SELECT COUNT(*) FROM orderdetail od WHERE od.fk_order_id={alias}.id AND od.isValid=0)>0");
		}
		return null;
	}
}
