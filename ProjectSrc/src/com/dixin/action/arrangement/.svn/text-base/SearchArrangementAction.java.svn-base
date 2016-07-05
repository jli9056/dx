/**
 * 
 */
package com.dixin.action.arrangement;

import java.util.Map;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.ArrangementHelper;
import com.dixin.hibernate.Arrangement;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchArrangementAction extends GenericSearchAction<Arrangement> {
	public SearchArrangementAction() {
		super(Arrangement.class, ArrangementHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
	
	protected void setDefaultQueryParams(Map<String, String> paramsMap) {
		if (!paramsMap.containsKey("cr_isFinished_eq")
				|| "".equals(paramsMap.get("cr_isFinished_eq"))) {
			paramsMap.put("cr_isFinished_eq", "0");
		}
	}
}
