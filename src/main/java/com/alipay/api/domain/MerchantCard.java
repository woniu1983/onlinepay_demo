package com.alipay.api.domain;

import java.util.Date;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 商户卡信息
 *
 * @author auto create
 * @since 1.0, 2016-08-22 10:33:05
 */
public class MerchantCard extends AlipayObject {

	private static final long serialVersionUID = 4235317891662779337L;

	/**
	 * 资金卡余额，单位：元，精确到小数点后两位。
	 */
	@ApiField("balance")
	private String balance;

	/**
	 * 支付宝业务卡号
	 */
	@ApiField("biz_card_no")
	private String bizCardNo;

	/**
	 * 商户卡ID
	 */
	@ApiField("external_card_no")
	private String externalCardNo;

	/**
	 * 会员卡等级，由商户自定义。
	 */
	@ApiField("level")
	private String level;

	/**
	 * 会员卡开卡时间，格式为yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("open_date")
	private Date openDate;

	/**
	 * 会员卡积分。
	 */
	@ApiField("point")
	private String point;

	/**
	 * 有效期
	 */
	@ApiField("valid_date")
	private String validDate;

	public String getBalance() {
		return this.balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBizCardNo() {
		return this.bizCardNo;
	}
	public void setBizCardNo(String bizCardNo) {
		this.bizCardNo = bizCardNo;
	}

	public String getExternalCardNo() {
		return this.externalCardNo;
	}
	public void setExternalCardNo(String externalCardNo) {
		this.externalCardNo = externalCardNo;
	}

	public String getLevel() {
		return this.level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

	public Date getOpenDate() {
		return this.openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getPoint() {
		return this.point;
	}
	public void setPoint(String point) {
		this.point = point;
	}

	public String getValidDate() {
		return this.validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

}
