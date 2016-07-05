package com.dixin.hibernate;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.*;
import com.dixin.hibernate.convert.converters.IntegerToSex;
import com.dixin.hibernate.convert.converters.SexToInteger;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractEmployee entity provides the base persistence definition of the
 * Employee entity.
 * 
 * 
 */
@Name("员工")
public abstract class AbstractEmployee extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Shop shop;
	private String name;
	private Integer sex;
	private String phone;
	private String comment;
	private Set deliveries = new HashSet(0);
	private Set arrivements = new HashSet(0);
	private Set factoryorders = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractEmployee() {
	}

	/** minimal constructor */
	public AbstractEmployee(Shop shop, String name, Integer sex) {
		this.shop = shop;
		this.name = name;
		this.sex = sex;
	}

	/** full constructor */
	public AbstractEmployee(Shop shop, String name, Integer sex, String phone,
			String comment, Set deliveries, Set arrivements, Set factoryorders) {
		this.shop = shop;
		this.name = name;
		this.sex = sex;
		this.phone = phone;
		this.comment = comment;
		this.deliveries = deliveries;
		this.arrivements = arrivements;
		this.factoryorders = factoryorders;
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

	@JSONalize(type = JSONalizeType.Properties, properties = "name")
	public Shop getShop() {
		return this.shop;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.ShopHelper.class, findFields = "name")
	public void setShop(Shop shop) {
		this.shop = shop;
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
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set getDeliveries() {
		return this.deliveries;
	}

	public void setDeliveries(Set deliveries) {
		this.deliveries = deliveries;
	}

	public Set getArrivements() {
		return this.arrivements;
	}

	public void setArrivements(Set arrivements) {
		this.arrivements = arrivements;
	}

	public Set getFactoryorders() {
		return this.factoryorders;
	}

	public void setFactoryorders(Set factoryorders) {
		this.factoryorders = factoryorders;
	}

}
