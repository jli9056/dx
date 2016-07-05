package com.dixin.hibernate;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.*;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractDelivery entity provides the base persistence definition of the
 * Delivery entity.
 * 
 * 
 */
@Name("送货")
public abstract class AbstractDelivery extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Employee employee;
	private Date deliverDate;
	private String comment;
	private Set deliverydetails = new HashSet(0);
	private int doubleCheck;
	private String checker;

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public int getDoubleCheck() {
		return doubleCheck;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setDoubleCheck(int doubleCheck) {
		this.doubleCheck = doubleCheck;
	}

	// Constructors

	/** default constructor */
	public AbstractDelivery() {
	}

	/** minimal constructor */
	public AbstractDelivery(Employee employee, Date deliverDate) {
		this.employee = employee;
		this.deliverDate = deliverDate;
	}

	/** full constructor */
	public AbstractDelivery(Employee employee, Date deliverDate,
			String comment, Set deliverydetails) {
		this.employee = employee;
		this.deliverDate = deliverDate;
		this.comment = comment;
		this.deliverydetails = deliverydetails;
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

	@JSONalize(type = JSONalizeType.Properties, properties = "id,name")
	public Employee getEmployee() {
		return this.employee;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.EmployeeHelper.class, findFields = "id")
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the deliverDate
	 */
	@JSONalize(type = JSONalizeType.Basic)
	public Date getDeliverDate() {
		return deliverDate;
	}

	/**
	 * @param deliverDate
	 *            the deliverDate to set
	 */
	@Parse(type = ParseType.Basic)
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	@JSONalize
	public Set getDeliverydetails() {
		return this.deliverydetails;
	}

	public void setDeliverydetails(Set deliverydetails) {
		this.deliverydetails = deliverydetails;
	}
	
	/**
	 * @return the checker
	 */
	@JSONalize
	public String getChecker() {
		return checker;
	}

	/**
	 * @param checker the checker to set
	 */
	@Parse
	public void setChecker(String checker) {
		this.checker = checker;
	}

}
