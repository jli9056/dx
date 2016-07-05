package com.dixin.hibernate;

import java.util.List;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.Parse;

/**
 * AbstractFactoryorderdetail entity provides the base persistence definition of
 * the Factoryorderdetail entity.
 * 
 * 
 */
@Name("产品工厂订货汇总")
public abstract class AbstractProductFactoryorderSummary extends BaseJDO
		implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8912090479247168273L;
	private Integer id;
	private Integer quantity;
	private Integer availableCount;
	private Integer ownedCount;
	private Integer deliveredCount;
	private Integer orderedCount;
	private String model;

	private List<Factoryorderdetail> details;

	public String getDetailsHTML() {
		return null;
	}

	public List<Factoryorderdetail> getDetails() {
		return details;
	}

	public void setDetails(List<Factoryorderdetail> details) {
		this.details = details;
	}

	/** default constructor */
	public AbstractProductFactoryorderSummary() {
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

	@Aggregate(name = "产品订货数量", property = "quantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getQuantity() {
		return this.quantity;
	}

	@Parse
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

}
