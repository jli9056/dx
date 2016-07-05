package com.dixin.hibernate;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.OrderdetailToProductModelConverter;

/**
 * AbstractReserved entity provides the base persistence definition of the
 * Reserved entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Name("留货")
public abstract class AbstractReserved extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Storehouse storehouse;
	private Orderdetail orderdetail;
	private Integer reservedCount;
	private String orderNo;

	// Constructors

	@JSONalize
	public String getOrderNo() {
		return orderNo;
	}

	@Parse
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/** default constructor */
	public AbstractReserved() {
	}

	/** minimal constructor */
	public AbstractReserved(Storehouse storehouse, Orderdetail orderdetail) {
		this.storehouse = storehouse;
		this.orderdetail = orderdetail;
	}

	/** full constructor */
	public AbstractReserved(Storehouse storehouse, Orderdetail orderdetail,
			Integer reservedCount) {
		this.storehouse = storehouse;
		this.orderdetail = orderdetail;
		this.reservedCount = reservedCount;
	}

	/** full constructor */
	public AbstractReserved(Storehouse storehouse, Orderdetail orderdetail,
			Integer reservedCount, String orderNo) {
		this.storehouse = storehouse;
		this.orderdetail = orderdetail;
		this.reservedCount = reservedCount;
		this.orderNo = orderNo;
	}

	// Property accessors
	@JSONalize
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id,name")
	public Storehouse getStorehouse() {
		return this.storehouse;
	}

	public void setStorehouse(Storehouse storehouse) {
		this.storehouse = storehouse;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = OrderdetailToProductModelConverter.class)
	public Orderdetail getOrderdetail() {
		return this.orderdetail;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.OrderdetailHelper.class)
	public void setOrderdetail(Orderdetail orderdetail) {
		this.orderdetail = orderdetail;
	}
	
	@Aggregate(name = "留货数量", property = "reservedCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getReservedCount() {
		return this.reservedCount;
	}

	public void setReservedCount(Integer reservedCount) {
		this.reservedCount = reservedCount;
	}

}