/**
 * 
 */
package com.dixin.action.delivery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.CurrentUser;
import com.dixin.action.GenericSaveAction;
import com.dixin.action.util.BeanParser;
import com.dixin.business.constants.Bool;
import com.dixin.business.impl.ArrangementHelper;
import com.dixin.business.impl.DeliveryHelper;
import com.dixin.business.impl.DeliverydetailHelper;
import com.dixin.hibernate.Arrangement;
import com.dixin.hibernate.Delivery;
import com.dixin.hibernate.Deliverydetail;
import com.dixin.util.TimestampUtil;

/**
 * Add Action For JDO Delivery
 * 
 * @author Luo
 * 
 */
public class SaveDoubleCheckedDeliveryAction extends
		GenericSaveAction<Delivery> {
	public SaveDoubleCheckedDeliveryAction() {
		super(Delivery.class, DeliveryHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected BeanParser<Deliverydetail> detailParser = new BeanParser<Deliverydetail>(
			Deliverydetail.class);
	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Delivery oldd = getHelper().findById(id);
		int oldDoubleCheck = oldd.getDoubleCheck();

		Delivery d = collect(request, result);
		if (d.getDoubleCheck() == Bool.FALSE && oldDoubleCheck == Bool.TRUE) {
			throw new ActionException("【复核】属性不能从【是】再改回【否】");
		}
		d.setDeliverydetails(new HashSet());

		String[] ids = request.getParameterValues("ids");
		String[] arrangeIds = request.getParameterValues("arrangeIds");

		DeliverydetailHelper ddhelper = new DeliverydetailHelper();
		ArrangementHelper ahelper = new ArrangementHelper();
		for (String s : ids) {
			if (s != null && s.matches("\\d+")) {
				Deliverydetail dd = ddhelper.findById(Integer.valueOf(s));
				if (dd != null) {
					d.getDeliverydetails().add(dd);
				}
			}
		}
		for (String s : arrangeIds) {
			if (s != null && s.matches("\\d+")) {
				Arrangement a = ahelper.findById(Integer.valueOf(s));
				Deliverydetail dd = new Deliverydetail();
				dd.setOrder(a.getOrderdetail().getOrder());
				dd.setComment(a.getComment());
				dd.setProduct(a.getProduct());
				dd.setQueueTime(TimestampUtil.combine(d.getDeliverDate(), a
						.getQueueTime()));
				dd.setQuantity(a.getQuantity());
				d.getDeliverydetails().add(dd);
			}
		}

		log.debug("Delivery: " + d);
		d.setChecker(CurrentUser.getCurrentUser().getUserName());
		getHelper().merge(d);
		return true;
	}
}
