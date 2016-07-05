package com.dixin.action.util;

import java.util.LinkedList;
import java.util.List;

public class ValidationUtil {

	/**
	 * 从items数组中查找出重复的名字,以List的形式返回.
	 * 
	 * @param items
	 * @return
	 */
	public static List<String> getDuplicateItems(String[] items) {
		assert items != null;
		List<String> list = new LinkedList<String>();
		boolean[] marked = new boolean[items.length];
		for (int i = 0; i < items.length; i++) {
			if (!marked[i]) {
				boolean found = false;
				for (int j = i + 1; j < items.length; j++) {
					if (equals(items[i], items[j])) {
						marked[j] = true;
						found = true;
					}
				}
				if (found) {
					list.add(items[i]);
				}
			}
		}
		return list;
	}

	/**
	 * to check whether two strings are equal.
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private static boolean equals(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		} else if (s1 != null) {
			return s1.equals(s2);
		} else {
			return s2.equals(s1);
		}
	}
}
