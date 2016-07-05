package com.dixin.action.fdrefund;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.FdRefundHelper;
import com.dixin.hibernate.FdRefund;

public class LoadFdRefundAction extends GenericLoadAction<FdRefund> {

	public LoadFdRefundAction() {
		super(FdRefund.class, FdRefundHelper.class);
	}

}
