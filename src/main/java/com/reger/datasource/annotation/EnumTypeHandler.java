package com.reger.datasource.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface EnumTypeHandler {
	
	/**
	 * 用来判断枚举转化关系的枚举field的名字 所有枚举默认可用 name,ordinal
	 * @return 用来判断枚举转化关系的枚举field的名字
	 */
	String field();
}
