/**
 * 
 */
package com.dixin.action.customer;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.CustomerHelper;
import com.dixin.hibernate.Customer;

/**
 * Load Action For JDO Customer
 * @author Luo
 * 
 */
public class LoadCustomerAction extends GenericLoadAction<Customer> {
	public LoadCustomerAction() {
		super(Customer.class, CustomerHelper.class);
	}
}
