package com.dixin.hibernate;

import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.Parse;

/**
 * AbstractProduct entity provides the base persistence definition of the
 * Product entity.
 * 
 * 
 */
@Name("产品")
public abstract class AbstractProduct extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String model;
	private String alias;
	private String barcode;
	private String name;
	private String color;
	private String size;
	private String material;
	private String unit;
	private Double cost;
	private Double price;
	private String comment;
	private Set deliverydetails = new HashSet(0);
	private Set factoryorderdetails = new HashSet(0);
	private Set arrivedetails = new HashSet(0);
	private Set repertories = new HashSet(0);
	private Set orderdetails = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractProduct() {
	}

	/** minimal constructor */
	public AbstractProduct(String model, Double cost, Double price) {
		this.model = model;
		this.cost = cost;
		this.price = price;
	}

	/** full constructor */
	public AbstractProduct(String model, String alias, String barcode,
			String name, String color, String size, String material,String unit,
			Double cost, Double price, String comment, Set deliverydetails,
			Set factoryorderdetails, Set arrivedetails, Set repertories,
			Set orderdetails) {
		this.model = model;
		this.alias = alias;
		this.barcode = barcode;
		this.name = name;
		this.color = color;
		this.size = size;
		this.material = material;
		this.unit = unit;
		this.cost = cost;
		this.price = price;
		this.comment = comment;
		this.deliverydetails = deliverydetails;
		this.factoryorderdetails = factoryorderdetails;
		this.arrivedetails = arrivedetails;
		this.repertories = repertories;
		this.orderdetails = orderdetails;
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

	@JSONalize
	public String getModel() {
		return this.model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

	@JSONalize
	public String getAlias() {
		return this.alias;
	}

	@Parse
	public void setAlias(String alias) {
		this.alias = alias;
	}

	@JSONalize
	public String getBarcode() {
		return this.barcode;
	}

	@Parse
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@JSONalize
	public String getName() {
		return this.name;
	}

	@Parse
	public void setName(String name) {
		this.name = name;
	}

	@JSONalize
	public String getColor() {
		return this.color;
	}

	@Parse
	public void setColor(String color) {
		this.color = color;
	}

	@JSONalize
	public String getSize() {
		return this.size;
	}

	@Parse
	public void setSize(String size) {
		this.size = size;
	}

	@JSONalize
	public String getMaterial() {
		return this.material;
	}

	@Parse
	public void setMaterial(String material) {
		this.material = material;
	}
	
	@JSONalize
	public String getUnit() {
		return unit != null ? unit : "件";
	}

	@Parse
	public void setUnit(String unit) {
		this.unit = unit;
	}

	@JSONalize
	public Double getCost() {
		return this.cost;
	}

	@Parse
	public void setCost(Double cost) {
		this.cost = cost;
	}

	@JSONalize
	public Double getPrice() {
		return this.price;
	}

	@Parse
	public void setPrice(Double price) {
		this.price = price;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set getDeliverydetails() {
		return this.deliverydetails;
	}

	public void setDeliverydetails(Set deliverydetails) {
		this.deliverydetails = deliverydetails;
	}

	public Set getFactoryorderdetails() {
		return this.factoryorderdetails;
	}

	public void setFactoryorderdetails(Set factoryorderdetails) {
		this.factoryorderdetails = factoryorderdetails;
	}

	public Set getArrivedetails() {
		return this.arrivedetails;
	}

	public void setArrivedetails(Set arrivedetails) {
		this.arrivedetails = arrivedetails;
	}

	public Set getRepertories() {
		return this.repertories;
	}

	public void setRepertories(Set repertories) {
		this.repertories = repertories;
	}

	public Set getOrderdetails() {
		return this.orderdetails;
	}

	public void setOrderdetails(Set orderdetails) {
		this.orderdetails = orderdetails;
	}

}
