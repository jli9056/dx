/**
 * 
 */
package com.dixin.action.productFactoryorderSummary;

import java.util.List;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.FactoryorderdetailHelper;
import com.dixin.business.impl.ProductFactoryorderSummaryHelper;
import com.dixin.business.impl.StatViewHelper;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.ProductFactoryorderSummary;
import com.dixin.hibernate.StatView;

/**
 * @author Luo
 * 
 */
public class SearchProductFactoryorderSummaryAction extends
		GenericSearchAction<ProductFactoryorderSummary> {
	public SearchProductFactoryorderSummaryAction() {
		super(ProductFactoryorderSummary.class, ProductFactoryorderSummaryHelper.class);
	}

	protected void processQueryResult(List<ProductFactoryorderSummary> result) {
//		FactoryorderdetailHelper helper = new FactoryorderdetailHelper();
//		for (ProductFactoryorderSummary gv : result) {
//			IPagedResult<Factoryorderdetail> pr = helper.findByProperty("model",
//					gv.getModel());
//			gv.setDetails(pr.getResult(0, pr.count()));
//		}
	}
}
