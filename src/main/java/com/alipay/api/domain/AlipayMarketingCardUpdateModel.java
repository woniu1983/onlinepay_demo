package com.alipay.api.domain;

import java.util.Date;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 会员卡更新
 *
 * @author auto create
 * @since 1.0, 2016-08-09 13:34:18
 */
public class AlipayMarketingCardUpdateModel extends AlipayObject {

	private static final long serialVersionUID = 8511199762152268589L;

	/**
	 * 卡信息
	 */
	@ApiField("card_info")
	private MerchantCard cardInfo;

	/**
	 * 扩展信息
	 */
	@ApiField("ext_info")
	private String extInfo;

	/**
	 * 标识业务发生的时间
	 */
	@ApiField("occur_time")
	private Date occurTime;

	/**
	 * 处理的卡号
	 */
	@ApiField("target_card_no")
	private String targetCardNo;

	/**
	 * 卡号ID类型
EXTERNAL_CARD：商户卡号
BIZ_CARD：支付宝卡号
	 */
	@ApiField("target_card_no_type")
	private String targetCardNoType;

	public MerchantCard getCardInfo() {
		return this.cardInfo;
	}
	public void setCardInfo(MerchantCard cardInfo) {
		this.cardInfo = cardInfo;
	}

	public String getExtInfo() {
		return this.extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public Date getOccurTime() {
		return this.occurTime;
	}
	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
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
