/**
 * 
 */
package com.dixin.action.orderdetail;

import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.ArrangementHelper;
import com.dixin.business.impl.OrderdetailHelper;
import com.dixin.hibernate.Orderdetail;

/**
 * Search Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class SearchOrderdetailAction extends GenericSearchAction<Orderdetail> {
	public SearchOrderdetailAction() {
		super(Orderdetail.class, OrderdetailHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	ArrangementHelper _arrhelper;

	ArrangementHelper getArrangementHelper() {
		return _arrhelper == null ? (_arrhelper = new ArrangementHelper())
				: _arrhelper;
	}

	protected Criterion getXCriterion(String key, String value) {
		if ("deliverable".equals(key) && "true".equals(value)) {
			return Restrictions.gt("reservedCount", 0);
		}
		if ("arrangable".equals(key) && "true".equals(value)) {
			//订单明细可以排货 当且仅当 该订单明细已留货且对应的未完成排货数量和未确认送货数量小于已留货数量
			return Restrictions
					.and(
							Restrictions.gt("reservedCount", 0),
							Restrictions
									.sqlRestriction(" reserved_count>IFNULL((select sum(a.quantity) from arrangement a where a.fk_orderdetail_id={alias}.id and a.is_finished=0),0)+IFNULL((select sum(d.quantity) from deliverydetail d,delivery dy where d.fk_order_id={alias}.fk_order_id and d.fk_product_id={alias}.fk_product_id and d.fk_delivery_id=dy.id and dy.doubleCheck=0),0)"));
		}
		if ("needProduct".equals(key) && !"".equals(value)) {
			return Restrictions
					.sqlRestriction("id in (select a.fk_orderdetail_id from available a where a.consumed_count>0 and a.fk_factoryorderdetail_id="
							+ value + ")");
		}
		if ("finished".equals(key)) {
			if ("on".equals(value)) {
				// return Restrictions.geProperty("deliveredCount", "quantity");
			} else {
				return Restrictions.ltProperty("deliveredCount", "quantity");
			}
		}
		if ("needToDeliver".equals(key) && "on".equals(value)) {
			return Restrictions
					.sqlRestriction("quantity-delivered_count-reserved_count>0");
		}
		return null;
	}

	protected void setDefaultQueryParams(Map<String, String> paramsMap) {
		if (!paramsMap.containsKey("cr_finished_X")) {
			paramsMap.put("cr_finished_X", "off");
		}
	}
}
