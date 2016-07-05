/**
 * 
 */
package com.dixin.action.refund;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSaveAction;
import com.dixin.business.constants.RefundMethod;
import com.dixin.business.impl.RefundHelper;
import com.dixin.business.impl.StorehouseHelper;
import com.dixin.hibernate.Refund;
import com.dixin.hibernate.Storehouse;

/**
 * @author Luo
 * 
 */
public class SaveRefundAction extends GenericSaveAction<Refund> {
	public SaveRefundAction() {
		super(Refund.class, RefundHelper.class);
		uniqueKeys = new String[] { "", "id", "model", "alias", "barcode" };
	}

	public String getLocalizedName() {
		return "修复返修";
	}

	public boolean process(HttpServletRequest request, JSONObject result) {

		String sid = request.getParameter("id");
		String ssh = request.getParameter("storehouse");
		String squ = request.getParameter("quantity");
		RefundHelper helper = this.getHelper();
		Integer id = Integer.valueOf(sid);
		Refund r = helper.findById(id);
		Storehouse sh = new StorehouseHelper().findByProperty("name", ssh)
				.getResult(0, 1).get(0);
		Integer quantity = Integer.valueOf(squ);

		if ("revive".equals(request.getParameter("method"))) {
			if (!RefundMethod.返修.toString().equals(r.getMethod())) {
				throw new ActionException("只有返修的退货才能修复并存库");
			}
			helper.revive(r, sh, quantity);
		} else {
			throw new ActionException("不支持的操作");
		}

		return true;
	}
}
