/**
 * 
 */
package com.dixin.business.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.dixin.annotation.Aggregate;
import com.dixin.business.DataException;
import com.dixin.business.IAggregation;
import com.dixin.business.IQueryDefn;
import com.dixin.hibernate.HibernateSessionFactory;

/**
 * @author Jason
 * 
 */
public class Aggregation implements IAggregation {

	protected Criteria criteria;

	/**
	 * create criteria object for subclasses to execute the query.
	 * 
	 * @param jdoClz
	 * @param queryDefn
	 */
	public Aggregation(Class jdoClz, IQueryDefn queryDefn) {
		Session session = HibernateSessionFactory.getSession();
		criteria = session.createCriteria(jdoClz);
		for (Criterion c : queryDefn.getCriterions()) {
			criteria.add(c);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IAggregation#getAggrResult(java.lang.String[],
	 *      int[])
	 */
	@SuppressWarnings("unchecked")
	public Object[] getAggrResult(Aggregate[] aggs) {
		if (aggs == null) {
			return null;
		}
		ProjectionList plist = Projections.projectionList();
		final int length = aggs.length;
		for (int i = 0; i < length; i++) {
			switch (aggs[i].type()) {
			case SUM:
				plist.add(Projections.sum(aggs[i].property()));
				break;
			case AVG:
				plist.add(Projections.avg(aggs[i].property()));
				break;
			case MAX:
				plist.add(Projections.max(aggs[i].property()));
				break;
			case MIN:
				plist.add(Projections.min(aggs[i].property()));
				break;
			case COUNT:
				plist.add(Projections.count(aggs[i].property()));
				break;
			case COUNT_DISTINCT:
				plist.add(Projections.countDistinct(aggs[i].property()));
				break;
			default:
				throw new DataException("在{0}列设置的聚合类型错误！",
						new Object[] { aggs[i].property() });
			}
		}
		if (plist.getLength() <= 0) {
			return new Object[0];
		}
		try {
			criteria.setProjection(plist);
			List<?> list = criteria.list();
			if (list.size() == 1 && list.get(0) instanceof Object[]) {
				return (Object[]) list.get(0);
			}
			return list.toArray();
		} catch (HibernateException ex) {
			throw new DataException("在某列上不能进行聚合计算，请联系软件供应商！", ex);
		}
	}
}
