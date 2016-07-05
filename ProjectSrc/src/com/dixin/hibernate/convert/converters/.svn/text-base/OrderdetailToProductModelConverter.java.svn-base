package com.dixin.hibernate.convert.converters;

import com.dixin.hibernate.AbstractOrderdetail;
import com.dixin.hibernate.convert.Converter;

public class OrderdetailToProductModelConverter implements Converter {

	public Object convert(Object src) {
		AbstractOrderdetail d = (AbstractOrderdetail) src;
		return d.getProduct().getModel();
	}
}
