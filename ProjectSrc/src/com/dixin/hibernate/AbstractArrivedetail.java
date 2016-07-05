package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.*;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractArrivedetail entity provides the base persistence definition of the
 * Arrivedetail entity.
 * 
 * 
 */
@Name("到货明细")
public abstract class AbstractArrivedetail extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Arrivement arrivement;
	private Storehouse storehouse;
	private Product product;
	private Integer quantity;
	private Double cost;
	private String model;
	private Factoryorder factoryorder;
	private int doubleCheck;
	private String arriveNo;
	private String storehouseName;
	private String factoryorderNo;
	private Date arriveDate;
	
	@JSONalize
	public Date getArriveDate() {
		return arriveDate;
	}
	@Parse
	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	@JSONalize
	public String getFactoryorderNo() {
		return factoryorderNo;
	}
	@Parse
	public void setFactoryorderNo(String factoryorderNo) {
		this.factoryorderNo = factoryorderNo;
	}

	@JSONalize
	public String getStorehouseName() {
		return storehouseName;
	}
	@Parse
	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}

	@JSONalize
	public String getArriveNo() {
		return arriveNo;
	}
	@Parse
	public void setArriveNo(String arriveNo) {
		this.arriveNo = arriveNo;
	}

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
	public AbstractArrivedetail() {
	}

	/** full constructor */
	public AbstractArrivedetail(Arrivement arrivement, Storehouse storehouse,
			Product product, Integer quantity, Double cost) {
		this.arrivement = arrivement;
		this.storehouse = storehouse;
		this.product = product;
		this.quantity = quantity;
		this.cost = cost;
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
	public Arrivement getArrivement() {
		return this.arrivement;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.ArrivementHelper.class, findFields = "id")
	public void setArrivement(Arrivement arrivement) {
		this.arrivement = arrivement;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "name")
	public Storehouse getStorehouse() {
		return this.storehouse;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.StorehouseHelper.class, findFields = "id,name")
	public void setStorehouse(Storehouse storehouse) {
		this.storehouse = storehouse;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "model")
	public Product getProduct() {
		return this.product;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.ProductHelper.class, findFields = "model")
	public void setProduct(Product product) {
		this.product = product;
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

	@Aggregate(name = "成本金额",property="cost", type = IAggregation.SUM)
	@JSONalize
	public Double getCost() {
		return this.cost;
	}

	@Parse
	public void setCost(Double cost) {
		this.cost = cost;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "orderNo")
	public Factoryorder getFactoryorder() {
		return this.factoryorder;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.FactoryOrderHelper.class, findFields = "orderNo")
	public void setFactoryorder(Factoryorder factoryorder) {
		this.factoryorder = factoryorder;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public int getDoubleCheck() {
		return doubleCheck;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setDoubleCheck(int doubleCheck) {
		this.doubleCheck = doubleCheck;
	}

}
