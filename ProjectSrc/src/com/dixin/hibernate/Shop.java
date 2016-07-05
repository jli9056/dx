package com.dixin.hibernate;

import java.util.Set;

/**
 * Shop entity.
 * 
 *
 */
public class Shop extends AbstractShop implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1763070450902036768L;

	/** default constructor */
	public Shop() {
	}

	/** minimal constructor */
	public Shop(String name) {
		super(name);
	}

	/** full constructor */
	public Shop(String name, String address, String phone, String fax, String comment,
			Set employees, Set orders) {
		super(name, address, phone, fax, comment, employees, orders);
	}

}
