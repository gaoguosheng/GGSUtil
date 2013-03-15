package org.ggs.orm.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 表
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	//定义属性，格式：属性类型+属性名称+括号
	String value();
}
