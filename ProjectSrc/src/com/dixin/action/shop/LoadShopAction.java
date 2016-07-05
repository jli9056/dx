/**
 * 
 */
package com.dixin.action.shop;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.ShopHelper;
import com.dixin.hibernate.Shop;

/**
 * Load Action For JDO Shop
 * @author Luo
 * 
 */
public class LoadShopAction extends GenericLoadAction<Shop> {
	public LoadShopAction() {
		super(Shop.class, ShopHelper.class);
	}
}
