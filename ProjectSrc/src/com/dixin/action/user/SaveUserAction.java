/**
 * 
 */
package com.dixin.action.user;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.User;
import com.dixin.util.SecurityUtil;

/**
 * @author Luo
 * 
 */
public class SaveUserAction extends GenericSaveAction<User> {
	public SaveUserAction() {
		super(User.class, UserHelper.class);
		uniqueKeys = new String[] { "", "id" };
	}

	protected User validate(User t) {
		t.setUserPass(SecurityUtil.encode(t.getUserPass()));
		t.setRoles(getHelper().findById(t.getId()).getRoles());
		return t;
	}
}
