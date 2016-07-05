/**
 * 
 */
package com.dixin.business.query;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.dixin.business.IQueryDefn;

/**
 * @author Jason
 * 
 */
public class QueryDefn implements IQueryDefn {
	private List<Criterion> criterions = new ArrayList<Criterion>();
	private List<Order> orders = new ArrayList<Order>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.query.IQuery#addCriterion(org.hibernate.criterion.Criterion)
	 */
	public void addCriterion(Criterion criterion) {
		criterions.add(criterion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.query.IQuery#getFilters()
	 */
	public List<Criterion> getCriterions() {
		return criterions;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dixin.business.IQueryDefn#addOrder(org.hibernate.criterion.Order)
	 */
	public void addOrder(Order order) {
		orders.add(order);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dixin.business.IQueryDefn#getOrders()
	 */
	public List<Order> getOrders() {
		return orders;
	}

}
