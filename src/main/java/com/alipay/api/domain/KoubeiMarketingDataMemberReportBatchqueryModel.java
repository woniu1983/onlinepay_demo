package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 会员报表批量查询接口
 *
 * @author auto create
 * @since 1.0, 2016-05-20 16:31:28
 */
public class KoubeiMarketingDataMemberReportBatchqueryModel extends AlipayObject {

	private static final long serialVersionUID = 5551161815647636797L;

	/**
	 * 查询报表数据的业务日期，精确到天，格式为yyyymmdd，数据按天返回
	 */
	@ApiField("biz_date")
	private String bizDate;

	public String getBizDate() {
		return this.bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

}
