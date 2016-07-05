package com.dixin.action.fdrefund;

import com.dixin.DixinException;
import com.dixin.action.GenericAddAction;
import com.dixin.business.constants.Bool;
import com.dixin.business.impl.FdRefundHelper;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.FdRefund;

public class AddFdRefundAction extends GenericAddAction<FdRefund> {

	public AddFdRefundAction() {
		super(FdRefund.class, FdRefundHelper.class);
		uniqueKeys = new String[] { "", "id" };
	}

	/* (non-Javadoc)
	 * @see com.dixin.action.GenericAddAction#validate(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	protected void validate(FdRefund fr) {
		Factoryorderdetail fod = fr.getFactoryorderdetail();
		int doubleCheck = fod.getFactoryorder().getDoubleCheck();
		if (doubleCheck == Bool.FALSE) {
			throw new DixinException("复核之前订单明细不能通过该方式退货,请直接修改工厂订单!");
		}
	}

}
