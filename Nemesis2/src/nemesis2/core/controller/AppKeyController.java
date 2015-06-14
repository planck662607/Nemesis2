package nemesis2.core.controller;

import java.util.Arrays;

import com.jogamp.newt.event.KeyEvent;

public class AppKeyController {
	
	private static AppKeyController instance;
	private boolean[] keytab;
	
	private AppKeyController() {
		keytab=new boolean[256];
		Arrays.fill(keytab, false);
	}
	
	public static AppKeyController getInstance() {
		if(instance==null) instance=new AppKeyController();
		return instance;
	}
	
	// dangereux mais plus perf. il faudra necessairement un getInstance avant
	public static boolean[] getKeys() {return instance.keytab;}
	public static boolean isKey(int key) {return instance.keytab[key];};
	public static void setKey(int key, boolean val) {
		if(key<instance.keytab.length)
			instance.keytab[key]=val;
	};
	public static boolean isUp() {return isKey(KeyEvent.VK_UP);}
	public static boolean isDown() {return isKey(KeyEvent.VK_DOWN);}
	public static boolean isRight() {return isKey(KeyEvent.VK_RIGHT);}
	public static boolean isLeft() {return isKey(KeyEvent.VK_LEFT);}
	public static boolean isSpace() {return isKey(KeyEvent.VK_SPACE);}
	public static boolean isAlt() {return isKey(KeyEvent.VK_ALT);}
	public static boolean isAltGr() {return isKey(KeyEvent.VK_ALT_GRAPH);}
	public static boolean isShift() {return isKey(KeyEvent.VK_SHIFT);}

}
