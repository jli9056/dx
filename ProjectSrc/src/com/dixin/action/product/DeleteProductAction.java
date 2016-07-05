/**
 * 
 */
package com.dixin.action.product;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.ProductHelper;
import com.dixin.hibernate.Product;

/**
 * @author Luo
 * 
 */
public class DeleteProductAction extends GenericDeleteAction<Product> {
	public DeleteProductAction() {
		super(Product.class, ProductHelper.class);
	}
}
