package com.dixin.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

/**
 * @author Jason
 * 
 */
public class RegistryHelper {

	/**
	 * Windows handles to <tt>HKEY_CURRENT_USER</tt> hives.
	 */
	public static final int HKEY_CURRENT_USER = 0x80000001;
	/**
	 * Windows handles to <tt>HKEY_LOCAL_MACHINE</tt> hives.
	 */
	public static final int HKEY_LOCAL_MACHINE = 0x80000002;

	private static final int KEY_QUERY_VALUE = 1;
	private static final int KEY_SET_VALUE = 2;
	private static final int KEY_READ = 0x20019;
	private static final int KEY_WRITE = 0x20006;

	private static final String OPEN_KEY = "openKey";
	private static final String CLOSE_KEY = "closeKey";
	private static final String WINDOWS_REG_QUERY_VALUE_EX = "WindowsRegQueryValueEx";
	private static final String WINDOWS_REG_SET_VALUE_EX = "WindowsRegSetValueEx";
	private static final String CLASS_WINDOWS_PREFERENCES = "java.util.prefs.WindowsPreferences";

	private static final int DEFAULT_NUMBER = Integer.MAX_VALUE;
	private static final boolean DEFAULT_BOOL = false;

	private Preferences root;
	@SuppressWarnings("unchecked")
	private Class rootClz;
	private Method openKey;
	private Method closeKey;
	private byte[] subKeyBytes;

	/**
	 * 
	 * @return
	 */
	public static RegistryHelper getDixinRegistry() {
		return new RegistryHelper(RegistryHelper.HKEY_LOCAL_MACHINE,
				"Software\\Dixin\\dxkc");
	}
	/**
	 * 
	 * @param rootKey
	 * @param subKey
	 */
	@SuppressWarnings("unchecked")
	public RegistryHelper(int rootKey, String subKey) {
		try {
			rootClz = Class.forName(CLASS_WINDOWS_PREFERENCES);
			Constructor constructor = rootClz.getDeclaredConstructor(int.class,
					byte[].class);
			constructor.setAccessible(true);
			subKeyBytes = toByteArray(subKey);
			root = (Preferences) constructor.newInstance(rootKey, subKeyBytes);
			openKey = rootClz.getDeclaredMethod(OPEN_KEY, byte[].class,
					int.class, int.class);
			openKey.setAccessible(true);
			closeKey = rootClz.getDeclaredMethod(CLOSE_KEY, int.class);
			closeKey.setAccessible(true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param subKey
	 * @param entry
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String get(String entry) {
		try {
			Method WindowsRegQueryValueEx = rootClz.getDeclaredMethod(
					WINDOWS_REG_QUERY_VALUE_EX, int.class, byte[].class);
			WindowsRegQueryValueEx.setAccessible(true);
			Integer handle = (Integer) openKey.invoke(root, subKeyBytes,
					KEY_READ, KEY_QUERY_VALUE);
			byte[] bytes = (byte[]) WindowsRegQueryValueEx.invoke(root, handle
					.intValue(), toByteArray(entry));
			closeKey.invoke(root, handle);
			return bytes != null ? new String(bytes).trim() : null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param rootKey
	 * @param subKey
	 * @param entry
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void put(String entry, String value) {
		try {
			Method WindowsRegSetValueEx = rootClz.getDeclaredMethod(
					WINDOWS_REG_SET_VALUE_EX, int.class, byte[].class,
					byte[].class);
			WindowsRegSetValueEx.setAccessible(true);
			Integer handle = (Integer) openKey.invoke(root, subKeyBytes,
					KEY_WRITE, KEY_SET_VALUE);
			WindowsRegSetValueEx.invoke(root, handle.intValue(),
					toByteArray(entry), toByteArray(value));
			closeKey.invoke(root, handle);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	private static byte[] toByteArray(String str) {
		byte[] source = str.getBytes();
		byte[] result = new byte[source.length + 1];
		System.arraycopy(source, 0, result, 0, source.length);
		return result;
	}

	public boolean getBoolean(String key) {
		boolean result = DEFAULT_BOOL;
		try {
			String value = get(key);
			if (value != null)
				result = Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public double getDouble(String key) {
		double result = DEFAULT_NUMBER;
		try {
			String value = get(key);
			if (value != null)
				result = Double.parseDouble(value);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public float getFloat(String key) {
		float result = DEFAULT_NUMBER;
		try {
			String value = get(key);
			if (value != null)
				result = Float.parseFloat(value);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public int getInt(String key) {
		int result = DEFAULT_NUMBER;
		try {
			String value = get(key);
			if (value != null)
				result = Integer.parseInt(value);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public long getLong(String key) {
		long result = DEFAULT_NUMBER;
		try {
			String value = get(key);
			if (value != null)
				result = Long.parseLong(value);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public void putBoolean(String key, boolean value) {
		put(key, String.valueOf(value));
	}

	public void putDouble(String key, double value) {
		put(key, String.valueOf(value));
	}

	public void putFloat(String key, float value) {
		put(key, String.valueOf(value));
	}

	public void putInt(String key, int value) {
		put(key, String.valueOf(value));
	}

	public void putLong(String key, long value) {
		put(key, String.valueOf(value));
	}
}
