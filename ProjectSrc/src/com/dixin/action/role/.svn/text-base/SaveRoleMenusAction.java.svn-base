/**
 * 
 */
package com.dixin.action.role;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.MenuHelper;
import com.dixin.business.impl.RoleHelper;
import com.dixin.hibernate.Menu;
import com.dixin.hibernate.Role;

/**
 * @author Luo
 * 
 */
public class SaveRoleMenusAction extends GenericSaveAction<Role> {
	public SaveRoleMenusAction() {
		super(Role.class, RoleHelper.class);
		uniqueKeys = new String[] { "", "id", "model", "alias", "barcode" };
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String roleId = request.getParameter("id");
		String[] menuIds = request.getParameterValues("menus");
		if (roleId == null) {
			throw new ActionException("角色ID未指定");
		}
		if (menuIds == null) {
			menuIds = new String[0];
		}
		Role r = getHelper().findById(Integer.valueOf(roleId));
		Set<Menu> menus = new HashSet<Menu>();
		MenuHelper rhelper = new MenuHelper();
		for (String mid : menuIds) {
			if (mid.matches("^[0-9]+$")) {
				Menu m = rhelper.findById(Integer.parseInt(mid));
				if (r != null) {
					menus.add(m);
				}
			}
		}
		r.setMenus(menus);
		getHelper().save(r);
		return true;
	}
}
