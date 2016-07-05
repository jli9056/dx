package com.dixin.hibernate.convert;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * @author Luo
 * 
 */
public class MapParseSource implements ParseSource {
	Map<String, String[]> map;

	public MapParseSource(Map<String, String[]> map) {
		this.map = map;
	}

	public boolean containsName(String name) {
		return map.containsKey(name);
	}

	public Collection<String> getNames() {
		return map.keySet();
	}

	public String getValue(String name) {
		String[] v = map.get(name);
		return v.length > 0 ? v[0] : null;
	}

	public String[] getValues(String name) {
		return map.get(name);
	}

}
