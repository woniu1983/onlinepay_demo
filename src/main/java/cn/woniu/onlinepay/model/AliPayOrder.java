/**   
 * @Title: AliPayOrder.java 
 * @Package cn.com.rits.app.yunprint.model 
 * @Description: TODO 
 * @author Jianghui Mao   
 * @date 2016年8月25日 下午2:06:04 
 * @version V1.0   
 */
package cn.woniu.onlinepay.model;


/** 
 * @ClassName: AliPayOrder 
 * @Description: TODO 
 * @author Jianghui Mao 
 * @date 2016年8月25日 下午2:06:04 
 *  
 */
public class AliPayOrder extends PayOrder {
    
    public AliPayOrder() {
    	super();
    	this.productBody = "云打印(支付宝当面付扫码消费)";
    }
	
}
