/**
 * 
 */
package com.dixin.action.arrivedetail;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.ArrivedetailHelper;
import com.dixin.hibernate.Arrivedetail;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchArrivedetailAction extends GenericSearchAction<Arrivedetail> {
	public SearchArrivedetailAction() {
		super(Arrivedetail.class, ArrivedetailHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}
}
