/**
 * 
 */
package com.dixin.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dixin.servlet.MyServlet;

/**
 * @author Jason
 * 
 */
public interface IAction {
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException,
	 *             ServletException
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException;

	/**
	 * Name for the action
	 */
	String getLocalizedName();

	void setMyServlet(MyServlet s);
}
