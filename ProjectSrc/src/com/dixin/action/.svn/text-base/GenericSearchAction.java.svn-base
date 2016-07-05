package com.dixin.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.hibernate.criterion.Order;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.AggregationFactory;
import com.dixin.business.IAggregation;
import com.dixin.business.IPagedResult;
import com.dixin.business.IQueryDefn;
import com.dixin.business.impl.BaseHelper;
import com.dixin.hibernate.BaseJDO;
import com.dixin.hibernate.convert.JSONalizer;

/**
 * Generic Search Action.
 * 
 * 
 * @param <T>
 */
public abstract class GenericSearchAction<T extends BaseJDO> extends
		AbstractAction<T> {
	private static final int ORDER_POLL_SIZE = 3;

	public GenericSearchAction(Class<T> jdoClass,
			Class<? extends BaseHelper<T>> helperClass) {
		super(jdoClass, helperClass);
		localizedName = "查找" + jdoClass.getAnnotation(Name.class).value()
				+ "信息";
	}
	private List<Order> orders = new LinkedList<Order>();
	private final String localizedName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.action.IAction#getLocalizedName()
	 */
	public String getLocalizedName() {
		return localizedName;
	}

	/**
	 * 
	 * @see com.dixin.action.JsonAction#process(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		try {
			int limit = getLimit(request);
			int start = getStart(request);
			IQueryDefn query = getQueryDefn(request, result);
			beforeQuery(query);
			search(result, query, start, limit);
			getAggregations(request, query, result);
			return true;
		} catch (Exception ex) {
			throw new ActionException("查询失败，请检查输入条件。" + ex.getMessage(), ex);
		}
	}

	/**
	 * subclass can rewrite the method to change the query before do the search
	 * 
	 * @param query
	 */
	public void beforeQuery(IQueryDefn query) {

	}

	/**
	 * Parse aggregation annotation and get the aggregation result and set it to
	 * result
	 * 
	 * @param request
	 * @param query
	 * @param result
	 */
	public void getAggregations(HttpServletRequest request, IQueryDefn query,
			JSONObject result) {
		Aggregate[] agg = AggregateFactory.getInstance()
				.getAggregateProperties(jdoClass);
		AggregationFactory aggregation = AggregationFactory.getInstance();
		IAggregation agn = aggregation.getAggregation(jdoClass, query);
		Object[] r = agn.getAggrResult(agg);
		List<String[]> aggresult = new ArrayList<String[]>();
		for (int i = 0; i < agg.length; i++) {
			String[] record = new String[3];
			record[0] = agg[i].name();
			record[1] = aggregation.getAggrTypeName(agg[i].type());
			Object o = r[i];
			String s = null;
			if (o instanceof Number) {
				s = JSONalizer.getInstance().getNumberFormat().format(o);
			} else if (o instanceof Date) {
				s = JSONalizer.getInstance().getDateFormat().format(o);
			} else {
				s = o + "";
			}
			record[2] = s;
			aggresult.add(record);
		}
		result.accumulate("aggregations", aggresult);
	}

	/**
	 * Get max number for returned result
	 * 
	 * @param request
	 * @return
	 */
	protected int getLimit(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("limit"));
		} catch (Exception ex) {
			log.debug("没有limit参数");
			return 20;
		}
	}

	/**
	 * Get start position in the whole search result from which the current
	 * page's result to be return
	 * 
	 * @param request
	 * @return
	 */
	protected int getStart(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("start"));
		} catch (Exception ex) {
			log.debug("没有start参数");
			return 0;
		}
	}

	/**
	 * 得到查询条件
	 * 
	 * @param request
	 * @param result
	 * @return
	 */
	protected IQueryDefn getQueryDefn(HttpServletRequest request,
			JSONObject result) {
		Map<String, String> paramsMap = getParameterMap(request);
		setDefaultQueryParams(paramsMap);
		IQueryDefn query = getQueryDefn(paramsMap);
		Order order = getOrder(request);
		if (order != null) {
			if (orders.size() >= ORDER_POLL_SIZE) {
				orders.remove(0);
			}
			orders.add(order);
		}
		if (!orders.isEmpty()) {
			for (int i = orders.size() - 1; i >= 0; i--) {
				query.addOrder(orders.get(i));
			}
		}
		return query;
	}

	/**
	 * Subclass override this method to update the params map before parse
	 * query.
	 * 
	 * @param paramsMap
	 */
	protected void setDefaultQueryParams(Map<String, String> paramsMap) {

	}

	/**
	 * Convert the HttpServletRequest parameters to a Map<String,String>
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, String> getParameterMap(HttpServletRequest request) {
		Map<String, String> ret = new HashMap<String, String>();
		Map<String, String[]> map = request.getParameterMap();
		for (String key : map.keySet()) {
			String value = map.get(key)[0];
			ret.put(key, value);
		}
		return ret;
	}

	/** 
	 */
	private Pattern sortOnForeignFieldPattern = Pattern.compile("(.*)_(.*)");

	/**
	 * 获得排序参数
	 * 
	 * @param request
	 * @return
	 */
	protected Order getOrder(HttpServletRequest request) {
		String dir = getDir(request);
		String sort = getSort(request);
		Order order = null;
		if (sort != null) {
			Matcher m = sortOnForeignFieldPattern.matcher(sort);
			if (m.matches()) {
				sort = m.group(1);
			}
			if ("ASC".equalsIgnoreCase(dir)) {
				order = Order.asc(sort);
			} else if ("DESC".equalsIgnoreCase(dir)) {
				order = Order.desc(sort);
			}
		}
		return order;
	}

	protected String getDir(HttpServletRequest request) {
		return request.getParameter("dir");
	}

	protected String getSort(HttpServletRequest request) {
		return request.getParameter("sort");
	}

	/**
	 * 执行查询操作
	 * 
	 * @param json
	 * @param query
	 * @param start
	 * @param limit
	 * @return
	 */
	protected void search(JSONObject json, IQueryDefn query, int start,
			int limit) {
		IPagedResult<T> pagedResult = getHelper().find(query);
		List<T> result = pagedResult.getResult(start, limit);
		processQueryResult(result);
		json.accumulate("totalCount", pagedResult.count());
		json.accumulate("data", result);
	}
	
	protected void processQueryResult(List<T> result){
		
	}

}
