package com.tencent.business;

import static java.lang.Thread.sleep;

//import com.rits.mypay_wechat.config.MyPayApplication;
import com.tencent.common.Signature;
import com.tencent.common.Util;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.tencent.service.ReverseService;
import com.tencent.service.ScanPayQueryService;
import com.tencent.service.ScanPayService;

import cn.woniu.onlinepay.WCPConstants;

/**
 * User: rizenguo
 * Date: 2014/12/1
 * Time: 17:05
 */
public class ScanPayBusiness {
	private ScanPayQueryResData scanPayQueryResData_m;
    public ScanPayBusiness() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
    	
        scanPayService = new ScanPayService();
       
        scanPayQueryService = new ScanPayQueryService();
       
        reverseService = new ReverseService();      
        
    }
    
    

    public interface ResultListener {

        //API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
        void onFailByReturnCodeError(ScanPayResData scanPayResData);

        //API返回ReturnCode为FAIL，支付API系统返回失败，请检测Post给API的数据是否规范合法
        void onFailByReturnCodeFail(ScanPayResData scanPayResData);

        //支付请求API返回的数据签名验证失败，有可能数据被篡改了
        void onFailBySignInvalid(ScanPayResData scanPayResData);


        //用户用来支付的二维码已经过期，提示收银员重新扫一下用户微信“刷卡”里面的二维码
        void onFailByAuthCodeExpire(ScanPayResData scanPayResData);

        //授权码无效，提示用户刷新一维码/二维码，之后重新扫码支付"
        void onFailByAuthCodeInvalid(ScanPayResData scanPayResData);

        //支付失败
        void onFail(ScanPayResData scanPayResData);

        //支付成功
        void onSuccess(ScanPayQueryResData scanPayQueryResData);
        
        //返回qr_code url成功
        void onRePaySuccess(ScanPayResData scanPayResData);
        
        //返回qr_code url失败  网络原因 //TODO Mao
        void onFailByNetwork();
    }

  

    //每次调用订单查询API时的等待时间，因为当出现支付失败的时候，如果马上发起查询不一定就能查到结果，所以这里建议先等待一定时间再发起查询

    private int waitingTimeBeforePayQueryServiceInvoked = 5000;

    //循环调用订单查询API的次数
    private int payQueryLoopInvokedCount = 60*6;

    //每次调用撤销API的等待时间
    private int waitingTimeBeforeReverseServiceInvoked = 5000;

    private ScanPayService scanPayService;

    private ScanPayQueryService scanPayQueryService;

    private ReverseService reverseService;
    
    private String paymentState = "NOTPAY";
   
    /**
     * 直接执行被扫支付业务逻辑（包含最佳实践流程）
     *
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public void run(ScanPayReqData scanPayReqData, ResultListener resultListener) throws Exception {

    	
    	paymentState = "NOTPAY"; //初始化支付状态
        //--------------------------------------------------------------------
        //构造请求“被扫支付API”所需要提交的数据
        //--------------------------------------------------------------------

        String outTradeNo = scanPayReqData.getOut_trade_no();

        //接受API返回
        String payServiceResponseString = null;

        long costTimeStart = System.currentTimeMillis();
       
        try {
        	payServiceResponseString = scanPayService.request(scanPayReqData);
        } catch (Exception e) {
        	// 一般是网络原因导致失败了
        	Util.error("Exception: " + e.getMessage());
        	resultListener.onFailByNetwork();
        	return;
        }
       

        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;
        Util.debug("api request time：" + totalTimeCost + "ms");

        //打印回包数据
        Util.debug("response data " + payServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        ScanPayResData scanPayResData = (ScanPayResData) Util.getObjectFromXML(payServiceResponseString, ScanPayResData.class);

        if (scanPayResData == null || scanPayResData.getReturn_code() == null) {
        	Util.error("[Pay Fail]Pay request wrong data, please check each parameter or make sure the API is accessable");
            resultListener.onFailByReturnCodeError(scanPayResData);
            return;
        }

        if (scanPayResData.getReturn_code().equals("FAIL")) {
            //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
        	Util.error("[Pay Fail]Fail returned from pay API, please make sure the post data is valid");
            resultListener.onFailByReturnCodeFail(scanPayResData);
            return;
        } else {
        	Util.debug("Pay API returned data successfully.");
            //--------------------------------------------------------------------
            //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
            //--------------------------------------------------------------------
            if (!Signature.checkIsSignValidFromResponseString(payServiceResponseString)) {
            	Util.error("[Pay Fail]Sign verified fail, maybe data is distorted");
                resultListener.onFailBySignInvalid(scanPayResData);
                return;
            }

            //获取错误码
            String errorCode = scanPayResData.getErr_code();
            //获取错误描述
            String errorCodeDes = scanPayResData.getErr_code_des();

            if (scanPayResData.getResult_code().equals("SUCCESS")) {

                //--------------------------------------------------------------------
                //1)预下单成功
                //--------------------------------------------------------------------

            	Util.debug("[Pre-order success]");
                resultListener.onRePaySuccess(scanPayResData);
                
                //查询订单支付结果
                if (doPayQueryLoop(payQueryLoopInvokedCount, outTradeNo)) {
                	Util.debug("[Pay success]");
                    resultListener.onSuccess(scanPayQueryResData_m);
                    scanPayQueryResData_m = null;
                } else {
                	Util.debug("[during a period of time, not paied, reverse the pre-order]");
                    doReverseLoop(outTradeNo);
                    resultListener.onFail(scanPayResData);
                }
            }else{

                //出现业务错误
            	Util.error("business return fail");
            	Util.error("err_code:" + errorCode);
            	Util.error("err_code_des:" + errorCodeDes);

                //业务错误时错误码有好几种，商户重点提示以下几种
                if (errorCode.equals("AUTHCODEEXPIRE") || errorCode.equals("AUTH_CODE_INVALID") || errorCode.equals("NOTENOUGH")) {

                    //--------------------------------------------------------------------
                    //2)扣款明确失败
                    //--------------------------------------------------------------------

                    //对于扣款明确失败的情况直接走撤销逻辑
                    doReverseLoop(outTradeNo);

                    //以下几种情况建议明确提示用户，指导接下来的工作
                    if (errorCode.equals("AUTHCODEEXPIRE")) {
                        //表示用户用来支付的二维码已经过期，提示收银员重新扫一下用户微信“刷卡”里面的二维码
                    	Util.debug("[Pay fail] the QR code is expired, reason is : " + errorCodeDes);
                        resultListener.onFailByAuthCodeExpire(scanPayResData);
                    } else if (errorCode.equals("AUTH_CODE_INVALID")) {
                        //授权码无效，提示用户刷新一维码/二维码，之后重新扫码支付
                    	Util.debug("[Pay fail] Authorty code is expired, reason is : " + errorCodeDes);
                        resultListener.onFailByAuthCodeInvalid(scanPayResData);
                    } 
                }
            }
        }
    }

    /**
     * 进行一次支付订单查询操作
     *
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @param immediately	TRUE: 立即进行查询  FALSE：默认等待5秒后开始查询
     * @return 该订单是否支付成功
     * @throws Exception
     */
    public boolean doOnePayQuery(String outTradeNo, boolean immediately) throws Exception {

    	if (!immediately) {
    		sleep(waitingTimeBeforePayQueryServiceInvoked);//等待一定时间再进行查询，避免状态还没来得及被更新
    	}

        String payQueryServiceResponseString;

        ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData("",outTradeNo);
        payQueryServiceResponseString = scanPayQueryService.request(scanPayQueryReqData);

//        Util.debug("Data returned from query API is : ");
//        Util.debug(payQueryServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        ScanPayQueryResData scanPayQueryResData = (ScanPayQueryResData) Util.getObjectFromXML(payQueryServiceResponseString, ScanPayQueryResData.class);
        scanPayQueryResData_m = scanPayQueryResData;
        if (scanPayQueryResData == null || scanPayQueryResData.getReturn_code() == null) {
        	Util.debug("query request logic erro, please make sure each parameter is valid");
            return false;
        }

        if (scanPayQueryResData.getReturn_code().equals("FAIL")) {
            //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
        	Util.debug("Fail returned from query API, detail is : " + scanPayQueryResData.getReturn_msg());
            return false;
        } else {
            if (scanPayQueryResData.getResult_code().equals("SUCCESS")) {//业务层成功
            	paymentState = scanPayQueryResData.getTrade_state();
            	Util.debug("Query result: Pay state = " + paymentState);
                if (paymentState.equals("SUCCESS")) {
                    //表示查单结果为“支付成功”
                	Util.debug("Query result: Pay success");
                    return true;
                } else {
                    //支付不成功
                	Util.debug("Query result: Pay fail, state = " + paymentState);
                    return false;
                }
            } else {
            	Util.debug("Query fail, error code: " + scanPayQueryResData.getErr_code() + "     detail: " + scanPayQueryResData.getErr_code_des());
                return false;
            }
        }
    }

    /**
     * 由于有的时候是因为服务延时，所以需要商户每隔一段时间（建议5秒）后再进行查询操作，多试几次（建议3次）
     *
     * @param loopCount     循环次数，至少一次
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @return 该订单是否支付成功
     * @throws InterruptedException
     */
    private boolean doPayQueryLoop(int loopCount, String outTradeNo) throws Exception {
        //至少查询一次
        if (loopCount == 0) {
            loopCount = 1;
        }
       
    	for (int i = 0; i < loopCount && (paymentState.equals("NOTPAY") || paymentState.equals("USERPAYING")); i++) {    		
    		if(!WCPConstants.IS_LOOP_QUERY_PAY_RESULT){
    			Util.debug("myquery_flag" + "don't query!");
    			break;
    		}
    		
    		if (i == 0) {
				// 第一次间隔5秒开始查询
				sleep(waitingTimeBeforePayQueryServiceInvoked);
			} else {
				// 之后间隔3秒查询
				sleep(3000);
			}
    		
            if (doOnePayQuery(outTradeNo, true)) {
            	Util.debug("my>>>>query" + "query");
                return true;
            }
        }
        
        return false;
    }

    //是否需要再调一次撤销，这个值由撤销API回包的recall字段决定
    private boolean needRecallReverse = false;

    /**
     * 进行一次撤销操作
     *
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @return 该订单是否支付成功
     * @throws Exception
     */
    public boolean doOneReverse(String outTradeNo) throws Exception {

        sleep(waitingTimeBeforeReverseServiceInvoked);//等待一定时间再进行查询，避免状态还没来得及被更新

        String reverseResponseString;

        ReverseReqData reverseReqData = new ReverseReqData("",outTradeNo);
        reverseResponseString = reverseService.request(reverseReqData);

        Util.debug("Data returned from reverse api: ");
        Util.debug(reverseResponseString);
        //将从API返回的XML数据映射到Java对象
        ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(reverseResponseString, ReverseResData.class);
        if (reverseResData == null) {
        	Util.debug("reverse logic error, please make sure each parameter is valid");
            return false;
        }
        if (reverseResData.getReturn_code().equals("FAIL")) {
            //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
        	Util.debug("Fail returned from reverse api, detail: " + reverseResData.getReturn_msg());
            return false;
        } else {
            if (reverseResData.getResult_code().equals("FAIL")) {
            	Util.debug("reverse error, error code: " + reverseResData.getErr_code() + "     detail: " + reverseResData.getErr_code_des());
                if (reverseResData.getRecall().equals("Y")) {
                    //表示需要重试
                    needRecallReverse = true;
                    return false;
                } else {
                    //表示不需要重试，也可以当作是撤销成功
                    needRecallReverse = false;
                    return true;
                }
            } else {
                //查询成功，打印交易状态
            	Util.debug("Reverse success");
                return true;
            }
        }
    }


    /**
     * 由于有的时候是因为服务延时，所以需要商户每隔一段时间（建议5秒）后再进行查询操作，是否需要继续循环调用关闭API由关闭API回包里面的recall字段决定。
     *
     * @param outTradeNo    商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
     * @throws InterruptedException
     */
    private void doReverseLoop(String outTradeNo) throws Exception {
        //初始化这个标记
        needRecallReverse = true;
        //进行循环撤销，直到撤销成功，或是API返回recall字段为"Y"
        while (needRecallReverse) {
            if (doOneReverse(outTradeNo)) {
                return;
            }
        }
    }

    /**
     * 设置循环多次调用订单查询API的时间间隔
     *
     * @param duration 时间间隔，默认为10秒
     */
    public void setWaitingTimeBeforePayQueryServiceInvoked(int duration) {
        waitingTimeBeforePayQueryServiceInvoked = duration;
    }

    /**
     * 设置循环多次调用订单查询API的次数
     *
     * @param count 调用次数，默认为三次
     */
    public void setPayQueryLoopInvokedCount(int count) {
        payQueryLoopInvokedCount = count;
    }

    /**
     * 设置循环多次调用撤销API的时间间隔
     *
     * @param duration 时间间隔，默认为5秒
     */
    public void setWaitingTimeBeforeReverseServiceInvoked(int duration) {
        waitingTimeBeforeReverseServiceInvoked = duration;
    }

    public void setScanPayService(ScanPayService service) {
        scanPayService = service;
    }

    public void setScanPayQueryService(ScanPayQueryService service) {
        scanPayQueryService = service;
    }

    public void setReverseService(ReverseService service) {
        reverseService = service;
    }

}
