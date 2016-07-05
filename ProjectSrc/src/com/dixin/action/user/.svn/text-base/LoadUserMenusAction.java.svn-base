/**
 * 
 */
package com.dixin.action.user;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import com.dixin.action.CurrentUser;
import com.dixin.action.GenericLoadAction;
import com.dixin.action.util.ResultUtil;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.User;

/**
 * Load Action For JDO Role
 * 
 * @author Luo
 * 
 */
public class LoadUserMenusAction extends GenericLoadAction<User> {
	public LoadUserMenusAction() {
		super(User.class, UserHelper.class);
	}

	public String getLocalizedName() {
		return "加载用户菜单";
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		CurrentUser cur = CurrentUser.getCurrentUser();
		result.accumulate(ResultUtil.DATA, cur.getMenus());
		return true;
	}
}
