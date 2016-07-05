/**
 * 
 */
package com.dixin.action.factoryorderdetail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.FactoryorderdetailHelper;
import com.dixin.hibernate.Factoryorderdetail;

/**
 * Add Action For JDO FactoryOrder
 * 
 * @author Luo
 * 
 */
public class SaveFactoryorderdetailAction extends
		GenericSaveAction<Factoryorderdetail> {
	public SaveFactoryorderdetailAction() {
		super(Factoryorderdetail.class, FactoryorderdetailHelper.class);
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
		String pickDate = request.getParameter("pickDate");
		String comment = request.getParameter("comment");
		FactoryorderdetailHelper helper = this.getHelper();
		try {
			Date date = null;
			if (pickDate != null && pickDate.length() > 0) {
				date = df.parse(pickDate);
			}
			for (String sid : ids) {
				Integer id = Integer.valueOf(sid);
				Factoryorderdetail de = helper.findById(id);
				if (de != null) {
					if (date != null)
						de.setPickDate(date);
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
