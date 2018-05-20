/** 
 * Copyright (c) 2017, RITS All Rights Reserved. 
 * 
 */ 
package com.unionpay.acp.sdk.request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/** 
 * @ClassName: AbstractUnipayRequest <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author maojianghui 
 * @date: 2017年6月22日 下午7:58:32 <br/>
 * @version  
 * @since JDK 1.6 
 */
public abstract class AbstractUnipayRequest {
	
	/** 
	 * 字段： version
	 * 说明： 版本号
	 * 默认： 
	 * 要求： Must */
	protected String version;
	
	/** 
	 * 字段： encoding
	 * 说明： 编码方式
	 * 默认： UTF-8
	 * 要求： Must */
	protected String encoding = "UTF-8";
	
	/** 
	 * 字段： certId
	 * 说明：证书ID
	 * 默认： 
	 * 要求： Must */
	protected String certId;
	
	/** 
	 * 字段： signature
	 * 说明：签名 （填写对报文摘要的签名）
	 * 默认： 
	 * 要求： Must */
	protected String signature;
	
	/** 
	 * 字段： signMethod
	 * 说明：签名方法(非对称签名：01（表示采用RSA签名）)
	 * 默认： 01
	 * 要求： Must */
	protected String signMethod = "01";
	
	/** 
	 * 字段： txnType
	 * 说明：交易类型
	 * 默认： 
	 * 要求： Must */
	protected String txnType;
	
	/** 
	 * 字段： txnSubType
	 * 说明：交易子类
	 * 默认：
	 * 要求： Must */
	protected String txnSubType;
	
	/** 
	 * 字段： bizType
	 * 说明：产品类型
	 * 默认：
	 * 要求： Must */
	protected String bizType = "000000";
	
	/** 
	 * 字段： accessType
	 * 说明：接入类型(0：普通商户直连接入 1：收单机构接入 2：平台类商户接入)
	 * 默认：0
	 * 要求： Must */
	protected String accessType = "0";
	
	/** 
	 * 字段： merId
	 * 说明：商户代码
	 * 默认：
	 * 要求： Must */
	protected String merId;
	
	public AbstractUnipayRequest(){
		this.merId = SDKConfig.getConfig().getMerId();
		this.version = SDKConfig.getConfig().getVersion();
	}
	
	
	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getEncoding() {
		return encoding;
	}


	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	public String getCertId() {
		return certId;
	}


	public void setCertId(String certId) {
		this.certId = certId;
	}


	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}


	public String getSignMethod() {
		return signMethod;
	}


	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}


	public String getTxnType() {
		return txnType;
	}


	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}


	public String getTxnSubType() {
		return txnSubType;
	}


	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}


	public String getBizType() {
		return bizType;
	}


	public void setBizType(String bizType) {
		this.bizType = bizType;
	}


	public String getAccessType() {
		return accessType;
	}


	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}


	public String getMerId() {
		return merId;
	}


	public void setMerId(String merId) {
		this.merId = merId;
	}

	/**
	 * 
	 * @Title: buildRequestData  
	 * @Description: 创建请求Data  
	 *
	 * @return Map<String, String>
	 */
	public abstract Map<String, String> buildRequestData();
	
	// 商户发送交易时间 格式:YYYYMMDDhhmmss
	public static String createCurrentTime() {
		String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return time;
	}
	
	// AN8..40 商户订单号，不能含"-"或"_"
	public static String createOrderId() {
		String orderId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		return orderId;
	}

}
