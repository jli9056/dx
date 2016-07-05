package com.dixin.action.product;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.JsonAction;
import com.dixin.business.impl.ProductHelper;

public class ModifyPriceAction extends JsonAction {

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String s = request.getParameter("percent");
		double d = 0.0;
		try {
			d = Double.parseDouble(s);
		} catch (Exception ex) {
			throw new ActionException("输入数值有误" + s);
		}
		new ProductHelper().modifyPrice(d/100);
		return true;
	}

	public String getLocalizedName() {
		return "调整产品价格";
	}
}
