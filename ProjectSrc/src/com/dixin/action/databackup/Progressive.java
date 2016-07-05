package com.dixin.action.databackup;

public interface Progressive extends Runnable {

	public abstract Progress getProgress();

}