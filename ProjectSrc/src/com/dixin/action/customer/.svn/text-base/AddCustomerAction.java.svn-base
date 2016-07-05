/**
 * 
 */
package com.dixin.action.customer;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.CustomerHelper;
import com.dixin.hibernate.Customer;

/**
 * Add Action For JDO Customer
 * 
 * @author Luo
 * 
 */
public class AddCustomerAction extends GenericAddAction<Customer> {
	public AddCustomerAction() {
		super(Customer.class, CustomerHelper.class);
		uniqueKeys = new String[] { "", "id", "order_no" };
	}
}
