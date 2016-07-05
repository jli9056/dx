/**
 * 
 */
package com.dixin.action.product;

import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSearchAction;
import com.dixin.action.factoryorder.SearchFactoryorderAction;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.FactoryOrderHelper;
import com.dixin.business.impl.ProductHelper;
import com.dixin.hibernate.AbstractFactoryorderdetail;
import com.dixin.hibernate.Factoryorder;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.Product;

/**
 * @author Luo
 * 
 */
public class SearchProductAction extends GenericSearchAction<Product> {
	public SearchProductAction() {
		super(Product.class, ProductHelper.class);
	}

	@SuppressWarnings("unchecked")
	protected Criterion getXCriterion(String key, String value) {
		if ("factoryorder".equals(key)) {
			FactoryOrderHelper fhelper = new FactoryOrderHelper();
			IPagedResult<Factoryorder> pr = fhelper
					.find(new SearchFactoryorderAction()
							.getQueryDefn("cr_orderNo_eq=" + value));
			if (pr.count() < 1) {
				throw new ActionException("指定的工厂订单不存在");
			}
			Factoryorder order = pr.getResult(0, 1).get(0);
			Set<Factoryorderdetail> dset = order
					.getFactoryorderdetails();
			Integer[] ids = new Integer[dset.size() + 1];
			int i = 0;
			for (AbstractFactoryorderdetail d : dset) {
				ids[i] = d.getProduct().getId();
				i++;
			}
			ids[i] = -1;
			return Restrictions.in("id", ids);
		}
		return null;
	}
}
