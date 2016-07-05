/**
 * 
 */
package com.dixin.action.role;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.RoleHelper;
import com.dixin.hibernate.Role;

/**
 * @author Luo
 * 
 */
public class SearchRoleAction extends GenericSearchAction<Role> {
	public SearchRoleAction() {
		super(Role.class, RoleHelper.class);
	}
}
