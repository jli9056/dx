/**
 * 
 */
package com.dixin.action.ladingBill;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.LadingBillHelper;
import com.dixin.hibernate.LadingBill;

/**
 * Add Action For JDO LadingBill
 * 
 * @author Luo
 * 
 */
public class SaveLadingBillAction extends GenericSaveAction<LadingBill> {
	public SaveLadingBillAction() {
		super(LadingBill.class, LadingBillHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat tf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public boolean process(HttpServletRequest request, JSONObject result) {
		String[] ids = request.getParameterValues("id");
		String dt = request.getParameter("ladingDate");
		String tm = request.getParameter("quantity");
		String comment = request.getParameter("comment");
		String sfinished = request.getParameter("finished");
		Integer finished = null;
		if (sfinished != null && sfinished.length() > 0) {
			if ("是".equals(sfinished)) {
				finished = 1;
			} else {
				finished = 0;
			}
		}
		if (comment == null || comment.length() == 0) {
			comment = null;
		}
		Date date = null;
		Integer qt = null;
		if (dt != null && dt.length() > 0) {
			try {
				date = df.parse(dt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (tm != null && tm.length() > 0) {
			try {
				qt = Integer.parseInt(tm);
			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}

		LadingBillHelper helper = (LadingBillHelper) this.getHelper();
		Date update = new Date();
		for (String s : ids) {
			if (s == null || s.length() == 0) {
				continue;
			}
			Integer id = Integer.valueOf(s);
			LadingBill ar = helper.findById(id);
			if (date != null) {
				ar.setLadingDate(date);
			}
			if (qt != null) {
				if (ar.getForderdetail().getUnladingCount() < (qt - ar
						.getQuantity())) {
					throw new ActionException("修改失败，增加的提货数量必须小于欠货数量减去已有提货数量。");
				} else {
					ar.setQuantity(qt);
				}
			}
			if (comment != null && comment.length() > 0 || ids.length == 1) {
				ar.setComment(comment);
			}
			if (finished != null) {
				ar.setFinished(finished);
			}
			ar.setLastUpdate(update);
			helper.merge(ar);
		}
		return true;

	}
}
