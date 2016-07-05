/**
 * 
 */
package com.dixin.action.ladingBill;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.LadingBillHelper;
import com.dixin.business.impl.FactoryorderdetailHelper;
import com.dixin.hibernate.LadingBill;
import com.dixin.hibernate.Factoryorderdetail;

/**
 * Add Action For JDO LadingBill
 * 
 * @author Luo
 * 
 */
public class AddLadingBillAction extends GenericAddAction<LadingBill> {
	public AddLadingBillAction() {
		super(LadingBill.class, LadingBillHelper.class);
		uniqueKeys = new String[] { "", "id", "orderDate", "orderNo",
				"employee" };
	}

	private FactoryorderdetailHelper _odhelper;

	FactoryorderdetailHelper getOdhelper() {
		return _odhelper == null ? (_odhelper = new FactoryorderdetailHelper())
				: _odhelper;
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String[] ids = request.getParameterValues("detailId");
		String[] sgCounts = request.getParameterValues("sgCount");
		FactoryorderdetailHelper odh = getOdhelper();
		Date update = new Date();
		for (int i=0;i<ids.length;i++) {
			String id = ids[i];
			int count = Integer.parseInt(sgCounts[i]);
			if (count <= 0) {
				continue;
			}
			LadingBill ar = new LadingBill();
			Factoryorderdetail od = odh.findById(Integer.valueOf(id));
			if (count > od.getOwnedCount()) {
				throw new ActionException("建议提货数量大于欠货数量, 订单号【"
						+ od.getFactoryorder().getOrderNo() + "】产品【"
						+ od.getProduct().getModel() + "】不能加入提货单。");
			}
			ar.setForderdetail(od);
			ar.setQuantity(count);
			ar.setLadingDate(od.getOrderDate());
			ar.setComment(od.getComment());
			ar.setLastUpdate(update);
			this.getHelper().save(ar);
		}
		return true;
	}
}
