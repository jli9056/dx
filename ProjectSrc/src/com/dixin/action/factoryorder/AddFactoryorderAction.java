/**
 * 
 */
package com.dixin.action.factoryorder;

import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;

import com.dixin.action.ActionException;
import com.dixin.action.GenericAddAction;
import com.dixin.action.util.BeanParser;
import com.dixin.business.impl.FactoryOrderHelper;
import com.dixin.hibernate.Factoryorder;
import com.dixin.hibernate.Factoryorderdetail;

/**
 * Add Action For JDO Factoryorder
 * 
 * @author Luo
 * 
 */
public class AddFactoryorderAction extends GenericAddAction<Factoryorder> {
	public AddFactoryorderAction() {
		super(Factoryorder.class, FactoryOrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderDate", "orderNo",
				"employee" };
	}

	protected BeanParser<Factoryorderdetail> detailParser = new BeanParser<Factoryorderdetail>(
			Factoryorderdetail.class);

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Factoryorder order = collect(request, result);
		if (order == null) {
			return false;
		}
		String[] product = request.getParameterValues("product");
		String[] cost = request.getParameterValues("cost");
		String[] quantity = request.getParameterValues("quantity");
		String[] pickDates = request.getParameterValues("pickDate");
		String[] comments = request.getParameterValues("dcomment");
		if (product == null || product.length == 0) {
			throw new ActionException("请填写订单明细");
		}
		
		checkDuplicateNames(product);
		
		Factoryorderdetail[] details = new Factoryorderdetail[product.length];
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> errors = new HashMap<String, String>();
		for (int i = 0; i < product.length; i++) {
			String prd = product[i];
			map.put("product", prd);
			map.put("quantity", quantity[i]);
			map.put("cost", cost[i]);
			map.put("pickDate", pickDates[i]);
			map.put("comment", comments[i]);
			details[i] = detailParser.parse(map, errors);
			if (errors.size() > 0) {
				throw new ActionException("请检查第" + i + "条订单明细。");
			}
			details[i].setAvailableCount(Integer.valueOf(0));
			details[i].setOwnedCount(Integer.valueOf(0));
		}
		HashSet ds = new HashSet();
		for (Factoryorderdetail d : details) {
			ds.add(d);
			d.setFactoryorder(order);
		}
		order.setFactoryorderdetails(ds);
		try {
			log.debug(order.toJSONString());
			getHelper().save(order);
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
