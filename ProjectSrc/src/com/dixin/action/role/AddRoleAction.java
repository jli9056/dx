/**
 * 
 */
package com.dixin.action.role;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.RoleHelper;
import com.dixin.hibernate.Role;

/**
 * @author Luo
 * 
 */
public class AddRoleAction extends GenericAddAction<Role> {
	public AddRoleAction() {
		super(Role.class, RoleHelper.class);
		uniqueKeys = new String[] { "", "id", "model", "alias", "barcode" };
	}
}
