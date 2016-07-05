/**
 * 
 */
package com.dixin.action.product;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.ProductHelper;
import com.dixin.hibernate.Product;

/**
 * @author Luo
 * 
 */
public class SaveProductAction extends GenericSaveAction<Product> {
	public SaveProductAction() {
		super(Product.class, ProductHelper.class);
		uniqueKeys = new String[] { "", "id", "model", "alias", "barcode" };
	}
}
