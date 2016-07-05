/**
 * 
 */
package com.dixin.action.user;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.RoleHelper;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.Role;
import com.dixin.hibernate.User;

/**
 * @author Luo
 * 
 */
public class SaveUserRolesAction extends GenericSaveAction<User> {
	public SaveUserRolesAction() {
		super(User.class, UserHelper.class);
		uniqueKeys = new String[] { "", "id" };
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String userId = request.getParameter("userId");
		String[] roleIds = request.getParameterValues("roles");
		if (userId == null) {
			throw new ActionException("未指定要分配角色的用户");
		}
		if (roleIds == null) {
			roleIds = new String[0];
		}
		User user = getHelper().findById(Integer.valueOf(userId));
		if (user == null) {
			throw new ActionException("指定的用户不存在");
		}
		Set<Role> roles = new HashSet<Role>();
		RoleHelper rhelper = new RoleHelper();
		for (String rid : roleIds) {
			if (rid.matches("[0-9]+")) {
				Role r = rhelper.findById(Integer.valueOf(rid));
				if (r != null) {
					roles.add(r);
				}
			}
		}
		user.setRoles(roles);
		getHelper().merge(user);
		return true;
	}
}
