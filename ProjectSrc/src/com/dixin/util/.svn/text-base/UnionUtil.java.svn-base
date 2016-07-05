package com.dixin.util;

import java.util.HashSet;
import java.util.Set;

public class UnionUtil {

	/**
	 * 
	 * @param ids
	 * @param args
	 * @return
	 */
	public static int[] union(int[] ids, int[]... args) {
		Set<Integer> unionSet = new HashSet<Integer>();
		for (int id : ids) {
			unionSet.add(id);
		}
		for (int i = 0; i < args.length; i++) {
			for (int id : args[i]) {
				unionSet.add(id);
			}
		}
		int[] unionIds = new int[unionSet.size()];
		int index = 0;
		for (Integer id : unionSet) {
			unionIds[index++] = id;
		}
		return unionIds;
	}

}
