/**
 * 
 */
package com.dixin.business.impl;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dixin.DixinException;
import com.dixin.business.DataException;
import com.dixin.business.IPagedResult;
import com.dixin.business.constants.Bool;
import com.dixin.business.constants.OrderStatus;
import com.dixin.business.query.PagedCollection;
import com.dixin.business.query.PagedResult;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Arrangement;
import com.dixin.hibernate.HibernateSessionFactory;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.Reserved;
import com.dixin.hibernate.Storehouse;

/**
 * @author Jason
 * 
 */
public class ReserveHelper {
	protected final Log log = LogFactory.getLog(ReserveHelper.class);

	/**
	 * find all reserved and partly reserved orders.
	 * 
	 * @return
	 */
	public IPagedResult<Order> findReservedOrders() {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		List<Order> reservedOrders = new LinkedList<Order>();
		try {
			Query query = session
					.createQuery("from Order o where o.isValid=1 order by o.id");// 查找所有未留货的订单
			for (Iterator i = query.iterate(); i.hasNext();) {
				Order order = (Order) i.next();
				int reserved = 0;
				for (Iterator j = order.getOrderdetails().iterator(); j
						.hasNext();) {
					Orderdetail detail = (Orderdetail) j.next();
					reserved += detail.getReservedCount();
				}
				if (reserved > 0)
					reservedOrders.add(order);
			}
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("查找所有已留货订单失败!", e);
		}
		return new PagedCollection<Order>(reservedOrders);
	}

	/**
	 * find all unreserved orders.
	 * 
	 * @return
	 */
	public IPagedResult<Order> findUnreservedOrders() {
		log.info("查找没有留货的订单...");
		QueryDefn query = new QueryDefn();
		query.addCriterion(Restrictions.eq("isVaid", 1));
		query.addCriterion(Restrictions.eq("status", OrderStatus.NEW_ORDER));
		query.addOrder(org.hibernate.criterion.Order.asc("deliverDate"));
		log.info("查找没有留货的订单成功!");
		return new PagedResult<Order>(Order.class, query);
	}

	/**
	 * 自动按照客戶订单送貨时间先后留货.
	 */
	public List<Order> reserveAll() {
		log.info("自动留货: 按照客户订单送货时间先后自动留货...");
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		List<Order> reservedOrders = new LinkedList<Order>();
		try {
			Query query = session
					.createQuery("from Orderdetail o where o.reserveLocked=0 and o.isValid=1 and o.doubleCheck=1 order by o.deliverDate, o.id");// 查找所有未留货的订单
			List list = query.list();
			if (list.size() == 0) {
				log.info("没有找到可以自动留货的订单明细!");
				throw new DataException("没有找到可以自动留货的订单明细!");
			}
			// 解除所有未锁定的已留货订单
			for (Iterator i = list.iterator(); i.hasNext();) {
				dereserveOrderdetail(session, (Orderdetail) i.next());
			}
			Order preOrder = null;
			// 重新按照送货时间先后自动留货
			for (Iterator i = list.iterator(); i.hasNext();) {
				Orderdetail detail = (Orderdetail) i.next();
				Order order = detail.getOrder();
				if (order.getIsValid() == Bool.FALSE || order.getDoubleCheck()==Bool.FALSE)
					continue;
				int delta = 0;
				try{
					delta = reserve(session, detail);
				}catch(Exception ex){
					//continue, because this is auto-reservlet
					continue;
				}				
				if (delta > 0 && (preOrder == null || !preOrder.equals(order))) {
					reservedOrders.add(order);
					preOrder = order;
				}
			}
			tx.commit();
			log.info("自动留货成功!");
		} catch (Exception e) {
			tx.rollback();
			log.info("自动留货过程中出现异常, 事务恢复!", e);
			if (e instanceof DataException)
				throw (DataException) e;
			else
				throw new DataException(e);
		}
		return reservedOrders;
	}

	/**
	 * 为某个客户订单留货.
	 * 
	 * @param order
	 */
	public void reserve(Order order) {
		if (order.getIsValid() == Bool.FALSE)
			return;
		if(order.getDoubleCheck() == Bool.FALSE){
			throw new DataException("客户订单还没有复核，不能留货："+order.getOrderNo());
		}
		log.info(MessageFormat.format("为客户订单{0}留货... ", order.getOrderNo()));
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		int delta = 0;
		try {
			delta = reserve(session, order);
			tx.commit();
			log.info(MessageFormat.format("客户订单{0}留货成功! 留货数量:{1}", order
					.getOrderNo(), delta));
		} catch (Exception e) {
			tx.rollback();
			log.error("客户订单留货失败, 事务回滚!", e);
			throw new DataException("为客户订单{0}留货失败!", new Object[] { order
					.getOrderNo() }, e);
		}
		if (delta == 0) {
			String msg = MessageFormat.format("库存不足, 无法为订单【{0}】留货!",
					new Object[] { order.getOrderNo() });
			log.error(msg);
			throw new DataException(msg);
		}
	}

	/**
	 * 自动为某个订单明细留货.
	 * 
	 * @param orderdetail
	 */
	public void reserve(Orderdetail orderdetail) {
		if (orderdetail.getIsValid() == Bool.FALSE)
			return;
		if(orderdetail.getOrder().getDoubleCheck() == Bool.FALSE){
			throw new DataException("客户订单还没有复核，不能留货："+orderdetail.getOrder().getOrderNo());
		}
		log.info("为客户订单明细留货... ID=" + orderdetail.getId());
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		int delta = 0;
		try {
			delta = reserve(session, orderdetail);
			tx.commit();
			log.info(MessageFormat.format("客户订单明细{0}留货成功! 留货数量:{1}",
					orderdetail.getId(), delta));
		} catch (DixinException de) {
			tx.rollback();
			throw de;
		} catch (Exception e) {
			tx.rollback();
			log.error("客户订单明细留货中出现异常, 事务回滚!", e);
			throw new DataException("为客户订单明细{0}留货失败!",
					new Object[] { orderdetail.getId() }, e);
		}
		if (delta == 0) {
			log.error("库存不足, 无法为该订单明细留货!");
			throw new DataException("库存不足, 无法为该订单明细留货!");
		}
	}

	/**
	 * 
	 * @param session
	 * @param order
	 * @return delta - the count of reserved
	 */
	private int reserve(Session session, Order order) {
		if (order.getIsValid() == Bool.FALSE)
			return 0;
		if(order.getDoubleCheck() == Bool.FALSE){
			throw new DataException("客户订单还没有复核，不能留货："+order.getOrderNo());
		}
		int delta = 0;
		Set details = order.getOrderdetails();
		for (Iterator j = details.iterator(); j.hasNext();) {
			Orderdetail detail = (Orderdetail) j.next();
			delta += reserve(session, detail);
		}
		return delta;
	}

	/**
	 * 在某个session的范围内为订单明细留货, 上层session需要控制session的打开和事务管理.
	 * 
	 * @param session
	 * @param detail
	 */
	int reserve(Session session, Orderdetail detail) {
		if (detail.getIsValid() == Bool.FALSE)
			return 0;
		if(detail.getOrder().getDoubleCheck() == Bool.FALSE){
			throw new DataException("客户订单还没有复核，不能留货："+detail.getOrder().getOrderNo());
		}
		int count = detail.getQuantity() - detail.getDeliveredCount()
				- detail.getReservedCount();
		if (count <= 0) {
			throw new DataException("订单明细已经全部送货或者留货");// 订单明细全部已经留货
		}
		int oldCount = count;
		// 查询所有可出货仓库中所有该订单明细产品的库存
		Query query = session
				.createQuery("from Repertory r where r.product.id=:id and r.storehouse.shippable=1 order by r.storehouse.id");
		query.setInteger("id", detail.getProduct().getId());
		for (Iterator k = query.iterate(); count > 0 && k.hasNext();) {
			Repertory r = (Repertory) k.next();
			int delta = reserve(session, detail, r, count, false);
			count -= delta;
		}
		return oldCount - count;
	}

	/**
	 * @param session
	 * @param detail
	 * @param repertory
	 * @param count
	 * @param restrict
	 *            是否强制抛出异常，如果为false则继续留货操作
	 * @return
	 */
	private int reserve(Session session, Orderdetail detail,
			Repertory repertory, int count, boolean restrict) {
		if (detail.getIsValid() == Bool.FALSE)
			return 0;
		if(detail.getOrder().getDoubleCheck() == Bool.FALSE){
			throw new DataException("客户订单还没有复核，不能留货："+detail.getOrder().getOrderNo());
		}
		log.info("手工留货...");
		int availableCount = repertory.getQuantity()
				- repertory.getReservedCount();
		if (availableCount < count && restrict) {
			log.error("库存不足, 无法留货!");
			throw new DataException("库存不足,无法留货!");
		}
		if (availableCount == 0)
			return 0;
		if (count > detail.getQuantity() - detail.getReservedCount()
				&& restrict) {
			log.error("留货数量不能大于订单明细数量!");
			throw new DataException("留货数量不能大于订单明细数量!");
		}
		int delta = availableCount >= count ? count : availableCount;
		detail.setReservedCount(detail.getReservedCount() + delta);// 增加订单明细中的已留货数目
		repertory.setReservedCount(repertory.getReservedCount() + delta);// 增加库存信息中的已留货数目
		session.merge(detail);
		session.merge(repertory);
		// 记录留货信息
		Reserved reserved = new Reserved(repertory.getStorehouse(), detail,
				delta);
		session.save(reserved);
		log.info("手工留货成功! 留货数量:" + delta);
		return delta;
	}

	/**
	 * 手动留货. 从仓库storehouse中为订单明细orderDetail留货count个
	 */
	public void reserve(Orderdetail orderdetail, Storehouse storehouse,
			int count) {
		if (orderdetail.getIsValid() == Bool.FALSE)
			return;
		if(orderdetail.getOrder().getDoubleCheck() == Bool.FALSE){
			throw new DataException("客户订单还没有复核，不能留货："+orderdetail.getOrder().getOrderNo());
		}
		if (storehouse.getShippable() == Bool.FALSE) {
			log.error("仓库" + storehouse.getName() + "沒有留貨权限!");
			throw new DataException("仓库" + storehouse.getName() + "沒有留貨权限!");
		}
		if (count <= 0) {
			log.error("留货数量必须大于0!");
			throw new DataException("留货数量必须大于0!");
		}
		Order order = orderdetail.getOrder();
		if (order != null && OrderStatus.FINISHED.equals(order.getStatus())) {
			log.error("该订单已送货完成, 不需要再留货!");
			throw new DataException("该订单已送货完成, 不需要再留货!");
		}
		// 不要用 == 两个integer可能不==
		if (orderdetail.getQuantity() <= orderdetail.getDeliveredCount()) {
			log.error("该订单明细已送货完成, 不需要再留货!");
			throw new DataException("该订单明细已送货完成, 不需要再留货!");
		}
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createQuery("from Repertory r where r.product.id=:id1 and r.storehouse.id=:id2");
			query.setInteger("id1", orderdetail.getProduct().getId());
			query.setInteger("id2", storehouse.getId());
			List list = query.list();
			if (list.isEmpty()) {
				String msg = MessageFormat.format("仓库【{0}】中没有产品【{1}】的库存信息!",
						new Object[] { storehouse.getName(),
								orderdetail.getProduct().getModel() });
				log.error(msg);
				throw new DataException(msg);
			}
			reserve(session, orderdetail, (Repertory) list.get(0), count, true);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.info("手工留货出现异常, 事务恢复!", e);
			if (e instanceof DataException)
				throw (DataException) e;
			else
				throw new DataException("手工留货操作失败!", e);
		}
	}

	/**
	 * 为订单解除留货.
	 * 
	 * @param order
	 */
	public void dereserve(Order order) {
		if (order.getIsValid() == Bool.FALSE)
			return;
		log.info("解除客户订单留货... 订单号:" + order.getOrderNo());
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			dereserveOrderdetail(session, order);
			tx.commit();
			log.info("解除客户订单留货成功!");
		} catch (Exception e) {
			tx.rollback();
			log.info("解除客户订单留货出现异常, 事务恢复!", e);
			throw new DataException("为订单{0}解除留货失败!", new Object[] { order
					.getOrderNo() }, e);
		}
	}

	/**
	 * 解除留货
	 */
	public void dereserve(Orderdetail orderDetail) {
		if (orderDetail.getIsValid() == Bool.FALSE)
			return;
		if (orderDetail.getReservedCount() == 0) {
			throw new DataException("订单明细{0}留货数量为0，解除留货失败!",
					new Object[] { orderDetail.getId() });
		}
		ArrangementHelper aHelper = new ArrangementHelper();
		IPagedResult<Arrangement> result = aHelper.findByProperties(
				new String[] { "orderdetail.id", "isFinished" }, new Object[] {
						orderDetail.getId(), Bool.FALSE });
		if (result.count() > 0)
			throw new DataException("订单明细{0}已有排货，不能解除留货！",
					new Object[] { orderDetail.getId() });
		log.info("解除订单明细的留货... ID=" + orderDetail.getId());
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			dereserveOrderdetail(session, orderDetail);
			tx.commit();
			log.info("解除订单明细的留货成功!");
		} catch (Exception e) {
			tx.rollback();
			log.info("解除订单明细的留货失败, 事务恢复!", e);
			if (e instanceof DataException) {
				throw (DataException) e;
			}
			throw new DataException("为订单明细{0}解除留货失败!",
					new Object[] { orderDetail.getId() }, e);
		}
	}

	/**
	 * 
	 * @param session
	 * @param order
	 */
	private void dereserveOrderdetail(Session session, Order order) {
		if (order.getIsValid() == Bool.FALSE)
			return;
		Set details = order.getOrderdetails();
		for (Iterator i = details.iterator(); i.hasNext();) {
			dereserveOrderdetail(session, (Orderdetail) i.next());
		}
	}

	/**
	 * 解除订单明细的留货.
	 * 
	 * @param session
	 * @param orderDetail
	 */
	void dereserveOrderdetail(Session session, Orderdetail orderDetail) {
		if (orderDetail.getIsValid() == Bool.FALSE)
			return;
		Integer reserveLocked = orderDetail.getReserveLocked();
		if (reserveLocked != null && reserveLocked.intValue() == Bool.TRUE) {
			String msg = MessageFormat.format("订单【{0}】中明细〔{1}〕已被锁定留貨, 不能解除留货!",
					new Object[] { orderDetail.getOrder().getOrderNo(),
							orderDetail.getId() });
			log.error(msg);
			throw new DataException(msg);
		}
		// 刪除所有该订单明细的所有留货信息
		Query query = session
				.createQuery("from Reserved r where r.orderdetail.id=:id");
		query.setInteger("id", orderDetail.getId());
		for (Iterator i = query.iterate(); i.hasNext();) {
			Reserved reserved = (Reserved) i.next();
			// 修改orderdetail和storehouse对应的库存信息
			Repertory r = findRepertory(session, reserved.getStorehouse()
					.getId(), orderDetail.getProduct().getId());
			Integer rCnt = reserved.getReservedCount();
			if (r != null) {
				r.setReservedCount(r.getReservedCount() - rCnt);
				session.merge(r);
				session.delete(reserved);
			} else if (rCnt > 0) {
				throw new DataException("无法找到相关的留货信息,解除留货失败!");
			}
		}
		orderDetail.setReservedCount(0);
		session.merge(orderDetail);
	}

	/**
	 * 根据库存id和产品id查找对应的库存, 如果不存在,则返回null. 正常情况下, 库存id和产品id组成的键对应唯一一个Reperoty记录
	 * 
	 * @param storehouseId
	 * @param productId
	 * @return
	 */
	private Repertory findRepertory(Session session, Integer storehouseId,
			Integer productId) {
		Query query = session
				.createQuery("from Repertory r where r.storehouse.id=:sid and r.product.id=:pid");
		query.setParameter("sid", storehouseId);
		query.setParameter("pid", productId);
		List result = query.list();
		if (result.isEmpty())
			return null;
		else {
			return (Repertory) result.get(0);
		}
	}
}
