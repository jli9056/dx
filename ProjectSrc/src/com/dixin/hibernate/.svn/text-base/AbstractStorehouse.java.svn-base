package com.dixin.hibernate;

import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractStorehouse entity provides the base persistence definition of the
 * Storehouse entity.
 * 
 * 
 */
@Name("仓库")
public abstract class AbstractStorehouse extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String address;
	private Integer shippable;
	private String comment;
	private Set repertories = new HashSet(0);
	private Set arrivedetails = new HashSet(0);
	private Set reserveds = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractStorehouse() {
	}

	/** minimal constructor */
	public AbstractStorehouse(String name, Integer shippable) {
		this.name = name;
		this.shippable = shippable;
	}

	/** full constructor */
	public AbstractStorehouse(String name, String address, Integer shippable,
			String comment, Set repertories, Set arrivedetails, Set reserveds) {
		this.name = name;
		this.address = address;
		this.shippable = shippable;
		this.comment = comment;
		this.repertories = repertories;
		this.arrivedetails = arrivedetails;
		this.reserveds = reserveds;
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

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getShippable() {
		return this.shippable;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setShippable(Integer shippable) {
		this.shippable = shippable;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set getRepertories() {
		return this.repertories;
	}

	public void setRepertories(Set repertories) {
		this.repertories = repertories;
	}

	public Set getArrivedetails() {
		return this.arrivedetails;
	}

	public void setArrivedetails(Set arrivedetails) {
		this.arrivedetails = arrivedetails;
	}

	public Set getReserveds() {
		return this.reserveds;
	}

	public void setReserveds(Set reserveds) {
		this.reserveds = reserveds;
	}

}
