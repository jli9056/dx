/**
 * 
 */
package com.dixin.business.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.dixin.business.IPagedResult;
import com.dixin.business.IQueryDefn;
import com.dixin.hibernate.BaseJDO;
import com.dixin.hibernate.HibernateSessionFactory;

/**
 * @author Jason
 * 
 */
public class PagedResult<T extends BaseJDO> implements IPagedResult<T> {
	private Criteria criteria;
	private Criteria countCriteria;

	/**
	 * 
	 * @param dao
	 * @param criteria
	 */
	public PagedResult(Class<T> jdoClz, IQueryDefn queryDefn) {
		Session session = HibernateSessionFactory.getSession();
		criteria = session.createCriteria(jdoClz);
		countCriteria = session.createCriteria(jdoClz);
		for (Criterion c : queryDefn.getCriterions()) {
			criteria.add(c);
			countCriteria.add(c);
		}

		for (Order o : queryDefn.getOrders()) {
			criteria.addOrder(o);
		}
		countCriteria.setProjection(Projections.rowCount());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IPagedResult#count(java.lang.Class)
	 */
	public int count() {
		return (Integer) countCriteria.list().get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IPagedResult#getResult(java.lang.Class, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<T> getResult(int start, int limit) {
		if (start >= 0)
			criteria.setFirstResult(start);
		if (limit > 0)
			criteria.setMaxResults(limit);
		return criteria.list();
	}
}
