package com.dixin.hibernate;

import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.*;

/**
 * AbstractRepertory entity provides the base persistence definition of the
 * Repertory entity.
 * 
 * 
 */
@Name("库存")
public abstract class AbstractRepertory extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Storehouse storehouse;
	private Product product;
	private Integer quantity;
	private Integer reservedCount;
	private Set biddings = new HashSet(0);
	private String model;

	// Constructors

	/** default constructor */
	public AbstractRepertory() {
	}

	/** full constructor */
	public AbstractRepertory(Storehouse storehouse, Product product,
			Integer quantity, Integer reservedCount) {
		this.storehouse = storehouse;
		this.product = product;
		this.quantity = quantity;
		this.reservedCount = reservedCount;
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
	@Aggregate(name = "已留货产品数量", property = "reservedCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getReservedCount() {
		return this.reservedCount;
	}

	@Parse
	public void setReservedCount(Integer reservedCount) {
		this.reservedCount = reservedCount;
	}

	@JSONalize
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the biddings
	 */
	public Set getBiddings() {
		return biddings;
	}

	/**
	 * @param biddings
	 *            the biddings to set
	 */
	public void setBiddings(Set biddings) {
		this.biddings = biddings;
	}
}
