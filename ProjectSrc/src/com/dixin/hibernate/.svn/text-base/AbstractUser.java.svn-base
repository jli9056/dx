package com.dixin.hibernate;

import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;

/**
 * AbstractUser entity provides the base persistence definition of the User
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Name("用户")
public abstract class AbstractUser extends com.dixin.hibernate.BaseJDO
		implements java.io.Serializable {

	// Fields

	private Integer id;
	private String userName;
	private String userPass;
	private Set<Role> roles = new HashSet<Role>(0);

	// Constructors

	/** default constructor */
	public AbstractUser() {
	}

	/** minimal constructor */
	public AbstractUser(String userName, String userPass) {
		this.userName = userName;
		this.userPass = userPass;
	}

	/** full constructor */
	public AbstractUser(String userName, String userPass, Set<Role> roles) {
		this.userName = userName;
		this.userPass = userPass;
		this.roles = roles;
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
	public String getUserName() {
		return this.userName;
	}

	@Parse
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JSONalize
	public String getUserPass() {
		return this.userPass;
	}

	@Parse
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	@JSONalize(type = JSONalizeType.ToString)
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}