package com.dixin.action.util;

import java.util.Map;

import javax.servlet.ServletRequest;

import com.dixin.action.ActionException;
import com.dixin.hibernate.BaseJDO;
import com.dixin.hibernate.convert.Parser;

/**
 * Parse Bean Properties from HttpServletRequest.
 * 
 * @author Luo
 * 
 * @param <T>
 *            The Type of the bean to be parse.
 */

public class BeanParser<T extends BaseJDO> extends Parser<T> {

	public BeanParser(Class<T> c) {
		super(c);
	}

	/**
	 * Parse one bean from request
	 * 
	 * @param request
	 * @param errors
	 * @return
	 */
	public T parse(ServletRequest request,
			Map<? super String, ? super String> errors) {
		T t = null;
		try {
			t = clazz.newInstance();
		} catch (Exception ex) {
			throw new ActionException("创建数据对象实例出错", ex);
		}
		parse(request, errors, t);
		return t;
	}

	/**
	 * Parse request parameters to the instance.
	 * 
	 * @param request
	 * @param errors
	 * @param instance
	 * @return the passed in instance
	 */
	@SuppressWarnings("unchecked")
	public T parse(ServletRequest request,
			Map<? super String, ? super String> errors, T instance) {
		parse(new ServletRequestParseSource(request), instance, errors);
		return instance;
	}
}
