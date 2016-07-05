/**
 * 
 */
package com.dixin.action.user;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.UserHelper;
import com.dixin.hibernate.User;

/**
 * @author Luo
 * 
 */
public class DeleteUserAction extends GenericDeleteAction<User> {
	public DeleteUserAction() {
		super(User.class, UserHelper.class);
	}
}
