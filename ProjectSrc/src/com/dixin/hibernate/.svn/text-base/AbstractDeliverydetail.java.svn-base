package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.DateToTimeConverter;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.TimeToDateConverter;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractDeliverydetail entity provides the base persistence definition of the
 * Deliverydetail entity.
 * 
 * 
 */
@Name("送货明细")
public abstract class AbstractDeliverydetail extends BaseJDO implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1707104860541781018L;
	private Integer id;
	private Delivery delivery;
	private Product product;
	private Order order;
	private Integer quantity;
	private Double cost = new Double(0);
	private Date queueTime;
	private String status;
	private String comment;
	private String orderNo;
	private String customerName;
	private Date orderDate;
	private String employee;
	private Date deliverDate;
	private String model;
	private int doubleCheck;
	private Integer deliveryId;

	@JSONalize
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

	// Constructors

	/** default constructor */
	public AbstractDeliverydetail() {
	}

	/** minimal constructor */
	public AbstractDeliverydetail(Delivery delivery, Product product,
			Order order, Integer quantity, Date queueTime) {
		this.delivery = delivery;
		this.product = product;
		this.order = order;
		this.quantity = quantity;
		this.queueTime = queueTime;
	}

	/** full constructor */
	public AbstractDeliverydetail(Delivery delivery, Product product,
			Order order, Integer quantity, Date queueTime, String status,
			String comment, String orderNo, String customerName,
			Date orderDate, String employee, Date deliverDate) {
		this.delivery = delivery;
		this.product = product;
		this.order = order;
		this.quantity = quantity;
		this.queueTime = queueTime;
		this.status = status;
		this.comment = comment;
		this.orderNo = orderNo;
		this.customerName = customerName;
		this.orderDate = orderDate;
		this.employee = employee;
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
	public String getEmployee() {
		return employee;
	}

	@Parse
	public void setEmployee(String employee) {
		this.employee = employee;
	}

	@JSONalize
	public Integer getId() {
		return this.id;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Delivery getDelivery() {
		return this.delivery;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.DeliveryHelper.class, findFields = "id")
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
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
	public Order getOrder() {
		return this.order;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.OrderHelper.class, findFields = "id,orderNo")
	public void setOrder(Order order) {
		this.order = order;
	}

	@Aggregate(name = "产品数量", property = "quantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getQuantity() {
		return this.quantity;
	}

	@Parse
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	// TODO ???
	@Aggregate(name = "产品金额", property = "cost", type = IAggregation.SUM)
	@JSONalize
	public Double getCost() {
		return this.cost;
	}

	@Parse
	public void setCost(Double cost) {
		this.cost = cost;
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
	public String getStatus() {
		return this.status;
	}

	@Parse
	public void setStatus(String status) {
		this.status = status;
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

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public int getDoubleCheck() {
		return doubleCheck;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setDoubleCheck(int doubleCheck) {
		this.doubleCheck = doubleCheck;
	}
	
	@JSONalize
	public Integer getDeliveryId() {
		return this.deliveryId;
	}
	
	@Parse
	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}
}
