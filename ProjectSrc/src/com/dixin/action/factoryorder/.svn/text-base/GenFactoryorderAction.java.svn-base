/**
 * 
 */
package com.dixin.action.factoryorder;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.GenericLoadAction;
import com.dixin.action.util.ResultUtil;

import com.dixin.business.impl.FactoryOrderHelper;
import com.dixin.hibernate.Factoryorder;

/**
 * Add Action For JDO Factoryorder
 * 
 * @author Luo
 * 
 */
public class GenFactoryorderAction extends GenericLoadAction<Factoryorder> {
	public GenFactoryorderAction() {
		super(Factoryorder.class, FactoryOrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderDate", "orderNo",
				"employee" };
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		FactoryOrderHelper helper = (FactoryOrderHelper) this.getHelper();
		Factoryorder order = new Factoryorder();
		order = helper.genFactoryorderFromOverflow(order);
		result.accumulate(ResultUtil.DATA, order);
		return true;
	}
}
