package com.alipay.api.domain;

import java.util.List;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;
import com.alipay.api.internal.mapping.ApiListField;

/**
 * 菜单创建接口
 *
 * @author auto create
 * @since 1.0, 2016-08-11 20:17:45
 */
public class KoubeiMarketingCdpMenuCreateModel extends AlipayObject {

	private static final long serialVersionUID = 2598926782758511961L;

	/**
	 * 封面
	 */
	@ApiField("cover")
	private Picture cover;

	/**
	 * 菜品ids列表
	 */
	@ApiListField("dishes")
	@ApiField("string")
	private List<String> dishes;

	/**
	 * 店铺图片适用的门店列表，最多支持500，可通过接口alipay.offline.market.shop.summary.batchquery来获取商户下的门店列表，请求参数query_type需要设置为CONTENT_RELATION
	 */
	@ApiListField("shop_ids")
	@ApiField("string")
	private List<String> shopIds;

	/**
	 * 菜单标题名称，最多12个字
	 */
	@ApiField("title")
	private String title;

	public Picture getCover() {
		return this.cover;
	}
	public void setCover(Picture cover) {
		this.cover = cover;
	}

	public List<String> getDishes() {
		return this.dishes;
	}
	public void setDishes(List<String> dishes) {
		this.dishes = dishes;
	}

	public List<String> getShopIds() {
		return this.shopIds;
	}
	public void setShopIds(List<String> shopIds) {
		this.shopIds = shopIds;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
