package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 环境图删除接口
 *
 * @author auto create
 * @since 1.0, 2016-08-11 20:18:05
 */
public class KoubeiMarketingCdpShopenvDelModel extends AlipayObject {

	private static final long serialVersionUID = 1636551346271431216L;

	/**
	 * 店铺图片id
	 */
	@ApiField("shop_pic_id")
	private String shopPicId;

	public String getShopPicId() {
		return this.shopPicId;
	}
	public void setShopPicId(String shopPicId) {
		this.shopPicId = shopPicId;
	}

}
