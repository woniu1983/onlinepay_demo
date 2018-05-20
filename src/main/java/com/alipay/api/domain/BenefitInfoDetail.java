package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 权益信息
 *
 * @author auto create
 * @since 1.0, 2016-08-09 13:34:57
 */
public class BenefitInfoDetail extends AlipayObject {

	private static final long serialVersionUID = 5368676686621257859L;

	/**
	 * 面额
	 */
	@ApiField("amount")
	private String amount;

	/**
	 * 权益类型
PRE_FUND（卡面额）
DISCOUNT：折扣
COUPON：券
	 */
	@ApiField("benefit_type")
	private String benefitType;

	/**
	 * 个数
	 */
	@ApiField("count")
	private String count;

	/**
	 * 描述
	 */
	@ApiField("description")
	private String description;

	public String getAmount() {
		return this.amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBenefitType() {
		return this.benefitType;
	}
	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}

	public String getCount() {
		return this.count;
	}
	public void setCount(String count) {
		this.count = count;
	}

	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
