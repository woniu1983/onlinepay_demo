package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: koubei.marketing.data.customtag.save response.
 * 
 * @author auto create
 * @since 1.0, 2016-08-19 11:39:02
 */
public class KoubeiMarketingDataCustomtagSaveResponse extends AlipayResponse {

	private static final long serialVersionUID = 8515528194251697128L;

	/** 
	 * 自定义标签CODE
	 */
	@ApiField("tag_code")
	private String tagCode;

	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}
	public String getTagCode( ) {
		return this.tagCode;
	}

}
