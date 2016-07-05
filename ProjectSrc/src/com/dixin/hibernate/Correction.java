package com.dixin.hibernate;

import java.util.Date;

/**
 * Correction entity.
 * 
 * 
 */
public class Correction extends AbstractCorrection implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8318141037124299590L;

	/** default constructor */
	public Correction() {
	}

	/** full constructor */
	public Correction(Storehouse storehouse, Product product, Integer quantity,
			Double cost, Date date, Integer isAuto, String comment) {
		super(storehouse, product, quantity, cost, date, isAuto, comment);
	}

}
