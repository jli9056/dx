package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.*;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractFactoryorderdetail entity provides the base persistence definition of
 * the Factoryorderdetail entity.
 * 
 * 
 */
@Name("工厂订单明细")
public abstract class AbstractFactoryorderdetail extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Factoryorder factoryorder;
	private Product product;
	private Integer quantity;
	private Double cost;
	private Integer availableCount;
	private Integer ownedCount;
	private Date pickDate;
	private String comment;
	private Integer deliveredCount;
	private Integer orderedCount;
	private Date orderDate;
	private String orderNo;
	private String model;
	private Integer unladingCount;
	private Integer ladingCount;
	private int doubleCheck;
	private Integer suggestLaddingCount = 0;

	// Constructors

	/** default constructor */
	public AbstractFactoryorderdetail() {
	}

	/** full constructor */
	public AbstractFactoryorderdetail(Factoryorder factoryorder,
			Product product, Integer quantity, Double cost,
			Integer availableCount, Integer ownedCount) {
		this.factoryorder = factoryorder;
		this.product = product;
		this.quantity = quantity;
		this.cost = cost;
		this.availableCount = availableCount;
		this.ownedCount = ownedCount;
	}

	public AbstractFactoryorderdetail(Factoryorder factoryorder,
			Product product, Integer quantity, Double cost,
			Integer availableCount, Integer ownedCount, Date pickDate,
			String comment, Integer deliveredCount, Integer orderedCount,
			Date orderDate) {
		this.factoryorder = factoryorder;
		this.product = product;
		this.quantity = quantity;
		this.cost = cost;
		this.availableCount = availableCount;
		this.ownedCount = ownedCount;
		this.pickDate = pickDate;
		this.comment = comment;
		this.deliveredCount = deliveredCount;
		this.orderedCount = orderedCount;
		this.orderDate = orderDate;
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

	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Factoryorder getFactoryorder() {
		return this.factoryorder;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.FactoryOrderHelper.class, findFields = "id")
	public void setFactoryorder(Factoryorder factoryorder) {
		this.factoryorder = factoryorder;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "model")
	public Product getProduct() {
		return this.product;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.ProductHelper.class, findFields = "model")
	public void setProduct(Product product) {
		this.product = product;
	}

	@Aggregate(name = "产品订货数量", property = "quantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getQuantity() {
		return this.quantity;
	}

	@Parse
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@JSONalize
	public Double getCost() {
		return this.cost;
	}

	@Parse
	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Aggregate(name = "可用数量", property = "availableCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getAvailableCount() {
		return this.availableCount;
	}

	@Parse
	public void setAvailableCount(Integer availableCount) {
		this.availableCount = availableCount;
	}

	@Aggregate(name = "欠货数量", property = "ownedCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getOwnedCount() {
		return this.ownedCount;
	}

	@Parse
	public void setOwnedCount(Integer ownedCount) {
		this.ownedCount = ownedCount;
	}

	@JSONalize
	public Date getPickDate() {
		return pickDate;
	}

	@Parse
	public void setPickDate(Date pickDate) {
		this.pickDate = pickDate;
	}

	@JSONalize
	public String getComment() {
		return comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Aggregate(name = "已到货产品数量", property = "deliveredCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getDeliveredCount() {
		return deliveredCount;
	}

	@Parse
	public void setDeliveredCount(Integer deliveredCount) {
		this.deliveredCount = deliveredCount;
	}

	@Aggregate(name = "客户预定产品数量", property = "orderedCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getOrderedCount() {
		return orderedCount;
	}

	@Parse
	public void setOrderedCount(Integer orderedCount) {
		this.orderedCount = orderedCount;
	}

	@JSONalize
	public Date getOrderDate() {
		return orderDate;
	}

	@Parse
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@JSONalize
	public String getOrderNo() {
		return orderNo;
	}

	@Parse
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@JSONalize
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

	@Aggregate(name = "提货单外产品数量", property = "unladingCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getUnladingCount() {
		return unladingCount;
	}

	@Parse
	public void setUnladingCount(Integer unladingCount) {
		this.unladingCount = unladingCount;
	}

	@Aggregate(name = "提货单内产品数量", property = "ladingCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getLadingCount() {
		return ladingCount;
	}

	@Parse
	public void setLadingCount(Integer ladingCount) {
		this.ladingCount = ladingCount;
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
	 * @return the suggestLaddingCount
	 */
	@JSONalize
	public Integer getSuggestLaddingCount() {
		return suggestLaddingCount;
	}

	/**
	 * @param suggestLaddingCount the suggestLaddingCount to set
	 */
	@Parse
	public void setSuggestLaddingCount(Integer suggestLaddingCount) {
		this.suggestLaddingCount = suggestLaddingCount;
	}

}
