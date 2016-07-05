package com.dixin.action.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.exception.ConstraintViolationException;

import com.dixin.hibernate.BaseJDO;
import com.dixin.hibernate.Customer;
import com.dixin.hibernate.Employee;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Shop;
import com.dixin.hibernate.Storehouse;

/**
 * Contains data integrity information
 * 
 * @author Luo
 * 
 */
public class DataIntegrity {
	private String field;
	private DataIntegrityType integrityType;

	public DataIntegrity(String field, DataIntegrityType integrityType) {
		this.field = field;
		this.integrityType = integrityType;
	}

	/**
	 * Used to match duplicated key exception error message.<BR>
	 * And parse the duplicate key index.
	 */
	private static Pattern dupKeyPattern = Pattern
			.compile(".*\\\"Duplicate entry '.*' for key ([0-9]+)\\\".*");

	/**
	 * 
	 * @param e
	 * @param clazz
	 * @param errors
	 */
	public static DataIntegrity getFromException(
			ConstraintViolationException e, Class<BaseJDO> clazz) {
		return get(e, clazz);
	}

	private static DataIntegrity get(Exception e, Class<BaseJDO> clazz) {
		Throwable t = e.getCause();
		// get root cause. getRootCause method not work here, so we just loop
		while (t.getCause() != null && t != t.getCause()) {
			t = t.getCause();
		}
		Matcher matcher = dupKeyPattern.matcher(t.getMessage());
		// duplicate key row exception
		if (matcher.matches()) {
			String keyIndex = matcher.group(1);
			String field = getUniqueField(clazz, Integer.parseInt(keyIndex));
			return new DataIntegrity(field, DataIntegrityType.Unique);
		} else {
			return new DataIntegrity("未知字段", DataIntegrityType.Unknown);
		}
	}

	/**
	 * 
	 * @param e
	 * @param clazz
	 * @param errors
	 */
	public static DataIntegrity getFromException(SQLException e,
			Class<BaseJDO> clazz) {
		return get(e, clazz);
	}

	public static String getUniqueField(Class<BaseJDO> clazz, int index) {
		String[] keys = clazzUniqueKeys.get(clazz);
		if (keys != null) {
			return keys[index];
		}
		return "";
	}

	private static Map<Class<? extends BaseJDO>, String[]> clazzUniqueKeys = new HashMap<Class<? extends BaseJDO>, String[]>();

	public static void registerUnique(Class<? extends BaseJDO> clazz,
			String[] keys) {
		clazzUniqueKeys.put(clazz, keys);
	}

	static {
		registerUnique(Product.class, new String[] { "0", "id", "model",
				"alias", "barcode" });
		registerUnique(Customer.class, new String[] { "id", "name", "phone" });
		registerUnique(Employee.class, new String[] { "id" });
		registerUnique(Shop.class, new String[] { "id", "name" });
		registerUnique(Storehouse.class, new String[] { "id", "name" });
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public DataIntegrityType getIntegrityType() {
		return integrityType;
	}

	public void setIntegrityType(DataIntegrityType integrityType) {
		this.integrityType = integrityType;
	}
}
