package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 会员卡查询
 *
 * @author auto create
 * @since 1.0, 2016-08-09 13:33:39
 */
public class AlipayMarketingCardQueryModel extends AlipayObject {

	private static final long serialVersionUID = 3384422728121752281L;

	/**
	 * 用户信息
和卡号不能同时为空，且如果提供userID且卡号不填时，查询该用户在商户下的所有卡信息
	 */
	@ApiField("card_user_info")
	private CardUserInfo cardUserInfo;

	/**
	 * 扩展信息，暂时没有
	 */
	@ApiField("ext_info")
	private String extInfo;

	/**
	 * 操作卡号。
target_card_no和card_user_info不能同时为空。
	 */
	@ApiField("target_card_no")
	private String targetCardNo;

	/**
	 * 卡号ID类型（会员卡查询，只能提供支付宝端的卡号）
BIZ_CARD：支付宝卡号
EXTERNAL_CARD：商户卡号
如果卡号不空，则类型不能为空
	 */
	@ApiField("target_card_no_type")
	private String targetCardNoType;

	public CardUserInfo getCardUserInfo() {
		return this.cardUserInfo;
	}
	public void setCardUserInfo(CardUserInfo cardUserInfo) {
		this.cardUserInfo = cardUserInfo;
	}

	public String getExtInfo() {
		return this.extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public String getTargetCardNo() {
		return this.targetCardNo;
	}
	public void setTargetCardNo(String targetCardNo) {
		this.targetCardNo = targetCardNo;
	}

	public String getTargetCardNoType() {
		return this.targetCardNoType;
	}
	public void setTargetCardNoType(String targetCardNoType) {
		this.targetCardNoType = targetCardNoType;
	}

}
