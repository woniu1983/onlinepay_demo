package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 持卡人信息
 *
 * @author auto create
 * @since 1.0, 2016-08-18 22:47:48
 */
public class CardUserInfo extends AlipayObject {

	private static final long serialVersionUID = 6831279237446588393L;

	/**
	 * 用户唯一标识
	 */
	@ApiField("user_uni_id")
	private String userUniId;

	/**
	 * ID类型
LOGON_ID：用户登录ID，邮箱或者手机号格式；
UID：用户支付宝用户号，以2088开头的16位纯数字组成；
BINDING_MOBILE：用户支付宝账号绑定的手机号。
D_WAVE_CODE：动态声波
D_QR_CODE：动态二维码
D_BAR_CODE：动态条码
	 */
	@ApiField("user_uni_id_type")
	private String userUniIdType;

	public String getUserUniId() {
		return this.userUniId;
	}
	public void setUserUniId(String userUniId) {
		this.userUniId = userUniId;
	}

	public String getUserUniIdType() {
		return this.userUniIdType;
	}
	public void setUserUniIdType(String userUniIdType) {
		this.userUniIdType = userUniIdType;
	}

}
