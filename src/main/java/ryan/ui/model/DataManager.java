package ryan.ui.model;

import java.util.ArrayList;

import cn.woniu.onlinepay.model.PayProfile;

/** 
 * @ClassName: DataManager <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author Woniu 
 * @version  
 * @since JDK 1.6 
 */
public class DataManager {
	
	private static DataManager instance = new DataManager();

	private DataManager() {
		init();
	}

	public synchronized static DataManager getInstance() {
		return instance;
	}
	
	private ArrayList<PayProfile> profiles = new ArrayList<PayProfile>();

	public ArrayList<PayProfile> getProfiles() {
		return profiles;
	}
	
	
	public void init() {
		//TODO 读取PayProfiles
		
	}
	
}
