/**
 * 
 */
package com.dixin.action.factoryorder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.action.ActionException;
import com.dixin.action.CurrentUser;
import com.dixin.action.GenericSaveAction;
import com.dixin.action.util.BeanParser;
import com.dixin.business.IPagedResult;
import com.dixin.business.constants.Bool;
import com.dixin.business.impl.FactoryOrderHelper;
import com.dixin.business.impl.ProductHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Factoryorder;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.Product;

/**
 * Add Action For JDO FactoryOrder
 * 
 * @author Luo
 * 
 */
public class SaveDoubleCheckedFactoryorderAction extends
		GenericSaveAction<Factoryorder> {
	public SaveDoubleCheckedFactoryorderAction() {
		super(Factoryorder.class, FactoryOrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected BeanParser<Factoryorderdetail> detailParser = new BeanParser<Factoryorderdetail>(
			Factoryorderdetail.class);

	public String getLocalizedName() {
		return "复核/保存复核过的工厂订单";
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Factoryorder order = collect(request, result);
		Factoryorder oldOrder = getHelper().findById(order.getId());
		if (oldOrder.getDoubleCheck() == Bool.TRUE
				&& order.getDoubleCheck() == Bool.FALSE) {
			throw new ActionException("订单的【复核】属性不能从【是】再改回【否】");
		}
		
		String[] detailsIds = request.getParameterValues("detailId");
		String[] product = request.getParameterValues("product");
		String[] cost = request.getParameterValues("cost");
		String[] quantity = request.getParameterValues("quantity");
		String[] pickDate = request.getParameterValues("pickDate");
		if (detailsIds == null || detailsIds.length == 0) {
			throw new ActionException("请填写订单明细");
		}
		
		checkDuplicateNames(product);
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		ProductHelper phelper = new ProductHelper();
		Set<Factoryorderdetail> ds = new HashSet<Factoryorderdetail>();
		Map<Integer, Factoryorderdetail> dsmap = new HashMap<Integer, Factoryorderdetail>();
		for (Factoryorderdetail fod : oldOrder.getFactoryorderdetails()) {
			dsmap.put(fod.getId(), fod);
		}
		for (int i = 0; i < detailsIds.length; i++) {
			Factoryorderdetail d = null;
			int did = Integer.parseInt(detailsIds[i]);
			if (did > 0) {
				d = dsmap.get(did);
				ds.add(d);
			} else {
				d = new Factoryorderdetail();
				d.setId(null);
				d.setAvailableCount(0);
				d.setOwnedCount(0);
				d.setFactoryorder(order);
				QueryDefn queryDefn = new QueryDefn();
				Criterion m = Restrictions.eq("model", product[i]);
				Criterion a = Restrictions.eq("alias", product[i]);
				Criterion b = Restrictions.eq("barcode", product[i]);
				m = Restrictions.or(m, a);
				m = Restrictions.or(m, b);
				queryDefn.addCriterion(m);
				IPagedResult<Product> prod = phelper.find(queryDefn);
				if (prod.count() > 0) {
					d.setProduct(prod.getResult(0, 1).get(0));
				} else {
					throw new ActionException("第" + (i + 1) + "条明细中的产品不存在");
				}
				d.setQuantity(Integer.parseInt(quantity[i]));
				d.setCost(Double.valueOf(cost[i]));
				try {
					d.setPickDate(formater.parse(pickDate[i]));
				} catch (ParseException e) {
					d.setPickDate(new Date());
				}
				ds.add(d);
			}
		}
		order.setFactoryorderdetails(ds);
		log.debug("order: " + order);
		order.setChecker(CurrentUser.getCurrentUser().getUserName());
		getHelper().merge(order);
		return true;
	}
}
