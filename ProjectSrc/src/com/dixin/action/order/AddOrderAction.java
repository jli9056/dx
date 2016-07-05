/**
 * 
 */
package com.dixin.action.order;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;

import com.dixin.action.ActionException;
import com.dixin.action.GenericAddAction;
import com.dixin.action.util.BeanParser;
import com.dixin.business.impl.OrderHelper;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Payment;
import com.dixin.hibernate.Product;

/**
 * Add Action For JDO Order
 * 
 * @author Luo
 * 
 */
public class AddOrderAction extends GenericAddAction<Order> {
	public AddOrderAction() {
		super(Order.class, OrderHelper.class);
		uniqueKeys = new String[] { "", "id", "orderNo", "customer" };
	}

	protected BeanParser<Orderdetail> detailParser = new BeanParser<Orderdetail>(
			Orderdetail.class);

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		Order order = collect(request, result);
		if (order == null) {
			return false;
		}
		String[] product = request.getParameterValues("product");
		String[] price = request.getParameterValues("price");
		String[] quantity = request.getParameterValues("quantity");
		String[] deliverDate = request.getParameterValues("deliverDate");
		String[] comment = request.getParameterValues("detailcomment");
		if (product == null || product.length == 0) {
			throw new ActionException("请填写订单明细");
		}
		
		checkDuplicateNames(product);
		
		Orderdetail[] details = new Orderdetail[product.length];
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> errors = new HashMap<String, String>();
		for (int i = 0; i < product.length; i++) {
			String prd = product[i];
			map.put("product", prd);
			map.put("quantity", quantity[i]);
			map.put("price", price[i]);
			map.put("comment", comment[i]);
			try {
				details[i] = detailParser.parse(map, errors);
				Product p = details[i].getProduct();
				if (p != null) {
					details[i].setCost(p.getCost());
				}
			} catch (Exception ex) {
				throw new ActionException("输入错误：请检查第" + (i + 1) + "条订单明细。");
			}
			if (errors.size() > 0) {
				throw new ActionException("输入错误：请检查第" + (i + 1) + "条订单明细。");
			}
			validateDetail(details[i], i + 1);
			details[i].setDeliveredCount(Integer.valueOf(0));
			details[i].setReservedCount(Integer.valueOf(0));
			try {
				details[i].setDeliverDate(detailParser.getDateFormat().parse(
						deliverDate[i]));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ActionException("订单明细日期格式错误");
			}
		}
		HashSet ds = new HashSet();
		for (Orderdetail d : details) {
			ds.add(d);
			d.setOrder(order);
		}
		order.setOrderdetails(ds);
		if(order.getRealTotal()>order.getTotal()){
			throw new ActionException("实际金额不能大于折前金额！");
		}

		Double pay = Double.valueOf(request.getParameter("pay"));
		Payment pm = null;
		if (pay > 0) {
			if(pay>order.getRealTotal()){
				throw new ActionException("付款金额不能大于实际订单金额！");
			}
			String payMethod = request.getParameter("payMethod");
			String payComment = request.getParameter("payComment");
			pm = new Payment(order, pay, payMethod, order.getOrderDate());
			pm.setComment(payComment);
			Set<Payment> pms = new HashSet<Payment>();
			pms.add(pm);
			order.setPayments(pms);
		}
		try {
			if (pm != null) {
				new OrderHelper().saveOrderAndPayment(order, pm);
			} else {
				new OrderHelper().save(order);
			}
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


	private void validateDetail(Orderdetail d, int index) {
		if (d.getProduct() == null) {
			throw new ActionException("第" + index + "条明细指定的产品可能不存在，请检查输入");
		}
	}
}
