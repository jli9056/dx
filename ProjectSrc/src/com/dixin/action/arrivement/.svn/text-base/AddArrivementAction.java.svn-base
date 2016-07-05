/**
 * 
 */
package com.dixin.action.arrivement;

import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;

import com.dixin.action.ActionException;
import com.dixin.action.GenericAddAction;
import com.dixin.action.util.BeanParser;
import com.dixin.business.impl.ArrivementHelper;
import com.dixin.hibernate.Arrivement;
import com.dixin.hibernate.Arrivedetail;

/**
 * Add Action For JDO Arrivement
 * 
 * @author Luo
 * 
 */
public class AddArrivementAction extends GenericAddAction<Arrivement> {
	public AddArrivementAction() {
		super(Arrivement.class, ArrivementHelper.class);
		uniqueKeys = new String[] { "", "id", "orderDate", "orderNo",
				"employee" };
	}

	protected BeanParser<Arrivedetail> detailParser = new BeanParser<Arrivedetail>(
			Arrivedetail.class);

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Arrivement arrive = collect(request, result);
		if (arrive == null) {
			throw new ActionException("请填写正确的基本信息");
		}
		// if (arrive.getFactoryorder() == null) {
		// throw new ActionException("请输入正确的工厂订单号");
		// }
		if (arrive.getEmployee() == null) {
			throw new ActionException("请选择正确的负责员工");
		}
		if (arrive.getArriveDate() == null) {
			throw new ActionException("请输入正确的到货日期");
		}
		String[] product = request.getParameterValues("product");
		String[] storehouse = request.getParameterValues("storehouse");
		String[] quantity = request.getParameterValues("quantity");
		String[] factoryorder = request.getParameterValues("factoryorder");
		String defaultStorehouseName = request
				.getParameter("defaultStorehouse");

		for (int i = 0; i < storehouse.length; i++) {
			if (storehouse[i] == null || storehouse[i].length() == 0) {
				storehouse[i] = defaultStorehouseName;
			}
		}

		if (product == null || product.length == 0) {
			throw new ActionException("请填写到货明细");
		}
		// HashSet<String> set = new HashSet<String>();
		Arrivedetail[] details = new Arrivedetail[product.length];
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> errors = new HashMap<String, String>();
		for (int i = 0; i < product.length; i++) {
			String prd = product[i];
			// if (set.contains(prd)) {
			// throw new ActionException("到货明细中有重复产品。请将同一产品作为一个条目。");
			// }
			// set.add(prd);

			map.put("product", prd);
			map.put("quantity", quantity[i]);
			map.put("factoryorder", factoryorder[i]);
			if (quantity[i] == null || !quantity[i].matches("[0-9]+")) {
				throw new ActionException("请输入到货数量,第" + (i + 1) + "条明细");
			}
			if (Integer.parseInt(quantity[i]) < 1) {
				continue;
			}
			map.put("storehouse", storehouse[i]);
			if (storehouse[i] == null || storehouse[i].trim().equals("")) {
				throw new ActionException("请选择存入的仓库,第" + (i + 1) + "条明细");
			}
			details[i] = detailParser.parse(map, errors);
			if (errors.size() > 0) {
				throw new ActionException("请检查第" + i + "条到货明细。");
			}
		}
		HashSet ds = new HashSet();
		for (Arrivedetail d : details) {
			if (d != null) {
				ds.add(d);
				d.setArrivement(arrive);
			}
		}
		arrive.setArrivedetails(ds);
		try {
			log.debug(arrive.toJSONString());
			getHelper().save(arrive);
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
