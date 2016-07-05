package com.dixin.action.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ResultUtil {
	public static final String SUCCESS = "{success:true}";
	public static final String FAILURE = "{success:false}";
	public static final String DATA = "data";
	public static final String ERRORS = "errors";
	public static final String ID = "id";
	public static final String MSG = "msg";

	private ResultUtil() {
	}

	/**
	 * 
	 * @return
	 */
	public static JSONObject getSuccessJSON() {
		return JSONObject.fromObject(SUCCESS);
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static JSONObject getSuccessJSON(JSON data) {
		JSONObject result = JSONObject.fromObject(SUCCESS);
		result.accumulate(DATA, data);
		return result;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static JSONObject getSuccessJSON(Object data) {
		JSONObject result = JSONObject.fromObject(SUCCESS);
		result.accumulate(DATA, data);
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public static JSONObject getFailureJSON() {
		return JSONObject.fromObject(FAILURE);
	}

	/**
	 * 
	 * @param errors
	 * @return
	 */
	public static JSONObject getFailureJSON(Map<String, String> errors) {
		JSONObject result = JSONObject.fromObject(FAILURE);
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (Iterator<String> i = errors.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			JSONObject obj = new JSONObject();
			obj.accumulate(ID, key);
			obj.accumulate(MSG, errors.get(key));
			list.add(obj);
		}
		JSONArray errorArray = JSONArray.fromObject(list);
		result.accumulate(ERRORS, errorArray);
		return result;
	}

	/**
	 * 
	 * @param errors
	 * @return
	 */
	public static void addErrors(JSONObject result, Map<String, String> errors) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (Iterator<String> i = errors.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			JSONObject obj = new JSONObject();
			obj.accumulate(ID, key);
			obj.accumulate(MSG, errors.get(key));
			list.add(obj);
		}
		JSONArray errorArray = JSONArray.fromObject(list);
		result.accumulate(ERRORS, errorArray);
	}

	/**
	 * 
	 * @param exception
	 * @return
	 */
	public static JSONObject getFailureJSON(Throwable exception) {
		JSONObject result = JSONObject.fromObject(FAILURE);
		result.accumulate(MSG, exception.getLocalizedMessage());
		return result;
	}

	/**
	 * 
	 * @param exception
	 * @return
	 */
	public static JSONObject getFailureJSON(String errorMsg) {
		JSONObject result = JSONObject.fromObject(FAILURE);
		result.accumulate(MSG, errorMsg);
		return result;
	}
}
