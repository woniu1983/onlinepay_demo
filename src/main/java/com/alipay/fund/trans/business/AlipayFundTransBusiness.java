package com.alipay.fund.trans.business;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.trade.config.Configs;

/** 
 * @ClassName: AlipayFundTransBusiness <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author woniu 
 * @version  
 * @since JDK 1.6 
 */
public class AlipayFundTransBusiness {

	/**
	 * 
	 * @Title: fundTransToAccountTrans  
	 * @Description: TODO  
	 *
	 * @param outBizNo       商家支付单号唯一
	 * @param payeeAccount   收款方账户
	 * @param amount         收款金额，元
	 * @param payeeRealName  收款人实名
	 * @param payerShowName  付款者显示名称
	 * @param remark         备注
	 * @throws AlipayApiException
	 */
	public static void fundTransToAccountTrans(String outBizNo, String payeeAccount, String amount, String payeeRealName, String payerShowName, String remark) throws AlipayApiException {
		AlipayClient alipayClient = build();
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizContent("{" +
				"    \"out_biz_no\":\"" + outBizNo + "\"," +
				"    \"payee_type\":\"ALIPAY_LOGONID\"," +
				"    \"payee_account\":\"" + payeeAccount + "\"," +
				"    \"amount\":\"" + amount + "\"," +
				"    \"payer_show_name\":\"" + payerShowName + "\"," +
				"    \"payee_real_name\":\"" + payeeRealName + "\"," +
				"    \"remark\":\"" + remark + "\"," +
				"  }");
		System.out.println(request.getBizContent());
		AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
			System.out.println(response.getBody());
		}
	}

	public static void fundTransOrderQuery(String outBizNo, String order_id) throws AlipayApiException {
		AlipayClient alipayClient = build();
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizContent("{" +
				"    \"out_biz_no\":\"" + outBizNo + "\"," +
				"    \"order_id\":\"" + order_id + "\"" +
				"  }");
		AlipayFundTransOrderQueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
			System.out.println(response.getBody());
		}
	}
	
	private static AlipayClient build() {
		AlipayClient alipayClient = new DefaultAlipayClient(
				Configs.getOpenApiDomain(),
				Configs.getAppid(),
				Configs.getPrivateKey(),
				"json",
				"GBK",
				Configs.getAlipayPublicKey(),
				"RSA");
		return alipayClient;
	}
}
