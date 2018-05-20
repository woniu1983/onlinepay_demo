package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 公众号标签管理-查询用户标签
 *
 * @author auto create
 * @since 1.0, 2016-08-10 17:28:18
 */
public class AlipayOpenPublicUserQueryModel extends AlipayObject {

	private static final long serialVersionUID = 5113557298394125142L;

	/**
	 * 支付宝用户的userid，2088开头长度为16位的字符串
	 */
	@ApiField("user_id")
	private String userId;

	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
