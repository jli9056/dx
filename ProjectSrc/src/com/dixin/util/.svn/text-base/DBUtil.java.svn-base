/**
 * 
 */
package com.dixin.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.business.constants.OrderStatus;
import com.dixin.hibernate.Delivery;
import com.dixin.hibernate.HibernateSessionFactory;

/**
 * @author Jason
 * 
 */
public class DBUtil {

	private static SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 * @param start
	 * @param end
	 * @param field
	 * @return
	 */
	private static String getFilterExpr(Date start, Date end, String field) {
		String filter = "";
		if (start != null) {
			filter += field + ">=:sd";
		}
		if (filter.length() > 0)
			filter += " and ";
		if (end != null) {
			filter += field + "<=:ed";
		}
		return filter;
	}

	/**
	 * remove the order related data from the existing DB system.
	 * 
	 * @param start
	 * @param end
	 * @throws SQLException
	 */
	public static void removeOrderData(Date start, Date end) {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			String filter = getFilterExpr(start, end, "o.orderDate");
			if (filter.length() == 0)
				throw new DataException("参数start和end至少一个不为空!");
			// 删除客户订单相关的记录, , 相关联的其他表的记录自动会级联删除
			Query query = session.createQuery("delete Order o where " + filter
					+ " and o.status=:status");
			if (filter.contains(":sd")) {
				query.setString("sd", dateFormater.format(start));
			}
			if (filter.contains(":ed")) {
				query.setString("ed", dateFormater.format(end));
			}
			query.setString("status", OrderStatus.FINISHED);
			query.executeUpdate();
			// 删除送货信息：相关的送货明细已经在客户订单的级联中被删除，仅仅留下一些没有送货明细的送货记录，也应该被删除
			query = session.createQuery("from Delivery");
			for (Iterator itr = query.iterate(); itr.hasNext();) {
				Delivery delivery = (Delivery) itr.next();
				if (delivery != null
						&& delivery.getDeliverydetails().size() == 0) {
					session.delete(delivery);
				}
			}
			// 删除工厂订单相关的记录, 相关联的其他表的记录自动会级联删除
			filter = getFilterExpr(start, end, "o.orderDate");
			query = session.createQuery("delete Factoryorder o where " + filter
					+ " and o.finished=1");
			if (filter.contains(":sd")) {
				query.setString("sd", dateFormater.format(start));
			}
			if (filter.contains(":ed")) {
				query.setString("ed", dateFormater.format(end));
			}
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new DataException("删除历史订单数据出现错误!");
		}
	}
}
