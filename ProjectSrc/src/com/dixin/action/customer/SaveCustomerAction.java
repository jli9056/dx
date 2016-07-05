/**
 * 
 */
package com.dixin.action.customer;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.CustomerHelper;
import com.dixin.hibernate.Customer;

/**
 * Save Action For JDO Customer
 * 
 * @author Luo
 * 
 */
public class SaveCustomerAction extends GenericSaveAction<Customer> {
	public SaveCustomerAction() {
		super(Customer.class, CustomerHelper.class);
		uniqueKeys = new String[] { "", "id", "order_no" };
	}
}
