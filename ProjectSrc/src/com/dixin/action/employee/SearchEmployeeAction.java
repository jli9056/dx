/**
 * 
 */
package com.dixin.action.employee;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.EmployeeHelper;
import com.dixin.hibernate.Employee;

/**
 * Search Action For JDO Employee
 * @author Luo
 * 
 */
public class SearchEmployeeAction extends GenericSearchAction<Employee> {
	public SearchEmployeeAction() {
		super(Employee.class, EmployeeHelper.class);
	}
}
