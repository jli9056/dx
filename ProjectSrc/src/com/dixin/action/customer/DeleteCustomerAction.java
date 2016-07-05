/**
 * 
 */
package com.dixin.action.customer;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.CustomerHelper;
import com.dixin.hibernate.Customer;

/**
 * Delete Action For JDO Customer
 * @author Luo
 * 
 */
public class DeleteCustomerAction extends GenericDeleteAction<Customer> {
	public DeleteCustomerAction() {
		super(Customer.class, CustomerHelper.class);
	}
}
