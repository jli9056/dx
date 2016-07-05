package com.dixin.hibernate.convert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.dixin.DixinException;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.BaseHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.BaseJDO;

/**
 * Parse Bean from given source
 * 
 * @author Luo
 * 
 */
public class Parser<T extends BaseJDO> {
	protected Log log = LogFactory.getLog(Parser.class);
	protected Class<T> clazz;
	protected BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
	/**
	 * Cached properties and setters
	 */
	protected Map<String, Method> setters = Collections
			.synchronizedMap(new HashMap<String, Method>());
	/**
	 * Pattern for setter method name
	 */
	protected static final Pattern setterPattern = Pattern
			.compile("set([A-Z]{1})([a-zA-Z_]*)");
	/**
	 * Trim value from source
	 */
	protected boolean trim = true;
	protected boolean emptyToNull = true;

	protected DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Cache of helper instance
	 */
	protected static Map<Class<? extends Converter>, Converter> coverterCache = Collections
			.synchronizedMap(new HashMap<Class<? extends Converter>, Converter>());
	/**
	 * Cache of helper instance
	 */
	@SuppressWarnings("unchecked")
	protected static Map<Class<?>, BaseHelper<?>> helperCache = Collections
			.synchronizedMap(new HashMap<Class<?>, BaseHelper<?>>());

	public Parser(Class<T> clazz) {
		this.clazz = clazz;
		// find all properties need to parse.
		for (Method m : clazz.getMethods()) {
			Parse p = m.getAnnotation(Parse.class);
			if (p != null) {
				Matcher matcher = setterPattern.matcher(m.getName());
				if (matcher.matches()) {
					setters.put(matcher.group(1).toLowerCase()
							+ matcher.group(2), m);
					setters.put(matcher.group(1) + matcher.group(2), m);
				}
			}
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param source
	 * 
	 * @return
	 */
	public T parse(ParseSource source,
			Map<? super String, ? super String> errors) {
		try {
			return parse(source, clazz.newInstance(), errors);
		} catch (InstantiationException e) {
			throw new ParseException("初始化数据对象实例出错", e);
		} catch (IllegalAccessException e) {
			throw new ParseException("初始化数据对象实例出错", e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param source
	 * 
	 * @return
	 */
	public T parse(Map<String, String> source,
			Map<? super String, ? super String> errors) {
		try {
			return parse(source, clazz.newInstance(), errors);
		} catch (InstantiationException e) {
			throw new ParseException("初始化数据对象实例出错", e);
		} catch (IllegalAccessException e) {
			throw new ParseException("初始化数据对象实例出错", e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param source
	 * @param instance
	 *            the object to set its properties. Its setter method should has
	 *            the annotation Parse
	 * @return
	 */
	public T parse(ParseSource source, T instance,
			Map<? super String, ? super String> errors) {
		for (String prop : setters.keySet()) {
			if (source.containsName(prop)) {
				try {
					Object value = parsePropertyValue(prop, source
							.getValue(prop));
					beanUtilsBean.copyProperty(instance, prop, value);
					log.debug("setProperty:" + prop + "=" + value);
				} catch (DixinException ex) {
					ex.printStackTrace();
					errors.put(prop, ex.getMessage());
				} catch (Exception ex) {
					ex.printStackTrace();
					errors.put(prop, "设置对象属性出错:" + prop);
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * @param <T>
	 * @param source
	 * @param instance
	 *            the object to set its properties. Its setter method should has
	 *            the annotation Parse
	 * @return
	 */
	public T parse(Map<String, String> source, T instance,
			Map<? super String, ? super String> errors) {
		for (String prop : setters.keySet()) {
			if (source.containsKey(prop)) {
				try {
					Object value = parsePropertyValue(prop, source.get(prop));
					beanUtilsBean.copyProperty(instance, prop, value);
				} catch (DixinException ex) {
					ex.printStackTrace();
					errors.put(prop, ex.getMessage());
				} catch (Exception ex) {
					ex.printStackTrace();
					errors.put(prop, "设置对象属性出错:" + prop);
				}
			}
		}
		return instance;
	}

	/**
	 * 从字符串属性值转换到值对象
	 * 
	 * @param prop
	 *            property name
	 * @param ov
	 *            the plain text value
	 * @return
	 */
	public Object parsePropertyValue(String prop, String ov) {
		log.debug("parsePropertyValue:" + prop + ov);
		if (ov == null) {
			return null;
		}
		Method m = setters.get(prop);
		Parse p = m.getAnnotation(Parse.class);
		ParseType type = p.type();
		if (trim) {
			ov = ov.trim();
		}
		Object value = ov;
		if (ov.length() == 0 && emptyToNull) {
			value = null;
		} else {
			switch (type) {
			case Basic:
				value = parseBasicType(m.getParameterTypes()[0], ov);
				break;
			case Helper:
				try {
					value = findByHelper(p.helper(), p.findFields().split(","),
							ov);
				} catch (Exception ex) {
					throw new ParseException("读取数据对象属性值出错:" + prop, ex);
				}
				break;
			case Converter:
				value = getConverter(p.converter()).convert(ov);
				break;
			case SetNull:
				value = null;
				break;
			}
		}
		return value;
	}

	/**
	 * 从字符串属性值转换到值对象
	 * 
	 * @param prop
	 *            property name
	 * @param ov
	 *            the plain text value
	 * @return
	 */
	public Object[] parsePropertyValues(String prop, String[] ov) {
		log.debug("parsePropertyValue:" + prop + ov);
		if (ov == null) {
			return null;
		}
		Method m = setters.get(prop);
		Parse p = m.getAnnotation(Parse.class);
		ParseType type = p.type();
		if (trim) {
			for (int i = 0; i < ov.length; i++) {
				String s = ov[i];
				if (s != null) {
					ov[i] = s.trim();
				}
			}
		}
		Object[] value = new Object[ov.length];
		switch (type) {
		case Basic:
			value = parseBasicTypeValues(m.getParameterTypes()[0], ov);
			break;
		case Helper:
			try {
				value = findByHelper(p.helper(), p.findFields().split(","), ov);
			} catch (Exception ex) {
				throw new ParseException("读取数据对象属性值出错:" + prop, ex);
			}
			break;
		case Converter:
			for (int i = 0; i < ov.length; i++)
				value[i] = getConverter(p.converter()).convert(ov[i]);
			break;
		case SetNull:
			value = null;
			break;
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	protected static BaseHelper<?> getHelper(Class<?> helperClass) {
		BaseHelper<?> helper = null;
		if (!helperCache.containsKey(helperClass)) {
			try {
				helper = (BaseHelper<?>) helperClass.newInstance();
				helperCache.put(helperClass, helper);
			} catch (Exception ex) {
				throw new ParseException("初始化业务逻辑对象时出错:" + helperClass, ex);
			}
		}
		return helperCache.get(helperClass);
	}

	/**
	 * 
	 * @param converterClass
	 * @return
	 */
	protected static Converter getConverter(
			Class<? extends Converter> converterClass) {
		Converter c = null;
		if (!coverterCache.containsKey(converterClass)) {
			try {
				c = converterClass.newInstance();
				coverterCache.put(converterClass, c);
			} catch (Exception ex) {
				throw new ParseException("初始化数据转化对象时出错:" + converterClass, ex);
			}
		}
		return coverterCache.get(converterClass);
	}

	/**
	 * Find JDO object by unique key
	 * 
	 * @param helperClass
	 *            Helper class name
	 * @param findByMethod
	 *            Find method name
	 * @param value
	 *            value
	 * @return JDO Object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object findByHelper(Class<?> helperClass, String[] findByFields,
			String value) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		log.debug("findByHelper");
		BaseHelper<?> helper = getHelper(helperClass);
		for (int i = 0; i < findByFields.length; i++) {
			String propertyName = findByFields[i];
			try {
				log.debug("findBy " + propertyName);
				Class<?> pc = null;
				Method[] methods = helper.getJdoClass().getMethods();
				String getter = "get" + propertyName;
				for (Method m : methods) {
					if (m.getName().equalsIgnoreCase(getter)) {
						pc = m.getReturnType();
						break;
					}
				}
				Object cv = parseBasicType(pc, value);
				Criterion c = Restrictions.eq(propertyName, cv);
				QueryDefn qf = new QueryDefn();
				qf.addCriterion(c);
				IPagedResult<?> pr = helper.find(qf);
				int count = pr.count();
				log.debug("Find " + count);
				if (count > 0) {
					return pr.getResult(0, 1).get(0);
				}
			} catch (Exception ex) {
				if (i == findByFields.length - 1) {
					throw new DixinException("查找对象失败", ex);
				}
			}
		}
		return null;
	}

	/**
	 * Find JDO object by unique key
	 * 
	 * @param helperClass
	 *            Helper class name
	 * @param findByMethod
	 *            Find method name
	 * @param value
	 *            value
	 * @return JDO Object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object[] findByHelper(Class<?> helperClass, String[] findByFields,
			String[] value) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		log.debug("findByHelper");
		BaseHelper<?> helper = getHelper(helperClass);
		for (String propertyName : findByFields) {
			log.debug("findBy " + propertyName);
			Class<?> pc = null;
			Method[] methods = helper.getJdoClass().getMethods();
			String getter = "get" + propertyName;
			for (Method m : methods) {
				if (m.getName().equalsIgnoreCase(getter)) {
					pc = m.getReturnType();
					break;
				}
			}
			Object[] cv = parseBasicTypeValues(pc, value);
			Criterion c = Restrictions.in(propertyName, cv);
			QueryDefn qf = new QueryDefn();
			qf.addCriterion(c);
			IPagedResult<?> pr = helper.find(qf);
			int count = pr.count();
			log.debug("Find " + count);
			if (count > 0) {
				return pr.getResult(0, count).toArray();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param pc
	 * @param value
	 * @return
	 */
	public Object parseBasicType(Class<?> pc, String value) {
		Object cv = value;
		if (Integer.class.isAssignableFrom(pc)) {
			cv = Integer.parseInt(value);
		} else if (Long.class.isAssignableFrom(pc)) {
			cv = Long.parseLong(value);
		} else if (Float.class.isAssignableFrom(pc)) {
			cv = Float.parseFloat(value);
		} else if (Double.class.isAssignableFrom(pc)) {
			cv = Double.parseDouble(value);
		} else if (Date.class.isAssignableFrom(pc)) {
			try {
				cv = dateFormat.parse(value);
			} catch (Exception ex) {
				throw new ParseException("日期格式错误", ex);
			}
		}
		return cv;
	}

	/**
	 * 
	 * @param pc
	 * @param value
	 * @return
	 */
	public Object[] parseBasicTypeValues(Class<?> pc, String[] value) {
		Object[] cv = new Object[value.length];
		if (Integer.class.isAssignableFrom(pc)) {
			for (int i = 0; i < value.length; i++)
				cv[i] = Integer.parseInt(value[i]);
		} else if (Long.class.isAssignableFrom(pc)) {
			for (int i = 0; i < value.length; i++)
				cv[i] = Long.parseLong(value[i]);
		} else if (Float.class.isAssignableFrom(pc)) {
			for (int i = 0; i < value.length; i++)
				cv[i] = Float.parseFloat(value[i]);
		} else if (Double.class.isAssignableFrom(pc)) {
			for (int i = 0; i < value.length; i++)
				cv[i] = Double.parseDouble(value[i]);
		} else if (Date.class.isAssignableFrom(pc)) {
			for (int i = 0; i < value.length; i++)
				try {
					cv[i] = dateFormat.parse(value[i]);
				} catch (Exception ex) {
					throw new ParseException("日期格式错误", ex);
				}
		}
		return cv;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isTrim() {
		return trim;
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	public boolean isEmptyToNull() {
		return emptyToNull;
	}

	public void setEmptyToNull(boolean emptyToNull) {
		this.emptyToNull = emptyToNull;
	}
}
