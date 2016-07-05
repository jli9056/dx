package com.dixin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContextFactory {

	private static ApplicationContext instance;

	private AppContextFactory() {
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static ApplicationContext getAppContext() {
		if (instance == null) {
			instance = new ClassPathXmlApplicationContext(Constants.APP_CONTEXT);
		}
		return instance;
	}

	/**
	 * 
	 * @param appContext
	 */
	public synchronized static void register(ApplicationContext appContext) {
		instance = appContext;
	}
}
