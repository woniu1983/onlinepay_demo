package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 用户增加标签接口
 *
 * @author auto create
 * @since 1.0, 2016-08-10 17:27:47
 */
public class AlipayOpenPublicLabelUserAddModel extends AlipayObject {

	private static final long serialVersionUID = 4262625546244172173L;

	/**
	 * 要绑定的标签Id
	 */
	@ApiField("label_id")
	private Long labelId;

	/**
	 * 支付宝用户id，2088开头长度为16位的字符串
	 */
	@ApiField("user_id")
	private String userId;

	public Long getLabelId() {
		return this.labelId;
	}
	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}

	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
