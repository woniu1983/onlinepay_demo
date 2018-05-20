package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 品牌故事下架接口
 *
 * @author auto create
 * @since 1.0, 2016-08-11 20:18:25
 */
public class KoubeiMarketingCdpBrandstoryOffshelfModel extends AlipayObject {

	private static final long serialVersionUID = 1288761353659957929L;

	/**
	 * 需要下架的品牌故事id
	 */
	@ApiField("brand_story_id")
	private String brandStoryId;

	public String getBrandStoryId() {
		return this.brandStoryId;
	}
	public void setBrandStoryId(String brandStoryId) {
		this.brandStoryId = brandStoryId;
	}

}
