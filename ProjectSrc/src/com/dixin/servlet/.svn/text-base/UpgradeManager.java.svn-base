package com.dixin.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dixin.util.RegistryHelper;

public class UpgradeManager {
	private Log log = LogFactory.getLog(UpgradeManager.class);
	@SuppressWarnings("unused")
	private HttpServlet servlet;

	private UpgradeManager(HttpServlet servlet) {
		this.servlet = servlet;
	}

	private static UpgradeManager instance;
	
	private static String version = RegistryHelper.getDixinRegistry().get(
			"version");

	/**
	 * 
	 * @return
	 */
	public static UpgradeManager getInstance(HttpServlet servlet) {
		if (instance == null) {
			synchronized (UpgradeManager.class) {
				instance = new UpgradeManager(servlet);
			}
		}
		return instance;
	}

	/**
	 * 
	 */
	public void upgrade() {
		Properties upgrades = new Properties();
		try {
			InputStream is = UpgradeManager.class
					.getResourceAsStream("upgrade.properties");
			if (is == null || is.available() <= 0) {
				log.info("There is no upgrade entries!");
				return;
			}
			upgrades.load(is);
			for (Object key : upgrades.keySet()) {
				String msg = (String) upgrades.get(key);
				upgrade((String) key, msg);
			}
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * 
	 */
	private void upgrade(final String id, final String msg) {
		new Thread() {
			public void run() {
				URL url = UpgradeManager.class.getResource(id);
				if (!objExist(url))
					return;
				try {
					Class<?> clz = Class.forName(id);
					if (IUpdater.class.isAssignableFrom(clz)) {
						IUpdater updater = (IUpdater) clz.newInstance();
						String targetVersion = updater.getTargetVersion();
						if (version == null || targetVersion == null
								|| version.equals(targetVersion)) {
							updater.update();
						}
					}
				} catch (ClassNotFoundException e) {
					log.error("Can't find class for update: " + id, e);
					return;
				} catch (InstantiationException e) {
					log.error("Can't instantiate class for update: " + id, e);
					return;
				} catch (IllegalAccessException e) {
					log.error("Can't access class for update: " + id, e);
					return;
				}
				log.info("upgrade " + msg + " successfully!");
				removeObj(url);
			}
		}.start();
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	private boolean objExist(URL url) {
		try {
			if (url == null || url.openStream() == null) {
				return false;
			}
		} catch (IOException e) {
			log.error(e);
		}
		return true;
	}

	/**
	 * @param url
	 */
	private void removeObj(URL url) {
		try {
			File upgradeObj = new File(url.toURI());
			upgradeObj.deleteOnExit();
		} catch (URISyntaxException e) {
			log.error(e);
		}
	}
}
