/**
 * 
 */
package com.dixin.action.user;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.User;
import com.dixin.util.SecurityUtil;

/**
 * @author Luo
 * 
 */
public class AddUserAction extends GenericAddAction<User> {
	public AddUserAction() {
		super(User.class, UserHelper.class);
		uniqueKeys = new String[] { "", "id" };
	}

	/* (non-Javadoc)
	 * @see com.dixin.action.GenericAddAction#validate(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	protected void validate(User t) {
		t.setUserPass(SecurityUtil.encode(t.getUserPass()));
	}
}
