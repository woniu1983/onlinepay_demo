package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 商户创建活动后，需营销核心平台，来发奖。
 *
 * @author auto create
 * @since 1.0, 2016-03-30 14:26:34
 */
public class MarketingCampaignActivityOfflineModel extends AlipayObject {

	private static final long serialVersionUID = 3325523681138153256L;

	/**
	 * 活动id
	 */
	@ApiField("camp_id")
	private String campId;

	/**
	 * 用户id
	 */
	@ApiField("user_id")
	private String userId;

	public String getCampId() {
		return this.campId;
	}
	public void setCampId(String campId) {
		this.campId = campId;
	}

	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
