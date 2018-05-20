package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 修改标签接口
 *
 * @author auto create
 * @since 1.0, 2016-08-10 17:27:32
 */
public class AlipayOpenPublicLabelUpdateModel extends AlipayObject {

	private static final long serialVersionUID = 3521122636879112353L;

	/**
	 * 要修改的标签id
	 */
	@ApiField("id")
	private String id;

	/**
	 * 要修改成的标签名
	 */
	@ApiField("name")
	private String name;

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
