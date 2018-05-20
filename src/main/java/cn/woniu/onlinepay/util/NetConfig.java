/**
 * 
 */
package cn.woniu.onlinepay.util;

import java.io.File;

/**
 * @author Ryan
 *
 */
public class NetConfig {

	private static NetConfig self = new NetConfig();
	
	private static final String CONFIG_FILE_NAME = "net.config.properties";
	
	private static final String CONFIG_FILE_PATH = "conf" + File.separator + CONFIG_FILE_NAME;
	
	private PropertiesHelper properties = new PropertiesHelper();
	
	public enum CONFIG_KEY {
		PROXY_HOST,
		PROXY_PORT,
		PROXY_USER,
		PROXY_PWD,
	}
	
	private static final String[] DEF_VALUE = {
		"",
		"80",
		"",
		"",
	};
	
	private NetConfig(){
		load();
	}

	private void load() {
		properties.load(CONFIG_FILE_PATH);
	}
	
	public synchronized static NetConfig getInstance() {
		return self;
	}
	
	public void recover(){
		load();
	}
	
	public void save() {
		properties.save(CONFIG_FILE_PATH);
	}
	
	public void setPropery(CONFIG_KEY key, String value) {
		properties.setPropery(key.toString(), value);
	}
	
	public String getProperty(CONFIG_KEY key) {
		return properties.getProperty(key.toString(), DEF_VALUE[key.ordinal()]);
	}
	
	public String getProxyHost()
	{
		return getProperty(CONFIG_KEY.PROXY_HOST);
	}
	
	public void setProxyHost(String proxyHost) {
		setPropery(CONFIG_KEY.PROXY_HOST, proxyHost);
	}
	
	public int getProxyPort()
	{
		String value = getProperty(CONFIG_KEY.PROXY_PORT);
		int number = 0;
		try
		{
			number = Integer.valueOf(value);
		}
		catch(NumberFormatException e)
		{
			number = Integer.valueOf(DEF_VALUE[CONFIG_KEY.PROXY_PORT.ordinal()]);
		}
		return number;
	}
	
	public void setProxyPort(int proxyPort) {
		setPropery(CONFIG_KEY.PROXY_PORT, String.valueOf(proxyPort));
	}
	
	public String getProxyUser()
	{
		return getProperty(CONFIG_KEY.PROXY_USER);
	}
	
	public void setProxyUser(String proxyUser) {
		setPropery(CONFIG_KEY.PROXY_USER, proxyUser);
	}
	
	public String getProxyPwd()
	{
		return getProperty(CONFIG_KEY.PROXY_PWD);
	}
	
	public void setProxyPwd(String proxyPwd) {
		setPropery(CONFIG_KEY.PROXY_PWD, proxyPwd);
	}
	
	public String getDefaultHttpResponseEncode()
	{
		return "UTF-8";
	}

}
