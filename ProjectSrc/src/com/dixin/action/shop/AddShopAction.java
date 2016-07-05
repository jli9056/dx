/**
 * 
 */
package com.dixin.action.shop;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.ShopHelper;
import com.dixin.hibernate.Shop;

/**
 * Add Action For JDO Shop
 * 
 * @author Luo
 * 
 */
public class AddShopAction extends GenericAddAction<Shop> {
	public AddShopAction() {
		super(Shop.class, ShopHelper.class);
		uniqueKeys = new String[] { "", "id", "name" };
	}
}
