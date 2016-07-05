package com.dixin.hibernate;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.*;
import com.dixin.hibernate.convert.converters.IntegerToSex;
import com.dixin.hibernate.convert.converters.SexToInteger;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractCustomer entity provides the base persistence definition of the
 * Customer entity.
 * 
 * 
 */
@Name("客户")
public abstract class AbstractCustomer extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Integer sex;
	private String phone;
	private String address;
	private String postcode;
	private String comment;
	private Set orders = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractCustomer() {
	}

	/** minimal constructor */
	public AbstractCustomer(String name, Integer sex, String phone) {
		this.name = name;
		this.sex = sex;
		this.phone = phone;
	}

	/** full constructor */
	public AbstractCustomer(String name, Integer sex, String phone,
			String address, String postcode, String comment, Set orders) {
		this.name = name;
		this.sex = sex;
		this.phone = phone;
		this.address = address;
		this.postcode = postcode;
		this.comment = comment;
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

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToSex.class)
	public Integer getSex() {
		return this.sex;
	}

	@Parse(type = ParseType.Converter, converter = SexToInteger.class)
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@JSONalize
	public String getPhone() {
		return this.phone;
	}

	@Parse
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getPostcode() {
		return this.postcode;
	}

	@Parse
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set getOrders() {
		return this.orders;
	}

	public void setOrders(Set orders) {
		this.orders = orders;
	}

}
