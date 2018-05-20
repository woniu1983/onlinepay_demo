package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 自定义标签删除接口
 *
 * @author auto create
 * @since 1.0, 2016-08-19 11:38:52
 */
public class KoubeiMarketingDataCustomtagDeleteModel extends AlipayObject {

	private static final long serialVersionUID = 3677314635418927637L;

	/**
	 * 标签code，创建自定义标签时返回的
	 */
	@ApiField("tag_code")
	private String tagCode;

	public String getTagCode() {
		return this.tagCode;
	}
	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}

}
