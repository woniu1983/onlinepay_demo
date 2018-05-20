package cn.woniu.onlinepay;



public class WCPConstants {

	private WCPConstants() {
		// No Constructor.
	}

	
	public static final String CERT_FILE_NAME 			= "apiclient_cert.p12";
	
	public static final String KEY_KEY = "key";
	public static final String KEY_APP_ID = "appID";
	public static final String KEY_MCH_ID = "mchID";
	public static final String KEY_SUB_MCH_ID = "subMchID";
	public static final String KEY_CERT_PWD = "certPassword";
	public static final String KEY_PAY_ENABLE = "wechatEnable";
	
	public static final String KEY = "fGrUtmAoXr1PuXRHwibXT6cjkyY1wN48";

	//微信分配的公众号ID（开通公众号之后可以获取到）
	public static final String APPID = "wx58b043b13dc26cfe";

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	public static final String MCHID = "1349778501";

	//受理模式下给子商户分配的子商户号
	public static final String SUBMCHID = "";

	/*//HTTPS证书的本地路径
	public static final String CERTLOCALPATH = "";

	
*/
	//HTTPS证书密码，默认密码等于商户号MCHID
	public static final String CERTPASSWORD = "1349778501";
	//是否使用异步线程的方式来上报API测速，默认为异步模式
	public static final boolean USETHREADTODOREPORT = true;

	//订单过期时间
	public static final int CLOSETIME = 60;
	
	//是否循环查询支付结果
	public static final boolean IS_LOOP_QUERY_PAY_RESULT = true;
	
	//机器IP
	public static String IP = "";

	// RITS账户， 调试可用，正式版必须以机器对应的运营商账户为准
//	public static void initDefault(){
//		Configure.setKey(KEY);
//		Configure.setAppID(APPID);
//		Configure.setMchID(MCHID);
//		Configure.setSubMchID(SUBMCHID);
//		/*Configure.setCertLocalPath(CERTLOCALPATH);
//		*/
//		Configure.setCertPassword(CERTPASSWORD);
//		Configure.setUseThreadToDoReport(USETHREADTODOREPORT);
//		Configure.setIp(IP);
//	}
}
