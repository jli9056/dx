package com.dixin.hibernate;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.*;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractArrivement entity provides the base persistence definition of the
 * Arrivement entity.
 * 
 * 
 */
@Name("到货")
public abstract class AbstractArrivement extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Employee employee;
	private Date arriveDate;
	private Set arrivedetails = new HashSet(0);
	private String arriveNo;
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
	public AbstractArrivement() {
	}

	/** minimal constructor */
	public AbstractArrivement(String arriveNo, Employee employee,
			Date arriveDate) {
		this.arriveNo = arriveNo;
		this.employee = employee;
		this.arriveDate = arriveDate;
	}

	/** full constructor */
	public AbstractArrivement(String arriveNo, Employee employee,
			Date arriveDate, Set arrivedetails) {
		this.arriveNo = arriveNo;
		this.employee = employee;
		this.arriveDate = arriveDate;
		this.arrivedetails = arrivedetails;
	}

	// Property accessors

	@JSONalize
	public Integer getId() {
		return this.id;
	}

	@Parse(type = ParseType.Basic)
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

	@JSONalize(type = JSONalizeType.Basic)
	public Date getArriveDate() {
		return this.arriveDate;
	}

	@Parse(type = ParseType.Basic)
	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	@JSONalize
	public Set<Arrivedetail> getArrivedetails() {
		return this.arrivedetails;
	}

	public void setArrivedetails(Set<Arrivedetail> arrivedetails) {
		this.arrivedetails = arrivedetails;
	}

	@JSONalize
	public String getArriveNo() {
		return arriveNo;
	}

	@Parse(type = ParseType.Basic)
	public void setArriveNo(String arriveNo) {
		this.arriveNo = arriveNo;
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
