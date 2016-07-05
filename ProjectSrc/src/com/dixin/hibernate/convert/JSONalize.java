package com.dixin.hibernate.convert;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONalize {
	JSONalizeType type() default JSONalizeType.Default;

	String properties() default "id";

	Class<? extends Converter> converter() default Converter.class;
}
