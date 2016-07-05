/**
 * 
 */
package com.dixin.action.delivery;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.DeliveryHelper;
import com.dixin.hibernate.Delivery;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchDeliveryAction extends GenericSearchAction<Delivery> {
	public SearchDeliveryAction() {
		super(Delivery.class, DeliveryHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
