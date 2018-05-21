/** 
 * Copyright (c) 2018, RITS All Rights Reserved. 
 * 
 */ 
package ryan.ui.swt;

import java.awt.Toolkit;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.woniu.onlinepay.model.PayProfile;
import ryan.ui.model.DataManager;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/** 
 * @ClassName: MainWindow <br/> 
 * @Description: TODO  <br/> 
 * 
 * @author Woniu 
 * @date: 2018年5月21日 下午7:23:13 <br/>
 * @version  
 * @since JDK 1.6 
 */
public class MainWindow {
	
	
	private static MainWindow mainWindow = new MainWindow();

	private MainWindow() {

	}

	public synchronized static MainWindow getInstance() {
		return mainWindow;
	}
	

	public static void main(String[] args) {
		try {
			MainWindow window = MainWindow.getInstance();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Shell shell;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
//		try {
//			DeployMetaData.getInstance().init();
//		} catch (DeployException e) {
//			e.printStackTrace();
//			return;
//		}
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.MIN);
		shell.setImage(SWTResourceManager.getImage(UIConstant.RES_IMG_DIR + "launcher.png"));
		shell.setSize(1024, 768);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setText(Wordings.getInstance().getText("tool.title"));
		
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;
		if (shellH > screenH)
			shellH = screenH;
		if (shellW > screenW)
			shellW = screenW;
		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem configMenuParent = new MenuItem(menu, SWT.CASCADE);
		configMenuParent.setText(Wordings.getInstance().getText("profile.config"));
		
		Menu menuAdd = new Menu(configMenuParent);
		configMenuParent.setMenu(menuAdd);
		
		MenuItem menuAddItem = new MenuItem(menuAdd, SWT.NONE);
		menuAddItem.setText(Wordings.getInstance().getText("profile.add"));
		
		new MenuItem(menuAdd, SWT.SEPARATOR);
		
		initProfile();
	}
	
	private void initProfile() {
		ArrayList<PayProfile> profiles = DataManager.getInstance().getProfiles();
		
	}
}
