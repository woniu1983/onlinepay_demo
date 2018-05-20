/**   
 * @Title: AliPayBusiness.java 
 * @Package com.tencent.business 
 * @Description: TODO 
 * @author Jianghui Mao   
 * @date 2016年8月25日 下午4:56:03 
 * @version V1.0   
 */
package com.alipay.trade.business;

import org.apache.commons.lang.StringUtils;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.trade.config.Constants;
import com.alipay.trade.model.TradeStatus;
import com.alipay.trade.model.builder.AlipayTradeCancelRequestBuilder;
import com.alipay.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.trade.model.result.AlipayF2FQueryResult;
import com.alipay.trade.model.result.AlipayF2FRefundResult;
import com.alipay.trade.service.AlipayTradeService;
import com.alipay.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.trade.utils.Utils;

import cn.woniu.onlinepay.PayHelper;
import cn.woniu.onlinepay.log.Logger;

/** 
 * @ClassName: AliPayBusiness 
 * @Description: TODO 
 * @author Jianghui Mao 
 * @date 2016年8月25日 下午4:56:03 
 *  
 */
public class AliPayBusiness {

	AlipayTradeService tradeService;

	private PaymentState paymentState = PaymentState.NOTPAY;

	//每次调用订单查询API时的等待时间，因为当出现支付失败的时候，如果马上发起查询不一定就能查到结果，所以这里建议先等待一定时间再发起查询
	private int waitingTimeBeforePayQueryServiceInvoked = 5000;

	//循环调用订单查询API的次数
	private int payQueryLoopInvokedCount = 60 * 6;

	//每次调用撤销API的等待时间
	private int waitingTimeBeforeReverseServiceInvoked = 5000;

	public AliPayBusiness() {
		tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
	}

	public interface ResultListener {
		//下单成功，返回QR-Code URL信息
		void onPreOrderSuccess(AlipayTradePrecreateResponse response);

		//下单失败
		void onPreOrderFail(AlipayTradePrecreateResponse response);

		// 支付成功
		void onPaySuccess(String outTradeNo);

		// 支付失败
		void onPayFail(String outTradeNo);
	}

	public enum PaymentState {
		NOTPAY,			// 未支付
		USERPAYING,		// 支付中
		SUCCESS,		// 支付成功
		FAIL,			// 支付失败
		USERCANCEL,		// 用户取消订单

	}

	/**
	 * 
	 * @Title: order 
	 * @Description: 执行逻辑： 下单--生成二维码--循环监听扫码支付结果
	 * @param request   AlipayTradePrecreateRequestBuilder
	 * @param resultListener  ResultListener
	 * @throws Exception     
	 * @return void
	 */
	public void doOrder(AlipayTradePrecreateRequestBuilder request, ResultListener resultListener) {

		paymentState = PaymentState.NOTPAY; //初始化支付状态

		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(request);
		TradeStatus status = result.getTradeStatus();
		if (status == TradeStatus.FAILED) {
			// 第一次失败，再次尝试
			Logger.Debug("<AliPayHelper>#initPay--tradePrecreate Again.");
			result = tradeService.tradePrecreate(request);
			status = result.getTradeStatus();
		}
		
		AlipayTradePrecreateResponse response = result.getResponse();
		dumpResponse(response);

		switch (status) {
		case SUCCESS:
			Logger.Info("<AliPayHelper>#initPay--AliPay order success.");

			if (response != null) {
				//--------------------------------------------------------------------
				//1)预下单成功
				//--------------------------------------------------------------------
				if (resultListener != null) {
					resultListener.onPreOrderSuccess(response);
				}

				//--------------------------------------------------------------------
				//2)开始查询订单支付结果
				//--------------------------------------------------------------------
				if (doPayQueryLoop(payQueryLoopInvokedCount, request.getOutTradeNo())) {
					Logger.Info("----------------AliPay Success");
					//TODO
					if (resultListener != null) {
						resultListener.onPaySuccess(request.getOutTradeNo());
					}
				} else {
					Logger.Error("----------------AliPay Fail or Closed");
					if (resultListener != null) {
						resultListener.onPayFail(request.getOutTradeNo());
					}
				}
			} else {
				// Response 获取失败
				Logger.Error("<AliPayHelper>#initPay--Response is Null(Fail).!!!");
				if (resultListener != null) {
					resultListener.onPreOrderFail(response);
				}
			}

			break;

		case FAILED:
			Logger.Error("<AliPayHelper>#initPay--AliPay order Fail.!!!");
			if (resultListener != null) {
				resultListener.onPreOrderFail(response);
			}
			break;

		case UNKNOWN:
			Logger.Error("<AliPayHelper>#initPay--System Exception, Unknown order state.!!!");
			if (resultListener != null) {
				resultListener.onPreOrderFail(response);
			}
			break;

		default:
			Logger.Error("<AliPayHelper>#initPay--Unsupport trade state, return exception.!!!");
			if (resultListener != null) {
				resultListener.onPreOrderFail(response);
			}
			break;
		}
	}

	/**
	 * 由于有的时候是因为服务延时，所以需要商户每隔一段时间（建议5秒）后再进行查询操作，多试几次（建议3次）
	 *
	 * @param loopCount     循环次数，至少一次
	 * @param outTradeNo    商户系统内部的订单号,64个字符内可包含字母, [确保在商户系统唯一]
	 * @return 该订单是否支付成功
	 * @throws InterruptedException
	 */
	private boolean doPayQueryLoop(int loopCount, String outTradeNo){
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
			

			AlipayF2FQueryResult result = doOnePayQuery(outTradeNo);


			if (result != null) {
				TradeStatus status = result.getTradeStatus();
				Logger.Debug("<AliPayBusiness>#doPayQueryLoop -- TradeStatus=" + status);

				switch (status) {
				case SUCCESS:
					Logger.Info("<AliPayBusiness>#doPayQueryLoop--Pay Success.");
					stopLoop = true;
					payResult = true;
					break;

				case FAILED:
					Logger.Error("<AliPayBusiness>#doPayQueryLoop--Payment is Failed or Closed!!!");
					break;

				case UNKNOWN:
					Logger.Error("<AliPayBusiness>#doPayQueryLoop--System exception, Payment state is unknown!!!");
					break;

				default:
					Logger.Error("<AliPayBusiness>#doPayQueryLoop--Unsupport trade state, return exception!!!");
					break;
				}

				AlipayTradeQueryResponse response = result.getResponse();
				if (response != null && Constants.SUCCESS.equals(response.getCode())) {
					if ("TRADE_CLOSED".equals(response.getTradeStatus())) {
						Logger.Info("<AliPayBusiness>#doPayQueryLoop--Trade is Closed, Stop Loop Query!!!");
						// 如果查询到交易关闭，则停止循环查询
						stopLoop = true;
					}
				}
				
				if (!outTradeNo.equals(PayHelper.getOutTradeNo())) {
					// 当前outTradeNo和参数查询的outTradeNo不一致，则可以停止循环查询
					stopLoop = true;
				}
				
			}
		}

		return payResult;
	}

	/**
	 * 进行一次支付订单查询操作
	 *
	 * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
	 * @return AlipayF2FQueryResult 订单支付结果
	 */
	public AlipayF2FQueryResult doOnePayQuery(String outTradeNo){

		// 创建查询请求builder，设置请求参数
		AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder().setOutTradeNo(outTradeNo);

		AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);

		return result;
	}

//	/**
//	 * 进行一次支付订单查询操作
//	 *
//	 * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
//	 * @param immediately	TRUE: 立即进行查询  FALSE：默认等待5秒后开始查询
//	 * @return 该订单是否支付成功
//	 * @throws Exception
//	 */
//	public boolean doOnePayQuery(String outTradeNo, boolean immediately){
//
//		if (!immediately) {
//			//等待一定时间再进行查询，避免状态还没来得及被更新
//			Utils.sleep(waitingTimeBeforePayQueryServiceInvoked);
//		}
//
//		// 创建查询请求builder，设置请求参数
//		AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder().setOutTradeNo(outTradeNo);
//
//		boolean payState = false;
//
//		AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
//		TradeStatus status = result.getTradeStatus();
//		Logger.Debug("<AliPayBusiness>#doOnePayQuery -- TradeStatus=" + status);
//
//		switch (status) {
//		case SUCCESS:
//			Logger.Info("<AliPayBusiness>#doOnePayQuery--Pay Success.");
//			paymentState = PaymentState.SUCCESS;
//
//			AlipayTradeQueryResponse response = result.getResponse();
//			dumpResponse(response);
//			Logger.Debug("<AliPayBusiness>#doOnePayQuery--" + response.getTradeStatus());
//
//			if (Utils.isListNotEmpty(response.getFundBillList())) {
//				for (TradeFundBill bill : response.getFundBillList()) {
//					Logger.Debug(bill.getFundChannel() + ":" + bill.getAmount());
//				}
//			}
//			payState = true;
//			break;
//
//		case FAILED:
//			Logger.Error("<AliPayBusiness>#doOnePayQuery--Payment is Failed or Closed!!!");
//			break;
//
//		case UNKNOWN:
//			Logger.Error("<AliPayBusiness>#doOnePayQuery--System exception, Payment state is unknown!!!");
//			break;
//
//		default:
//			Logger.Error("<AliPayBusiness>#doOnePayQuery--Unsupport trade state, return exception!!!");
//			break;
//		}
//		return payState;
//	}

	/**
	 * 进行一次撤销操作
	 *
	 * @param outTradeNo    商户系统内部的订单号,64个字符内可包含字母, [确保在商户系统唯一]
	 */
	public void doOneReverse(String outTradeNo){
		if (outTradeNo == null || outTradeNo.isEmpty()) {
			return;
		}
		
		AlipayTradeCancelRequestBuilder builder = new AlipayTradeCancelRequestBuilder().setOutTradeNo(outTradeNo);
		AlipayTradeCancelResponse cancelResponse = tradeService.tradeCancel(builder);
		
		String code = cancelResponse.getCode();
		String tradeNo = cancelResponse.getTradeNo();
		Logger.Info("<AliPayBusiness>#doOneReverse-- outTradeNo=" + outTradeNo 
				+ " -- code=" + code
				+ " -- tradeNo=" + tradeNo);

	}

	// 简单打印应答
	private void dumpResponse(AlipayResponse response) {
		if (response != null) {
			Logger.Debug(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
			if (StringUtils.isNotEmpty(response.getSubCode())) {
				Logger.Debug(String.format("subCode:%s, subMsg:%s", response.getSubCode(), response.getSubMsg()));
			}
			Logger.Test("body:" + response.getBody());
		}
	}
	
	/**
	 * 退费
	 * @Title: doRefund  
	 * @Description: TODO  
	 *
	 * @param request
	 * @param resultListener
	 */
	public void doRefund(AlipayTradeRefundRequestBuilder request, RefundResultListener listener) {
		AlipayF2FRefundResult result = tradeService.tradeRefund(request);
		TradeStatus status = result.getTradeStatus();
		if (status == TradeStatus.FAILED) {
			// 第一次失败，再次尝试
			Logger.Debug("<AliPayHelper>#doRefund--Refund Again.");
			result = tradeService.tradeRefund(request);
			status = result.getTradeStatus();
		}
		
		AlipayTradeRefundResponse response = result.getResponse();
		dumpResponse(response);
		
		switch (status) {
		case SUCCESS:
			Logger.Info("<AliPayHelper>#doRefund--AliPay order success.");

			if (response != null) {
				//--------------------------------------------------------------------
				//1) 退费成功
				//--------------------------------------------------------------------
				if (listener != null) {
					listener.onRefundSuccess();
				}
			} else {
				// Response 获取失败
				Logger.Error("<AliPayHelper>#doRefund--Response is Null(Fail).!!!");
				if (listener != null) {
					listener.onRefundFail(response);
				}
			}

			break;

		case FAILED:
			Logger.Error("<AliPayHelper>#doRefund--AliPay Refund Fail.!!!");
			if (response != null) {
				Logger.Error("<AliPayHelper>#doRefund--Code=" + response.getCode());
			}
			if (listener != null) {
				listener.onRefundFail(response);
			}
			break;

		case UNKNOWN:
			Logger.Error("<AliPayHelper>#doRefund--System Exception, Unknown order state.!!!");
			if (listener != null) {
				listener.onRefundFail(response);
			}
			break;

		default:
			Logger.Error("<AliPayHelper>#doRefund--Unsupport trade state, return exception.!!!");
			if (listener != null) {
				listener.onRefundFail(response);
			}
			break;
		}
	}
	
	public interface RefundResultListener {

		// 退费失败
		void onRefundFail(AlipayTradeRefundResponse response);

		// 退费成功
		void onRefundSuccess();

	}

}
