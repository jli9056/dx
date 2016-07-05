/**
 * 
 */
package com.dixin.action.role;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Restrictions;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericLoadAction;
import com.dixin.action.model.CheckboxMenu;
import com.dixin.action.util.ResultUtil;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.MenuHelper;
import com.dixin.business.impl.RoleHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Menu;
import com.dixin.hibernate.Role;

/**
 * Load Action For JDO Role
 * 
 * @author Luo
 * 
 */
public class LoadRoleMenusAction extends GenericLoadAction<Role> {
	public LoadRoleMenusAction() {
		super(Role.class, RoleHelper.class);
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String sid = request.getParameter("id");
		if (sid == null) {
			throw new ActionException("没有指定要加载的数据");
		}
		Role t = getHelper().findById(Integer.parseInt(sid));
		MenuHelper mhelper = new MenuHelper();
		QueryDefn qd = new QueryDefn();
		qd.addCriterion(Restrictions.isNull("parent"));
		IPagedResult<Menu> pr = mhelper.find(qd);
		List<Menu> menus = pr.getResult(0, pr.count());
		List<CheckboxMenu> checklist = new ArrayList<CheckboxMenu>(20);
		for (Menu m : menus) {
			checklist.add(new CheckboxMenu(m, t));
		}
		result.accumulate(ResultUtil.DATA, checklist);
		return true;
	}
}
