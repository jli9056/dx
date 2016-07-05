package com.dixin.business;

import com.dixin.annotation.Aggregate;

public interface IAggregation {

	/**
	 * constants for aggregation types.
	 */
	public static final int SUM = 0;
	public static final int AVG = 1;
	public static final int MAX = 2;
	public static final int MIN = 3;
	public static final int COUNT = 4;
	public static final int COUNT_DISTINCT = 5;

	/**
	 * get the aggregation result for the specified field. If invalid field is
	 * applied to the aggregation, an exception will be thrown.
	 * 
	 * @param aggs
	 * @return
	 * @throws DataException
	 */
	public Object[] getAggrResult(Aggregate[] aggs);

}