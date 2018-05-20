package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 优惠活动白名单设置
 *
 * @author auto create
 * @since 1.0, 2016-06-27 12:58:41
 */
public class AlipayMarketingCampaignWhitelistUpdateModel extends AlipayObject {

	private static final long serialVersionUID = 8525264228892198334L;

	/**
	 * 活动id
	 */
	@ApiField("camp_id")
	private String campId;

	/**
	 * 白名单。逗号分隔开
	 */
	@ApiField("user_white_list")
	private String userWhiteList;

	public String getCampId() {
		return this.campId;
	}
	public void setCampId(String campId) {
		this.campId = campId;
	}

	public String getUserWhiteList() {
		return this.userWhiteList;
	}
	public void setUserWhiteList(String userWhiteList) {
		this.userWhiteList = userWhiteList;
	}

}
