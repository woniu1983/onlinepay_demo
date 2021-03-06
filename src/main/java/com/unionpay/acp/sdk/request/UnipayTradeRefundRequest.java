/** 
 * Copyright (c) 2017, WONIU All Rights Reserved. 
 * 
 */ 
package com.unionpay.acp.sdk.request;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.SDKConfig;

import cn.woniu.onlinepay.log.Logger;
import cn.woniu.onlinepay.model.UniPayOrder;

/** 
 * @ClassName: UnipayTradeRefundRequest <br/> 
 * @Description: 退货可以在原始消费发生后30日内，可以部分金额退货，可以多次退货，但累计退货金额不能超过原始消费金额。  <br/> 
 * 
 * @author WONIU 
 * @date: 2017年6月22日 下午7:26:40 <br/>
 * @version  
 * @since JDK 1.6 
 */
public class UnipayTradeRefundRequest extends AbstractUnipayRequest {

	
//	/** 
//	 * 字段： version
//	 * 说明： 版本号
//	 * 默认： 
//	 * 要求： Must */
//	private String version;
//	
//	/** 
//	 * 字段： encoding
//	 * 说明： 编码方式
//	 * 默认： UTF-8
//	 * 要求： Must */
//	private String encoding = "UTF-8";
//	
//	/** 
//	 * 字段： certId
//	 * 说明：证书ID
//	 * 默认： 
//	 * 要求： Must */
//	private String certId;
//	
//	/** 
//	 * 字段： signature
//	 * 说明：签名
//	 * 默认： 
//	 * 要求： Must */
//	private String signature;
//	
//	/** 
//	 * 字段： signMethod
//	 * 说明：签名方法(非对称签名：01（表示采用RSA签名）)
//	 * 默认： 01
//	 * 要求： Must */
//	private String signMethod = "01";
//	
//	/** 
//	 * 字段： txnType
//	 * 说明：交易类型
//	 * 默认： 04  -  退货
//	 * 要求： Must */
//	private String txnType = "04";
//	
//	/** 
//	 * 字段： txnSubType
//	 * 说明：交易子类
//	 * 默认： 00
//	 * 要求： Must */
//	private String txnSubType = "00";
//	
//	/** 
//	 * 字段： bizType
//	 * 说明：产品类型
//	 * 默认：
//	 * 要求： Must */
//	private String bizType = "000000";
	
	/** 
	 * 字段： channelType
	 * 说明：渠道类型
	 * 默认：08手机
	 * 要求： Must */
	private String channelType = "08";
	
	/** 
	 * 字段： backUrl
	 * 说明：交易通知地址(支付结果将送到本地址)
	 * 默认：
	 * 要求： Must */
	private String backUrl;
	
//	/** 
//	 * 字段： accessType
//	 * 说明：接入类型(0：普通商户直连接入 1：收单机构接入 2：平台类商户接入)
//	 * 默认：0
//	 * 要求： Must */
//	private String accessType = "0";
	
	/** 
	 * 字段： acqInsCode
	 * 说明：收单机构代码 （接入类型为收单机构接入时需上送）
	 * 默认：
	 * 要求： C */
	private String acqInsCode;
	
	/** 
	 * 字段： merCatCode
	 * 说明：商户类别 （接入类型为收单机构接入时需上送）
	 * 默认：
	 * 要求： C */
	private String merCatCode;
	
//	/** 
//	 * 字段： merId
//	 * 说明：商户代码
//	 * 默认：
//	 * 要求： Must */
//	private String merId;
	
	/** 
	 * 字段： merName
	 * 说明：商户名称 （接入类型为收单机构接入时需上送）
	 * 默认：
	 * 要求： C */
	private String merName;
	
	/** 
	 * 字段： merAbbr
	 * 说明：商户简称 （接入类型为收单机构接入时需上送）
	 * 默认：
	 * 要求： C */
	private String merAbbr;
	
	/** 
	 * 字段： subMerId
	 * 说明：二级商户代码 （商户类型为平台类商户接入时必须上送）
	 * 默认：
	 * 要求： C */
	private String subMerId;
	
	/** 
	 * 字段： subMerName
	 * 说明：二级商户全称 （商户类型为平台类商户接入时必须上送）
	 * 默认：
	 * 要求： C */
	private String subMerName;
	
	/** 
	 * 字段： subMerAbbr
	 * 说明：二级商户简称 （商户类型为平台类商户接入时必须上送）
	 * 默认：
	 * 要求： C */
	private String subMerAbbr;
	
	/** 
	 * 字段： orderId
	 * 说明：退货的订单号，由商户生成
	 * 默认：
	 * 要求： Must */
	private String orderId;
	
	/** 
	 * 字段： txnTime
	 * 说明：订单发送时间 （商户端生成表示申请二维码和一笔真实订单的订单号）
	 * 默认：
	 * 要求： Must */
	private String txnTime;
	
	/** 
	 * 字段： origQryId
	 * 说明：原始交易流水号(1.原始消费交易的queryId；2.origQryId、origOrderId + origTxnTime、二者必送其一)
	 * 默认：
	 * 要求： C */
	private String origQryId;
	
	/** 
	 * 字段： origOrderId
	 * 说明：原交易商户订单号(1.原始消费交易的queryId；2.origQryId、origOrderId + origTxnTime、二者必送其一)
	 * 默认：
	 * 要求： C */
	private String origOrderId;
	
	/** 
	 * 字段： origOrderId
	 * 说明：原交易商户发送交易时间(1.原始消费交易的queryId；2.origQryId、origOrderId + origTxnTime、二者必送其一)
	 * 默认：
	 * 要求： C */
	private String origTxnTime;
	
	/** 
	 * 字段： txnAmt
	 * 说明：交易金额 （与原始消费交易一致）
	 * 默认：
	 * 要求： Must */
	private String txnAmt;
	
	/** 
	 * 字段： currencyCode
	 * 说明：交易币种
	 * 默认：默认为156(RMB)
	 * 要求： Must */
	private String currencyCode = "156";
	
	/** 
	 * 字段： termId
	 * 说明：终端号
	 * 默认：
	 * 要求： O */
	private String termId;
	
	/** 
	 * 字段： reqReserved
	 * 说明：请求方保留域(商户自定义保留域，交易应答时会原样返回)
	 * 默认：
	 * 要求： O */
	private String reqReserved;
	
	/** 
	 * 字段： reserved
	 * 说明：保留域
	 * 默认：
	 * 要求： O */
	private String reserved;
	
	public UnipayTradeRefundRequest() {
		super();
		this.txnType = "04";
		this.txnSubType = "00";
		this.backUrl = SDKConfig.getConfig().getBackUrl();
	}

//	public String getVersion() {
//		return version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
//
//	public String getEncoding() {
//		return encoding;
//	}
//
//	public void setEncoding(String encoding) {
//		this.encoding = encoding;
//	}
//
//	public String getCertId() {
//		return certId;
//	}
//
//	public void setCertId(String certId) {
//		this.certId = certId;
//	}
//
//	public String getSignature() {
//		return signature;
//	}
//
//	public void setSignature(String signature) {
//		this.signature = signature;
//	}
//
//	public String getSignMethod() {
//		return signMethod;
//	}
//
//	public void setSignMethod(String signMethod) {
//		this.signMethod = signMethod;
//	}
//
//	public String getTxnType() {
//		return txnType;
//	}
//
//	public void setTxnType(String txnType) {
//		this.txnType = txnType;
//	}
//
//	public String getTxnSubType() {
//		return txnSubType;
//	}
//
//	public void setTxnSubType(String txnSubType) {
//		this.txnSubType = txnSubType;
//	}
//
//	public String getBizType() {
//		return bizType;
//	}
//
//	public void setBizType(String bizType) {
//		this.bizType = bizType;
//	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

//	public String getAccessType() {
//		return accessType;
//	}
//
//	public void setAccessType(String accessType) {
//		this.accessType = accessType;
//	}

	public String getAcqInsCode() {
		return acqInsCode;
	}

	public void setAcqInsCode(String acqInsCode) {
		this.acqInsCode = acqInsCode;
	}

	public String getMerCatCode() {
		return merCatCode;
	}

	public void setMerCatCode(String merCatCode) {
		this.merCatCode = merCatCode;
	}

//	public String getMerId() {
//		return merId;
//	}
//
//	public void setMerId(String merId) {
//		this.merId = merId;
//	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getMerAbbr() {
		return merAbbr;
	}

	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
	}

	public String getSubMerId() {
		return subMerId;
	}

	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}

	public String getSubMerName() {
		return subMerName;
	}

	public void setSubMerName(String subMerName) {
		this.subMerName = subMerName;
	}

	public String getSubMerAbbr() {
		return subMerAbbr;
	}

	public void setSubMerAbbr(String subMerAbbr) {
		this.subMerAbbr = subMerAbbr;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getOrigQryId() {
		return origQryId;
	}

	public void setOrigQryId(String origQryId) {
		this.origQryId = origQryId;
	}

	public String getOrigOrderId() {
		return origOrderId;
	}

	public void setOrigOrderId(String origOrderId) {
		this.origOrderId = origOrderId;
	}

	public String getOrigTxnTime() {
		return origTxnTime;
	}

	public void setOrigTxnTime(String origTxnTime) {
		this.origTxnTime = origTxnTime;
	}

	public String getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getReqReserved() {
		return reqReserved;
	}

	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
	public static UnipayTradeRefundRequest build(UniPayOrder order) {
		UnipayTradeRefundRequest request = new UnipayTradeRefundRequest();
		
		String txnTime = createCurrentTime();
		request.setTxnTime(txnTime);
		
		String refundOrderId = generateRefundOrderId();
		request.setOrderId(refundOrderId);
		
		request.setOrigOrderId(order.getOutTradeNo());
		request.setOrigTxnTime(order.getTxnTime());
		request.setTxnAmt(String.valueOf(order.getReFundFee()));
		
		return request;
	}
	
	private static String generateRefundOrderId() {
		
		long time = System.currentTimeMillis();   // 目前13位
		long random = (long)(Math.random() * 100000L); // 最大6位
		String no = "UNPR" + random + time; // GW+ 机器
		Logger.Debug("generateRefundOrderId = " + no);
		return no;
	}

	@Override
	public Map<String, String> buildRequestData() {
		Map<String, String> contentData = new HashMap<String, String>();
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		contentData.put("version", 		this.version);		//版本号 全渠道默认值
		contentData.put("encoding", 	this.encoding);		//字符集编码 可以使用UTF-8,GBK两种方式
		contentData.put("signMethod", 	this.signMethod); 	//签名方法
		contentData.put("txnType", 		this.txnType);      //交易类型 04-退货
		contentData.put("txnSubType", 	this.txnSubType);   //交易子类  默认00
		contentData.put("bizType", 		this.bizType);      //填写000000
		contentData.put("channelType", 	this.channelType);  //渠道类型 08手机

		/***商户接入参数***/
		contentData.put("merId", 		this.merId);   		//商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		contentData.put("accessType", 	this.accessType);   //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
		contentData.put("orderId", 		this.orderId);     	//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		contentData.put("txnTime", 		this.txnTime);		//订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("txnAmt", 		this.txnAmt);		//交易金额 单位为分，不能带小数点
		contentData.put("currencyCode", this.currencyCode); //境内商户固定 156 人民币
		
		contentData.put("backUrl", 		this.backUrl);
		
		/***要调通交易以下字段必须修改***/
		contentData.put("origOrderId", 		this.origOrderId);
		contentData.put("origTxnTime", 		this.origTxnTime); 
//		contentData.put("origQryId", 	this.origQryId);   			  //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		
		//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		Map<String, String> reqData = AcpService.sign(contentData, this.encoding);		
		
		return reqData;
	}
}
