/**
 * 
 */
package com.dixin.action.delivery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;

import com.dixin.action.ActionException;
import com.dixin.action.GenericAddAction;
import com.dixin.action.util.BeanParser;
import com.dixin.business.impl.ArrangementHelper;
import com.dixin.business.impl.DeliveryHelper;
import com.dixin.hibernate.Delivery;
import com.dixin.hibernate.Deliverydetail;

/**
 * Add Action For JDO Delivery
 * 
 * @author Luo
 * 
 */
public class AddDeliveryAction extends GenericAddAction<Delivery> {
	public AddDeliveryAction() {
		super(Delivery.class, DeliveryHelper.class);
		uniqueKeys = new String[] { "", "id", "orderDate", "orderNo",
				"employee" };
	}

	protected BeanParser<Deliverydetail> detailParser = new BeanParser<Deliverydetail>(
			Deliverydetail.class);
	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Delivery delivery = collect(request, result);
		if (delivery == null) {
			throw new ActionException("请输入正确的送货信息");
		}
		String[] arrangId = request.getParameterValues("arrangeIds");
		if (arrangId.length == 0) {
			throw new ActionException("请添加送货明细。");
		}
		int[] ids = new int[arrangId.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Integer.parseInt(arrangId[i]);
		}
		try {
			new ArrangementHelper().confirmDelivery(delivery.getEmployee(),
					ids, delivery.getDeliverDate(), delivery.getComment());
			return true;
		} catch (DataIntegrityViolationException ex) {
			if (parseDataIntegrityViolationException(ex, result)) {
				return false;
			}
			throw ex;
		} catch (JDBCException ex) {
			if (parseDataIntegrityViolationException(ex, result)) {
				return false;
			}
			throw ex;
		}
	}
}
