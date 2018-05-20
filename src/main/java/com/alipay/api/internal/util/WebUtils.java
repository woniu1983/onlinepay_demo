package com.alipay.api.internal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alipay.api.AlipayConstants;
import com.alipay.api.FileItem;
import com.sun.net.ssl.HostnameVerifier;
import com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl;

/**
 * 网络工具类。
 * 
 * @author carver.gu
 * @since 1.0, Sep 12, 2009
 */
public abstract class WebUtils {

	private static final String     DEFAULT_CHARSET = AlipayConstants.CHARSET_UTF8;
	private static final String     METHOD_POST     = "POST";
	private static final String     METHOD_GET      = "GET";

	private static SSLContext       ctx             = null;

	private static HostnameVerifier verifier        = null;

	private static SSLSocketFactory socketFactory   = null;

	private static class DefaultTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain,
				String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain,
				String authType) throws CertificateException {
		}
	}

	static {
		// ------------------------------- 2016-08-26 Add to use HttpsURLConnectionOldImpl
		// SDK v10.x、v11.xはそれ以前のSDKアプリとの互換性のために
		// java.protocol.handler.pkgsにcom.sun.net.ssl.internal.www.protocolを指定しています。
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		// DSDK is JDK 1.6, but PC server is JDK1.8, the default https protocal is different.
		// So. force set the https protocal = TLSv1 in MFP client, and same as server side
		System.setProperty("https.protocols", "TLSv1");
		// -------------------------------

		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() },
					new SecureRandom());

			ctx.getClientSessionContext().setSessionTimeout(15);
			ctx.getClientSessionContext().setSessionCacheSize(1000);

			socketFactory = ctx.getSocketFactory();
			
			HttpsURLConnectionOldImpl.setDefaultSSLSocketFactory(socketFactory);
		} catch (Exception e) {

		}

		// ------------------------------- 2016-08-26 Comment to use HttpsURLConnectionOldImpl
		//        verifier = new HostnameVerifier() {
		//            public boolean verify(String hostname, SSLSession session) {
		//                return false;//默认认证不通过，进行证书校验。
		//            }
		//        };

		verifier = new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, String arg1) {
				return true; //默认认证通过，无须进行证书校验。
			}
		};
		HttpsURLConnectionOldImpl.setDefaultHostnameVerifier(verifier);
		// -------------------------------

	}

	private WebUtils() {
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params, int connectTimeout,
			int readTimeout) throws IOException {
		return doPost(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params, String charset,
			int connectTimeout, int readTimeout) throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		String query = buildQuery(params, charset);
		byte[] content = {};
		if (query != null) {
			content = query.getBytes(charset);
		}
		return doPost(url, ctype, content, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param ctype 请求类型
	 * @param content 请求字节数组
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, String ctype, byte[] content, int connectTimeout,
			int readTimeout) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			// 1. Get Connection
			try {
				conn = getConnection(new URL(url), METHOD_POST, ctype);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				AlipayLogger.logCommError(e, url, map.get("app_key"), map.get("method"), content);
				throw e;
			}
			
			// 2. POST Data
			try {
				out = conn.getOutputStream();
				out.write(content);
				out.flush();
				out.close();
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				AlipayLogger.logCommError(e, conn, map.get("app_key"), map.get("method"), content);
				throw e;
			}
			
			// 3. Get Response
			try {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				AlipayLogger.logCommError(e, conn, map.get("app_key"), map.get("method"), content);
				throw e;
			}
			

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch(Exception e) {
					
				}
			}
			
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * 执行带文件上传的HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param textParams 文本请求参数
	 * @param fileParams 文件请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params,
			Map<String, FileItem> fileParams, int connectTimeout,
			int readTimeout) throws IOException {
		if (fileParams == null || fileParams.isEmpty()) {
			return doPost(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
		} else {
			return doPost(url, params, fileParams, DEFAULT_CHARSET, connectTimeout, readTimeout);
		}
	}

	/**
	 * 执行带文件上传的HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param textParams 文本请求参数
	 * @param fileParams 文件请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params,
			Map<String, FileItem> fileParams, String charset,
			int connectTimeout, int readTimeout) throws IOException {
		if (fileParams == null || fileParams.isEmpty()) {
			return doPost(url, params, charset, connectTimeout, readTimeout);
		}

		String boundary = System.currentTimeMillis() + ""; // 随机分隔线
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				String ctype = "multipart/form-data;boundary=" + boundary + ";charset=" + charset;
				conn = getConnection(new URL(url), METHOD_POST, ctype);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				AlipayLogger.logCommError(e, url, map.get("app_key"), map.get("method"), params);
				throw e;
			}

			try {
				out = conn.getOutputStream();

				byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n").getBytes(charset);

				// 组装文本请求参数
				Set<Entry<String, String>> textEntrySet = params.entrySet();
				for (Entry<String, String> textEntry : textEntrySet) {
					byte[] textBytes = getTextEntry(textEntry.getKey(), textEntry.getValue(),
							charset);
					out.write(entryBoundaryBytes);
					out.write(textBytes);
					out.flush();
				}

				// 组装文件请求参数
				Set<Entry<String, FileItem>> fileEntrySet = fileParams.entrySet();
				for (Entry<String, FileItem> fileEntry : fileEntrySet) {
					FileItem fileItem = fileEntry.getValue();
					byte[] fileBytes = getFileEntry(fileEntry.getKey(), fileItem.getFileName(),
							fileItem.getMimeType(), charset);
					out.write(entryBoundaryBytes);
					out.write(fileBytes);
					out.write(fileItem.getContent());
					out.flush();
				}

				// 添加请求结束标志
				byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n").getBytes(charset);
				out.write(endBoundaryBytes);
				out.flush();
				
				//获取Response
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				AlipayLogger.logCommError(e, conn, map.get("app_key"), map.get("method"), params);
				throw e;
			}

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch(Exception e) {
					
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	private static byte[] getTextEntry(String fieldName, String fieldValue,
			String charset) throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
		entry.append(fieldValue);
		return entry.toString().getBytes(charset);
	}

	private static byte[] getFileEntry(String fieldName, String fileName, String mimeType,
			String charset) throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\";filename=\"");
		entry.append(fileName);
		entry.append("\"\r\nContent-Type:");
		entry.append(mimeType);
		entry.append("\r\n\r\n");
		return entry.toString().getBytes(charset);
	}

	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params) throws IOException {
		return doGet(url, params, DEFAULT_CHARSET);
	}

	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params,
			String charset) throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;

		try {
			String ctype = "application/x-www-form-urlencoded;charset=" + charset;
			String query = buildQuery(params, charset);
			try {
				conn = getConnection(buildGetUrl(url, query), METHOD_GET, ctype);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				AlipayLogger.logCommError(e, url, map.get("app_key"), map.get("method"), params);
				throw e;
			}

			try {
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				Map<String, String> map = getParamsFromUrl(url);
				AlipayLogger.logCommError(e, conn, map.get("app_key"), map.get("method"), params);
				throw e;
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	private static HttpURLConnection getConnection(URL url, String method,
			String ctype) throws IOException {
		HttpURLConnection conn = null;
		if ("https".equals(url.getProtocol())) {
			// 2016-08-26 Change to HttpsURLConnectionOldImpl in MFP(10.x,11.x...)

			//        	HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
			//            connHttps.setSSLSocketFactory(socketFactory);
			//            connHttps.setHostnameVerifier(verifier);

			HttpsURLConnectionOldImpl connHttps = (HttpsURLConnectionOldImpl) url.openConnection();

			conn = connHttps;
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}

		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
		conn.setRequestProperty("User-Agent", "aop-sdk-java");
		conn.setRequestProperty("Content-Type", ctype);
		return conn;
	}

	private static URL buildGetUrl(String strUrl, String query) throws IOException {
		URL url = new URL(strUrl);
		if (StringUtils.isEmpty(query)) {
			return url;
		}

		if (StringUtils.isEmpty(url.getQuery())) {
			if (strUrl.endsWith("?")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "?" + query;
			}
		} else {
			if (strUrl.endsWith("&")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "&" + query;
			}
		}

		return new URL(strUrl);
	}

	public static String buildQuery(Map<String, String> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (StringUtils.areNotEmpty(name, value)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=").append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}

	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream(); 
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (StringUtils.isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}

	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		StringWriter writer = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
			writer = new StringWriter();

			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				try {
					stream.close();	
				} catch (Exception e) {
				}
			}
			
			if (writer != null) {
				try {
					writer.close();	
				} catch (Exception e) {
				}
			}
			
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;

		if (!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

	/**
	 * 使用默认的UTF-8字符集反编码请求参数值。
	 * 
	 * @param value 参数值
	 * @return 反编码后的参数值
	 */
	public static String decode(String value) {
		return decode(value, DEFAULT_CHARSET);
	}

	/**
	 * 使用默认的UTF-8字符集编码请求参数值。
	 * 
	 * @param value 参数值
	 * @return 编码后的参数值
	 */
	public static String encode(String value) {
		return encode(value, DEFAULT_CHARSET);
	}

	/**
	 * 使用指定的字符集反编码请求参数值。
	 * 
	 * @param value 参数值
	 * @param charset 字符集
	 * @return 反编码后的参数值
	 */
	public static String decode(String value, String charset) {
		String result = null;
		if (!StringUtils.isEmpty(value)) {
			try {
				result = URLDecoder.decode(value, charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	/**
	 * 使用指定的字符集编码请求参数值。
	 * 
	 * @param value 参数值
	 * @param charset 字符集
	 * @return 编码后的参数值
	 */
	public static String encode(String value, String charset) {
		String result = null;
		if (!StringUtils.isEmpty(value)) {
			try {
				result = URLEncoder.encode(value, charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	private static Map<String, String> getParamsFromUrl(String url) {
		Map<String, String> map = null;
		if (url != null && url.indexOf('?') != -1) {
			map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
		}
		if (map == null) {
			map = new HashMap<String, String>();
		}
		return map;
	}

	/**
	 * 从URL中提取所有的参数。
	 * 
	 * @param query URL地址
	 * @return 参数映射
	 */
	public static Map<String, String> splitUrlQuery(String query) {
		Map<String, String> result = new HashMap<String, String>();

		String[] pairs = query.split("&");
		if (pairs != null && pairs.length > 0) {
			for (String pair : pairs) {
				String[] param = pair.split("=", 2);
				if (param != null && param.length == 2) {
					result.put(param[0], param[1]);
				}
			}
		}

		return result;
	}

	public String buildForm(String baseUrl, RequestParametersHolder requestHolder) {
		return null;
	}

	public static String buildForm(String baseUrl, Map<String, String> parameters) {
		java.lang.StringBuffer sb = new StringBuffer();
		sb.append("<form name=\"punchout_form\" method=\"post\" action=\"");
		sb.append(baseUrl);
		sb.append("\">\n");
		sb.append(buildHiddenFields(parameters));

		sb.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n");
		sb.append("</form>\n");
		sb.append("<script>document.forms[0].submit();</script>");
		java.lang.String form = sb.toString();
		return form;
	}

	private static String buildHiddenFields(Map<String, String> parameters) {
		if (parameters == null || parameters.isEmpty()) {
			return "";
		}
		java.lang.StringBuffer sb = new StringBuffer();
		Set<String> keys = parameters.keySet();
		for (String key : keys) {
			String value = parameters.get(key);
			// 除去参数中的空值
			if (key == null || value == null) {
				continue;
			}
			sb.append(buildHiddenField(key, value));
		}
		java.lang.String result = sb.toString();
		return result;
	}

	private static String buildHiddenField(String key, String value) {
		java.lang.StringBuffer sb = new StringBuffer();
		sb.append("<input type=\"hidden\" name=\"");
		sb.append(key);

		sb.append("\" value=\"");
		//转义双引号
		String a = value.replace("\"", "&quot;");
		sb.append(a).append("\">\n");
		return sb.toString();
	}

}
