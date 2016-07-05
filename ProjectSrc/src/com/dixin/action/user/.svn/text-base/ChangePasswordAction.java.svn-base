package com.dixin.action.user;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.CurrentUser;
import com.dixin.action.JsonAction;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.User;
import com.dixin.util.SecurityUtil;

public class ChangePasswordAction extends JsonAction {

	public boolean process(HttpServletRequest request, JSONObject response) {
		String password = SecurityUtil.encode(request.getParameter("password"));
		String newpassword = request.getParameter("newpassword");
		String newpassword1 = request.getParameter("newpassword1");
		CurrentUser cu = CurrentUser.getCurrentUser();
		if (cu == null) {
			throw new ActionException("您未登录或登录超时，请重新登录后再修改密码。");
		}
		Integer userId = cu.getUserId();
		UserHelper helper = new UserHelper();

		User user = helper.findById(userId);
		if (!user.getUserPass().equals(password)) {
			throw new ActionException("当前密码错误，请重新输入");
		}
		if (newpassword == null) {
			throw new ActionException("请输入新密码");
		}

		if (newpassword.length() < 3) {
			throw new ActionException("新密码长度太短，请输入最短三个字符");
		}

		if (!newpassword.equals(newpassword1)) {
			throw new ActionException("输入的新密码不一致，请重新输入");
		}

		user.setUserPass(SecurityUtil.encode(newpassword));
		helper.save(user);
		return true;
	}

	public String getLocalizedName() {
		return "修改登录密码";
	}

}
