/**
 * 
 */
package com.dixin.action.shop;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.ShopHelper;
import com.dixin.hibernate.Shop;

/**
 * Save Action For JDO Shop
 * 
 * @author Luo
 * 
 */
public class SaveShopAction extends GenericSaveAction<Shop> {
	public SaveShopAction() {
		super(Shop.class, ShopHelper.class);
		uniqueKeys = new String[] { "", "id", "name" };
	}
}
