package com.dixin.hibernate;

import java.util.Set;

/**
 * Role entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Role extends AbstractRole implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(String roleName) {
		super(roleName);
	}

	/** full constructor */
	public Role(String roleName, Set users, Set menus) {
		super(roleName, users, menus);
	}

}
