/**
 * 
 */
package com.dixin.action.storehouse;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.StorehouseHelper;
import com.dixin.hibernate.Storehouse;

/**
 * Save Action For JDO Storehouse
 * 
 * @author Luo
 * 
 */
public class SaveStorehouseAction extends GenericSaveAction<Storehouse> {
	public SaveStorehouseAction() {
		super(Storehouse.class, StorehouseHelper.class);
		uniqueKeys = new String[] { "", "id", "name" };
	}
}
