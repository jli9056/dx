package com.dixin.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.dixin.action.model.CheckboxMenu;
import com.dixin.hibernate.Action;
import com.dixin.hibernate.Menu;
import com.dixin.hibernate.Role;
import com.dixin.hibernate.User;
import com.dixin.hibernate.convert.JSONalize;

/**
 * User object in session.
 * 
 * @author Luo
 * 
 */
public class CurrentUser {
	private Integer userId;
	private String userName;
	private Set<String> roles = new HashSet<String>();
	private Set<String> actions = new HashSet<String>();
	private List<CheckboxMenu> menus = new ArrayList<CheckboxMenu>();

	/**
	 * Hold the current user only in request scope. The current user will set in
	 * session after user login.
	 */
	private static ThreadLocal<CurrentUser> threadUser = new ThreadLocal<CurrentUser>();

	/**
	 * Get user in thread local
	 * 
	 * @return
	 */
	public static CurrentUser getCurrentUser() {
		return threadUser.get();
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public static void setCurrentUser(CurrentUser user) {
		threadUser.set(user);
	}

	public static void setSessionUser(HttpSession session, CurrentUser user) {
		session.setAttribute("sessionUser", user);
	}

	public static CurrentUser getSessionUser(HttpSession session) {
		Object o = session.getAttribute("sessionUser");
		return o instanceof CurrentUser ? (CurrentUser) o : null;
	}

	@SuppressWarnings("unchecked")
	public CurrentUser(User user) {
		this.userName = user.getUserName();
		this.userId = user.getId();
		HashSet<Integer> menuIds = new HashSet<Integer>();
		for (Role role : user.getRoles()) {
			roles.add(role.getRoleName());
			for (Menu m : role.getMenus()) {
				if (menuIds.contains(m.getId())) {
					continue;
				}
				menuIds.add(m.getId());
				if (m.getParent() == null && !menus.contains(m)) {
					menus.add(new CheckboxMenu(m, user));
				}
				for (Action a : m.getActions()) {
					this.actions.add(a.getName());
				}
			}
		}
		Collections.sort(menus);
	}

	@JSONalize
	public String getUserName() {
		return userName;
	}

	public String toString() {
		return userName;
	}

	public List<CheckboxMenu> getMenus() {
		return menus;
	}

	public boolean isAuthorized(String action) {
		return actions.contains(action);
	}

	public Set<String> getActions() {
		return actions;
	}

	@JSONalize
	public Integer getUserId() {
		return userId;
	}

}
