package com.dixin.hibernate.convert;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * Convert JDO to JSON
 * 
 * @author Luo
 * 
 */
public class JSONalizer {
	protected DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	protected NumberFormat numberFormat = NumberFormat.getInstance();
	{
		numberFormat.setGroupingUsed(false);
		numberFormat.setMaximumFractionDigits(10);
	}
	private static JSONalizer instance = new JSONalizer();
	private Logger log = Logger.getLogger(JSONalizer.class);

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public static JSONalizer getInstance() {
		return instance;
	}

	/**
	 * Convert JDO to JSON String
	 * 
	 * @param jdo
	 * @return
	 */
	public String toJSONString(Object jdo) {
		return toJSONObject(jdo).toString();
	}

	/**
	 * Convert JDO to JSONObject
	 * 
	 * @param jdo
	 * @return
	 */
	public JSONObject toJSONObject(Object jdo) {
		return convert(jdo);
	}

	/**
	 * 
	 */
	final Pattern getterPattern = Pattern.compile("^get([a-zA-Z0-9_]+)$");

	public Object basic(Object o) {
		if (o instanceof Date) {
			return dateFormat.format((Date) o);
		}
		return o;
	}

	Object maskNull(Object o) {
		return o == null ? "" : o;
	}

	/**
	 * Convert jdo to JSONObject
	 * 
	 * @param t
	 * @return
	 */
	JSONObject convert(Object t) {
		JSONObject obj = new JSONObject();
		Method[] ms = t.getClass().getMethods();
		for (Method m : ms) {
			String property = null;
			try {
				JSONalize js = m.getAnnotation(JSONalize.class);
				if (js == null) {
					continue;
				}
				property = m.getName();
				Matcher matcher = getterPattern.matcher(property);
				if (matcher.matches()) {
					property = matcher.group(1);
					property = property.substring(0, 1)//
							.toLowerCase() //
							+ property.substring(1);
				}
				Object value = m.invoke(t);
				switch (js.type()) {
				case Basic:
				case Default:
					if (value != null) {
						if (value instanceof Double) {
							value = numberFormat.format((Double) value);
						} else if (value instanceof Date) {
							value = dateFormat.format((Date) value);
						}
					}
					obj.element(property, maskNull(value));
					break;
				case ToString:
					value = "" + value;
					obj.element(property, maskNull(value));
					break;
				case Properties:
					String[] properties = js.properties().split(",");
					if (value == null) {
						break;
					}
					obj.element(property, maskNull(//
							basic(//
							BeanUtil.getNestedPropertyValue(//
									value, //
									properties[0]))));
					for (int i = 1; i < properties.length; i++) {
						obj.element(property + "_" + properties[i], maskNull(//
								basic(//
								BeanUtil.getNestedPropertyValue(value,//
										properties[i]))));
					}
					break;
				case Converter:
					obj.element(property,// 
							maskNull(//
							js.converter()//
									.newInstance()//
									.convert(value)));
					break;
				}
				// if (log.isDebugEnabled())
				// log.debug(String.format("JSONalize Property %s= %s",
				// property, value + ""));
			} catch (Exception ex) {
				throw new ConvertException("转化数据对象时出错:" + t + ", property:"
						+ property, ex);
			}
		}
		return obj;
	}
}
