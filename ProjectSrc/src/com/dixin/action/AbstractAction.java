package com.dixin.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.JDBCException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;

import com.dixin.action.util.BeanParser;
import com.dixin.action.util.ResultUtil;
import com.dixin.action.util.ValidationUtil;
import com.dixin.business.IQueryDefn;
import com.dixin.business.impl.BaseHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.BaseJDO;
import com.dixin.hibernate.convert.BeanUtil;

/**
 * Abstract action. Provide collect method and getHelper() method.
 * 
 * @author Luo
 * 
 * @param <T>
 */
public abstract class AbstractAction<T extends BaseJDO> extends JsonAction {
	protected BeanParser<T> parser;
	protected Class<T> jdoClass;
	protected Class<? extends BaseHelper<T>> helperClass;
	private BaseHelper<T> helper;
	Pattern dupPattern = Pattern
			.compile(".*\\\"Duplicate entry '(.*)' for key ([0-9]+)\\\"");
	protected String[] uniqueKeys = { "", "id", "Unknow", "Unknow", "Unknow" };
	protected Pattern crPattern = Pattern
			.compile("^cr_(.*)_(like|eq|gt|lt|in|ge|le|isNull|isNotNull|isEmpty|isNotEmpty|X)$");

	/**
	 * Lazily init. Avoid dead loop during spring init context.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <H extends BaseHelper<T>> H getHelper() {
		if (helper == null)
			try {
				helper = helperClass.newInstance();
			} catch (Exception ex) {
				throw new ActionException("创建Business Helper失败", ex);
			}
		return (H) helper;
	}

	public AbstractAction(Class<T> jdoClass,
			Class<? extends BaseHelper<T>> helperClass) {
		this.jdoClass = jdoClass;
		this.helperClass = helperClass;
		parser = new BeanParser<T>(jdoClass);
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	abstract public boolean process(HttpServletRequest request,
			JSONObject result);

	/**
	 * 
	 * @param request
	 * @param errors
	 * @return null if any error
	 */
	protected T collect(HttpServletRequest request, JSONObject result) {
		Map<String, String> errors = new HashMap<String, String>();
		T obj = parser.parse(request, errors);
		if (errors.isEmpty() == false) {
			ResultUtil.addErrors(result, errors);
			return null;
		}
		return obj;
	}

	protected Criterion getXCriterion(String key, String value) {
		return null;
	}

	/**
	 * 根据数据完整性异常检查出错字段
	 * 
	 * @param ex
	 * @param result
	 * @return
	 */
	public boolean parseDataIntegrityViolationException(
			DataIntegrityViolationException ex, JSONObject result) {
		return parseDataIntegrityViolationException((Exception) ex, result);
	}

	/**
	 * 根据数据完整性异常检查出错字段
	 * 
	 * @param ex
	 * @param result
	 * @return
	 */
	public boolean parseDataIntegrityViolationException(JDBCException ex,
			JSONObject result) {
		return parseDataIntegrityViolationException((Exception) ex, result);
	}

	/**
	 * 根据数据完整性异常检查出错字段
	 * 
	 * @param ex
	 * @param result
	 * @return
	 */
	private boolean parseDataIntegrityViolationException(Exception ex,
			JSONObject result) {
		Throwable t = ex;
		while (t.getCause() != null && t.getCause() != t) {
			t = t.getCause();
		}
		String msg = t.getMessage();
		log.debug("DataIntegrityViolationException：" + msg);
		Matcher m = dupPattern.matcher(msg);
		if (m.matches()) {
			result.element("msg", "与已有数据重复");
			return true;
		}
		return false;
	}

	/**
	 * 创建查询条件
	 * 
	 * @param propertyName
	 *            要在其上做查询的属性名字
	 * @param type
	 *            查询条件的匹配类型 like, in, isNull, etc.
	 * @param svalue
	 *            查询条件的值
	 * @return
	 */
	public Criterion createCriterion(String propertyName, String type,
			String svalue) {
		log.debug("createCriterion: " + propertyName + type + svalue);
		// 先分解或关系条件，查询产品信息时可以支持多字段或关系查询一个值
		if (propertyName.contains("$")) {// or
			String[] ps = propertyName.split("\\$");
			Criterion c = null;
			for (int index = 0; index < ps.length; index++) {
				String p = ps[index];
				try {
					Criterion c1 = createCriterion(p, type, svalue);
					if (c == null) {
						c = c1;
					} else if (c1 != null) {
						c = Restrictions.or(c, c1);
					}
				} catch (Exception ex) {
					// 可能会有数据类型异常，如果是或条件中的一个，将被忽略
					log.warn("转化属性值出错，可能是数据类型不匹配。" + ex.getMessage());
				}
			}
			return c;
		}
		// 再分解与关系，一般情况下不会使用
		if (propertyName.contains("-")) {// and
			String[] ps = propertyName.split("\\$");
			Criterion c = createCriterion(ps[0], type, svalue);
			for (int index = 1; index < ps.length; index++) {
				String p = ps[index];
				Criterion c1 = createCriterion(p, type, svalue);
				if (c1 != null) {
					c = Restrictions.and(c, c1);
				}
			}
			return c;
		}

		if ("like".equals(type)) {
			return Restrictions.like(propertyName, "%" + svalue + "%");
		} else if ("eq".equals(type)) {
			Object value = parser.parsePropertyValue(propertyName, svalue);
			return Restrictions.eq(propertyName, value);
		} else if ("gt".equals(type)) {
			Object value = parser.parsePropertyValue(propertyName, svalue);
			return Restrictions.gt(propertyName, value);
		} else if ("lt".equals(type)) {
			Object value = parser.parsePropertyValue(propertyName, svalue);
			return Restrictions.lt(propertyName, value);
		} else if ("ge".equals(type)) {
			Object value = parser.parsePropertyValue(propertyName, svalue);
			return Restrictions.ge(propertyName, value);
		} else if ("le".equals(type)) {
			Object value = parser.parsePropertyValue(propertyName, svalue);
			return Restrictions.le(propertyName, value);
		} else if ("in".equals(type)) {
			if (svalue == null) {
				return null;
			}
			Object[] values = parser.parseBasicTypeValues(BeanUtil
					.getNestedPropertyType(this.jdoClass, propertyName),
					(svalue + "").split(","));
			return Restrictions.in(propertyName, values);
		} else if ("isNull".equals(type)) {
			return Restrictions.isNull(propertyName);
		} else if ("isNotNull".equals(type)) {
			return Restrictions.isNotNull(propertyName);
		} else if ("isEmpty".equals(type)) {
			return Restrictions.isEmpty(propertyName);
		} else if ("isNotEmpty".equals(type)) {
			return Restrictions.isNotEmpty(propertyName);
		} else if ("X".equals(type)) {
			return getXCriterion(propertyName, svalue);
		}

		return null;
	}

	/**
	 * 得到查询条件
	 * 
	 * @param request
	 * @param result
	 * @return
	 */
	public IQueryDefn getQueryDefn(Map<String, String> map) {
		IQueryDefn query = new QueryDefn();
		for (Object key : map.keySet()) {
			String name = (String) key;
			Matcher matcher = crPattern.matcher(name);
			String value = map.get(name);
			if (matcher.matches() && value.length() > 0) {
				String propertyName = matcher.group(1);// properties
				// seperate by $ for or. - for and
				String type = matcher.group(2);// query type
				Criterion c = createCriterion(propertyName, type, value);
				if (c != null) {
					query.addCriterion(c);
				}
			}
		}
		return query;
	}

	/**
	 * 得到查询条件
	 * 
	 * @param request
	 * @param result
	 * @return
	 */
	public IQueryDefn getQueryDefn(String... params) {
		IQueryDefn query = new QueryDefn();
		for (String s : params) {
			String[] kv = s.split("=");
			String name = kv[0];
			Matcher matcher = crPattern.matcher(name);
			String value = kv[1];
			if (matcher.matches() && value.length() > 0) {
				String propertyName = matcher.group(1);// properties
				// seperate by $ for or. - for and
				String type = matcher.group(2);// query type
				Criterion c = createCriterion(propertyName, type, value);
				if (c != null) {
					query.addCriterion(c);
				}
			}
		}
		return query;
	}
	
	/**
	 * 检查订单中是否有重复的产品型号,如果有则抛出异常.
	 * 
	 * @param product
	 */
	protected void checkDuplicateNames(String[] product) {
		List<String> dupProducts = ValidationUtil.getDuplicateItems(product);
		if (!dupProducts.isEmpty()) {
			StringBuffer buf = new StringBuffer();
			for (String s : dupProducts) {
				buf.append(s);
				buf.append(", ");
			}
			if (buf.length() > 0) {
				buf.setLength(buf.length() - 2);
			}
			throw new ActionException("订单中不能有重复的产品型号,请核对以下型号:<br/>"
					+ buf.toString());
		}
	}

}
