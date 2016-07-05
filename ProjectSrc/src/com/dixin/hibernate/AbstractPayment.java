package com.dixin.hibernate;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.*;

import java.util.Date;

/**
 * AbstractPayment entity provides the base persistence definition of the
 * Payment entity.
 * 
 * 
 */
@Name("客户付款")
public abstract class AbstractPayment extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Order order;
	private Double amount;
	private String method;
	private Date payDate;
	private String comment;
	private String customerName;

	// Constructors
	@JSONalize
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/** default constructor */
	public AbstractPayment() {
	}

	/** minimal constructor */
	public AbstractPayment(Order order, Double amount, String method,
			Date payDate) {
		this.order = order;
		this.amount = amount;
		this.method = method;
		this.payDate = payDate;
	}

	/** full constructor */
	public AbstractPayment(Order order, Double amount, String method,
			Date payDate, String comment, String customerName) {
		this.order = order;
		this.amount = amount;
		this.method = method;
		this.payDate = payDate;
		this.comment = comment;
		this.customerName = customerName;
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

	@JSONalize(type = JSONalizeType.Properties, properties = "orderNo")
	public Order getOrder() {
		return this.order;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.OrderHelper.class, findFields = "id,orderNo")
	public void setOrder(Order order) {
		this.order = order;
	}
	@Aggregate(name = "付款金额", property = "amount", type = IAggregation.SUM)
	@JSONalize
	public Double getAmount() {
		return this.amount;
	}

	@Parse
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JSONalize
	public String getMethod() {
		return this.method;
	}

	@Parse
	public void setMethod(String method) {
		this.method = method;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getPayDate() {
		return this.payDate;
	}

	@Parse
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

}
