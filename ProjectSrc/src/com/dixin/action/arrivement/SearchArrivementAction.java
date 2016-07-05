/**
 * 
 */
package com.dixin.action.arrivement;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.ArrivementHelper;
import com.dixin.hibernate.Arrivement;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchArrivementAction extends GenericSearchAction<Arrivement> {
	public SearchArrivementAction() {
		super(Arrivement.class, ArrivementHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
