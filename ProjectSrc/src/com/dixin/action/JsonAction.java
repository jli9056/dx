/**
 * 
 */
package com.dixin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dixin.action.util.ResultUtil;
import com.dixin.servlet.MyServlet;

/**
 * @author Jason
 * 
 */
public abstract class JsonAction implements IAction {
	protected final Log log = LogFactory.getLog(this.getClass());
	protected MyServlet myServlet;

	public void setMyServlet(MyServlet my) {
		this.myServlet = my;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.action.IAction#execute(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.debug("Enter JsonAction.execute");// for performance monitor
		PrintWriter writer = response.getWriter();
		JSONObject result = new JSONObject();
		try {
			if (process(request, result)) {
				result.accumulate("success", Boolean.TRUE);
			} else {
				result.accumulate("success", Boolean.FALSE);
			}
		} catch (ActionRedirectedException ex) {
			return;
		}
		String ret = result.toString();
		log.debug("返回JSON:" + ret);
		writer.write(ret);
		writer.flush();
		writer.close();
		log.debug("End JsonAction.execute");// for performance monitor
	}

	protected void callAction(String actionName) {
		myServlet.executeAction(actionName);
		throw new ActionRedirectedException();
	}

	public static void addErrors(JSONObject result, Map<String, String> errors) {
		ResultUtil.addErrors(result, errors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.action.IAction#process(javax.servlet.http.HttpServletRequest)
	 */
	public abstract boolean process(HttpServletRequest request,
			JSONObject result);

}
