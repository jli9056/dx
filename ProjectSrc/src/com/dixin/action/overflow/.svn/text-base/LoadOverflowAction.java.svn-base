/**
 * 
 */
package com.dixin.action.overflow;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.OverflowHelper;
import com.dixin.hibernate.Overflow;

/**
 * Load Action For JDO Overflow
 * 
 * @author Luo
 * 
 */
public class LoadOverflowAction extends GenericLoadAction<Overflow> {
	public LoadOverflowAction() {
		super(Overflow.class, OverflowHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
