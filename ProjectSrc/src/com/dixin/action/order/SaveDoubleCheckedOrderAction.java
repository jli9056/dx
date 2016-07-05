/**
 * 
 */
package com.dixin.action.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.dixin.business.impl.OrderHelper;
import com.dixin.business.impl.ProductHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Product;

/**
 * Add Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class SaveDoubleCheckedOrderAction extends GenericSaveAction<Order> {
	public SaveDoubleCheckedOrderAction() {
		super(Order.class, OrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected BeanParser<Orderdetail> detailParser = new BeanParser<Orderdetail>(
			Orderdetail.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public String getLocalizedName() {
		return "复核/保存复核过的订单";
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Order order = collect(request, result);
		Order oldOrder = getHelper().findById(order.getId());
		if(oldOrder.getDoubleCheck()==1&&order.getDoubleCheck()==0){
			throw new ActionException("不能把已经订单的【复核】从【是】改回【否】！");
		}
		order.setOrderdetails(oldOrder.getOrderdetails());
		order.setIsPaid(oldOrder.getIsPaid());

		String[] detailsIds = request.getParameterValues("detailId");
		String[] product = request.getParameterValues("product");
		String[] price = request.getParameterValues("price");
		String[] quantity = request.getParameterValues("quantity");
		String[] deliverDate = request.getParameterValues("deliverDate");
		String[] comment = request.getParameterValues("detailcomment");

		if (detailsIds == null || detailsIds.length == 0) {
			throw new ActionException("请填写订单明细");
		}
		
		checkDuplicateNames(product);
		
		ProductHelper phelper = new ProductHelper();
		Set<Orderdetail> ds = new HashSet<Orderdetail>();
		Map<Integer, Orderdetail> dsmap = new HashMap<Integer, Orderdetail>();
		for (Iterator i = order.getOrderdetails().iterator(); i.hasNext();) {
			Orderdetail od = (Orderdetail) i.next();
			dsmap.put(od.getId(), od);
		}
		for (int i = 0; i < detailsIds.length; i++) {
			Orderdetail d = null;
			int did = Integer.parseInt(detailsIds[i]);
			if (did > 0) {
				d = dsmap.get(did);
				Date date = null;
				try {
					date = df.parse(deliverDate[i]);
				} catch (Exception e) {
					throw new ActionException("送货日期格式错误。第" + (i + 1) + "条明细。",
							e);
				}
				log.debug("DeliverDate:" + date);
				d.setDeliverDate(date);
				d.setComment(comment[i]);
				ds.add(d);
			} else {
				d = new Orderdetail();
				d.setId(null);
				d.setDeliveredCount(0);
				d.setReservedCount(0);
				d.setOrder(order);
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
				d.setPrice(Double.valueOf(price[i]));
				d.setComment(comment[i]);
				try {
					d.setDeliverDate(detailParser.getDateFormat().parse(
							deliverDate[i]));
					d.setScheduleDate(d.getDeliverDate());
				} catch (Exception e) {
					e.printStackTrace();
					throw new ActionException("订单明细日期格式错误");
				}
				ds.add(d);
			}
		}
		order.setOrderdetails(ds);
		order.setChecker(CurrentUser.getCurrentUser().getUserName());
		getHelper().merge(order);
		return true;
	}
}
