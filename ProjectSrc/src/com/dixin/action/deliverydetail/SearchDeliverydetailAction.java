/**
 * 
 */
package com.dixin.action.deliverydetail;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.DeliverydetailHelper;
import com.dixin.hibernate.Deliverydetail;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchDeliverydetailAction extends
		GenericSearchAction<Deliverydetail> {
	public SearchDeliverydetailAction() {
		super(Deliverydetail.class, DeliverydetailHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
