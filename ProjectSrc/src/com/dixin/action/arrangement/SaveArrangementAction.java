/**
 * 
 */
package com.dixin.action.arrangement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.ArrangementHelper;
import com.dixin.hibernate.Arrangement;

/**
 * Add Action For JDO Arrangement
 * 
 * @author Luo
 * 
 */
public class SaveArrangementAction extends GenericSaveAction<Arrangement> {
	public SaveArrangementAction() {
		super(Arrangement.class, ArrangementHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat tf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public boolean process(HttpServletRequest request, JSONObject result) {
//		if ("batch".equals(request.getParameter("method"))) {
			String[] ids = request.getParameterValues("id");
			String dt = request.getParameter("deliverDate");
			String tm = request.getParameter("queueTime");
			String comment = request.getParameter("comment");
			if (comment == null || comment.length() == 0) {
				comment = null;
			}
			Date date = null;
			Date time = null;
			if (dt != null && dt.length() > 0) {
				try {
					date = df.parse(dt);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (tm != null && tm.length() > 0) {
				try {
					time = tf.parse(df.format(date) + ' ' + tm);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			Date update = new Date();
			ArrangementHelper helper = (ArrangementHelper) this.getHelper();
			for (String s : ids) {
				if (s == null || s.length() == 0) {
					continue;
				}
				Integer id = Integer.valueOf(s);
				Arrangement ar = helper.findById(id);
				if (date != null) {
					ar.setDeliverDate(date);
				}
				if (time != null) {
					ar.setQueueTime(time);
				}
				if (comment != null) {
					ar.setComment(comment);
				}
				ar.setLastUpdate(update);
				helper.merge(ar);
			}

			return true;
//		} else {
//			return super.process(request, result);
//		}
	}

	protected Arrangement validate(Arrangement t) {
		Arrangement a = this.getHelper().findById(t.getId());
		a.setDeliverDate(t.getDeliverDate());
		a.setQueueTime(t.getQueueTime());
		a.setComment(t.getComment());
		return a;
	}
}
