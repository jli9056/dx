package com.dixin.hibernate;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;

/**
 * AbstractOverflow entity provides the base persistence definition of the
 * Overflow entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Name("超计划订货")
public abstract class AbstractOverflow extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Orderdetail orderdetail;
	private Integer overflowCount;
	private String orderNo;
	private Integer customerId;
	private String customerName;
	private String model;

	// Constructors
	@Parse
	public void setModel(String productModel) {
		this.model = productModel;
	}

	/** default constructor */
	public AbstractOverflow() {
	}

	/** minimal constructor */
	public AbstractOverflow(Orderdetail orderdetail) {
		this.orderdetail = orderdetail;
	}

	/** minimal constructor */
	public AbstractOverflow(Orderdetail orderdetail, Integer count) {
		this.orderdetail = orderdetail;
		this.overflowCount = count;
	}

	/** full constructor */
	public AbstractOverflow(Orderdetail orderdetail, Integer overflowCount,
			String orderNo, Integer customerId, String customerName,
			String productModel) {
		this.orderdetail = orderdetail;
		this.overflowCount = overflowCount;
	}

	// Property accessors
	@JSONalize
	public Integer getId() {
		return this.id;
	}

	@Parse
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Parse
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Parse
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Orderdetail getOrderdetail() {
		return this.orderdetail;
	}

	@JSONalize
	public String getModel() {
		return this.model;
	}

	@JSONalize
	public String getOrderNo() {
		return this.orderNo;
	}

	@JSONalize
	public Integer getCustomerId() {
		return this.customerId;
	}

	@JSONalize
	public String getCustomerName() {
		return this.customerName;
	}

	@Parse
	public void setOrderdetail(Orderdetail orderdetail) {
		this.orderdetail = orderdetail;
	}
	@Aggregate(name = "计划外订单数量",property="overflowCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getOverflowCount() {
		return this.overflowCount;
	}

	@Parse
	public void setOverflowCount(Integer overflowCount) {
		this.overflowCount = overflowCount;
	}

}