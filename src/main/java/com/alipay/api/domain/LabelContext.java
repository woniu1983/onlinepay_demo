package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 标签组发圈人条件
 *
 * @author auto create
 * @since 1.0, 2016-08-10 17:28:21
 */
public class LabelContext extends AlipayObject {

	private static final long serialVersionUID = 6328949953914448993L;

	/**
	 * 标签组发圈人的单个过滤器信息
	 */
	@ApiField("a")
	private LabelFilter a;

	public LabelFilter getA() {
		return this.a;
	}
	public void setA(LabelFilter a) {
		this.a = a;
	}

}
