/**
 * 
 */
package com.dixin.action.ladingBill;

import java.util.Map;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.LadingBillHelper;
import com.dixin.hibernate.LadingBill;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchLadingBillAction extends GenericSearchAction<LadingBill> {
	public SearchLadingBillAction() {
		super(LadingBill.class, LadingBillHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected void setDefaultQueryParams(Map<String, String> paramsMap) {
		if (!paramsMap.containsKey("cr_finished_eq")) {
			paramsMap.put("cr_finished_eq", "否");
		}
	}
}
