/**
 * 
 */
package com.dixin.action.role;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.RoleHelper;
import com.dixin.hibernate.Role;

/**
 * @author Luo
 * 
 */
public class SaveRoleAction extends GenericSaveAction<Role> {
	public SaveRoleAction() {
		super(Role.class, RoleHelper.class);
		uniqueKeys = new String[] { "", "id", "model", "alias", "barcode" };
	}

	protected Role validate(Role r) {
		Role o = getHelper().findById(r.getId());
		r.setMenus(o.getMenus());
		r.setUsers(o.getUsers());
		return r;
	}
}
