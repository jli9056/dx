/**
 * 
 */
package com.dixin.business;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * @author Jason
 * 
 */
public interface IQueryDefn {
	/**
	 * add an Criterion to current query. User utility methods of Expression to
	 * create Criterion, such as Expression.eq("field",value);
	 * 
	 * @param criterion
	 */
	public void addCriterion(Criterion criterion);

	/**
	 * get all filters for this query. Return an empty list if there is no
	 * filter,and in this condition all the records in the table will be
	 * returned.
	 * 
	 * @return
	 */
	public List<Criterion> getCriterions();

	/**
	 * 
	 * @param order
	 */
	public void addOrder(Order order);

	/**
	 * 
	 * @return
	 */
	public List<Order> getOrders();
}
