/** 
 * Copyright (c) 2016, RITS All Rights Reserved. 
 * 
 */ 
package com.tencent.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import cn.com.rits.app.yunprint.util.Constant;

/** 
 * @ClassName: WCPSSLSocketFactory <br/> 
 * @Description: 用于Common-HttpClient3.1使用SSL  <br/> 
 * 
 * @author woniu 
 * @date: 2018年08月20日 更新 <br/>
 * @version  
 * @since JDK 1.6 
 */
public class WCPSSLSocketFactory implements SecureProtocolSocketFactory {

	private SSLContext sslcontext = null;

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port, localAddress, localPort);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort,
			HttpConnectionParams params)
					throws IOException, UnknownHostException, ConnectTimeoutException {

		if (params == null) {
			throw new IllegalArgumentException("Parameters should not be null");
		}

		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress, localPort);
		} else {
			Socket socket = socketfactory.createSocket();
			SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	private SSLContext createSSLContext() throws IOException {
		SSLContext sslcontext = null;
		InputStream instream = null;
		KeyStore keyStore = null;
		try {
			String certPath = Configure.getCertLocalPath();
			if (certPath == null || certPath.trim().isEmpty()) {
				instream = getClass().getResourceAsStream(Constant.CERT_FILE_NAME);
			} else {
				instream = new FileInputStream(new File(certPath));
			}
			
			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(instream, Configure.getCertPassword().toCharArray());//设置证书密码
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch(Exception e) {
				}

			}
		}

		if (keyStore == null) {
			return null;
		}
		
		KeyManager[] kms = null;
		try {
			kms = loadKeyMaterial(keyStore, Configure.getCertPassword().toCharArray());
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		
		if (kms == null) {
			return null;
		}
		
		try {
			sslcontext = SSLContext.getInstance("TLSv1");
			sslcontext.init(kms, 
					new TrustManager[] { new DefaultTrustManager() },
					new SecureRandom());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} 

		return sslcontext;
	}
	


	private static class DefaultTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}

	private SSLContext getSSLContext() throws IOException {
		if (this.sslcontext == null) {
			this.sslcontext = createSSLContext();
		}

		if (this.sslcontext == null) {
			throw new IOException("KeyStore Load failed.");
		}
		return this.sslcontext;
	}

	/**
	 * 
	 * @Title: loadKeyMaterial  
	 * @Description: 加载  KeyManager(不带Alias)
	 *
	 * @param keystore
	 * @param keyPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 */
	private KeyManager[] loadKeyMaterial(KeyStore keystore, char[] keyPassword)
			throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
		KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmfactory.init(keystore, keyPassword);
		KeyManager[] kms = kmfactory.getKeyManagers();
		return kms;
	}
}
