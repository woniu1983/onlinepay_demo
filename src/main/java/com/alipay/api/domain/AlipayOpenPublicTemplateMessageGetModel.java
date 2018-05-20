package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 模板消息领取接口
 *
 * @author auto create
 * @since 1.0, 2016-08-10 17:26:58
 */
public class AlipayOpenPublicTemplateMessageGetModel extends AlipayObject {

	private static final long serialVersionUID = 4563162973289488458L;

	/**
	 * 消息母板id
	 */
	@ApiField("template_id")
	private String templateId;

	public String getTemplateId() {
		return this.templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

}
