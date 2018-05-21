/** 
 * Copyright (c) 2018, RITS All Rights Reserved. 
 * 
 */ 
package ryan.ui.swt;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** 
 * @ClassName: Wordings <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author woniu 
 * @date: 2018年3月2日 上午11:09:24 <br/>
 * @version  
 * @since JDK 1.6 
 */
public class Wordings {
	
	/**
	 * 名字请参考 resource/lans 下的properties文件的前缀名
	 * @see yinjitool_zh_CN.properties
	 */
	private static String BASE = "tool";
	
	private static Wordings instance;
	
	private ResourceBundle resourceBundle;
	
	/**
	 * 默认是中文
	 */
	private Locale locale = Locale.CHINA;
	
	private Wordings() {
		try {
			locale = Locale.CHINA;
			resourceBundle = ResourceBundle.getBundle(BASE, locale);
		
		} catch(NullPointerException e) {
			e.printStackTrace();
			
		} catch(MissingResourceException e) {
			e.printStackTrace();
		} 
	}
	
	public static synchronized Wordings getInstance() {
		if (instance == null) {
			instance = new Wordings();
		}
		return instance;
	}
	
	public String getText(String key) {
		if (resourceBundle == null) {
			return key;
		}
		
		if (key == null) {
			return "";
		}
		
		return resourceBundle.getString(key);
	}

}
