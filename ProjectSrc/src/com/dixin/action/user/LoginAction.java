package com.dixin.action.user;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.AbstractAction;
import com.dixin.action.ActionException;
import com.dixin.action.CurrentUser;
import com.dixin.business.DataException;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.User;
import com.dixin.util.AuthoriseUtil;
import com.dixin.util.SecurityUtil;

/**
 * 
 * @author Luo
 * 
 */
public class LoginAction extends AbstractAction<User> {

	public LoginAction() {
		super(User.class, UserHelper.class);
	}

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String logout = request.getParameter("logout");
		if ("true".equals(logout)) {
			request.getSession().invalidate();
			return true;
		}
		
		try {
			AuthoriseUtil.authorize();
		} catch (DataException ex) {
			throw new ActionException(ex.getLocalizedMessage());
		}
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null) {
			throw new ActionException("请输入用户名！");
		}
		if (password == null) {
			throw new ActionException("请输入密码！");
		}
		UserHelper helper = (UserHelper) getHelper();
		User user = helper.getUserByName(username);
		password = SecurityUtil.encode(password);
		if (user == null || !password.equals(user.getUserPass())) {
			throw new ActionException("用户名或者密码不正确，请重新输入！");
		}
		CurrentUser theUser = new CurrentUser(user);
		log.debug("User login:" + theUser.getUserName());
		log.debug("Menus of the current User:" + theUser.getMenus());
		log.debug("Authorized Actions:" + theUser.getActions());
		CurrentUser.setSessionUser(request.getSession(), theUser);
		return true;
	}

	public String getLocalizedName() {
		return "登录";
	}
}
