/**
 * 
 */
package com.dixin.hibernate;

import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;

/**
 * @author Jason
 * 
 */
public abstract class AbstractBidding extends BaseJDO {

	private Integer id;
	private Repertory repertory;
	private Double cost;

	/**
	 * default contractor.
	 */
	public AbstractBidding() {

	}

	/**
	 * full contractor.
	 * 
	 * @param repertory
	 * @param cost
	 */
	public AbstractBidding(Repertory repertory, Double cost) {
		this.repertory = repertory;
		this.cost = cost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.hibernate.BaseJDO#getId()
	 */
	@JSONalize
	public Integer getId() {
		return id;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Repertory getRepertory() {
		return repertory;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.RepertoryHelper.class, findFields = "id")
	public void setRepertory(Repertory repertory) {
		this.repertory = repertory;
	}

	@JSONalize
	public Double getCost() {
		return cost;
	}

	@Parse
	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

}
