/**
 * 
 */
package com.dixin.business;

import com.dixin.business.query.Aggregation;

/**
 * @author Jason
 * 
 */
public class AggregationFactory {

	private static AggregationFactory instance = null;

	private AggregationFactory() {
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static AggregationFactory getInstance() {
		if (instance == null) {
			synchronized (AggregationFactory.class) {
				instance = new AggregationFactory();
			}
		}
		return instance;
	}

	/**
	 * 
	 * @param clz
	 * @param queryDefn
	 * @return
	 */
	public IAggregation getAggregation(Class<?> clz, IQueryDefn queryDefn) {
		return new Aggregation(clz, queryDefn);
	}
	
	/**
	 * 
	 * @param aggrType
	 * @return
	 */
	public String getAggrTypeName(int aggrType) {
		switch (aggrType) {
		case IAggregation.SUM:
			return "求和";
		case IAggregation.AVG:
			return "平均值";
		case IAggregation.MAX:
			return "最大值";
		case IAggregation.MIN:
			return "最小值";
		case IAggregation.COUNT:
			return "计数";
		case IAggregation.COUNT_DISTINCT:
			return "去重计数";
		default:
			return "未知";
		}
	}
}
