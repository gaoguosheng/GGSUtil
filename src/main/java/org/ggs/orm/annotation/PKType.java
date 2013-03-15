package org.ggs.orm.annotation;

/***
 * 主键类型
 * */
public enum PKType {
	/**
	 * 用户手动赋值
	 * */
	assigned,
	/**
	 * 自增长，如mysql/sqlserver自增长字段
	 * */
	increment,
	/**
	 * Oracle序列，默认为seq_加表名，如表名为T_user，序列为sql_T_user
	 * */
	sequence
}
