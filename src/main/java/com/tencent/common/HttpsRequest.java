package com.tencent.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;

//import com.rits.mypay_wechat.R;
import com.tencent.service.IServiceRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import cn.woniu.onlinepay.util.CusProxy;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:36
 */
public class HttpsRequest implements IServiceRequest{

	public interface ResultListener {


		public void onConnectionPoolTimeoutError();

	}

	//   static {
	//	   CusProxy.getInstance().setProxy();
	//   }

	private static final String UTF8 = "UTF-8";

	//表示请求器是否已经做了初始化工作
	private boolean hasInit = false;

	//连接超时时间，默认10秒
	private int socketTimeout = 30000;

	//传输超时时间，默认30秒
	private int connectTimeout = 30000;

	//    //请求器的配置
	//    private RequestConfig requestConfig;

	//HTTP请求器
	//    private CloseableHttpClient httpClient;
	private HttpClient httpClient;

	public HttpsRequest() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		init();
	}
	public HttpsRequest(String type)throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException{
		init_refund();
	}
	private void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

		httpClient = new HttpClient();

		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,UTF8);
		//        httpClient.getParams().setParameter(HTTP.CONTENT_ENCODING, UTF8);  //TODO  2016/11/23
		//        httpClient.getParams().setParameter(HTTP.CHARSET_PARAM, UTF8);  
		//        httpClient.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET,UTF8);

		if (CusProxy.getInstance().isUseHTTPProxy()) {
			httpClient.getHostConfiguration().setProxy(CusProxy.getInstance().getHttpHost(),
					CusProxy.getInstance().getHttpPort());

			if (CusProxy.getInstance().isUseHTTPAuth()) {
				//				httpClient.getState().setProxyCredentials("my-proxy-realm", CusProxy.getInstance().getHttpHost(),
				//						new UsernamePasswordCredentials(CusProxy.getInstance().getHttpUserName(),
				//								CusProxy.getInstance().getHttpPwd()));
				httpClient.getState().setProxyCredentials(
						new AuthScope(CusProxy.getInstance().getHttpHost(), CusProxy.getInstance().getHttpPort(), null, null),
						new UsernamePasswordCredentials(CusProxy.getInstance().getHttpUserName(), CusProxy.getInstance().getHttpPwd())
						); //TODO  2016-08-11
			}
		}

		//        //根据默认超时限制初始化requestConfig
		//        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

		hasInit = true;
	}
	private void init_refund() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

		Protocol myhttps = new Protocol("https", new WCPSSLSocketFactory(), 443);
		Protocol.registerProtocol("https",  myhttps);
		
		httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,UTF8);
		if (CusProxy.getInstance().isUseHTTPProxy()) {
			httpClient.getHostConfiguration().setProxy(CusProxy.getInstance().getHttpHost(),
					CusProxy.getInstance().getHttpPort());

			if (CusProxy.getInstance().isUseHTTPAuth()) {
				httpClient.getState().setProxyCredentials(
						new AuthScope(CusProxy.getInstance().getHttpHost(), CusProxy.getInstance().getHttpPort(), null, null),
						new UsernamePasswordCredentials(CusProxy.getInstance().getHttpUserName(), CusProxy.getInstance().getHttpPwd())
						); //TODO  2016-08-11
			}
		}

		hasInit = true;
	}

	/**
	 * 通过Https往API post xml数据
	 *
	 * @param url    API地址
	 * @param xmlObj 要提交的XML数据对象
	 * @return API回包的实际数据
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */

	public String sendPost(String url, Object xmlObj) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

		if (!hasInit) {
			init();
		}

		String result = null;

		PostMethod httpPost = new PostMethod(url);

		httpPost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,UTF8);
		//        httpPost.getParams().setParameter(HTTP.CONTENT_ENCODING, UTF8);   //TODO  2016/11/23
		//        httpPost.getParams().setParameter(HTTP.CHARSET_PARAM, UTF8);  
		//        httpPost.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, UTF8); 

		// MFP上每次重试的间隔时间为75秒，默认重试3次，加上之前的时间，总体等待约5分钟
		//        httpPost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));

		//解决XStream对出现双下划线的bug
		XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

		//将要提交给API的数据对象转换成XML格式数据Post给API
		String postDataXML = xStreamForRequestPostData.toXML(xmlObj);

		//        Util.debug("API,POST data is：");
		//        Util.debug(postDataXML);

		try {        	
			//得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
			StringRequestEntity requestEntity = new StringRequestEntity(
					postDataXML,
					"text/xml",
					"UTF-8");

			httpPost.addRequestHeader("Content-Type", "text/xml; charset=UTF-8");
			httpPost.setRequestEntity(requestEntity);

			httpPost.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,socketTimeout);
			httpPost.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,connectTimeout);
			int returnCode = httpClient.executeMethod(httpPost);
			Util.debug("returnCode = " + returnCode);

			result = httpPost.getResponseBodyAsString();

			//        	Util.debug("httpPost.getResponseContentLength >>> " + httpPost.getResponseContentLength());
			//        	Util.debug("httpPost.getResponseCharSet >>> " + httpPost.getResponseCharSet());

			//        	Util.debug("return content >>> " + result);

		}
		//        catch (ConnectionPoolTimeoutException e) {
		//        	Util.debug("http get throw ConnectionPoolTimeoutException(wait time out)");
		//
		//        } catch (ConnectTimeoutException e) {
		//        	Util.debug("http get throw ConnectTimeoutException");
		//
		//        } 
		catch (SocketTimeoutException e) {
			Util.debug("http get throw SocketTimeoutException");

		} catch (UnsupportedEncodingException e) {
			Util.debug(" StringRequestEntity UnsupportedEncodingException");

		} 
		/*catch (Exception e) {
        	Util.debug("http error" + e.toString());

        } */
		finally {
			httpPost.abort();
		}

		return result;
	}

	/**
	 * 设置连接超时时间
	 *
	 * @param socketTimeout 连接时长，默认10秒
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
		resetRequestConfig();
	}

	/**
	 * 设置传输超时时间
	 *
	 * @param connectTimeout 传输时长，默认30秒
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		resetRequestConfig();
	}

	private void resetRequestConfig(){
		//    	this.requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
	}

	//    /**
	//     * 允许商户自己做更高级更复杂的请求器配置
	//     *
	//     * @param requestConfig 设置HttpsRequest的请求器配置
	//     */
	//    public void setRequestConfig(RequestConfig requestConfig) {
	//    	this.requestConfig = requestConfig;
	//    }
}
