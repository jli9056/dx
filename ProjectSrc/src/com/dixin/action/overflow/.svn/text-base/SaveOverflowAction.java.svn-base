/**
 * 
 */
package com.dixin.action.overflow;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.OverflowHelper;
import com.dixin.hibernate.Overflow;

/**
 * Add Action For JDO Overflow
 * 
 * @author Luo
 * 
 */
public class SaveOverflowAction extends GenericSaveAction<Overflow> {
	public SaveOverflowAction() {
		super(Overflow.class, OverflowHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

}
