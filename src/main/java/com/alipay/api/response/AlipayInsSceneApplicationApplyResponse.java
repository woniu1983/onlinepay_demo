package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.ins.scene.application.apply response.
 * 
 * @author auto create
 * @since 1.0, 2016-07-22 13:36:41
 */
public class AlipayInsSceneApplicationApplyResponse extends AlipayResponse {

	private static final long serialVersionUID = 2479512871868932339L;

	/** 
	 * 交易操作流水号;收银台付款需要
	 */
	@ApiField("operation_id")
	private String operationId;

	/** 
	 * 商户生成的外部投保业务号,必须保证唯一
	 */
	@ApiField("out_biz_no")
	private String outBizNo;

	/** 
	 * 支付交易订单号,收银台付款需要
	 */
	@ApiField("trade_no")
	private String tradeNo;

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getOperationId( ) {
		return this.operationId;
	}

	public void setOutBizNo(String outBizNo) {
		this.outBizNo = outBizNo;
	}
	public String getOutBizNo( ) {
		return this.outBizNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeNo( ) {
		return this.tradeNo;
	}

}
