/**
 * 
 */
package com.dixin.action.orderdetail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.OrderdetailHelper;
import com.dixin.hibernate.Orderdetail;

/**
 * Add Action For JDO FactoryOrder
 * 
 * @author Luo
 * 
 */
public class SaveOrderdetailAction extends GenericSaveAction<Orderdetail> {
	public SaveOrderdetailAction() {
		super(Orderdetail.class, OrderdetailHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String[] ids = request.getParameterValues("id");
		String scheduleDate = request.getParameter("scheduleDate");
		String comment = request.getParameter("comment");
		OrderdetailHelper helper = this.getHelper();
		try {
			Date date = null;
			if (scheduleDate != null && scheduleDate.length() > 0) {
				date = df.parse(scheduleDate);
			}
			for (String sid : ids) {
				Integer id = Integer.valueOf(sid);
				Orderdetail de = helper.findById(id);
				if (de != null) {
					if (date != null || ids.length == 1)
						de.setScheduleDate(date);
					if (comment != null && comment.length() > 0
							|| ids.length == 1)
						de.setComment(comment);
					helper.merge(de);
				}
			}
		} catch (ParseException e) {
			throw new ActionException("输入数据格式不正确，请检查输入。", e);
		}
		return true;
	}
}
