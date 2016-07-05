package com.dixin.business.impl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

import com.dixin.business.DataException;
import com.dixin.business.StatManager;
import com.dixin.business.constants.Bool;
import com.dixin.business.constants.DeliveryStatus;
import com.dixin.business.constants.RefundMethod;
import com.dixin.business.impl.RepertoryHelper.BiddingResult;
import com.dixin.hibernate.Delivery;
import com.dixin.hibernate.Deliverydetail;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Refund;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.Reserved;
import com.dixin.hibernate.Storehouse;
import com.dixin.util.UnionUtil;

public class DeliveryHelper extends BaseHelper<Delivery> {
	private SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private CorrectionHelper correctionHelper = new CorrectionHelper();
	private RepertoryHelper repertoryHelper = new RepertoryHelper();
	private OrderHelper orderHelper = new OrderHelper();

	/**
	 * 查找可以送货的订单明细, start和end分别表示开始日期和结束日期, 至少有一个不为null.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Orderdetail> findDeliverableOderdetail(Date start, Date end) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			log.info(MessageFormat.format("查找可送货的订单明细! 起始日期:{0} 截至日期:{1}", start, end));
			String filter = getFilterExpr(start, end);
			if (filter.length() == 0)
				throw new DataException("参数start和end至少一个不为空!");
			Query query = session
					.createQuery("from Orderdetail o where o.reservedCount>0 and o.deliveredCount<o.quantity and "
							+ filter + " order by o.deliverDate");
			if (filter.contains(":sd")) {
				query.setString("sd", dateFormater.format(start));
			}
			if (filter.contains(":ed")) {
				query.setString("ed", dateFormater.format(end));
			}
			List<Orderdetail> result = query.list();
			tx.commit();
			return result;
		} catch (Exception e) {
			tx.rollback();
			log.info("查找可送货订单信息出现异常, 事务回滚!" , e);
			throw new DataException("生成发货信息失败!", e);
		}
	}

	/**
	 * 查找送货日期从日期<code>start</code>开始到日期<code>end</code>截至(包括)的所有订单,
	 * 把所有已留货且付款完成的订单明细生成送货明细.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Deliverydetail> generateDelivery(List<Integer> orderdetailIds,
			Date queueTime) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			List<Deliverydetail> deliveryDetails = new ArrayList<Deliverydetail>();
			for (Integer id : orderdetailIds) {
				Orderdetail od = (Orderdetail) session.get(Orderdetail.class,
						id);
				Deliverydetail deliverydetail = new Deliverydetail(
						null, // Delivery暂时为null
						od.getProduct(), od.getOrder(), od.getReservedCount(),
						queueTime);
				deliveryDetails.add(deliverydetail);// 根据留货数目确定送货数量
			}
			tx.commit();
			return deliveryDetails;
		} catch (Exception e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("生成发货信息失败!", e);
		}
	}

	/**
	 * 查找送货明细中没有完全付款的订单信息.
	 * 
	 * @param deliveryDetails
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<Order> findUnpayedOrders(Date start, Date end) {
		List<Orderdetail> orderdetails = findDeliverableOderdetail(start, end);
		Session session = getSession();
		Set<Order> unpayedOrders = new HashSet<Order>();
		Transaction tx = session.beginTransaction();
		try {
			for (Orderdetail dd : orderdetails) {
				Order order = dd.getOrder();
				Query query = session
						.createQuery("select sum(p.amount) from Payment p where p.order.id=:oid");// 该订单可以有多次付款,
				// 其总和即为用户已付款金额
				query.setParameter("oid", order.getId());
				List result = query.list();
				Double payedAmount = (Double) result.get(0);
				if (payedAmount.compareTo(order.getRealTotal()) < 0) {// 已付金额小于实际应付金额
					unpayedOrders.add(order);
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("查找未付款信息失败!", e);
		}
		return unpayedOrders;
	}

	/**
	 * @param startDateStr
	 * @param endDateStr
	 */
	private String getFilterExpr(Date start, Date end) {
		String filter = "";
		if (start != null) {
			filter += "o.deliverDate>=:sd";
		}
		if (filter.length() > 0)
			filter += " and ";
		if (end != null) {
			filter += "o.deliverDate<=:ed";
		}
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#deleteById(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteById(Integer id) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			log.info("删除送货记录... ID=" + id);
			Delivery delivery = (Delivery) session.get(Delivery.class, id);
			Set details = delivery.getDeliverydetails();
			
			// 删除送货明细
			if(delivery.getDoubleCheck()==Bool.TRUE){
				for (Iterator i = details.iterator(); i.hasNext();) {
					Deliverydetail detail = (Deliverydetail) i.next();
					// 订单明细的已送货数量减少, 库存增加
					modifyDeliveredCount(session, detail, -detail.getQuantity());
					log.info("删除送货明细记录. ID=" + detail.getId());
					session.delete(detail);
				}
			}else{
				for (Iterator i = details.iterator(); i.hasNext();) {
					Deliverydetail detail = (Deliverydetail) i.next();
					// 订单明细的已送货数量减少, 库存增加
					//modifyDeliveredCount(session, detail, -detail.getQuantity());
					log.info("删除送货明细记录. ID=" + detail.getId());
					session.delete(detail);
				}
			}
			session.delete(delivery);
			tx.commit();
			if (delivery.getDoubleCheck() == Bool.TRUE) {
				int[] ids = getProductIds(delivery);
				StatManager.getInstance().updateStatView(session, ids);
			}
			log.info("删除送货记录成功!");
			return true;
		} catch (Exception e) {
			tx.rollback();
			log.error("rollback:", e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	public Delivery merge(Delivery delivery) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			
			int hadDoubleChecked = this.findById(delivery.getId()).getDoubleCheck();
			
			log.info("编辑送货记录... ID=" + delivery.getId());
			Delivery mergedDelivery = (Delivery) session.merge(delivery);
			
			int doubleChecked = delivery.getDoubleCheck();
			// 删除旧的送货明细
			Query query = session
					.createQuery("from Deliverydetail dd where dd.delivery.id=:id");
			query.setParameter("id", delivery.getId());
			if(hadDoubleChecked==Bool.TRUE){
				for (Iterator j = query.iterate(); j.hasNext();) {
					Deliverydetail detail = (Deliverydetail) j.next();
					modifyDeliveredCount(session, detail, -detail.getQuantity());
					session.delete(detail);
				}
			}else{
				for (Iterator j = query.iterate(); j.hasNext();) {
					Deliverydetail detail = (Deliverydetail) j.next();
					//modifyDeliveredCount(session, detail, -detail.getQuantity());
					session.delete(detail);
				}
			}
			// 保存修改后的订单明细
			Set details = delivery.getDeliverydetails();
			if(doubleChecked==Bool.TRUE){
				for (Iterator i = details.iterator(); i.hasNext();) {
					Deliverydetail detail = (Deliverydetail) i.next();
					modifyDeliveredCount(session, detail, detail.getQuantity());
					session.save(detail);
				}
			}else{
				for (Iterator i = details.iterator(); i.hasNext();) {
					Deliverydetail detail = (Deliverydetail) i.next();
					//modifyDeliveredCount(session, detail, detail.getQuantity());
					session.save(detail);
				}
			}
			tx.commit();
			if (mergedDelivery.getDoubleCheck() == Bool.TRUE) {
				int[] ids = getProductIds(delivery);
				int[] mergedIds = getProductIds(mergedDelivery);
				StatManager.getInstance().updateStatView(session,
						UnionUtil.union(ids, mergedIds));
			}
			log.info("编辑送货记录成功!");
			return mergedDelivery;
		} catch (Exception e) {
			tx.rollback();
			log.error("rollback:", e);
			throw new DataException("保存失败，请检查输入", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	public void save(Delivery delivery) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			saveDelivery(delivery, session);
			tx.commit();
			log.info("保存送货信息成功!");
		} catch (Exception e) {
			tx.rollback();
			log.error("保存送貨信息中出现异常, 事务回滚!", e);
			if (e instanceof DataException) {
				throw (DataException) e;
			} else
				throw new DataException("保存失败，请检查输入", e);
		}
	}

	/**
	 * @param delivery
	 * @param session
	 * @throws HibernateException
	 * @throws DataException
	 */
	public void saveDelivery(Delivery delivery, Session session)
			throws HibernateException, DataException {
		session.save(delivery);
		log.info("保存送货信息... ");
		Set details = delivery.getDeliverydetails();
		if (details.isEmpty()) {
			log.error("添加送货失败, 送货明细不能为空!");
			throw new DataException("添加送货失败, 送货明细不能为空!");
		}
		for (Iterator i = details.iterator(); i.hasNext();) {
			Deliverydetail detail = (Deliverydetail) i.next();
			//modifyDeliveredCount(session, detail, detail.getQuantity());
			//detail.setStatus(DeliveryStatus.全部送货成功.toString());
			session.save(detail);
			//Order order = detail.getOrder();
			//session.merge(order);
		}
	}

	/**
	 * set the deliveredCount in Orderdetail with <code>count</code>.
	 * 
	 * @param session
	 * @param detail
	 * @param delta
	 */
	private void modifyDeliveredCount(Session session, Deliverydetail detail, Integer delta) {
		Product product = detail.getProduct();
		Set orderdetails = detail.getOrder().getOrderdetails();
		for (Iterator j = orderdetails.iterator(); j.hasNext();) {
			Orderdetail od = (Orderdetail) j.next();
			if (od.getProduct().getId().equals(product.getId())) {
				log.info(MessageFormat.format("修改订单明细{0}的已送货数量. 原数量:{1} 变化量:{2}", od
						.getId(), od.getDeliveredCount(), delta));
				od.setDeliveredCount(od.getDeliveredCount() + delta);
				session.merge(od);
				changeOrderStatus(session, od.getOrder());
				// 修改仓库库存和仓库的已留货数量
				resetReservedCount(session, od, product, -delta);
				detail.setCost(od.getCost());
				//TODO 这样不支持一个客户订单里有多个订单明细订同样的产品
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param session
	 * @param detail
	 * @param delta
	 * @param isRefund
	 */
	private void changeDeliveredForRefund(Session session,
			Deliverydetail detail, Integer delta, boolean isRefund) {
		Product product = detail.getProduct();
		Set orderdetails = detail.getOrder().getOrderdetails();
		for (Iterator j = orderdetails.iterator(); j.hasNext();) {
			Orderdetail od = (Orderdetail) j.next();
			Product pd = od.getProduct();
			if (pd.equals(product)) {
				log.info(MessageFormat.format("订单明细{0} 原送货量:{1} 变化量:{2}", od.getId(),
						od.getDeliveredCount(), delta));
				od.setDeliveredCount(od.getDeliveredCount() + delta);
				session.merge(od);
				
				// 修改仓库库存和仓库的已留货数量
				if (isRefund) {
					// 退货: 订单数量减少(delta为负值), 库存数量增加
					log.info(String
							.format("退货: 订单数量减小量:{0}, 库存增加量:{0},", -delta));
					int remain = od.getQuantity() + delta;
					if (remain == 0) {
						od.setQuantity(delta);
						od.setIsValid(Bool.FALSE);
						session.merge(od);
					} else {
						od.setQuantity(remain);
						Orderdetail deltaOd = od.clone();
						deltaOd.setQuantity(delta);
						session.save(deltaOd);
					}
					Storehouse st = getDefaultStorehouse(session);
					Query query = session.createQuery("from Repertory r where r.storehouse.id=:sid and r.product.id=:pid");
					query.setInteger("sid", st.getId());
					query.setInteger("pid", pd.getId());
					List list = query.list();
					if (list.isEmpty()) {
						Repertory repertory = new Repertory(st, pd, -delta, 0);
						session.save(repertory);
						Product p = repertory.getProduct();
						repertoryHelper.ingather(session, repertory, -delta, od
								.getCost());
						correctionHelper.log(session,
								repertory.getStorehouse(), p, -delta, od
										.getCost(), "入库[退货]");
					} else {
						Repertory repertory = (Repertory) list.get(0);
						repertory.setQuantity(repertory.getQuantity() - delta);
						session.merge(repertory);
						Product p = repertory.getProduct();
						repertoryHelper.ingather(session, repertory, -delta, od
								.getCost());
						correctionHelper.log(session,
								repertory.getStorehouse(), p, -delta, od
										.getCost(), "入库[退货]");
					}
				}
				changeOrderStatus(session, od.getOrder());
				return;
			}
		}
	}

	/**
	 * change the status of the specified order.
	 * 
	 * @param order
	 */
	private void changeOrderStatus(Session session, Order order) {
		orderHelper.autoUpdateStatus(session, order);
	}

	/**
	 * delta正值: 订单明细已留货数量增加, 库存数量和已留货数量增加, Reserved增加
	 * delta负值: 订单明细已留货数量减少, 库存数量和已留货数量减少, Reserved减少
	 * @param session
	 * @param orderdetail
	 * @param modifyRepertory
	 * @param detail
	 */
	private void resetReservedCount(Session session, Orderdetail orderdetail, Product p, int delta) {
		log.info("修改已留货数量和库存信息...");
		if (p.equals(orderdetail.getProduct())) {
			log.info(MessageFormat.format("订单明细{0} 原数量:{1} 变化量:{2}", orderdetail
					.getId(), orderdetail.getReservedCount(), delta));
			int newReservecCnt = orderdetail.getReservedCount() + delta;
			if (newReservecCnt < 0)
				throw new DataException("订单明细{0}留货数量为负数{1}，不能继续操作！",
						new Object[] { orderdetail.getId(), newReservecCnt });
			orderdetail.setReservedCount(newReservecCnt);
			session.merge(orderdetail);
			// 修改所有该订单明细的所有留货信息
			Query query = session
					.createQuery("from Reserved r where r.orderdetail.id=:id");
			query.setInteger("id", orderdetail.getId());
			List list = query.list();
			if (list.isEmpty()) {// 恢复(增加)库存 ,此时delta必定为正
				Storehouse st = getDefaultStorehouse(session);
				Reserved reserved = new Reserved(st, orderdetail, delta);
				session.save(reserved);
				Repertory r = repertoryHelper.findRepertoryInfo(session,
						reserved.getStorehouse(), orderdetail.getProduct());
				if (r != null) {
					r.setQuantity(r.getQuantity() + delta);
					r.setReservedCount(r.getReservedCount() + delta);
					session.merge(r);
				} else {
					Repertory repertory = new Repertory(st, orderdetail.getProduct(), delta, delta);
					session.save(repertory);
					r = repertory;
				}
				updateBiddingsAndCorrection(session, r, p, delta, orderdetail);
			}
			int total = delta >= 0 ? delta : -delta;
			for (Iterator i = list.iterator(); total > 0 && i.hasNext();) {
				Reserved reserved = (Reserved) i.next();
				// 修改orderdetail和storehouse对应的库存信息
				Repertory r = repertoryHelper.findRepertoryInfo(session,
						reserved.getStorehouse(), orderdetail.getProduct());
				if (r != null) {
					int reservedCount = reserved.getReservedCount();
					int v = 0;
					if (delta < 0) {
						v = -Math.min(total, reservedCount);
						total += v;
					} else {
						v = total;
						total = 0;
					}
					// 修改仓库库存和已留货数量
					int quantity = r.getQuantity() + v;
					if (quantity == 0) {
						session.delete(r);
					} else {
						r.setReservedCount(r.getReservedCount() + v);
						r.setQuantity(quantity);
						session.merge(r);
					}
					updateBiddingsAndCorrection(session, r, p, v, orderdetail);
					// 修改留货信息
					int remainCount = reservedCount + v;
					if (remainCount == 0) {
						session.delete(reserved);
					} else {
						reserved.setReservedCount(remainCount);
						session.merge(reserved);
					}
				}
			}
		}
	}

	/**
	 * @param session
	 * @param r
	 * @param p
	 * @param delta
	 * @param orderdetail
	 */
	private void updateBiddingsAndCorrection(Session session, Repertory r,
			Product p, int delta, Orderdetail orderdetail) {
		if (delta > 0) {//入库
			repertoryHelper.ingather(session, r, delta,
					orderdetail.getCost());
		} else if (delta < 0) {// 出库
			BiddingResult bindingResult = repertoryHelper.consume(session, r, -delta);
			orderdetail.setCost(bindingResult.cost);
		}
		correctionHelper.log(session, orderdetail, r.getStorehouse(), p, delta,
				orderdetail.getCost(), delta < 0 ? "出库[送货]" : "入库[删除送货]");
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	private Storehouse getDefaultStorehouse(Session session) {
		log.info("查找默认可出货的仓库...");
		Criteria criteria = session.createCriteria(Storehouse.class);// 第一个可出货的仓库为默认仓库
		criteria.add(Expression.eq("shippable", Bool.TRUE));
		criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
		criteria.setFetchSize(1);
		List stores = criteria.list();
		if (stores.isEmpty()){
			log.error("没有找到可出货的仓库!");
			throw new DataException("错误! 没有找到可出货的仓库!");
		}
		Storehouse st = (Storehouse) stores.get(0);
		return st;
	}


	/**
	 * 退貨. 退貨只能对应某个订单明细, quantity表示订单明细中的需要返修产品个数. 返修操作后,
	 * 该送货明细中的送货数量减少quantity个.
	 * 
	 * @param deliveryDetail
	 * @param refundQuantity
	 * @param date
	 */
	public void refundProduct(Deliverydetail deliveryDetail,
			Integer refundQuantity, Date date) {
		log.info(MessageFormat.format("退货... 送貨明細:{0} 退貨數量:{1}", deliveryDetail
				.getId(), refundQuantity));
		if (deliveryDetail.getDoubleCheck() == Bool.FALSE) {
			throw new DataException("要退货请先复核该送货明细！");
		}
		if (refundQuantity > deliveryDetail.getQuantity()) {
			log.error("退货数目不能大于送货数目!");
			throw new DataException("退货数目不能大于送货数目!");
		}

		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			log.info("保存退货记录...");
			String method = RefundMethod.退货.toString();
			Refund refund = new Refund(deliveryDetail, refundQuantity, method,
					Bool.TRUE, date);
			session.save(refund);// 生成退货信息
			
			// 送货数目减少
			log.info(MessageFormat.format("送货明细{0} 原数量:{1} 退货量:{2}", deliveryDetail.getId(), deliveryDetail.getQuantity(),refundQuantity));
			int remainCount = deliveryDetail.getQuantity() - refundQuantity;
			deliveryDetail.setQuantity(remainCount);
			session.merge(deliveryDetail);
			// 修改订单明细的已送货数量, 修改库存
			changeDeliveredForRefund(session, deliveryDetail, -refundQuantity, true);
			changeDeliveryStatus(deliveryDetail, true, session, refundQuantity);
			tx.commit();
			StatManager.getInstance().updateStatView(session,
					new int[] { deliveryDetail.getProduct().getId() });
			log.info("退货成功!");
		} catch (Exception e) {
			tx.rollback();
			log.error("退货中出现异常, 事务回滚!", e);
			throw new DataException("失败，请检查输入", e);
		}
	}

	/**
	 * @param deliveryDetail
	 * @param session
	 * @param quantity 
	 */
	private void changeDeliveryStatus(Deliverydetail deliveryDetail,
			boolean isRefund, Session session, Integer quantity) {
		if (isRefund) {
			deliveryDetail.setStatus(DeliveryStatus.已退货.toString() + quantity	+ "件");
		} else {
			deliveryDetail.setStatus(DeliveryStatus.已返修.toString() + quantity	+ "件");
		}
		session.merge(deliveryDetail);
	}

	/**
	 * 返修产品. 返修只能对应某个订单明细, quantity表示订单明细中的需要返修产品个数. 返修操作后,
	 * 该送货明细中的送货数量减少quantity个.
	 * 
	 * @param deliveryDetail
	 * @param returnQuantity
	 * @param date
	 */
	public void repairProduct(Deliverydetail deliveryDetail,
			Integer returnQuantity, Date date) {
		if (returnQuantity > deliveryDetail.getQuantity()) {
			throw new DataException("返修数目不能大于送货数目!");
		}

		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			changeDeliveryStatus(deliveryDetail, false, session, returnQuantity);
			String method = RefundMethod.返修.toString();
			Refund refund = new Refund(deliveryDetail, returnQuantity, method,
					0, date);
			session.save(refund);// 生成退货信息

			// 送货数目减少
			int remainCount = deliveryDetail.getQuantity() - returnQuantity;
			deliveryDetail.setQuantity(remainCount);
			session.merge(deliveryDetail);
			// 修改订单明细的已送货数量
			changeDeliveredForRefund(session, deliveryDetail, -returnQuantity, false);
			tx.commit();
			StatManager.getInstance().updateStatView(session,
					new int[] { deliveryDetail.getProduct().getId() });
		} catch (Exception e) {
			tx.rollback();
			log.error("rollback:", e);
			throw new DataException("失败，请检查输入", e);
		}
	}
	
	/**
	 * 
	 * @param delivery
	 * @return
	 */
	private int[] getProductIds(Delivery delivery) {
		Set dd = delivery.getDeliverydetails();
		int[] ids = new int[dd.size()];
		int index = 0;
		for (Iterator i = dd.iterator(); i.hasNext();) {
			Deliverydetail ddt = (Deliverydetail) i.next();
			ids[index++] = ddt.getProduct().getId();
		}
		return ids;
	}
}
