package com.asus.instantpage.screenshot;



import java.lang.reflect.Method;

import com.asus.instantpage.Utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceControl;

import static android.view.WindowManager.LayoutParams.*;
// 肯能会再次用到androidl的screenshort
public class ScreenShot {

	//from WindowManagerService
		/** How much to multiply the policy's type layer, to reserve room
	     * for multiple windows of the same type and Z-ordering adjustment
	     * with TYPE_LAYER_OFFSET. */
	    static final int TYPE_LAYER_MULTIPLIER = 10000;


	    /** Offset from TYPE_LAYER_MULTIPLIER for moving a group of windows above
	     * or below others in the same layer. */
	    static final int TYPE_LAYER_OFFSET = 1000;
		public static Bitmap getAllScreenShot(int w,int h){
			Bitmap bitmap=null;
			final int ver=Build.VERSION.SDK_INT;
			if (ver<=17) {
				Method method=null;
				try {
					method =Surface.class.getMethod("screenshot", int.class,int.class);
					if (method!=null) {
					bitmap=(Bitmap)	method.invoke(null, new Object[]{w,h});
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}else {
				// SurfaceControl5248b3
				Method method=null;
				try {
					method =SurfaceControl.class.getMethod("screenshot", int.class,int.class);
					if (method!=null) {
					bitmap=(Bitmap)	method.invoke(null, new Object[]{w,h});
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			return bitmap;
		}
		
		private static float getDegreesForRotation(int value) {  
			  switch (value) {  
			  case Surface.ROTATION_90:  
			   return 360f - 90f;  
			  case Surface.ROTATION_180:  
			   return 360f - 180f;  
			  case Surface.ROTATION_270:  
			   return 360f - 270f;  
			  }  
			  return 0f;  
		} 
		/***
		 * return the screen shot less than the window type
		 * @param w
		 * @param h
		 * @param windowType
		 * @return Bitmap
		 */

		public static void setContext(Context context){

		}
		public static Bitmap getScreenShotByWindowType(int w,int h,int rotaion,int windowType){
			Bitmap bitmap=null;
			final int ver=Build.VERSION.SDK_INT;
//			w = w -UIManager.mHomeBar_X;
//			h = h - UIManager.mHomeBar_Y;
			float[] dims = {w,h};

			Matrix mDisplayMatrix = new Matrix(); 
	        float degrees = getDegreesForRotation(rotaion);   
	        
	        boolean requiresRotation = (degrees > 0);   
	        if (requiresRotation) {   
	            // Get the dimensions of the device in its native orientation      
	        	mDisplayMatrix.reset();  
	        	mDisplayMatrix.preRotate(-degrees);  
	        	mDisplayMatrix.mapPoints(dims); 
	            dims[0] = Math.abs(dims[0]);   
	            dims[1] = Math.abs(dims[1]);   
	        }   
			if (ver<=17) {
				Method method=null;
				try {
					method =Surface.class.getMethod("screenshot", int.class,int.class,int.class,int.class);
					if (method!=null) {
					bitmap=(Bitmap)	method.invoke(null, new Object[]{dims[0],dims[1],0,getZOrderByWindowType(windowType)-1});
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}else {
				Method method=null;
				try {
					Class<?> classtype = Class.forName("android.view.SurfaceControl");
					
					method =classtype.getMethod("screenshot", Rect.class,int.class,int.class,int.class,int.class,boolean.class,int.class);
					if (method!=null) {
					    bitmap=(Bitmap)	method.invoke(null, new Object[]{new Rect(),(int)dims[0],(int)dims[1],0,getZOrderByWindowType(windowType)-1,false,Surface.ROTATION_0});
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}

			
		  if (requiresRotation && bitmap != null) {  
	            // Rotate the screenshot to the current orientation  
	            Bitmap ss = Bitmap.createBitmap(w,  h, Bitmap.Config.ARGB_8888);  
	            Canvas c = new Canvas(ss);  
	            c.translate(ss.getWidth() / 2, ss.getHeight() / 2);  
	            c.rotate(degrees);  
	            c.translate(-dims[0] / 2, -dims[1] / 2);  
	            c.drawBitmap(bitmap, 0, 0, null);  
	            c.setBitmap(null);  
	            bitmap = ss;  
	        } 
		  // - homebar

		  
		  bitmap=  Bitmap.createBitmap(bitmap,0,0, Utils.CONTENT_WIDTH,Utils.CONTENT_HEIGHT);

		  return bitmap;
		}
		
		private static int getZOrderByWindowType(int wType){
			return windowTypeToLayerLw(wType)*TYPE_LAYER_MULTIPLIER+TYPE_LAYER_OFFSET;
		}
		/***
		 * from PhoneWindowManager.windowTypeToLayerLw(int type)
		 * @param type
		 * @return
		 */
		 public static int windowTypeToLayerLw(int type) {
		        if (type >= FIRST_APPLICATION_WINDOW && type <= LAST_APPLICATION_WINDOW) {
		            return 2;
		        }
		        switch (type) {
		        case TYPE_UNIVERSE_BACKGROUND:
		            return 1;
		        case TYPE_WALLPAPER:
		            // wallpaper is at the bottom, though the window manager may move it.
		            return 2;
		        case TYPE_PHONE:
		            return 3;
		        case TYPE_SEARCH_BAR:
		            return 4;
		        case TYPE_RECENTS_OVERLAY:
		        case TYPE_SYSTEM_DIALOG:
		            return 5;
		        case TYPE_TOAST:
		            // toasts and the plugged-in battery thing
		            return 6;
		        case TYPE_PRIORITY_PHONE:
		            // SIM errors and unlock.  Not sure if this really should be in a high layer.
		            return 7;
		        case TYPE_DREAM:
		            // used for Dreams (screensavers with TYPE_DREAM windows)
		            return 8;
		        case TYPE_SYSTEM_ALERT:
		            // like the ANR / app crashed dialogs
		            return 9;
		        case TYPE_INPUT_METHOD:
		            // on-screen keyboards and other such input method user interfaces go here.
		            return 10;
		        case TYPE_INPUT_METHOD_DIALOG:
		            // on-screen keyboards and other such input method user interfaces go here.
		            return 11;
		        case TYPE_KEYGUARD:
		            // the keyguard; nothing on top of these can take focus, since they are
		            // responsible for power management when displayed.
		            return 12;
		        case TYPE_KEYGUARD_DIALOG:
		            return 13;
		        case TYPE_STATUS_BAR_SUB_PANEL:
		            return 14;
		        case TYPE_STATUS_BAR:
		            return 15;
		        case TYPE_STATUS_BAR_PANEL:
		            return 16;
		        case TYPE_VOLUME_OVERLAY:
		            // the on-screen volume indicator and controller shown when the user
		            // changes the device volume
		            return 17;
		        case TYPE_SYSTEM_OVERLAY:
		            // the on-screen volume indicator and controller shown when the user
		            // changes the device volume
		            return 18;
		        case TYPE_NAVIGATION_BAR:
		            // the navigation bar, if available, shows atop most things
		            return 19;
		        case TYPE_NAVIGATION_BAR_PANEL:
		            // some panels (e.g. search) need to show on top of the navigation bar
		            return 20;
		        case TYPE_SYSTEM_ERROR:
		            // system-level error dialogs
		            return 21;
		        case TYPE_MAGNIFICATION_OVERLAY:
		            // used to highlight the magnified portion of a display
		            return 22;
		        case TYPE_DISPLAY_OVERLAY:
		            // used to simulate secondary display devices
		            return 23;
		        case TYPE_DRAG:
		            // the drag layer: input for drag-and-drop is associated with this window,
		            // which sits above all other focusable windows
		            return 24;
		        case TYPE_SECURE_SYSTEM_OVERLAY:
		            return 25;
		        case TYPE_BOOT_PROGRESS:
		            return 26;
		        case TYPE_POINTER:
		            // the (mouse) pointer layer
		            return 27;
		        case TYPE_HIDDEN_NAV_CONSUMER:
		            return 28;
		        }
		        return 2;
		    }
}
