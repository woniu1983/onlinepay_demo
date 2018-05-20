package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 获取好友列表信息
 *
 * @author auto create
 * @since 1.0, 2016-07-27 16:19:49
 */
public class AlipaySocialBaseRelationFriendsGetModel extends AlipayObject {

	private static final long serialVersionUID = 6827492442212772593L;

	/**
	 * 获取类型。1=获取双向好友   2=获取双向+单向好友
	 */
	@ApiField("get_type")
	private Long getType;

	/**
	 * 好友列表中是否返回自己， true=返回   false=不返回    默认false
	 */
	@ApiField("include_self")
	private Boolean includeSelf;

	public Long getGetType() {
		return this.getType;
	}
	public void setGetType(Long getType) {
		this.getType = getType;
	}

	public Boolean getIncludeSelf() {
		return this.includeSelf;
	}
	public void setIncludeSelf(Boolean includeSelf) {
		this.includeSelf = includeSelf;
	}

}
