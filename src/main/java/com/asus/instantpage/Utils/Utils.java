package com.asus.instantpage.Utils;

import com.asus.instantpage.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class Utils {
	
	//事件
	public static final int IS_LOCK_SCREEN_OPEN = 2;
	public static final String SHOWINSTANTPAGT = "showinstatnpage";
	public static final String HIDEINSTANTPAGE = "hideinstantpage";
	public static final String HIDEANDSAVEINSTANTPAGE = "hideandsaveinstantpage";
	public static final String OPENINSTANTPAGE = "openinstantpage";
	public static final String CLOSEINSTANTPAGE = "closeinstantpage";
	public static final int SETSCREENSHOT = 4;
	public static boolean CURRENTISSHOW = false;
	public static final int SHARECOMPLETED = 5;
	public static final int STOPSERVICE = 6;
	public static final int RECREATEUI = 7;
	
	public static long SCREENSHOTTIME = 1200;
	public static long DELAYSTOP = 1000;
	
	public static final String SYSTEM_REASON = "reason";  
	public static final String SYSTEM_HOME_KEY = "homekey";//home key  
	public static final String SYSTEM_RECENT_APPS = "recentapps";//long home key  
	
	//android l screenshot
	public static final String MEDIA_PROJECTION_SERVICE = "media_projection";
	public static final int VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR = 16;
	public static final int CAPTUREIMAGEFORMAT = PixelFormat.RGBA_8888;
	// 全局参数
	public static final int BUTTON_ALPHA_NEW_DISABLE = (int) (255*0.25);
	public static final int BUTTON_ALPHA_NEW_ENABLE =255;
	public static final String MODEL = android.os.SystemProperties.get("ro.product.model");
	public static String PREFERENCE_NAME = "InstantPage";
	public static final String SYSTEM_DIR = Environment
			.getExternalStorageDirectory().toString();
	public static final String DIR = Environment.getExternalStorageDirectory()
			+ "/InstantPage/";
	public static final String PICTURES_DIR=SYSTEM_DIR+"/Pictures/";
	public static final String INSTANT_PAGE_PIC=PICTURES_DIR+"InstantPage/";
	public static int CONTENT_WIDTH;
	public static int CONTENT_HEIGHT;
	
	//0x00000800
	public static int SYSTEM_UI_FLAG_IMMERSIVE = 2048;
	//static method
	public static Rect getStatuBarRect(Context context){
		Rect rect = new Rect();
		Point displayPoint = getDisplayPoint(context);
		Point statuBarPoint = caculateStatuBarHeight(displayPoint,context);
		rect.left=rect.top = 0;
		rect.right = displayPoint.x - statuBarPoint.x;
		rect.bottom = displayPoint.y - statuBarPoint.y;
		int w = rect.width();
		return rect;
	}
	
	@SuppressLint("NewApi")
	public static Point getDisplayPoint(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display dp = windowManager.getDefaultDisplay();
		Point point = new Point();
		int ver = Build.VERSION.SDK_INT;
		if (ver >= 17) {
			dp.getRealSize(point);
		} else {
			dp.getSize(point);
		}
		return point;
	}
	
	private static Point caculateStatuBarHeight(Point displayPoint,Context context) {
		Point statubarPoint = new Point();
		TextView tempView = new TextView(context);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(80,
				80, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		// paramsMain.x=-point.x;
		params.x = 0;
		params.y = 0;
		tempView.setBackgroundColor(Color.TRANSPARENT);
		Rect temp = new Rect();
		windowManager.addView(tempView, params);
		tempView.getWindowVisibleDisplayFrame(temp);
		statubarPoint.x = displayPoint.x - temp.right;
		statubarPoint.y = displayPoint.y - temp.bottom;
		windowManager.removeView(tempView);
		tempView = null;
		
		return statubarPoint;
	}
	
}
