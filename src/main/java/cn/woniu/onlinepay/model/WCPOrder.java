package cn.woniu.onlinepay.model;


public class WCPOrder extends PayOrder {
    
    public WCPOrder() {
    	super();
    	this.productBody = "Test(WechatPay)";// Must English
    }
}
