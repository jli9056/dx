package com.dixin.hibernate;

import java.util.Set;

/**
 * Product entity.
 * 
 * 
 */
public class Product extends AbstractProduct implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 9207513405661779209L;

	/** default constructor */
	public Product() {
	}

	/** minimal constructor */
	public Product(String model, Double cost, Double price) {
		super(model, cost, price);
	}

	/** full constructor */
	public Product(String model, String alias, String barcode, String name,
			String color, String size, String material, String unit,
			Double cost, Double price, String comment, Set deliverydetails,
			Set factoryorderdetails, Set arrivedetails, Set repertories,
			Set orderdetails) {
		super(model, alias, barcode, name, color, size, material, unit, cost,
				price, comment, deliverydetails, factoryorderdetails,
				arrivedetails, repertories, orderdetails);
	}

}
