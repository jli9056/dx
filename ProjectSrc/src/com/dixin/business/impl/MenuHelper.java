package com.dixin.business.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.dixin.business.IPagedResult;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Menu;

public class MenuHelper extends BaseHelper<Menu> {
	public List<Menu> getTopLevelMenus() {
		QueryDefn qd = new QueryDefn();
		qd.addCriterion(Restrictions.eq("parent", null));
		IPagedResult<Menu> result = this.find(qd);
		return result.getResult(0, result.count());
	}
}
