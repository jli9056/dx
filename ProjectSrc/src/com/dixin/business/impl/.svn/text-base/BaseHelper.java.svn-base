/**
 * 
 */
package com.dixin.business.impl;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import com.dixin.action.util.Messages;
import com.dixin.business.DataException;
import com.dixin.business.IBaseHelper;
import com.dixin.business.IPagedResult;
import com.dixin.business.IQueryDefn;
import com.dixin.business.query.PagedResult;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.BaseJDO;
import com.dixin.hibernate.HibernateSessionFactory;

/**
 * @author Jason
 * 
 */
public abstract class BaseHelper<T extends BaseJDO> implements IBaseHelper<T> {
	protected final Log log = LogFactory.getLog(this.getClass());
	private Class<T> jdoClass;

	public Class<T> getJdoClass() {
		return jdoClass;
	}

	@SuppressWarnings("unchecked")
	public BaseHelper() {
		jdoClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 
	 */
	protected Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#find(com.dixin.business.query.IQuery)
	 */
	public IPagedResult<T> find(IQueryDefn queryDefn) {
		log.info(MessageFormat.format("查找表{0}...", jdoClass.getSimpleName()));
		return new PagedResult<T>(jdoClass, queryDefn);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public IPagedResult<T> findByProperty(String propertyName, Object value) {
		IQueryDefn defn = new QueryDefn();
		log.info(MessageFormat.format("查询条件: {0}={1}", propertyName, value));
		defn.addCriterion(Restrictions.eq(propertyName, value));
		return find(defn);
	}

	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public IPagedResult<T> findByProperties(String[] propertyName,
			Object[] value) {
		IQueryDefn defn = new QueryDefn();
		for (int i = 0; i < propertyName.length; i++) {
			defn.addCriterion(Restrictions.eq(propertyName[i], value[i]));
		}
		return find(defn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#findAll()
	 */
	public IPagedResult<T> findAll() {
		return find(new QueryDefn());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#findById(int)
	 */
	@SuppressWarnings("unchecked")
	public T findById(Integer id) {
		return (T) getSession().get(jdoClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	@SuppressWarnings("unchecked")
	public T merge(T detachedInstance) {
		log.info(MessageFormat.format("编辑{0}记录...", jdoClass.getSimpleName()));
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Object obj = session.merge(detachedInstance);
			tx.commit();
			log.info(MessageFormat.format("编辑{0}记录成功!", jdoClass
					.getSimpleName()));
			return (T) obj;
		} catch (RuntimeException e) {
			tx.rollback();
			throwDataException(e);
		}
		return detachedInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	public void save(T transientInstance) {
		log.info(MessageFormat.format("保存{0}记录...", jdoClass.getSimpleName()));
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(transientInstance);
			tx.commit();
			log.info(MessageFormat.format("保存{0}记录成功!", jdoClass
					.getSimpleName()));
		} catch (RuntimeException e) {
			tx.rollback();
			throwDataException(e);
		}
	}

	/**
	 * @param e
	 * @throws DataException
	 */
	@SuppressWarnings("unchecked")
	private void throwDataException(RuntimeException e) throws DataException {
		log.error("数据库操作出现异常, 事务回滚!", e);
		if (e instanceof ConstraintViolationException) {
			Map<String, String> errors = new HashMap<String, String>();
			Messages.getMessage((ConstraintViolationException) e,
					(Class<BaseJDO>) jdoClass, errors);
			StringBuffer buf = new StringBuffer();
			for (String key : errors.keySet()) {
				String errorMsg = errors.get(key);
				buf
						.append(String.format("字段【%s】违反数据库约束: %s.\n", key,
								errorMsg));
			}
			throw new DataException(buf.toString(), e);
		} else {
			throw new DataException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#deleteById(java.lang.Integer)
	 */
	public boolean deleteById(Integer id) {
		log.info(MessageFormat.format("删除{0}记录... ID={1}", jdoClass
				.getSimpleName(), id));
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery(String.format(
					"delete %s where id=%d", jdoClass.getSimpleName(), id));
			query.executeUpdate();
			tx.commit();
			log.info(MessageFormat.format("删除{0}记录成功! ID={1}", jdoClass
					.getSimpleName(), id));
			return true;
		} catch (RuntimeException e) {
			tx.rollback();
			throwDataException(e);
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#restoreById(java.lang.Integer)
	 */
	public boolean restoreById(Integer id) {
		throw new DataException("不支持该恢复操作！");
	}
}
