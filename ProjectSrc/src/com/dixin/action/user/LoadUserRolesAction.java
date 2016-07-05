/**
 * 
 */
package com.dixin.action.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericLoadAction;
import com.dixin.action.model.CheckboxRole;
import com.dixin.action.util.ResultUtil;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.RoleHelper;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.Role;
import com.dixin.hibernate.User;

/**
 * Load Action For JDO User
 * 
 * @author Luo
 * 
 */
public class LoadUserRolesAction extends GenericLoadAction<User> {
	public LoadUserRolesAction() {
		super(User.class, UserHelper.class);
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String sid = request.getParameter("id");
		if (sid == null) {
			throw new ActionException("没有指定要加载的数据");
		}
		User t = getHelper().findById(Integer.parseInt(sid));
		IPagedResult<Role> rpr = new RoleHelper().findAll();
		List<Role> allRoles = rpr.getResult(0, rpr.count());
		List<CheckboxRole> CheckboxRoles = new ArrayList<CheckboxRole>();
		for (Role r : allRoles) {
			CheckboxRole ur = new CheckboxRole(r);
			ur.setChecked(r.getUsers().contains(t));
			CheckboxRoles.add(ur);
		}
		result.accumulate(ResultUtil.DATA, CheckboxRoles);
		result.accumulate("totalCount", CheckboxRoles.size());
		return true;
	}
}
