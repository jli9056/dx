package com.dixin.hibernate;

import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.converters.MenusSummaryConverter;

/**
 * AbstractRole entity provides the base persistence definition of the Role
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Name("角色")
public abstract class AbstractRole extends com.dixin.hibernate.BaseJDO
		implements java.io.Serializable {

	// Fields

	private Integer id;
	private String roleName;
	private Set<User> users = new HashSet<User>(0);
	private Set<Menu> menus = new HashSet<Menu>(0);

	// Constructors
	public String toString() {
		return roleName;
	}

	/** default constructor */
	public AbstractRole() {
	}

	/** minimal constructor */
	public AbstractRole(String roleName) {
		this.roleName = roleName;
	}

	/** full constructor */
	public AbstractRole(String roleName, Set<User> users, Set<Menu> menus) {
		this.roleName = roleName;
		this.users = users;
		this.menus = menus;
	}

	// Property accessors
	@JSONalize
	public Integer getId() {
		return this.id;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	@JSONalize
	public String getRoleName() {
		return this.roleName;
	}

	@Parse
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = MenusSummaryConverter.class)
	public Set<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

}