package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: koubei.marketing.campaign.activity.create response.
 * 
 * @author auto create
 * @since 1.0, 2016-08-22 17:47:56
 */
public class KoubeiMarketingCampaignActivityCreateResponse extends AlipayResponse {

	private static final long serialVersionUID = 2415161767717712672L;

	/** 
	 * 活动审批状态，
仅限服务商代商户创建活动时返回
AUDITING，审核中
	 */
	@ApiField("audit_status")
	private String auditStatus;

	/** 
	 * 活动ID
	 */
	@ApiField("camp_id")
	private String campId;

	/** 
	 * 活动状态，目前返回以下状态，
STARTING，活动启动中
STARTED，活动已启动
	 */
	@ApiField("camp_status")
	private String campStatus;

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditStatus( ) {
		return this.auditStatus;
	}

	public void setCampId(String campId) {
		this.campId = campId;
	}
	public String getCampId( ) {
		return this.campId;
	}

	public void setCampStatus(String campStatus) {
		this.campStatus = campStatus;
	}
	public String getCampStatus( ) {
		return this.campStatus;
	}

}
