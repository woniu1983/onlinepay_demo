package cn.woniu.onlinepay.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import cn.woniu.onlinepay.log.Logger;



public class CusProxy {
	
	private static CusProxy _proxy = new CusProxy();
	
	private CusProxy() {	
		init();
	}
	
	public static CusProxy getInstance() {
		return _proxy;
	}
	
	private void init() {
		if (NetConfig.getInstance().getProxyHost() != null && !NetConfig.getInstance().getProxyHost().isEmpty()) {
			this.setUseHTTPProxy(true);
			this.setHttpHost(NetConfig.getInstance().getProxyHost());
			this.setHttpPort(NetConfig.getInstance().getProxyPort());
		} else {
			this.setUseHTTPProxy(false);
		}

		if (NetConfig.getInstance().getProxyUser() != null && !NetConfig.getInstance().getProxyUser().isEmpty()) { 
			this.setUseHTTPAuth(true);
			this.setHttpUserName(NetConfig.getInstance().getProxyUser());
			this.setHttpPwd(NetConfig.getInstance().getProxyPwd());
		} else {
			this.setUseHTTPAuth(false);
		}
	}

	private boolean isUseHTTPProxy = true;
	private boolean isUseHTTPAuth = false;
	private boolean isUseSOCKSProxy = false;
	private boolean isUseSOCKSAuth = false;
	String httpHost = "";
	private int httpPort = 80;
	private String httpUserName = "";
	private String httpPwd = "";
	
	private String socksHost = "";
	private int socksPort = 80;
	private String socksUserName = "";
	private String socksPwd = "";
	
	public void setProxy() {
		
		init();
		
		Logger.Test("actual isUseHTTPProxy : " + isUseHTTPProxy);
		Logger.Test("actual isUseHTTPAuth : " + isUseHTTPAuth);
		Logger.Test("actual proxy host : " + httpHost);
		Logger.Test("actual proxy httpPort : " + httpPort);
		Logger.Test("actual proxy httpUserName : " + httpUserName);
		Logger.Test("actual proxy httpPwd : " + httpPwd);
		
			if (isUseHTTPProxy) {
				// HTTP/HTTPS Proxy
				System.setProperty("http.proxyHost", httpHost);
				System.setProperty("http.proxyPort", String.valueOf(httpPort));
				System.setProperty("https.proxyHost", httpHost);
				System.setProperty("https.proxyPort", String.valueOf(httpPort));
				if (isUseHTTPAuth) {
					// String encoded =
					// Base64.getEncoder().encodeToString((httpUserName + ":" +
					// httpPwd).getBytes());
					// con.setRequestProperty("Proxy-Authorization", "Basic " +
					// encoded);
					Authenticator.setDefault(new ProxyAuth(httpUserName,
							httpPwd));
				}
			}
			if (isUseSOCKSProxy) {
				// SOCKS Proxy
				System.setProperty("socksProxyHost", String.valueOf(socksPort));
				System.setProperty("socksProxyPort", String.valueOf(socksPort));
				if (isUseSOCKSAuth) {
					System.setProperty("java.net.socks.username", socksUserName);
					System.setProperty("java.net.socks.password", socksPwd);
					Authenticator.setDefault(new ProxyAuth(socksUserName,
							socksPwd));
				}
			}
	}
	
	public class ProxyAuth extends Authenticator {
	    private PasswordAuthentication auth;

	    private ProxyAuth(String user, String password) {
	        auth = new PasswordAuthentication(user, password == null ? new char[]{} : password.toCharArray());
	    }

	    protected PasswordAuthentication getPasswordAuthentication() {
	        return auth;
	    }
	}

	/**
	 * @return the isUseHTTPProxy
	 */
	public boolean isUseHTTPProxy() {
		return isUseHTTPProxy;
	}

	/**
	 * @param isUseHTTPProxy the isUseHTTPProxy to set
	 */
	public void setUseHTTPProxy(boolean isUseHTTPProxy) {
		this.isUseHTTPProxy = isUseHTTPProxy;
	}

	/**
	 * @return the isUseHTTPAuth
	 */
	public boolean isUseHTTPAuth() {
		return isUseHTTPAuth;
	}

	/**
	 * @param isUseHTTPAuth the isUseHTTPAuth to set
	 */
	public void setUseHTTPAuth(boolean isUseHTTPAuth) {
		this.isUseHTTPAuth = isUseHTTPAuth;
	}

	/**
	 * @return the httpHost
	 */
	public String getHttpHost() {
		return httpHost;
	}

	/**
	 * @param httpHost the httpHost to set
	 */
	public void setHttpHost(String httpHost) {
		this.httpHost = httpHost;
	}

	/**
	 * @return the httpPort
	 */
	public int getHttpPort() {
		return httpPort;
	}

	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * @return the httpUserName
	 */
	public String getHttpUserName() {
		return httpUserName;
	}

	/**
	 * @param httpUserName the httpUserName to set
	 */
	public void setHttpUserName(String httpUserName) {
		this.httpUserName = httpUserName;
	}

	/**
	 * @return the httpPwd
	 */
	public String getHttpPwd() {
		return httpPwd;
	}

	/**
	 * @param httpPwd the httpPwd to set
	 */
	public void setHttpPwd(String httpPwd) {
		this.httpPwd = httpPwd;
	}
}
