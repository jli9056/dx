/**
 * 
 */
package com.dixin.action.employee;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.EmployeeHelper;
import com.dixin.hibernate.Employee;

/**
 * Delete Action For JDO Employee
 * @author Luo
 * 
 */
public class DeleteEmployeeAction extends GenericDeleteAction<Employee> {
	public DeleteEmployeeAction() {
		super(Employee.class, EmployeeHelper.class);
	}
}
