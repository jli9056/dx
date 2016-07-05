/**
 * 
 */
package com.dixin.business.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.business.IPagedResult;
import com.dixin.business.constants.Bool;
import com.dixin.business.query.PagedCollection;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Payment;

/**
 * @author Jason
 * 
 */
public class PaymentHelper extends BaseHelper<Payment> {

	@Override
	public Payment merge(Payment detachedInstance) {
		log.info("编辑付款信息... ID="+detachedInstance.getId());
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Payment obj = (Payment) session.merge(detachedInstance);
			modifyPaidStatus(session, obj.getOrder());
			tx.commit();
			log.info("编辑付款信息完成!");
			return obj;
		} catch (RuntimeException e) {
			tx.rollback();
			log.info("编辑付款信息出现异常, 事务回滚!", e);
			if (e instanceof DataException) {
				throw (DataException) e;
			} else
				throw new DataException(e);
		}
	}

	@Override
	public boolean deleteById(Integer id) {
		log.info("删除付款记录...ID="+id);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Payment payment = (Payment) session.get(Payment.class, id);
			Order order = payment.getOrder();
			session.delete(payment);
			modifyPaidStatus(session, order);
			tx.commit();
			log.info("删除付款记录成功!");
			return true;
		} catch (Exception e) {
			tx.rollback();
			log.info("删除付款记录中出现异常, 事务回滚!",e);
			return false;
		}
	}

	@Override
	public void save(Payment transientInstance) {
		log.info("保存付款信息...");
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			savePayment(session, transientInstance);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			log.info("保存付款信息出现异常, 事务回滚!" + e);
			if (e instanceof DataException) {
				throw (DataException) e;
			} else
				throw new DataException(e);
		}
	}

	/**
	 * @param session
	 * @param payment
	 */
	public void savePayment(Session session, Payment payment) {
		session.save(payment);
		modifyPaidStatus(session, payment.getOrder());
	}

	/**
	 * 
	 * @param session
	 * @param order
	 */
	public void modifyPaidStatus(Session session, Order order) {
		log.info("修改付清状态...");
		if (isAllPaid(session, order)) {
			order.setIsPaid(Bool.TRUE);
		} else {
			order.setIsPaid(Bool.FALSE);
		}
		session.merge(order);
	}

	/**
	 * 
	 * @param session 
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isAllPaid(Session session, Order order) {
		Query query = session
				.createQuery("select sum(p.amount) from Payment p where p.order.id=:id");
		query.setInteger("id", order.getId());
		List result = query.list();
		double paidAmount = 0;
		if (!result.isEmpty()) {
			Object obj = result.get(0);
			if (obj != null) {
				paidAmount = Double.valueOf(obj.toString());
			}
		}
		double ret = paidAmount - order.getRealTotal();
		if (Math.abs(ret) < 1 || ret > 0) {// 当实际付款之和与成交金额相差不到1元时认为付完已完成
			return true;
		}
		return false;
	}

	/**
	 * 查找送货明细中没有完全付款的订单信息.
	 * 
	 * @deprecated
	 * @param deliveryDetails
	 * @return
	 */
	public List<Order> findUnpayedOrders(String order, String dir, int limit,
			int start) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {

			Query query = session
					.createQuery("select o from Payment p, Order o where p.order.id=o.id group by o.id having sum(p.amount)<o.realTotal order by o."
							+ order
							+ " "
							+ dir
							+ " limit "
							+ limit
							+ ","
							+ start);// 该订单可以有多次付款,
			List<Order> unpayedOrders = query.list();
			tx.commit();
			return unpayedOrders;
		} catch (Exception e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("查找未付款信息失败!", e);
		}
	}

	/**
	 * find payment information with customerName.
	 * 
	 * @param customerName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IPagedResult<Payment> findByCustomer(String customerName) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createQuery("select p from Payment p, Order o, Customer c where p.order.id=o.id and o.customer.id=c.id and c.name like :name");
			query.setParameter("name", customerName);
			tx.commit();
			return new PagedCollection<Payment>(query.list());
		} catch (RuntimeException e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("查找客户{0}的付款信息失败!",
					new Object[] { customerName }, e);
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IPagedResult<Payment> findByCustomerId(Integer id) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createQuery("select p from Payment p, Order o where p.order.id=o.id and o.customer.id=:id");
			query.setParameter("id", id);
			tx.commit();
			return new PagedCollection<Payment>(query.list());
		} catch (RuntimeException e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("查找客户ID{0}的付款信息失败!", new Object[] { id }, e);
		}
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IPagedResult<Payment> findByOrderId(Integer orderId) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createQuery("from Payment p where p.order.id=:id");
			query.setParameter("id", orderId);
			tx.commit();
			return new PagedCollection<Payment>(query.list());
		} catch (RuntimeException e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("查找订单{0}的付款信息失败!",
					new Object[] { orderId }, e);
		}
	}
}
