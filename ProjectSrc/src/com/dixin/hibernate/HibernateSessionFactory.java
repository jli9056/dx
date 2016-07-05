package com.dixin.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import com.dixin.AppContextFactory;
import com.dixin.Constants;

/**
 * 
 */
public class HibernateSessionFactory {
	private static final Log log = LogFactory.getLog(HibernateSessionFactory.class);

	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

	private static SessionFactory sessionFactory;

	private HibernateSessionFactory() {
	}

	/**
	 * Returns the ThreadLocal Session instance. Lazy initialize the
	 * <code>HibernateSessionFactory</code> if needed.
	 * 
	 * @return Session
	 * @throws HibernateException
	 */
	public static Session getSession() throws HibernateException {
		Session session = threadLocal.get();

		if (session == null || !session.isOpen()) {
			if (sessionFactory == null) {
				buildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			threadLocal.set(session);
			log.debug("===============New Session:" + session.hashCode());
		} else
			log.debug("###############Old Session:" + session.hashCode());
		return session;
	}

	/**
	 * Rebuild hibernate session factory
	 * 
	 */
	public static void buildSessionFactory() {
		try {
			ApplicationContext ac = AppContextFactory.getAppContext();
			sessionFactory = (SessionFactory) ac.getBean(Constants.SESSION_FACTORY);
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * Close the single hibernate session instance.
	 * 
	 * @throws HibernateException
	 */
	public static void closeSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);
		if (session != null && session.isOpen()) {
			session.close();
			log.debug("***************Close Session:" + session.hashCode());
		}
	}
}