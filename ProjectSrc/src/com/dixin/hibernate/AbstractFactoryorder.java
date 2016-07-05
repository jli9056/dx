package com.dixin.hibernate;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.*;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractFactoryorder entity provides the base persistence definition of the
 * Factoryorder entity.
 * 
 * 
 */
@Name("工厂订单")
public abstract class AbstractFactoryorder extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Employee employee;
	private String orderNo;
	private Date orderDate;
	private Double total = 0.0;
	private Integer isplan;
	private Integer finished;
	private String comment;
	private Set factoryorderdetails = new HashSet(0);
	private Set arrivements = new HashSet(0);
	private int doubleCheck;
	private String checker;

	// Constructors

	/** default constructor */
	public AbstractFactoryorder() {
	}

	/** minimal constructor */
	public AbstractFactoryorder(Employee employee, String orderNo,
			Date orderDate, Integer isplan) {
		this.employee = employee;
		this.orderNo = orderNo;
		this.orderDate = orderDate;
		this.isplan = isplan;
	}

	/** full constructor */
	public AbstractFactoryorder(Employee employee, String orderNo,
			Date orderDate, Double total, Integer isplan, Integer finished,
			String comment, Set factoryorderdetails, Set arrivements) {
		this.employee = employee;
		this.orderNo = orderNo;
		this.orderDate = orderDate;
		this.total = total;
		this.isplan = isplan;
		this.finished = finished;
		this.comment = comment;
		this.factoryorderdetails = factoryorderdetails;
		this.arrivements = arrivements;
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

	@JSONalize
	public String getOrderNo() {
		return this.orderNo;
	}

	@Parse
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getOrderDate() {
		return this.orderDate;
	}

	@Parse
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the total
	 */
	@Aggregate(name = "订单金额", property = "total", type = IAggregation.SUM)
	@JSONalize
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	@Parse
	public void setTotal(Double total) {
		this.total = total;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getIsplan() {
		return this.isplan;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setIsplan(Integer isplan) {
		this.isplan = isplan;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getFinished() {
		return this.finished;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setFinished(Integer finished) {
		this.finished = finished;
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
	public Set<Factoryorderdetail> getFactoryorderdetails() {
		return this.factoryorderdetails;
	}

	public void setFactoryorderdetails(Set factoryorderdetails) {
		this.factoryorderdetails = factoryorderdetails;
	}

	@JSONalize
	public Set getArrivements() {
		return this.arrivements;
	}

	public void setArrivements(Set arrivements) {
		this.arrivements = arrivements;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public int getDoubleCheck() {
		return doubleCheck;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setDoubleCheck(int doubleCheck) {
		this.doubleCheck = doubleCheck;
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
