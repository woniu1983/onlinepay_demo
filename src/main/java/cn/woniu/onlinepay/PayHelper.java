/**   
 * @Title: PayHelper.java 
 * @Package cn.com.rits.app.yunprint.communication 
 * @Description: TODO 
 * @author Woniu  
 * @date 2016年8月25日 下午1:35:07 
 * @version V1.0   
 */
package cn.woniu.onlinepay;

import com.alipay.trade.config.Configs;
import com.tencent.common.Configure;

import cn.woniu.onlinepay.log.Logger;
import cn.woniu.onlinepay.model.PayProfile;

/** 
 * @ClassName: PayHelper 
 * @Description: 支付工具类 
 * @author Woniu
 * @date 2016年8月25日 下午1:35:07 
 *  
 */
public class PayHelper {

	// 静态变量： 存放当前的OutTradeNo(商户订单号)
	// 目的： 当新创建一个订单时，前一个订单付费查询操作停止(比较订单号)
	// 操作： 在创建订单前，设置sOutTradeNo
	private static String sOutTradeNo = "";

	/*
	 * 初始化支付参数，从server拿到当前运营商的支付信息
	 */
	public static void initPayConfig(PayProfile profile) {
		boolean result = false;
		
		// 微信支付信息
		Configure.setPayAvailable(profile.isWechatPayAva());//微信支付是否支持

		Configure.setKey(profile.wp_private_key);
		Configure.setAppID(profile.wp_pid);
		Configure.setMchID(profile.wp_mchid);
		Configure.setSubMchID(profile.wp_sub_bid);
		Configure.setCertPassword(profile.wp_mchid);
		Configure.setCertLocalPath(profile.wp_cert_path);
//		Configure.setNOTIFY_URL("http://" + MFPConfig.getInstance().getServerIP() + "/getWeChatPayInfo");//2017-07-17 支付回调

		Logger.Debug("###<PayHelper>initPayConfig-- WechatPay  is =" + profile.isWechatPayAva()
				+ " APPID=" + profile.wp_bid);

		// 支付宝支付信息
		Configs.setPayAvailable(profile.isAliPayAva());

		Configs.setPid(profile.ap_pid); //PID
		Configs.setSellerId(profile.ap_pid);//SellerID==PID
		Configs.setAppid(profile.ap_appid);
		Configs.setPrivateKey(profile.ap_private_pkcs8);
		Configs.setPublicKey(profile.ap_public_key);
		Configs.setAlipayPublicKey(profile.ap_myjf_public_key);
//		Configs.setNOTIFY_URL("http://" + MFPConfig.getInstance().getServerIP() + "/getAliPayInfo");//2017-07-17 支付回调

		Logger.Debug("<PayHelper>initPayConfig-- AliPay is " + profile.isAliPayAva() 
				+ "  Account= " + profile.ap_pid
				+ "  APPID=" + profile.ap_appid);


		if (Configure.isPayAvailable() || Configs.isPayAvailable()) {
			// 任一种支付可用即为可行
			result = true;
		} else {
			Logger.Error("<PayHelper> Both AliPay and WechatPay is disabled.");
		}

	}


	public static void setOutTradeNo(String outTradeNo) {
		sOutTradeNo = outTradeNo;
	}

	public static String getOutTradeNo() {
		return sOutTradeNo;
	}

}
