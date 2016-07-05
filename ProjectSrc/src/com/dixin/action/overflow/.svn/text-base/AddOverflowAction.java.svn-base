/**
 * 
 */
package com.dixin.action.overflow;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.OverflowHelper;
import com.dixin.hibernate.Overflow;

/**
 * Add Action For JDO Overflow
 * 
 * @author Luo
 * 
 */
public class AddOverflowAction extends GenericAddAction<Overflow> {
	public AddOverflowAction() {
		super(Overflow.class, OverflowHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
