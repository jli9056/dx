package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractOrderdetail entity provides the base persistence definition of the
 * Orderdetail entity.
 * 
 * 
 */
@Name("客户订单明细")
public abstract class AbstractOrderdetail extends BaseJDO implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 317869462218199219L;
	private Integer id;
	private Product product;
	private Order order;
	private Integer quantity;
	private Double price;
	private Double cost = new Double(0);
	private Integer reservedCount;
	private Integer deliveredCount;
	private Date deliverDate;
	private Integer reserveLocked = new Integer(0);
	private Integer isValid = new Integer(1);
	private String comment;
	private String customerName;
	private Date scheduleDate;
	private String model;
	private Integer doubleCheck;
	private String checker;

	@JSONalize
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

	// Constructors

	@JSONalize
	public String getComment() {
		return comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	@JSONalize
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/** default constructor */
	public AbstractOrderdetail() {
	}

	/** minimal constructor */
	public AbstractOrderdetail(Product product, Order order, Integer quantity,
			Double price, Integer reservedCount, Integer deliveredCount,
			Date deliverDate) {
		this.product = product;
		this.order = order;
		this.quantity = quantity;
		this.price = price;
		this.reservedCount = reservedCount;
		this.deliveredCount = deliveredCount;
		this.deliverDate = deliverDate;
	}

	/** full constructor */
	public AbstractOrderdetail(Product product, Order order, Integer quantity,
			Double price, Double cost, Integer reservedCount,
			Integer deliveredCount, Date deliverDate, Integer reserveLocked,
			Integer isValid, String comment, String customerName) {
		this.product = product;
		this.order = order;
		this.quantity = quantity;
		this.price = price;
		this.cost = cost;
		this.reservedCount = reservedCount;
		this.deliveredCount = deliveredCount;
		this.deliverDate = deliverDate;
		this.reserveLocked = reserveLocked;
		this.comment = comment;
		this.isValid = isValid;
	}
	@JSONalize(type=JSONalizeType.Converter,converter=IntegerToYesNo.class)
	public Integer getDoubleCheck() {
		return doubleCheck;
	}

	@Parse(type=ParseType.Converter,converter=YesNoToInteger.class)
	public void setDoubleCheck(Integer doubleCheck) {
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

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.ProductHelper.class, findFields = "model,alias,barcode")
	public void setProduct(Product product) {
		this.product = product;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id,orderNo")
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

	@JSONalize
	public Double getMoney() {
		if (this.getQuantity() == null || this.getPrice() == null) {
			return 0.0;
		}
		return this.getQuantity() * this.getPrice();
	}

	@JSONalize
	public Double getPrice() {
		return this.price;
	}

	@Parse
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the cost
	 */
	@JSONalize
	public Double getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	@Parse
	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Aggregate(name = "已留货数量", property = "reservedCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getReservedCount() {
		return this.reservedCount;
	}

	@Parse
	public void setReservedCount(Integer reservedCount) {
		this.reservedCount = reservedCount;
	}

	@Aggregate(name = "已送数量", property = "deliveredCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getDeliveredCount() {
		return this.deliveredCount;
	}

	@Parse
	public void setDeliveredCount(Integer deliveredCount) {
		this.deliveredCount = deliveredCount;
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

	/**
	 * @return the reserveLocked
	 */
	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getReserveLocked() {
		return reserveLocked;
	}

	/**
	 * @param reserveLocked
	 *            the reserveLocked to set
	 */
	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setReserveLocked(Integer reserveLocked) {
		this.reserveLocked = reserveLocked;
	}

	/**
	 * @return the isValid
	 */
	@JSONalize
	public Integer getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid
	 *            the isValid to set
	 */
	@Parse
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	@JSONalize
	public Date getScheduleDate() {
		return scheduleDate;
	}

	@Parse
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
}
