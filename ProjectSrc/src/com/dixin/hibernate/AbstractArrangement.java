package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.DateLongFormatConverter;
import com.dixin.hibernate.convert.converters.DateToTimeConverter;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.TimeToDateConverter;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractArrangement entity provides the base persistence definition of the
 * Arrangement entity.
 * 
 * 
 */
@Name("排货信息")
public abstract class AbstractArrangement extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Orderdetail orderdetail;
	private Integer quantity;
	private Date queueTime;
	private Integer isFinished;
	private String comment;
	private Product product;
	private String orderNo;
	private String customerName;
	private Date orderDate;
	private String shopName;
	private Date deliverDate;
	private String model;
	private Date lastUpdate;

	// Constructors

	/** default constructor */
	public AbstractArrangement() {
	}

	/** minimal constructor */
	public AbstractArrangement(Orderdetail orderdetail, Integer quantity,
			Date queueTime) {
		this.orderdetail = orderdetail;
		this.quantity = quantity;
		this.queueTime = queueTime;
	}

	/** full constructor */
	public AbstractArrangement(Orderdetail orderdetail, Integer quantity,
			Date queueTime, Integer isFinished, String comment,
			Product product, String orderNo, String customerName,
			Date orderDate, String shopName, Date deliverDate) {
		this.product = product;
		this.orderdetail = orderdetail;
		this.quantity = quantity;
		this.queueTime = queueTime;
		this.isFinished = isFinished;
		this.comment = comment;
		this.orderNo = orderNo;
		this.customerName = customerName;
		this.orderDate = orderDate;
		this.shopName = shopName;
		this.deliverDate = deliverDate;
	}

	// Property accessors

	@JSONalize
	public String getCustomerName() {
		return customerName;
	}

	@Parse
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getOrderDate() {
		return orderDate;
	}

	@Parse
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getDeliverDate() {
		return deliverDate;
	}

	@Parse
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	@JSONalize
	public String getShopName() {
		return shopName;
	}

	@Parse
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@JSONalize
	public Integer getId() {
		return this.id;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "model")
	public Product getProduct() {
		return this.product;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.ProductHelper.class, findFields = "model,id")
	public void setProduct(Product product) {
		this.product = product;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Orderdetail getOrderdetail() {
		return this.orderdetail;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.OrderdetailHelper.class, findFields = "id")
	public void setOrderdetail(Orderdetail orderdetail) {
		this.orderdetail = orderdetail;
	}

	@Aggregate(name = "产品数量",property="quantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getQuantity() {
		return this.quantity;
	}

	@Parse
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the queueTime
	 */
	@JSONalize(type = JSONalizeType.Converter, converter = DateToTimeConverter.class)
	public Date getQueueTime() {
		return queueTime;
	}

	/**
	 * @param queueTime
	 *            the queueTime to set
	 */
	@Parse(type = ParseType.Converter, converter = TimeToDateConverter.class)
	public void setQueueTime(Date queueTime) {
		this.queueTime = queueTime;
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
	public String getOrderNo() {
		return orderNo;
	}

	@Parse
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the isFinished
	 */
	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getIsFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished
	 *            the isFinished to set
	 */
	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setIsFinished(Integer isFinished) {
		this.isFinished = isFinished;
	}

	@JSONalize
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = DateLongFormatConverter.class)
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
