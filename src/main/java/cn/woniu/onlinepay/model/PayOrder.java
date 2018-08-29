/**   
 * @Title: PayOrder.java 
 * @Package cn.com.rits.app.yunprint.model 
 * @Description: TODO 
 * @author Woniu  
 * @date 2016年8月25日 下午2:44:34 
 * @version V1.0   
 */
package cn.woniu.onlinepay.model;

import cn.woniu.onlinepay.log.Logger;


/** 
 * @ClassName: PayOrder 
 * @Description: TODO 
 * @author Woniu
 * @date 2016年8月25日 下午2:44:34 
 *  
 */
public class PayOrder {

    // 支付宝：(必填) 商户网站订单系统中唯一订单号，64个字符以内(微信32个字符以内)，只能包含字母、数字、下划线，
    // 需保证商户系统端不能重复，建议通过数据库sequence生成，
	protected String outTradeNo;
	
	//商品描述
	protected String productBody = "";
	
	//商品详情
	protected String productDetail = "";	
	
	//费用，单位为分
	protected int fee = 0;	

	//商品标签
	protected String goodsTag = "";
	
	//商品编号
	protected String productId = "";
	
	//退款额， 单位为RMB1分
	private int reFundFee = fee;
	
	
	public PayOrder(){
    	this.outTradeNo = generateOutTradeNo(); 
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	/**
	 * @return the productBody
	 */
	public String getProductBody() {
		return productBody;
	}

	/**
	 * @param productBody the productBody to set
	 */
	public void setProductBody(String productBody) {
		this.productBody = productBody;
	}

	/**
	 * @return the productDetail
	 */
	public String getProductDetail() {
		return productDetail;
	}

	/**
	 * @param productDetail the productDetail to set
	 */
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}	

	/**
	 * @return the fee
	 */
	public int getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(int fee) {
		this.fee = fee;
	}

	/**
	 * @return the goodsTag
	 */
	public String getGoodsTag() {
		return goodsTag;
	}

	/**
	 * @param goodsTag the goodsTag to set
	 */
	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * 
	 * @Title: getReFundFee  
	 * @Description: 获取需要退款的金额  
	 *
	 * @return
	 */
	public int getReFundFee() {
		return reFundFee;
	}

	public void setReFundFee(int reFundFee) {
		this.reFundFee = reFundFee;
	}

	
	/**
	 * 
	 * @Title: updateRefundFee  
	 * @Description: 从当前的退款额中减去已经印完的费用的到最新的退款额(RMB1分=1积分)  
	 *
	 * @param fee
	 */
	public void updateRefundFee(int fee) {
		Logger.Test("<PayOrder>updateRefundFee --Before--reFundFee=" + reFundFee + " new used fee=" + fee);
		this.reFundFee -= fee;
		Logger.Test("<PayOrder>updateRefundFee --After--reFundFee=" + reFundFee);
	}
	
	
	public static String generateOutTradeNo() {
		// 支付宝：(必填) 商户网站订单系统中唯一订单号，64个字符以内(微信32个字符以内)，只能包含字母、数字、下划线，
    	// 银联: 商户订单号，不能含“-”或“_”, 长度8-40 
		// 所以创建的字符串必须在32位以内
		
		long time = System.currentTimeMillis();   // 目前13位
		long random = (long)(Math.random() * 100000L); // 最大6位
		String no = "x" + "Test" + random + time; 
		
		
		Logger.Debug("out_trade_no = " + no);
		return no;
	}
	
}
