package com.dixin.business.impl;

import com.dixin.business.DataException;
import com.dixin.hibernate.Deliverydetail;

public class DeliverydetailHelper extends BaseHelper<Deliverydetail> {
	public void save(Deliverydetail d) {
		throw new DataException("不支持的操作");
	}

	public Deliverydetail merge(Deliverydetail d) {
		throw new DataException("不支持的操作");
	}

	public boolean deleteById(Integer id) {
		throw new DataException("不支持的操作");
	}
}
