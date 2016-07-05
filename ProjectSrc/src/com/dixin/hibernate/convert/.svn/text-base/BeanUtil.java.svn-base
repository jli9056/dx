package com.dixin.hibernate.convert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;

public class BeanUtil {
	private BeanUtil() {
	}

	/**
	 * 
	 * @param clazz
	 * @param property
	 * @return
	 */
	public static Class<?> getNestedPropertyType(Class<?> clazz, String property) {
		if (property == null) {
			return null;
		}
		int dotIndex = property.indexOf('.');
		if (dotIndex > 0) {
			String prop = property.substring(0, dotIndex);
			String next = property.substring(dotIndex + 1);
			return getNestedPropertyType(BeanUtils.getPropertyDescriptor(clazz,
					prop).getPropertyType(), next);
		} else {
			return BeanUtils.getPropertyDescriptor(clazz, property)
					.getPropertyType();
		}
	}

	public static Object getNestedPropertyValue(Object o, String property) {
		if (property == null) {
			return null;
		}
		int dotIndex = property.indexOf('.');
		if (dotIndex > 0) {
			String prop = property.substring(0, dotIndex);
			String next = property.substring(dotIndex + 1);
			return getNestedPropertyValue(getDirectPropertyValue(o, prop), next);
		} else {
			return getDirectPropertyValue(o, property);
		}
	}

	public static Object getDirectPropertyValue(Object o, String property) {
		Class<?> clazz = o.getClass();
		Object v = null;
		try {

			Method method = getGetter(clazz, property);
			if (method != null) {
				v = method.invoke(o);
			} else {
				Field f = clazz.getDeclaredField(property);
				v = f.get(o);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return v;
	}

	/**
	 * 
	 * @param clazz
	 * @param property
	 * @return method or null
	 */
	public static Method getGetter(Class<?> clazz, String property) {
		String gettter = "get" + property.substring(0, 1).toUpperCase()
				+ property.substring(1);
		Method method = null;
		try {
			method = clazz.getMethod(gettter);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return method;
	}
}
