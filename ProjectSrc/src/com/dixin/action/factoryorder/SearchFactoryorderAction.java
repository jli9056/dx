/**
 * 
 */
package com.dixin.action.factoryorder;

import java.util.Map;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.FactoryOrderHelper;
import com.dixin.hibernate.Factoryorder;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchFactoryorderAction extends GenericSearchAction<Factoryorder> {
	public SearchFactoryorderAction() {
		super(Factoryorder.class, FactoryOrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected void setDefaultQueryParams(Map<String, String> paramsMap) {
		if (!paramsMap.containsKey("cr_finished_eq")) {
			paramsMap.put("cr_finished_eq", "否");
		}
	}

}
