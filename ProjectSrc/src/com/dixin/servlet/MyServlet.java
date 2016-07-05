/**
 * 
 */
package com.dixin.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import com.dixin.AppContextFactory;
import com.dixin.DixinException;
import com.dixin.action.ActionException;
import com.dixin.action.CurrentUser;
import com.dixin.action.IAction;
import com.dixin.action.databackup.MySQLDump;
import com.dixin.action.util.ResultUtil;
import com.dixin.hibernate.HibernateSessionFactory;

/**
 * @author Jason
 * 
 */
public class MyServlet extends HttpServlet {

	private static final String WEB_APPLICATION_CONTEXT_ROOT = "org.springframework.web.context.WebApplicationContext.ROOT";
	/**
	 * 
	 */
	private static final long serialVersionUID = 3695460065378295431L;
	private ApplicationContext ac;
	private Log log = LogFactory.getLog(MyServlet.class);
	DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		initAppContext();
		backupDB(config);
		UpgradeManager.getInstance(this).upgrade();
	}

	

	/**
	 * @param config
	 */
	private void backupDB(ServletConfig config) {
		try {
			File dir = new File(config.getInitParameter("backup-directory"));
			Integer keep = Integer.valueOf(config
					.getInitParameter("backup-keep-number"));

			if (!dir.exists()) {
				dir.mkdir();
			}
			File backupFile = new File(dir.getAbsolutePath() + "/"
					+ df.format(new Date()) + ".data");
			log.info("Backup to " + backupFile.getAbsolutePath());
			new MySQLDump().dump(backupFile);

			String[] list = dir.list();
			if (list.length > keep) {
				Arrays.sort(list);
				for (int i = list.length - keep - 1; i >= 0; i--) {
					File d = new File(dir.getAbsolutePath() + "/" + list[i]);
					log.info("delete " + d.getAbsolutePath() + " ...");
					d.delete();
				}
			}

		} catch (Exception ex) {
			log.error("backup", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}

	/**
	 * make this part ok for re-enter
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void executeAction(String actionName) {
		HttpServletRequest req = localRequest.get();
		HttpServletResponse resp = localResponse.get();
		CurrentUser user = CurrentUser.getCurrentUser();
		IAction action = (IAction) ac.getBean(actionName);
		action.setMyServlet(this);
		try {
			if (actionName.equals("loginAction")) {
				action.execute(req, resp);
			} else if (user == null) {
				resp.setStatus(401);
				throw new ActionException("未登录或者登录超时，请登录后再重试。");
			} else if ("getCurrentUserAction".equals(actionName)
					|| "loadUserMenusAction".equals(actionName)
					|| "changePasswordAction".equals(actionName)
					|| user != null && user.isAuthorized(actionName)) {
				action.execute(req, resp);
			} else {
				resp.setStatus(403);
				throw new ActionException("您没有权限执行此操作: "+action.getLocalizedName());
			}
		} catch (Exception ex) {
			String msg = "错误，请检查您的输入数据";
			if (ex instanceof NoSuchBeanDefinitionException) {
				msg = "不支持的操作";
			} else if (ex instanceof DixinException) {
				msg = ex.getMessage();
			}
			String error = null;
			if ((req.getContentType() + "").startsWith("multipart/form-data")) {
				error = "<html><body>错误：" + msg + loginJS + "</body></html>";
			} else {
				error = ResultUtil.getFailureJSON(msg).toString();
			}
			log.error(error, ex);
			try {
				PrintWriter writer = resp.getWriter();
				writer.write(error);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
			return;
		}
	}

	ThreadLocal<HttpServletRequest> localRequest = new ThreadLocal<HttpServletRequest>();
	ThreadLocal<HttpServletResponse> localResponse = new ThreadLocal<HttpServletResponse>();

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		setEncoding(req, resp);
		localRequest.set(req);
		localResponse.set(resp);
		if (log.isDebugEnabled()) {
			log.debug("QueryString:" + req.getQueryString());
			StringBuffer buf = new StringBuffer();
			for (Object s : req.getParameterMap().keySet()) {
				buf.append(s + "=");
				for (String sv : req.getParameterValues((String) s)) {
					buf.append(sv + ",");
				}
				buf.append("\n");
			}
			log.debug("\n================RequestParameters==========\n" + buf);
		}

		try {
			CurrentUser user = CurrentUser.getSessionUser(req.getSession());
			CurrentUser.setCurrentUser(user);
			initAppContext();
			String actionName = (String) req.getParameter("action");
			executeAction(actionName);
		} finally {
			HibernateSessionFactory.closeSession();
			CurrentUser.setCurrentUser(null);
			localRequest.set(null);
			localResponse.set(null);
		}
	}

	private static final String loginJS = "<script language='javascript'>"
			+ "if(parent.Ext){"
			+ "parent.Ext.Ajax.request({url: '/ms?action=getCurrentUserAction'});"
			+ "}</script>";

	/**
	 * 
	 */
	private void initAppContext() {
		if (ac == null) {
			ac = (ApplicationContext) getServletContext().getAttribute(
					WEB_APPLICATION_CONTEXT_ROOT);
			if (ac != null) {
				AppContextFactory.register(ac);
			} else {
				ac = AppContextFactory.getAppContext();
			}
		}
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws UnsupportedEncodingException
	 */
	private void setEncoding(HttpServletRequest req, HttpServletResponse resp)
			throws UnsupportedEncodingException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
	}
}
