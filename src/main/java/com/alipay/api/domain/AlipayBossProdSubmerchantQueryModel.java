package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 商户查询接口
 *
 * @author auto create
 * @since 1.0, 2016-07-13 17:19:13
 */
public class AlipayBossProdSubmerchantQueryModel extends AlipayObject {

	private static final long serialVersionUID = 2511333396971466396L;

	/**
	 * 二级商户编号，与sub_merchant_id二选一必传
	 */
	@ApiField("external_id")
	private String externalId;

	/**
	 * 二级商户在支付宝入驻后的识别号，商户入驻后由支付宝返回，与external_id二选一必传
	 */
	@ApiField("sub_merchant_id")
	private String subMerchantId;

	public String getExternalId() {
		return this.externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getSubMerchantId() {
		return this.subMerchantId;
	}
	public void setSubMerchantId(String subMerchantId) {
		this.subMerchantId = subMerchantId;
	}

}
