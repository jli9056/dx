package com.dixin.hibernate;


import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.Parse;

/**
 * AbstractShop entity provides the base persistence definition of the Shop
 * entity.
 * 
 * 
 */
@Name("店铺")
public abstract class AbstractShop extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String address;
	private String phone;
	private String fax;
	private String comment;
	private Set employees = new HashSet(0);
	private Set orders = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractShop() {
	}

	/** minimal constructor */
	public AbstractShop(String name) {
		this.name = name;
	}

	/** full constructor */
	public AbstractShop(String name, String address, String phone, String fax,
			String comment, Set employees, Set orders) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.fax = fax;
		this.comment = comment;
		this.employees = employees;
		this.orders = orders;
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
	public String getName() {
		return this.name;
	}

	@Parse
	public void setName(String name) {
		this.name = name;
	}

	@JSONalize
	public String getAddress() {
		return this.address;
	}

	@Parse
	public void setAddress(String address) {
		this.address = address;
	}

	@JSONalize
	public String getPhone() {
		return this.phone;
	}

	@Parse
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	@JSONalize
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	@Parse
	public void setFax(String fax) {
		this.fax = fax;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set employees) {
		this.employees = employees;
	}

	public Set getOrders() {
		return this.orders;
	}

	public void setOrders(Set orders) {
		this.orders = orders;
	}

}
