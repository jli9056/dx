package com.dixin.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Tell the browsers the encoding of the static content explicitly. <BR>
 * IE will use default encoding ISO-8859-1 to expain the html and js file.<BR>
 * 
 * @author Luo
 * 
 */
public class StaticTextFileEncodingFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		HttpServletResponse res = ((HttpServletResponse) response);
		// TODO only for dev stage
		res.setHeader("Expires", "0");
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
