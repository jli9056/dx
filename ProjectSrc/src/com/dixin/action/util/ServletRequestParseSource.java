package com.dixin.action.util;

import java.util.Collection;

import javax.servlet.ServletRequest;
import com.dixin.hibernate.convert.ParseSource;

public class ServletRequestParseSource implements ParseSource {
	private ServletRequest request;

	public ServletRequestParseSource(ServletRequest request) {
		this.request = request;
	}

	public boolean containsName(String name) {
		return request.getParameterMap().containsKey(name);
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getNames() {
		return request.getParameterMap().keySet();
	}

	public String getValue(String name) {
		return request.getParameter(name);
	}

	public String[] getValues(String name) {
		return request.getParameterValues(name);
	}
}
