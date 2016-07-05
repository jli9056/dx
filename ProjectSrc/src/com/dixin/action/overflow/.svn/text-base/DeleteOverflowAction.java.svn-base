/**
 * 
 */
package com.dixin.action.overflow;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.OverflowHelper;
import com.dixin.hibernate.Overflow;

/**
 * Delete Action For JDO Overflow
 * 
 * @author Luo
 * 
 */
public class DeleteOverflowAction extends GenericDeleteAction<Overflow> {
	public DeleteOverflowAction() {
		super(Overflow.class, OverflowHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
