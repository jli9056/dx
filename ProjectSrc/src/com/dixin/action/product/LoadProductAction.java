/**
 * 
 */
package com.dixin.action.product;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.ProductHelper;
import com.dixin.hibernate.Product;

/**
 * Load Action For JDO Product
 * @author Luo
 * 
 */
public class LoadProductAction extends GenericLoadAction<Product> {
	public LoadProductAction() {
		super(Product.class, ProductHelper.class);
	}
}
