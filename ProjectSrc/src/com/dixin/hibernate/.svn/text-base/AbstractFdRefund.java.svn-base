package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.business.impl.EmployeeHelper;
import com.dixin.business.impl.FactoryorderdetailHelper;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;

/**
 * AbstractRefund entity provides the base persistence definition of the Refund
 * entity.
 * 
 */
@Name("工厂退货")
public abstract class AbstractFdRefund extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Factoryorderdetail factoryorderdetail;
	private Integer quantity;
	private Date refundDate;
	private Employee employee;
	private String comment;

	// Constructors

	/** default constructor */
	public AbstractFdRefund() {
	}

	/** minimal constructor */
	public AbstractFdRefund(Factoryorderdetail factoryorderdetail, Integer quantity,
			Date refundDate, Employee employee) {
		this.factoryorderdetail = factoryorderdetail;
		this.quantity = quantity;
		this.refundDate = refundDate;
		this.employee = employee;
	}

	/** full constructor */
	public AbstractFdRefund(Factoryorderdetail factoryorderdetail, Integer quantity,
			Date refundDate, Employee employee, String comment) {
		this.factoryorderdetail = factoryorderdetail;
		this.quantity = quantity;
		this.refundDate = refundDate;
		this.employee = employee;
		this.comment = comment;
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

	@Aggregate(name = "数量", property = "quantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getQuantity() {
		return this.quantity;
	}

	@Parse
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getRefundDate() {
		return this.refundDate;
	}

	@Parse
	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	/**
	 * @return the fod
	 */
	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Factoryorderdetail getFactoryorderdetail() {
		return factoryorderdetail;
	}

	/**
	 * @param fod
	 *            the fod to set
	 */
	@Parse(type = ParseType.Helper, helper = FactoryorderdetailHelper.class, findFields = "id")
	public void setFactoryorderdetail(Factoryorderdetail fod) {
		this.factoryorderdetail = fod;
	}

	/**
	 * @return the employee
	 */
	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	@Parse(type = ParseType.Helper, helper = EmployeeHelper.class, findFields = "id")
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	

	/**
	 * @return the employeeName
	 */
	@JSONalize
	public String getEmployeeName() {
		return employee != null ? employee.getName() : "";
	}

	/**
	 * @return the comment
	 */
	@JSONalize
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

}
