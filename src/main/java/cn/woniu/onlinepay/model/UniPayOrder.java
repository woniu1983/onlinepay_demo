package cn.woniu.onlinepay.model;

/** 
 * @ClassName: UniPayOrder <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author Woniu 
 * @version  
 * @since JDK 1.6 
 */
public class UniPayOrder extends PayOrder {
	
	/** 
	 * 字段： txnTime
	 * 说明：订单发送时间 （商户端生成表示申请二维码和一笔真实订单的订单号）
	 * 默认：
	 * 要求： Must */
	private String txnTime;
    
    public UniPayOrder() {
    	super();
    	this.productBody = "测试(银联扫码支付)";
    	//outTradeNo: 商户订单号，不能含“-”或“_”, 长度8-40
    }

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

    
}
