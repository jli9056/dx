/**
 * 
 */
package com.dixin.action.factoryorderdetail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.action.ActionException;
import com.dixin.action.GenericSearchAction;
import com.dixin.business.IPagedResult;
import com.dixin.business.IQueryDefn;
import com.dixin.business.impl.FactoryorderdetailHelper;
import com.dixin.hibernate.Factoryorderdetail;

/**
 * Search Action For JDO Factroryorder
 * 
 * @author Luo
 * 
 */
public class SearchFactoryorderdetailAction extends
		GenericSearchAction<Factoryorderdetail> {
	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public SearchFactoryorderdetailAction() {
		super(Factoryorderdetail.class, FactoryorderdetailHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.action.AbstractAction#getXCriterion(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	protected Criterion getXCriterion(String key, String value) {
		Criterion start = null, end = null;
		if ("deliverDateStart".equals(key) && !"".equals(value)) {
			String sql = "IFNULL((select SUM(od.quantity-od.delivered_count-od.reserved_count) from orderdetail od where od.fk_product_id={alias}.fk_product_id and od.deliver_date>'"
					+ value + "'),0)>0";
			start = Restrictions.sqlRestriction(sql);
		}
		if ("deliverDateEnd".equals(key) && !"".equals(value)) {
			String sql = "IFNULL((select SUM(od.quantity-od.delivered_count-od.reserved_count) from orderdetail od where od.fk_product_id={alias}.fk_product_id and od.deliver_date<'"
					+ value + "'),0)>0";
			end = Restrictions.sqlRestriction(sql);
		}
		if (start != null && end != null) {
			return Restrictions.and(start, end);
		} else {
			if (start != null) {
				return start;
			} else if (end != null) {
				return end;
			}
		}
		return null;
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		try {
			int limit = getLimit(request);
			int start = getStart(request);
			IQueryDefn query = getQueryDefn(request, result);
			IPagedResult<Factoryorderdetail> allResult = getHelper()
					.find(query);
			processAllResult(request, allResult.getResult(0, allResult.count()));
			beforeQuery(query);
			search(result, query, start, limit);
			getAggregations(request, query, result);
			return true;
		} catch (Exception ex) {
			throw new ActionException("查询失败，请检查输入条件。" + ex.getMessage(), ex);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param result
	 */
	private void processAllResult(HttpServletRequest request,
			List<Factoryorderdetail> result) {
		String start = request.getParameter("cr_deliverDateStart_X");
		String end = request.getParameter("cr_deliverDateEnd_X");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = df.parse("0001-01-01");
			endDate = df.parse("2999-12-31");
			if (start != null && !"".equals(start))
				startDate = df.parse(start);
			if (end != null && !"".equals(end))
				endDate = df.parse(end);
		} catch (ParseException e1) {
		}
		new FactoryorderdetailHelper().suggestLaddingCount(result, startDate,
				endDate);
	}
}
