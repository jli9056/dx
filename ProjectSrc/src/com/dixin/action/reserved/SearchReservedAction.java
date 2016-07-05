/**
 * 
 */
package com.dixin.action.reserved;

import java.util.HashSet;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.IPagedResult;
import com.dixin.business.IQueryDefn;
import com.dixin.business.impl.OrderHelper;
import com.dixin.business.impl.OrderdetailHelper;
import com.dixin.business.impl.ReservedHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Reserved;

/**
 * Load Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class SearchReservedAction extends GenericSearchAction<Reserved> {
	public SearchReservedAction() {
		super(Reserved.class, ReservedHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	@SuppressWarnings("unchecked")
	protected Criterion getXCriterion(String key, String value) {
		if ("orderNo".equals(key)) {
			IPagedResult<Order> pr = new OrderHelper().findByProperty(
					"orderNo", value);
			HashSet<Orderdetail> details = new HashSet<Orderdetail>();
			List<Order> order = pr.getResult(0, pr.count());
			for (Order o : order) {
				details.addAll(o.getOrderdetails());
			}
			return Restrictions.in("orderdetail", details);
		} else if ("orderdetail".equals(key)) {
			String[] sids = value.split(",");
			Integer[] id = new Integer[sids.length];
			for (int i = 0; i < sids.length; i++) {
				id[i] = Integer.valueOf(sids[i]);
			}
			IQueryDefn def = new QueryDefn();
			def.addCriterion(Restrictions.in("id", id));
			IPagedResult<Orderdetail> pr = new OrderdetailHelper().find(def);
			List<Orderdetail> details = pr.getResult(0, pr.count());
			return Restrictions.in("orderdetail", details);
		}
		return null;
	}
}
