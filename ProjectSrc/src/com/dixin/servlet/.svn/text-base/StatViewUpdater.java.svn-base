package com.dixin.servlet;

import java.util.List;

import com.dixin.business.DataException;
import com.dixin.business.IPagedResult;
import com.dixin.business.StatManager;
import com.dixin.business.impl.ProductHelper;
import com.dixin.hibernate.HibernateSessionFactory;
import com.dixin.hibernate.Product;

public class StatViewUpdater implements IUpdater {

	@Override
	public String getTargetVersion() {
		return null;
	}

	@Override
	public void update() throws DataException {
		ProductHelper helper = new ProductHelper();
		IPagedResult<Product> users = helper.findAll();
		List<Product> list = users.getResult(0, users.count());
		int[] pids = new int[list.size()];
		for (int i = 0; i < pids.length; i++) {
			pids[i] = list.get(i).getId();
		}
		StatManager.getInstance().updateStatView(
				HibernateSessionFactory.getSession(), pids);
	}

}
