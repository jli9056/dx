/**
 * 
 */
package com.dixin.action.user;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.User;

/**
 * Load Action For JDO User
 * 
 * @author Luo
 * 
 */
public class LoadUserAction extends GenericLoadAction<User> {
	public LoadUserAction() {
		super(User.class, UserHelper.class);
	}
}
