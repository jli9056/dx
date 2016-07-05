/**
 * 
 */
package com.dixin.action.shop;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.ShopHelper;
import com.dixin.hibernate.Shop;

/**
 * Search Action For JDO Shop
 * @author Luo
 * 
 */
public class SearchShopAction extends GenericSearchAction<Shop> {
	public SearchShopAction() {
		super(Shop.class, ShopHelper.class);
	}
}
