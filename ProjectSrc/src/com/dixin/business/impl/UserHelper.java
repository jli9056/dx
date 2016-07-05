package com.dixin.business.impl;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.business.IPagedResult;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.User;

public class UserHelper extends BaseHelper<User> {

	@SuppressWarnings("unchecked")
	public User getUserByName(String name) {

		try {
			Criterion c = Restrictions.eq("userName", name);
			QueryDefn qf = new QueryDefn();
			qf.addCriterion(c);
			IPagedResult<User> result = this.find(qf);
			if (result.count() > 0) {
				return result.getResult(0, 1).get(0);
			}
			return null;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}
