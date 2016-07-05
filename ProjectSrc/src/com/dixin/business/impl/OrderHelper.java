package com.dixin.business.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.business.StatManager;
import com.dixin.business.constants.Bool;
import com.dixin.business.constants.OrderStatus;
import com.dixin.hibernate.Available;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Overflow;
import com.dixin.hibernate.Payment;
import com.dixin.hibernate.Product;

public class OrderHelper extends BaseHelper<Order> {

	private ReserveHelper reserveHelper;

	/**
	 * 
	 */
	public OrderHelper() {
		reserveHelper = new ReserveHelper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#deleteById(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteById(Integer id) {
		log.info("删除客户订单... ID=" + id);
		Session session = getSession();
		Order order = findById(id);
		if (order == null) {
			log.error("无法删除不存在的订单:" + id);
			throw new DataException("无法删除不存在的订单:" + id);
		} else if (OrderStatus.FINISHED.equals(order.getStatus())) {
			log.error("订单已完成, 无法删除!");
			throw new DataException("订单已完成, 无法删除该订单!");
		} else if (order.getDeliverydetails().size() > 0) {
			log.error("订单已送货，无法删除！");
			// Qiang: 添加了 请先退货 
			throw new DataException("订单已送货，如需删除该订单，请遵循以下的流程：\n1.查看该订单对应的送货记录，通过退货流程使已送货数量变为0；\n2.删除该订单对应的送货记录;\n3.删除该客户订单。");
		}
		Transaction tx = session.beginTransaction();
		try {
			Set orderdetails = order.getOrderdetails();
			boolean persistentDelete = order.getIsValid()==Bool.FALSE;//彻底删除无效的订单
			for (Iterator i = orderdetails.iterator(); i.hasNext();) {
				Orderdetail detail = (Orderdetail) i.next();
				deleteDetail(session, detail, order.getDoubleCheck(), persistentDelete);
			}
			if (persistentDelete) {
				session.delete(order);
			} else {
				order.setIsValid(Bool.FALSE);
				order.setTotal(-order.getTotal());
				order.setRealTotal(-order.getRealTotal());
				session.merge(order);
			}
			tx.commit();
			log.info("删除客户订单成功! 订单号:" + order.getOrderNo());
			return true;
		} catch (Exception e) {
			tx.rollback();
			log.info("删除客户订单中出现异常, 事务回滚!", e);
			throw new DataException("删除客户订单出错! 订单号:{0}", new Object[] { order
					.getOrderNo() }, e);
		}
	}

	/**
	 * Qiang:
	 * 恢复被删除（退单）的客户订单
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean restoreById(Integer id) {
		log.info("恢复客户订单... ID=" + id);
		Session session = getSession();
		Order order = findById(id);
		if (order == null) {
			log.error("无法恢复不存在的订单:" + id);
			throw new DataException("无法恢复不存在的订单:" + id);
		}
		Transaction tx = session.beginTransaction();
		try {
			order.setStatus(OrderStatus.NEW_ORDER);
			order.setIsValid(Bool.TRUE);
			Double total = order.getTotal();
			order.setTotal(total >= 0 ? total : -total);
			Double realTotal = order.getRealTotal();
			order.setRealTotal(realTotal >= 0 ? realTotal : -realTotal);
			order.setChecker("");
			List details = new ArrayList(order.getOrderdetails());
			int[] pid = new int[details.size()];
			int index = 0;
			for (Iterator i = details.iterator(); i.hasNext();) {
				Orderdetail detail = (Orderdetail) i.next();
				if (detail.getIsValid() == Bool.FALSE) {
					restoreOrderdetail(session, detail);
				}
				pid[index++] = detail.getProduct().getId();
			}
			session.merge(order);
			StatManager.getInstance().updateStatView(session, pid);
			tx.commit();
			log.info("恢复客户订单成功!");
			return true;
		} catch (Exception e) {
			tx.rollback();
			log.info("恢复客户订单中出现异常, 事务回滚!", e);
			throw new DataException("恢复客户订单出错! 订单号:{0}", new Object[] { order
					.getOrderNo() }, e);
		}
	}
	
	/**
	 * @param session
	 * @param detail
	 */
/*	private void restoreOrderdetail(Session session, Orderdetail detail) {
		log.info("恢复订单明细...");
		
		int total = detail.getQuantity();
		detail.setIsValid(Bool.TRUE);
		int delta = reserveHelper.reserve(session, detail); // 如果库存有现货,
		// 则自动为该订单明细留货
		if (delta > 0) {
			log.info(MessageFormat.format("为订单明细留货, 留货数量:{0}", delta));
		}
		total -= delta;
		if (total > 0) {
			delta = comsumeFactoryAvailable(session, detail);// 如果现货不足,则从工厂订单里获取可用指标
			if (delta > 0) {
				log.info(MessageFormat.format("从工厂订单消费可用数量:{0}", delta));
			}
			total -= delta;
		}
		if (total > 0) {
			log.info(MessageFormat.format("生成计划外工厂订单, 数量:{0}", total));
			comsumeOverflow(session, detail, total);// 如果工厂订单的可用指标仍不够,
													// 则记录到溢出数据表, 用于生成计划外订单
		}
		session.merge(detail);
	}*/
	
	/**
	 * @param session
	 * @param detail
	 * @param persistentDelete 
	 */
	private void deleteArrangements(Session session, Orderdetail detail, boolean persistentDelete) {
		String sql = null;
		if (persistentDelete) {
			sql = "delete Arrangement a where a.orderdetail.id=:id";
		} else {
			sql = "update Arrangement a set a.isFinished=" + Bool.TRUE
					+ " where a.orderdetail.id=:id";
		}
		Query query = session.createQuery(sql);
		query.setInteger("id",detail.getId());
		query.executeUpdate();
	}

	/**
	 * @param session
	 * @param detail
	 * @param doubleCheck 
	 * @param persistentDelete
	 * 		true	-	从数据库中彻底删除数据;	false	-	仅仅标记为已删除
	 */
	private void deleteDetail(Session session, Orderdetail detail, int doubleCheck, boolean persistentDelete) {
		deleteArrangements(session, detail, persistentDelete);// 删除排货
		if (doubleCheck == Bool.TRUE) {
			// 解除该订单明细的留货
			if (detail.getReservedCount() > 0) {
				log.info(MessageFormat.format("解除订单明细{0}的留货! 解除数量:{1}", detail
						.getId(), detail.getReservedCount()));
				reserveHelper.dereserveOrderdetail(session, detail);
			}
			// 删除该订单明细相关的溢出数据
			log.info("删除订单明细相关的计划外订单... ID=" + detail.getId());
			deleteOverflow(session, detail);
			// 恢复计划可用量
			log.info("恢复工厂计划可用量...");
			restoreFactoryAvailable(session, detail);
			log.info("删除客户订单明细... ID=" + detail.getId());

			updateStatView(session, detail);
		}
		if (persistentDelete) {
			session.delete(detail);
		} else {
			detail.setIsValid(Bool.FALSE);
			int q = detail.getQuantity();
			detail.setQuantity(q >= 0 ? -q : q);
			session.merge(detail);
		}
	}

	/**
	 * @param session
	 * @param detail
	 */
	private void updateStatView(Session session, Orderdetail detail) {
		Product product = detail.getProduct();
		StatManager.getInstance().updateStatView(session,
				new int[] { product.getId() });
	}

	/**
	 * @param session
	 * @param detail
	 */
	@SuppressWarnings("unchecked")
	private void restoreFactoryAvailable(Session session, Orderdetail detail) {
		Query query = session
				.createQuery("from Available a where a.orderdetail.id=:id");
		query.setInteger("id", detail.getId());
		for (Iterator i = query.iterate(); i.hasNext();) {
			Available available = (Available) i.next();
			Factoryorderdetail fod = available.getFactoryorderdetail();
			if (fod == null) {
				continue;
			}
			log.info(MessageFormat.format("工厂订单{0} 原可用数量:{1} 增加数量:{2}", fod
					.getId(), fod.getAvailableCount(), available
					.getConsumedCount()));
			fod.setAvailableCount(fod.getAvailableCount()
					+ available.getConsumedCount());
			session.merge(fod);
			session.delete(available);
		}
	}

	/**
	 * @param session
	 * @param detail
	 */
	private void deleteOverflow(Session session, Orderdetail detail) {
		Query query = session
				.createQuery("delete Overflow o where o.orderdetail.id=:detailId");
		query.setInteger("detailId", detail.getId());
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	@SuppressWarnings("unchecked")
	public Order merge(Order detachedInstance) {
		log.info("编辑客户订单... 单号:" + detachedInstance.getOrderNo());
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			if (OrderStatus.FINISHED.equals(detachedInstance.getStatus())) {
				log.error("客户订单已完成, 不能再编辑!");
				throw new DataException("客户订单已完成, 不能再编辑。如有需要请添加新订单!");
			}
			Set details = detachedInstance.getOrderdetails();
			Order oldOrder = this.findById(detachedInstance.getId());
			Set oldDetails = oldOrder.getOrderdetails();
			int doubleCheck = detachedInstance.getDoubleCheck();
			for (Iterator j = oldDetails.iterator(); j.hasNext();) {
				Orderdetail od = (Orderdetail) j.next();
				// 删除不需要的订单明细
				if (needToDelete(od, details)) {
					deleteDetail(session, od, doubleCheck, true);
				}
			}
			int reserveFlag = doubleCheck-oldOrder.getDoubleCheck();//仅当第一次复核时为已存在明细留货
			oldOrder.setDoubleCheck(doubleCheck);//to make reserve helper work correctly
			for (Iterator i = details.iterator(); i.hasNext();) {
				Orderdetail od = (Orderdetail) i.next();
				// 新增订单明细
				saveDetail(session, od, doubleCheck, reserveFlag);
			}
			Order result = (Order) session.merge(detachedInstance);
			new PaymentHelper().modifyPaidStatus(session, result);
			autoUpdateStatus(session, result);//auto update the status of the newly merged order
			tx.commit();
			log.info("编辑客户订单成功! 单号:" + detachedInstance.getOrderNo());
			return result;
		} catch (RuntimeException e) {
			log.error("编辑客户订单过程中出现异常, 事务回滚!", e);
			tx.rollback();
			if (e instanceof DataException) {
				throw (DataException) e;
			} else
				throw new DataException("编辑订单{0}失败!",
						new Object[] { detachedInstance.getOrderNo() }, e);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean needToDelete(Orderdetail detail, Set details) {
		for (Iterator i = details.iterator(); i.hasNext();) {
			Orderdetail od = (Orderdetail) i.next();
			if (detail.getId().equals(od.getId()))
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	public void save(Order order) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			log.info("保存订单...订单号:"+order.getOrderNo());
			saveOrder(session, order);
			tx.commit();
			log.info("保存订单成功!");
		} catch (RuntimeException e) {
			log.error("保存订单过程中出现运行时异常, 事务回滚!", e);
			tx.rollback();
			throw e;
		} catch (Exception ex) {
			log.error("保存订单过程中出现异常, 事务回滚!", ex);
			tx.rollback();
			if (ex instanceof DataException) {
				throw (DataException) ex;
			} else
				throw new DataException("保存订单失败! 订单号:{0}", new Object[] { order
						.getOrderNo() }, ex);
		}
		log.info("保存订单成功! 订单号:"+order.getOrderNo());
	}

	/**
	 * @param session
	 * @param order
	 */
	@SuppressWarnings("unchecked")
	private void saveOrder(Session session, Order order) {
		/*if (OrderStatus.FINISHED.equals(order.getStatus())) {
			log.error("客户订单已完成, 不能再保存!");
			throw new DataException("客户订单已完成, 不能再保存!");
		}*/
		// 保存订单和相应的订单明细到数据库
		order.setStatus(OrderStatus.NEW_ORDER);
		session.save(order);
		Set details = order.getOrderdetails();
		for (Iterator i = details.iterator(); i.hasNext();) {
			Orderdetail detail = (Orderdetail) i.next();
			saveDetail(session, detail, order.getDoubleCheck(), Bool.FALSE);
		}
	}

	/**
	 * 保存订单和付款信息.
	 * 
	 * @param order
	 * @param payment
	 */
	public void saveOrderAndPayment(Order order, Payment payment) {
		log.error("保存客户订单和付款信息...订单号:"+order.getOrderNo());
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			saveOrder(session, order);
			new PaymentHelper().savePayment(session, payment);
			tx.commit();
		} catch (RuntimeException e) {
			log.error("保存订单过程中出现运行时异常, 事务回滚!", e);
			tx.rollback();
			throw e;
		} catch (Exception ex) {
			log.error("保存订单过程中出现异常, 事务回滚!", ex);
			tx.rollback();
			if (ex instanceof DataException) {
				throw (DataException) ex;
			} else
				throw new DataException("保存订单失败! 订单号:{0}", new Object[] { order
						.getOrderNo() }, ex);
		}
		log.info("保存客户订单和付款信息成功! 订单号:"+order.getOrderNo());
	}

	/**
	 * 
	 * @param session
	 * @param detail
	 * @param doubleCheck
	 * @param reserveFlag 
	 */
	private void saveDetail(Session session, Orderdetail detail, int doubleCheck, int reserveFlag) {
		if (detail.getId() == null) {
			session.save(detail);//
		} else {
			session.merge(detail);// 有可能更新客户明细的预约日期
			if (reserveFlag == Bool.FALSE) {
				return;
			}
		}
		
		// 订单复核之后才能进行留货
		if (doubleCheck == Bool.FALSE ) {
			log.info("Simply save order detail! id=" + detail.getId());
			return;
		}
		
		int total = detail.getQuantity();

		int delta = reserveHelper.reserve(session, detail); // 如果库存有现货,
		// 则自动为该订单明细留货
		if (delta > 0) {
			log.info(MessageFormat.format("为订单明细留货, 留货数量:{0}", delta));
		}
		total -= delta;
		if (total > 0) {
			delta = consumeFactoryAvailable(session, detail);// 如果现货不足,则从工厂订单里获取可用指标
			if (delta > 0) {
				log.info(MessageFormat.format("从工厂订单消费可用数量:{0}", delta));
			}
			total -= delta;
		}
		if (total > 0) {
			log.info(MessageFormat.format("生成计划外工厂订单, 数量:{0}", total));
			consumeOverflow(session, detail, total);// 如果工厂订单的可用指标仍不够,
													// 则记录到溢出数据表, 用于生成计划外订单
		}
		log.info("保存订单明细成功! id="+detail.getId());
		
		updateStatView(session, detail);
	}

	/**
	 * @param session
	 * @param detail
	 * @param count
	 */
	private void consumeOverflow(Session session, Orderdetail detail, int count) {
		if (count > 0) {
			session.save(new Overflow(detail, count));
		}
	}

	/**
	 * @param session
	 * @param detail
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int consumeFactoryAvailable(Session session, Orderdetail detail) {
		int deltaSum = 0;
		int count = detail.getQuantity() - detail.getReservedCount();// 留货后订单明细的产品总量
		// 查看工厂计划, 如果仍有指标可用则从计划指标中扣除
		if (count > 0) {
			Query query = session
					.createQuery("from Factoryorderdetail fd where fd.product.id=:id AND fd.factoryorder.doubleCheck=1 order by fd.factoryorder.id");
			query.setInteger("id", detail.getProduct().getId());
			for (Iterator itr = query.iterate(); count > 0 && itr.hasNext();) {
				Factoryorderdetail fod = (Factoryorderdetail) itr.next();
				final int availableCount = fod.getAvailableCount();
				int delta = Math.min(count, availableCount);
				if (delta == 0) {
					continue;
				}
				fod.setAvailableCount(availableCount - delta);
				session.merge(fod);
				count -= delta;
				deltaSum += delta;
				// log available information
				Available logAvailable = new Available(fod, detail, delta);
				session.save(logAvailable);
			}
		}
		return deltaSum;
	}
	
	/*private int[] getProductIds(Order order) {
		Set odSet = order.getOrderdetails();
		int[] ids = new int[odSet.size()];
		int index = 0;
		for (Iterator i = odSet.iterator(); i.hasNext();) {
			Orderdetail od = (Orderdetail) i.next();
			ids[index++] = od.getProduct().getId();
		}
		return ids;
	}*/
	
	/**
	 * auto update the status of the specified order.
	 * 
	 * @param order
	 */
	@SuppressWarnings("unchecked")
	public void autoUpdateStatus(Session session, Order order) {
		int total = 0;
		int deliveredCount = 0;
		Set details = order.getOrderdetails();
		for (Iterator i = details.iterator(); i.hasNext();) {
			Orderdetail od = (Orderdetail) i.next();
			if(od.getIsValid()==Bool.FALSE)
				continue;
			total += od.getQuantity();
			deliveredCount += od.getDeliveredCount();
		}
		if (total == deliveredCount) {
			log.info(MessageFormat.format("修改订单{0}状态为\"已完成\"!", order.getOrderNo()));
			order.setStatus(OrderStatus.FINISHED);
		} else {
			log.info(MessageFormat.format("修改订单{0}状态为\"新订单\"!", order.getOrderNo()));
			order.setStatus(OrderStatus.NEW_ORDER);
		}
		session.merge(order);
	}

	/**
	 * refund order detail.
	 * 
	 * @param od
	 * @param refundCount
	 * @param refundAmount
	 * @param refundDate
	 * @param refundMethod
	 * @param comment
	 */
	public void refundOrderdetail(Orderdetail od, int refundCount,
			double refundAmount, Date refundDate, String refundMethod,
			String comment) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			int remainCount = 0;
			if (refundCount < od.getQuantity()) {
				remainCount = od.getQuantity() - refundCount;
			}
			//release current order detail and set it as invalid
			deleteDetail(session, od, Bool.TRUE, false);
			if (remainCount > 0) {
				od.setQuantity(remainCount);
				od.setIsValid(Bool.TRUE);
			}
			if (remainCount == 0) {
				od.setComment(comment);
			}
			session.merge(od);
			//add refund order detail
			if (remainCount > 0) {
				Orderdetail refundOd = od.clone();
				refundOd.setQuantity(-refundCount);
				refundOd.setComment(comment);
				saveDetail(session, refundOd, od.getDoubleCheck(), Bool.FALSE);
			}
			if (refundAmount > 0) {
				// reduce real total for this order
				Order order = od.getOrder();
				order.setRealTotal(order.getRealTotal() - refundAmount);
				session.merge(order);
				// handle payment refund
				Payment refundPay = new Payment(order, -refundAmount,
						refundMethod, refundDate, comment, order.getCustomer()
								.getName());
				session.save(refundPay);
			}
			tx.commit();
		} catch (Exception e) {
			log.error("退单过程中发生错误!", e);
			tx.rollback();
			throw new DataException("退单过程中发生错误！", e);
		}
	}

	/**
	 * restore order detail.
	 * @param session
	 * @param orderdetail
	 */
	@SuppressWarnings("unchecked")
	private void restoreOrderdetail(Session session, Orderdetail orderdetail) {
		Integer id = orderdetail.getProduct().getId();
		Set details = orderdetail.getOrder().getOrderdetails();
		Orderdetail existOd = null;
		for (Iterator i = details.iterator(); i.hasNext();) {
			Orderdetail od = (Orderdetail) i.next();
			if (od.getIsValid() == Bool.TRUE
					&& od.getProduct().getId().equals(id)) {
				existOd = od;
				break;
			}
		}
		Integer quantity = orderdetail.getQuantity();//quantity is negative
		if (existOd == null) {
			orderdetail.setIsValid(Bool.TRUE);
			orderdetail.setQuantity(-quantity);
			session.merge(orderdetail);
		} else {
			existOd.setQuantity(existOd.getQuantity() - quantity);
			session.delete(orderdetail);
			session.merge(existOd);
		}
	}
}
