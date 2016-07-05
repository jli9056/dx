/**
 * 
 */
package com.dixin.action.statgroupview;

import java.util.List;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.StatGroupViewHelper;
import com.dixin.business.impl.StatViewHelper;
import com.dixin.hibernate.StatGroupView;
import com.dixin.hibernate.StatView;

/**
 * @author Luo
 * 
 */
public class SearchStatGroupViewAction extends
		GenericSearchAction<StatGroupView> {
	public SearchStatGroupViewAction() {
		super(StatGroupView.class, StatGroupViewHelper.class);
	}

	protected void processQueryResult(List<StatGroupView> result) {
		StatViewHelper helper = new StatViewHelper();
		for (StatGroupView gv : result) {
			IPagedResult<StatView> pr = helper.findByProperty("productModel",
					gv.getProductModel());
			gv.setStatViews(pr.getResult(0, pr.count()));
		}
	}
}
