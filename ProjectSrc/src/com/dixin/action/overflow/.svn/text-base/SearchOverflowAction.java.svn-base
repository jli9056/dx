/**
 * 
 */
package com.dixin.action.overflow;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.OverflowHelper;
import com.dixin.hibernate.Overflow;

/**
 * Search Action For JDO Overflow
 * 
 * @author Luo
 * 
 */
public class SearchOverflowAction extends GenericSearchAction<Overflow> {
	public SearchOverflowAction() {
		super(Overflow.class, OverflowHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
