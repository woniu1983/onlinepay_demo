package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 模板中占位符的值及文字颜色，value和color都为必填项，color为当前文字颜色
 *
 * @author auto create
 * @since 1.0, 2016-08-10 17:27:24
 */
public class Keyword extends AlipayObject {

	private static final long serialVersionUID = 4876382272597243984L;

	/**
	 * 当前文字颜色
	 */
	@ApiField("color")
	private String color;

	/**
	 * 模板中占位符的值
	 */
	@ApiField("value")
	private String value;

	public String getColor() {
		return this.color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
