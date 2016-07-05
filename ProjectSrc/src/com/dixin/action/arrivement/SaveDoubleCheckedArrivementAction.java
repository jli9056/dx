/**
 * 
 */
package com.dixin.action.arrivement;

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
import com.dixin.business.impl.ArrivementHelper;
import com.dixin.business.impl.FactoryOrderHelper;
import com.dixin.business.impl.ProductHelper;
import com.dixin.business.impl.StorehouseHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Arrivedetail;
import com.dixin.hibernate.Arrivement;
import com.dixin.hibernate.Factoryorder;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Storehouse;

/**
 * Add Action For JDO FactoryOrder
 * 
 * @author Luo
 * 
 */
public class SaveDoubleCheckedArrivementAction extends
		GenericSaveAction<Arrivement> {
	public SaveDoubleCheckedArrivementAction() {
		super(Arrivement.class, ArrivementHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected BeanParser<Arrivedetail> detailParser = new BeanParser<Arrivedetail>(
			Arrivedetail.class);
	ProductHelper phelper = new ProductHelper();
	StorehouseHelper shelper = new StorehouseHelper();
	FactoryOrderHelper forderhelper = new FactoryOrderHelper();

	public String getLocalizedName() {
		return "复核/修改复核过的到货";
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {

		String id = request.getParameter("id");

		Arrivement oldarrive = getHelper().findById(Integer.parseInt(id));

		Arrivement arrive = collect(request, result);

		if (arrive.getDoubleCheck() == Bool.FALSE
				&& oldarrive.getDoubleCheck() == Bool.TRUE) {
			throw new ActionException("【复核】不能由【是】改回【否】");
		}

		String[] detailsIds = request.getParameterValues("detailId");
		String[] product = request.getParameterValues("product");
		String[] storehouse = request.getParameterValues("storehouse");
		String[] quantity = request.getParameterValues("quantity");
		String[] factoryorder = request.getParameterValues("factoryorder");
		if (detailsIds == null || detailsIds.length == 0) {
			throw new ActionException("请填写到货明细");
		}

		for (int i = 0; i < detailsIds.length; i++) {
			Arrivedetail d = new Arrivedetail();
			d.setId(new Integer(detailsIds[i]));
			d.setArrivement(arrive);
			Product p = findProduct(product[i]);
			if (p == null) {
				throw new ActionException("第" + (i + 1) + "条明细中的产品不存在");
			}
			d.setProduct(p);
			Storehouse sh = findStorehouse(storehouse[i]);
			if (sh == null) {
				throw new ActionException("第" + (i + 1) + "条明细中的仓库不存在");
			}
			d.setStorehouse(sh);

			Factoryorder forder = findFactoryorder(factoryorder[i]);
			if (forder == null) {
				throw new ActionException("第" + (i + 1) + "条明细没有指定有效工厂订单号");
			}
			d.setFactoryorder(forder);
			d.setQuantity(Integer.valueOf(quantity[i]));
			arrive.getArrivedetails().add(d);
		}
		log.debug("Arrivement: " + arrive.toJSONString());
		arrive.setChecker(CurrentUser.getCurrentUser().getUserName());
		getHelper().merge(arrive);
		return true;
	}

	Factoryorder findFactoryorder(String orderNo) {
		QueryDefn queryDefn = new QueryDefn();
		queryDefn = new QueryDefn();
		queryDefn.addCriterion(Restrictions.eq("orderNo", orderNo));
		IPagedResult<Factoryorder> strs = forderhelper.find(queryDefn);
		if (strs.count() > 0) {
			return strs.getResult(0, 1).get(0);
		} else {
			return null;
		}
	}

	Storehouse findStorehouse(String name) {
		QueryDefn queryDefn = new QueryDefn();

		queryDefn = new QueryDefn();
		queryDefn.addCriterion(Restrictions.eq("name", name));
		IPagedResult<Storehouse> strs = shelper.find(queryDefn);
		if (strs.count() > 0) {
			return strs.getResult(0, 1).get(0);
		} else {
			return null;
		}
	}

	Product findProduct(String model) {
		QueryDefn queryDefn = new QueryDefn();
		Criterion m = Restrictions.eq("model", model);
		Criterion a = Restrictions.eq("alias", model);
		Criterion b = Restrictions.eq("barcode", model);
		m = Restrictions.or(m, a);
		m = Restrictions.or(m, b);
		queryDefn.addCriterion(m);
		IPagedResult<Product> prod = phelper.find(queryDefn);
		if (prod.count() > 0) {
			return prod.getResult(0, 1).get(0);
		} else {
			return null;
		}
	}
}
