package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 条件模型
 *
 * @author auto create
 * @since 1.0, 2016-08-19 15:07:49
 */
public class Condition extends AlipayObject {

	private static final long serialVersionUID = 5611377694311212358L;

	/**
	 * 字段名称，目前支持以下字段：
name: 活动名称 
startTime: 开始时间
endTime: 结束时间
status：活动状态
	 */
	@ApiField("field_name")
	private String fieldName;

	/**
	 * 对应于field_name的字段值，当field_name为status时，field_value支持CREATED，ENABLED，DISABLED，STARTED，CLOSED,FINISHED几种值，用|分隔，代表查询这些状态中的活动
	 */
	@ApiField("field_value")
	private String fieldValue;

	/**
	 * 操作符,EQUAL:等于,IN:范围。目前支持field_name=status且operator=IN,多个状态以"|"分隔
	 */
	@ApiField("operator")
	private String operator;

	public String getFieldName() {
		return this.fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return this.fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getOperator() {
		return this.operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

}
