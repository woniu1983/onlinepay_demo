package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 文本消息内容
 *
 * @author auto create
 * @since 1.0, 2016-08-10 17:26:53
 */
public class Text extends AlipayObject {

	private static final long serialVersionUID = 6834113611975831197L;

	/**
	 * 你好!
	 */
	@ApiField("content")
	private String content;

	public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
