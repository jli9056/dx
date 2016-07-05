package com.dixin.servlet;

import com.dixin.business.DataException;

public interface IUpdater {

	/**
	 * get the target system version that this updater applies on.
	 * 
	 * @return
	 */
	public String getTargetVersion();

	/**
	 * do the update action.
	 * 
	 * @throws DataException
	 */
	public void update() throws DataException;

}
