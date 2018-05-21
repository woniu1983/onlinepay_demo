/**
 * 
 */
package cn.woniu.onlinepay.model;

import org.apache.commons.lang.StringUtils;

/**
 * @author Ryan
 *
 */
public class PayProfile {
	
	public String name = "";
	public long id = 0L;
	// 微信
	
	/** 微信支付私有key */
	public String wp_private_key;
	/** 公众号ID */
	public String wp_pid;
	/** 商户号ID */
	public String wp_bid;
	/** 子商户号ID */
	public String wp_sub_bid;
	/** 商户号MCHID */
	public String wp_mchid;
	/** 支付证书 */
	public String wp_cert_path;
	
	// 支付宝
	
	/** 支付宝账号 */
	public String ap_account;
	/** PID */
	public String ap_pid;
	/** APPID */
	public String ap_appid;
	/** 商户私钥（Java） */
	public String ap_private_pkcs8;
	/** 商户公钥 */
	public String ap_public_key;
	/** 支付宝公钥 */
	public String ap_myjf_public_key;
	/** 密钥长度 1024/2048 */
	public int ap_key_len = 2048;
	
	
	public PayProfile(String name) {
		this.name = name;
		this.id  = (long) (Math.random() * Math.pow(10, 7));
	}
	
	
	
	public boolean isAliPayAva() {
		if (StringUtils.isEmpty(ap_account)
				|| StringUtils.isEmpty(ap_pid)
				|| StringUtils.isEmpty(ap_appid)
				|| StringUtils.isEmpty(ap_private_pkcs8)
				|| StringUtils.isEmpty(ap_public_key)
				|| StringUtils.isEmpty(ap_myjf_public_key)) {
			return false;
		}
		return true;
	}
	
	public boolean isWechatPayAva() {
		if (StringUtils.isEmpty(wp_private_key)
				|| StringUtils.isEmpty(wp_pid)
				|| StringUtils.isEmpty(wp_bid)
				|| StringUtils.isEmpty(wp_sub_bid)
				|| StringUtils.isEmpty(wp_mchid)) {
			return false;
		}
		return true;
	}
	
	

}
