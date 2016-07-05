/**
 * 
 */
package com.dixin.action.delivery;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.DeliveryHelper;
import com.dixin.hibernate.Delivery;

/**
 * Load Action For JDO Delivery
 * 
 * @author Luo
 * 
 */
public class LoadDeliveryAction extends GenericLoadAction<Delivery> {
	public LoadDeliveryAction() {
		super(Delivery.class, DeliveryHelper.class);
	}
}
