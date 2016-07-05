package com.dixin.hibernate;


import com.dixin.hibernate.convert.*;

/**
 * AbstractWarehouse entity provides the base persistence definition of the
 * Warehouse entity.
 * 
 */

public abstract class AbstractWarehouse implements java.io.Serializable {

	// Fields

	private int id;
	private String name;
	private String address;
	private String comment;

	// Constructors

	/** default constructor */
	public AbstractWarehouse() {
	}

	/** minimal constructor */
	public AbstractWarehouse(String name) {
		this.name = name;
	}

	/** full constructor */
	public AbstractWarehouse(String name, String address, String comment) {
		this.name = name;
		this.address = address;
		this.comment = comment;
	}

	// Property accessors

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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
	public String getAddress() {
		return this.address;
	}

	@Parse
	public void setAddress(String address) {
		this.address = address;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

}
