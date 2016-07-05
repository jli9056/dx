/**
 * 
 */
package com.dixin.business;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import com.dixin.business.constants.Bool;
import com.dixin.business.impl.ProductHelper;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.StatView;
import com.dixin.hibernate.Storehouse;

/**
 * @author Jason
 * 
 */
public class StatManager {

	private static final Log log = LogFactory.getLog(StatManager.class);
	
	private static StatManager manager;
	private static Storehouse defaultStorehouse;

	/**
	 * 
	 * @return
	 */
	public static StatManager getInstance() {
		if (manager == null) {
			manager = new StatManager();
		}
		return manager;
	}


	private StatManager() {
	}
	
	
	/**
	 * 更新统计视图的数据.
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	public void updateStatView(Session session, int[] pIds) {
		String queryStr = null;
		if (pIds == null) {
			queryStr = "SELECT r FROM Repertory r where r.storehouse.id=:stId";
		} else if (pIds.length == 0) {
			return;
		} else {
			queryStr = "SELECT r FROM Repertory r where r.storehouse.id=:stId AND r.product.id in ("
					+ getIdStr(pIds) + ")";
		}
		Query query = session.createQuery(queryStr);
		Storehouse st = getDefaultStorehouse(session);
		query.setInteger("stId", st.getId());
		List list = query.list();
		if (list.isEmpty()) {
			for (int i = 0; i < pIds.length; i++) {
				Product product = new ProductHelper().findById(pIds[i]);
				Repertory r = new Repertory(st, product, 0, 0);
				session.save(r);
				updateRelatedData(session, r);
			}
		} else {
			for (Iterator i = list.iterator(); i.hasNext();) {
				Repertory r = (Repertory) i.next();
				updateRelatedData(session, r);
			}
		}
	}


	/**
	 * @param session
	 * @param r
	 */
	@SuppressWarnings("unchecked")
	private void updateRelatedData(Session session, Repertory r) {
		String sql1 = "SELECT sum(od.quantity-od.deliveredCount) FROM Orderdetail od WHERE od.product.id=:pid  AND od.isValid=1 AND od.deliveredCount<od.quantity AND od.order.doubleCheck=1";
		Query csCountQuery = session.createQuery(sql1);
		Integer pid = r.getProduct().getId();
		csCountQuery.setInteger("pid", pid);
		List csCountResult = csCountQuery.list();

		String sql2 = "SELECT sum(fod.ownedCount) FROM Factoryorderdetail fod where fod.product.id=:pid AND fod.factoryorder.doubleCheck=1";
		Query faCountQuery = session.createQuery(sql2);
		faCountQuery.setInteger("pid", pid);
		List faCountResult = faCountQuery.list();
		updateStatCsCount(session, r, csCountResult, faCountResult);
	}

	/**
	 * @param pIds
	 * @return
	 */
	private String getIdStr(int[] pIds) {
		StringBuffer idStr = new StringBuffer();
		for (int i = 0; i < pIds.length - 1; i++) {
			idStr.append(pIds[i]);
			idStr.append(',');
		}
		idStr.append(pIds[pIds.length - 1]);
		return idStr.toString();
	}
	
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Storehouse getDefaultStorehouse(Session session) {
		if (defaultStorehouse == null) {
			Criteria criteria = session.createCriteria(Storehouse.class);// 第一个可出货的仓库为默认仓库
			criteria.add(Expression.eq("shippable", Bool.TRUE));
			criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
			criteria.setFetchSize(1);
			List stores = criteria.list();
			if (stores.isEmpty()) {
				throw new DataException("没有找到可出货的仓库，请至少设定一个可出货仓库！");
			}
			defaultStorehouse = (Storehouse) stores.get(0);
		}
		return defaultStorehouse;
	}

	/**
	 * 
	 * @param session
	 * @param r
	 * @param csCountResult
	 * @param faCountResult
	 */
	@SuppressWarnings("unchecked")
	private void updateStatCsCount(Session session, Repertory r,
			List csCountResult, List faCountResult) {
		Integer csCount = 0;
		Integer faCount = 0;
		if (r.getStorehouse().getId().equals(
				getDefaultStorehouse(session).getId())) {
			if (!csCountResult.isEmpty() && csCountResult.get(0) != null) {
				csCount = Integer.parseInt(csCountResult.get(0).toString());
			}
			if (!faCountResult.isEmpty() && faCountResult.get(0) != null) {
				faCount = Integer.parseInt(faCountResult.get(0).toString());
			}
		}
		Query query = session
				.createQuery("SELECT sv FROM StatView sv where sv.repertory.id=:id");
		query.setInteger("id", r.getId());
		List list = query.list();
		if (list.isEmpty()) {
			StatView sv = new StatView(r, csCount, faCount);
			session.save(sv);
		} else {
			StatView sv = (StatView) list.get(0);
			sv.setCsCount(csCount);
			sv.setFaCount(faCount);
			session.merge(sv);
		}
		session.flush();
		String pattern = "成功更新库存状态视图! 仓库=[%s] 产品=[%s] 已订数量=[%d] 工厂订单数量=[%d]";
		log.info(String.format(pattern, r.getStorehouse().getName(), r
				.getProduct().getModel(), csCount, faCount));
	}
}
