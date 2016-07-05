package com.dixin.hibernate.convert;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;

import com.dixin.business.impl.BaseHelper;
import com.dixin.hibernate.BaseJDO;

/**
 * Mark on the setter method. Indicate this property is capable for collect from
 * user input and may need some convert before set.
 * 
 * @author Luo
 * 
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Parse {
	ParseType type() default ParseType.Basic;

	@SuppressWarnings("unchecked")
	Class<? extends BaseHelper> helper() default com.dixin.business.impl.BaseHelper.class;

	/**
	 * 搜索字段。如果包含多个搜索字段，先找第一个，在找不到的情况下才找下一个。多个搜索字段用逗号隔开。
	 * 
	 * @return
	 */
	String findFields() default "id";

	Class<? extends Converter> converter() default Converter.class;
}
