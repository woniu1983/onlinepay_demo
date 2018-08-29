/** 
 * Copyright (c) 2017, WONIU All Rights Reserved. 
 * 
 */ 
package com.unionpay.acp.business;

import java.util.Map;

import com.alipay.trade.utils.Utils;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.request.UnipayTradeCancelRequest;
import com.unionpay.acp.sdk.request.UnipayTradePrecreateRequest;
import com.unionpay.acp.sdk.request.UnipayTradeQueryRequest;
import com.unionpay.acp.sdk.request.UnipayTradeRefundRequest;

import cn.woniu.onlinepay.PayHelper;
import cn.woniu.onlinepay.log.Logger;


/** 
 * @ClassName: UnipayBusiness <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author woniu 
 * @date: 2017年6月22日 下午7:55:11 <br/>
 * @version  
 * @since JDK 1.6 
 */
public class UnipayBusiness {

	//每次调用订单查询API时的等待时间，因为当出现支付失败的时候，如果马上发起查询不一定就能查到结果，所以这里建议先等待一定时间再发起查询
	private int waitingTimeBeforePayQueryServiceInvoked = 5000;

	//循环调用订单查询API的次数
	private int payQueryLoopInvokedCount = 60 * 6;

	//每次调用撤销API的等待时间
	private int waitingTimeBeforeReverseServiceInvoked = 5000;

	public interface ResultListener {
		//下单成功，返回QR-Code URL信息
		void onPreOrderSuccess(String qrCode);

		//下单失败
		void onPreOrderFail();

		// 支付成功
		void onPaySuccess(String orderId);

		// 支付失败
		void onPayFail(String orderId);
	}


	public void doOrder(UnipayTradePrecreateRequest request, ResultListener resultListener) {


		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		Map<String, String> reqData = request.buildRequestData();

		String requestAppUrl = SDKConfig.getConfig().getBackRequestUrl();								 //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		Map<String, String> rspData = AcpService.post(reqData, requestAppUrl, request.getEncoding());  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		String qrCode = null;
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, request.getEncoding())){
				Logger.Debug("###<UnipayBusiness>Valide resp Data sign success.");
				String respCode = rspData.get("respCode") ;

				if(("00").equals(respCode)){
					// 成功,获取qrcode号
					qrCode = rspData.get("qrCode") ;
					Logger.Info("###<UnipayBusiness>[doOrder] qrCode=" + qrCode);
				}else{
					//其他应答码为失败请排查原因或做失败处理
					//TODO
					Logger.Info("!!!<UnipayBusiness>Request Fail----respCode=" + respCode);
				}
			}else{
				Logger.Error("!!!<UnipayBusiness>Valide resp Data sign Fail.");
				//TODO 检查验证签名失败的原因
			}
		} else{
			//未返回正确的http状态
			Logger.Error("!!!<UnipayBusiness>No Response or http is not 200.");
		}

		if (resultListener != null) {
			if (qrCode == null) {
				resultListener.onPreOrderFail();
			} else {
				//--------------------------------------------------------------------
				//1)预下单成功
				//--------------------------------------------------------------------
				resultListener.onPreOrderSuccess(qrCode);

				//--------------------------------------------------------------------
				//2)开始查询订单支付结果
				//--------------------------------------------------------------------
				UnipayTradeQueryRequest queryReq = new UnipayTradeQueryRequest();
				queryReq.setTxnTime(request.getTxnTime());
				queryReq.setOrderId(request.getOrderId());

				if (doPayQueryLoop(payQueryLoopInvokedCount, queryReq)) {
					Logger.Info("----------------Unipay Success");
					resultListener.onPaySuccess(request.getOrderId());
				} else {
					Logger.Error("----------------Unipay Fail or Closed");
					resultListener.onPayFail(request.getOrderId());
				}
			}
		}

	}



	/**
	 * 由于有的时候是因为服务延时，所以需要商户每隔一段时间（建议5秒）后再进行查询操作，多试几次（建议3次）
	 *
	 * @param loopCount     循环次数，至少一次
	 * @param request    	UnipayTradeQueryRequest
	 * @return 该订单是否支付成功
	 * @throws InterruptedException
	 */
	private boolean doPayQueryLoop(int loopCount, UnipayTradeQueryRequest request){
		//至少查询一次
		if (loopCount == 0) {
			loopCount = 1;
		}
		boolean payResult = false;
		boolean stopLoop = false;
		for (int i = 0; i < loopCount && (!stopLoop); i++) {   
			//等待一定时间再进行查询，避免状态还没来得及被更新
			if (i == 0) {
				// 第一次间隔5秒开始查询
				Utils.sleep(waitingTimeBeforePayQueryServiceInvoked);
			} else {
				// 之后间隔3秒查询
				Utils.sleep(3000);
			}

			String orderId = request.getOrderId();
			UnipayTradeStatus status = doOnePayQuery(request);

			Logger.Debug("<UnipayBusiness>#doPayQueryLoop -- TradeStatus=" + status + " orderId=" + orderId);
			Logger.Debug("<UnipayBusiness>#doPayQueryLoop -- PayHelper.getOutTradeNo()=" + PayHelper.getOutTradeNo());
			switch (status) {
			case SUCCESS:
				Logger.Info("<UnipayBusiness>#doPayQueryLoop--Pay Success.");
				stopLoop = true;
				payResult = true;
				break;

			case FAILED:
				Logger.Error("<UnipayBusiness>#doPayQueryLoop--Payment is Failed or Closed!!!");
				break;

			case UNKNOWN:
				Logger.Error("<UnipayBusiness>#doPayQueryLoop--System exception, Payment state is unknown!!!");
				break;

			default:
				Logger.Error("<UnipayBusiness>#doPayQueryLoop--Unsupport trade state, return exception!!!");
				break;
			}
			
			if (!orderId.equals(PayHelper.getOutTradeNo())) {
				// 当前outTradeNo和参数查询的outTradeNo不一致，则可以停止循环查询
				stopLoop = true;
			}

		}

		return payResult;
	}


	/**
	 * 进行一次支付订单查询操作
	 *
	 * @param orderId    商户系统内部的订单号[确保在商户系统唯一]
	 * @return UnipayTradeStatus 订单支付结果
	 */
	public UnipayTradeStatus doOnePayQuery(UnipayTradeQueryRequest request){
		UnipayTradeStatus status = UnipayTradeStatus.UNKNOWN;

		Map<String, String> reqData = request.buildRequestData();

		String url = SDKConfig.getConfig().getSingleQueryUrl();								//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
		Map<String, String> rspData = AcpService.post(reqData, url, request.getEncoding()); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, request.getEncoding())){
				Logger.Debug("<UnipayBusiness>Validate Signature Success.");
				if(("00").equals(rspData.get("respCode"))){
					//如果查询交易成功
					String origRespCode = rspData.get("origRespCode");
					if(("00").equals(origRespCode)){
						//交易成功，更新商户订单状态
						//TODO
						status = UnipayTradeStatus.SUCCESS;
						Logger.Info("###<UnipayBusiness>Order is success paid.");
					}else if(("03").equals(origRespCode)||
							("04").equals(origRespCode)||
							("05").equals(origRespCode)){
						//订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】
						//TODO
						status = UnipayTradeStatus.UNKNOWN;
						Logger.Error("!!!<UnipayBusiness>Order is processing or unknown, please wait and query.");
					}else{
						//其他应答码为交易失败
						//TODO
						status = UnipayTradeStatus.FAILED;
					}
				} else if(("34").equals(rspData.get("respCode"))){
					//订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准
					Logger.Error("!!!<UnipayBusiness>Order is not exist, Trade is unknown, please wait and query.");
					status = UnipayTradeStatus.UNKNOWN;

				} else{//查询交易本身失败，如应答码10/11检查查询报文是否正确
					//TODO
					Logger.Error("!!!<UnipayBusiness>Query fail, such as 10/11 code, please check your request body.");
					status = UnipayTradeStatus.UNKNOWN;
				}
			} else{
				Logger.Error("!!!<UnipayBusiness>Valide resp Data sign Fail.");
				//TODO 检查验证签名失败的原因
				status = UnipayTradeStatus.UNKNOWN;
			}
		} else{
			//未返回正确的http状态
			Logger.Error("!!!<UnipayBusiness>No Response or http is not 200.");
			status = UnipayTradeStatus.UNKNOWN;
		}

		return status;
	}

	/**
	 * 进行一次撤销操作
	 *
	 * @param orderId    商户系统内部的订单号[确保在商户系统唯一]
	 */
	public void doOneReverse(UnipayTradeCancelRequest request){
		
		Logger.Info("<UnipayBusiness>#doOneReverse-- orderId=" + request.getOrigOrderId());

	}
	
	/**
	 * 退费
	 * @Title: doRefund  
	 * @Description: TODO  
	 *
	 * @param request
	 * @param resultListener
	 */
	public void doRefund(UnipayTradeRefundRequest request, RefundResultListener listener) {
		
		Logger.Info("<UnipayBusiness>#doRefund-- orderId=" + request.getOrigOrderId());
		Map<String, String> reqData = request.buildRequestData();
		String url = SDKConfig.getConfig().getBackRequestUrl();						     //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		Map<String,String> rspData = AcpService.post(reqData, url, request.getEncoding()); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, request.getEncoding())){
				Logger.Debug("###<UnipayBusiness> Validate Signature Success");
				String respCode = rspData.get("respCode") ;
				Logger.Info("###<UnipayBusiness> respCode=" + respCode);
				
				if(("00").equals(respCode)){
					//交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
					//TODO
					Logger.Info("###<UnipayBusiness> Refund request is accepted(Not means refund success)");
					if (listener != null) {
						listener.onRefundSuccess();
					}
				}else if(("03").equals(respCode)||
						 ("04").equals(respCode)||
						 ("05").equals(respCode)){
					//后续需发起交易状态查询交易确定交易状态
					//TODO
					Logger.Info("!!!<UnipayBusiness> Need check refund state.");
				}else{
					//其他应答码为失败请排查原因
					//TODO
					Logger.Info("!!!<UnipayBusiness> Other state code: Fail.");
				}
			}else{
				Logger.Error("!!!<UnipayBusiness> Validate Signature Failed");
				//TODO 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			Logger.Error("!!!<UnipayBusiness>No Response or http is not 200.");
		}
	}
	
	public interface RefundResultListener {

		// 退费失败
		void onRefundFail();

		// 退费成功
		void onRefundSuccess();

	}

}
