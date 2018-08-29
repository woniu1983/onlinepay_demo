/** 
 * Copyright (c) 2017, WONIU All Rights Reserved. 
 * 
 */ 
package com.unionpay.acp.sdk.request;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.AcpService;

import cn.woniu.onlinepay.model.UniPayOrder;

/** 
 * @ClassName: UnipayTradeQueryRequest <br/> 
 * @Description: 交易状态查询交易  <br/> 
 * 
 * @author WONIU 
 * @date: 2017年6月22日 下午4:57:08 <br/>
 * @version  
 * @since JDK 1.6 
 */
public class UnipayTradeQueryRequest extends AbstractUnipayRequest {
	
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
//	 * 默认： 00
//	 * 要求： Must */
//	private String txnType = "00";
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
	
//	/** 
//	 * 字段： merId
//	 * 说明：商户代码
//	 * 默认：
//	 * 要求： Must */
//	private String merId;
	
	/** 
	 * 字段： queryId
	 * 说明：交易查询流水号(交易子类为流水号查询时必填)
	 * 默认：
	 * 要求： C */
	private String queryId;
	
	/** 
	 * 字段： orderId
	 * 说明：商户订单号(同被查询交易orderId + txnTime 、queryId二者必送其一)
	 * 默认：
	 * 要求： C */
	private String orderId;
	
	/** 
	 * 字段： txnTime
	 * 说明：订单发送时间(同被查询交易orderId + txnTime 、queryId二者必送其一)
	 * 默认：
	 * 要求： C */
	private String txnTime;
	
	/** 
	 * 字段： reserved
	 * 说明：保留域
	 * 默认：
	 * 要求： O */
	private String reserved;
	
	public UnipayTradeQueryRequest() {
		super();
		this.txnType = "00";
		this.txnSubType = "00";
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
//
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

//	public String getMerId() {
//		return merId;
//	}
//
//	public void setMerId(String merId) {
//		this.merId = merId;
//	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
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

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
	public static UnipayTradeQueryRequest build(UniPayOrder order) {
		UnipayTradeQueryRequest request = new UnipayTradeQueryRequest();
		
		request.setTxnTime(order.getTxnTime());
		request.setOrderId(order.getOutTradeNo());
		
		return request;
	}
	
	@Override
	public Map<String, String> buildRequestData() {
		Map<String, String> contentData = new HashMap<String, String>();
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		contentData.put("version", 		this.version);		//版本号 全渠道默认值
		contentData.put("encoding", 	this.encoding);		//字符集编码 可以使用UTF-8,GBK两种方式
		contentData.put("signMethod", 	this.signMethod); 	//签名方法
		contentData.put("txnType", 		this.txnType);      //交易类型  默认00
		contentData.put("txnSubType", 	this.txnSubType);   //交易子类  默认00
		contentData.put("bizType", 		this.bizType);      //填写000000

		/***商户接入参数***/
		contentData.put("merId", 		this.merId);   		//商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		contentData.put("accessType", 	this.accessType);   //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
		contentData.put("orderId", 		this.orderId);     	//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		contentData.put("txnTime", 		this.txnTime);		//订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		
		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		Map<String, String> reqData = AcpService.sign(contentData, this.encoding);		
		
		return reqData;
	}

}
