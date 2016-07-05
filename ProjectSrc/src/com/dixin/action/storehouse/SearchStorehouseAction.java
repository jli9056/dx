/**
 * 
 */
package com.dixin.action.storehouse;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.StorehouseHelper;
import com.dixin.hibernate.Storehouse;

/**
 * Search Action For JDO Storehouse
 * @author Luo
 * 
 */
public class SearchStorehouseAction extends GenericSearchAction<Storehouse> {
	public SearchStorehouseAction() {
		super(Storehouse.class, StorehouseHelper.class);
	}
}
