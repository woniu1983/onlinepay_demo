package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 零售商信息
 *
 * @author auto create
 * @since 1.0, 2016-08-15 12:15:24
 */
public class Retailer extends AlipayObject {

	private static final long serialVersionUID = 8658514386627539336L;

	/**
	 * 零售商名称
	 */
	@ApiField("name")
	private String name;

	/**
	 * 零售商pid
	 */
	@ApiField("partner_id")
	private String partnerId;

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPartnerId() {
		return this.partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

}
