package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.business.constants.Bool;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractCorrection entity provides the base persistence definition of the
 * Correction entity.
 * 
 * 
 */
@Name("库存修正")
public abstract class AbstractCorrection extends BaseJDO implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2347671699723581432L;
	private Integer id;
	private Storehouse storehouse;
	private Product product;
	private Integer quantity;
	private Double cost;
	private Date correctionDate;
	private Integer isAuto = Bool.TRUE;
	private String comment;
	private String model;

	// Constructors

	/** default constructor */
	public AbstractCorrection() {
	}

	/** full constructor */
	public AbstractCorrection(Storehouse storehouse, Product product,
			Integer quantity, Double cost, Date correctionDate, Integer isAuto,
			String comment) {
		this.storehouse = storehouse;
		this.product = product;
		this.quantity = quantity;
		this.cost = cost;
		this.correctionDate = correctionDate;
		this.isAuto = isAuto;
		this.comment = comment;
	}

	// Property accessors
	@JSONalize
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

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

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.StorehouseHelper.class, findFields = "name,id")
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
	
	@JSONalize
	public Double getCost() {
		return cost;
	}

	@Parse
	public void setCost(Double cost) {
		this.cost = cost;
	}

	/**
	 * @return the isAuto
	 */
	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getIsAuto() {
		return isAuto;
	}

	/**
	 * @param isAuto
	 *            the isAuto to set
	 */
	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setIsAuto(Integer isAuto) {
		this.isAuto = isAuto;
	}


	/**
	 * @return the total
	 */
	@Aggregate(name = "金额", property = "total", type = IAggregation.SUM)
	@JSONalize
	public Double getTotal() {
		return this.quantity * this.cost;
	}

	/**
	 * @param total the total to set
	 */
	@Parse
	public void setTotal(Double total) {
		//do nothing need this method
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getCorrectionDate() {
		return correctionDate;
	}

	@Parse(type = ParseType.Basic)
	public void setCorrectionDate(Date correctionDate) {
		this.correctionDate = correctionDate;
	}

}
