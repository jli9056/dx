/**
 * 
 */
package com.dixin.action.product;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.ProductHelper;
import com.dixin.hibernate.Product;

/**
 * @author Luo
 * 
 */
public class AddProductAction extends GenericAddAction<Product> {
	public AddProductAction() {
		super(Product.class, ProductHelper.class);
		uniqueKeys = new String[] { "", "id", "model", "alias", "barcode" };
	}
}
