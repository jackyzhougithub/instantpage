package com.asus.instantpage.Task;

import com.asus.instantpage.screenshot.ScreenShot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

public class StoreBitmap {
	
	private Context mContext;
	
	public StoreBitmap(Context context){
		mContext = context;
		
	}
	
	private class StoreBitmapAsyncTaskWithParam extends AsyncTask<Bitmap, Void, Void>
	{
		Bitmap bitmap;
		@Override
		protected Void doInBackground(Bitmap... arg0) {
			// TODO Auto-generated method stub
			bitmap = arg0[0];
			try {
			//	Bitmap mergedbitmap = mergeCurrentBitmap(MergeKind.SCREENSHOT,bitmap);
				StoregeBitmap( bitmap);
				
				bitmap = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
		
	}
	
	public void StoreBitmapInBackground(Bitmap bitmap) {
		StoreBitmapAsyncTaskWithParam task = new StoreBitmapAsyncTaskWithParam();
		task.execute(bitmap);
	}
	
	private void StoregeBitmap(Bitmap bitmap) {
		if(bitmap!= null)
		{
			QuickNoteImageMerge.storeBitmap(mContext, bitmap);
			QuickNoteImageMerge.updatePicturesContentDatbase(mContext);
			
		}
	}
	
	private Bitmap getScreenBitmap(int w,int h){
		return ScreenShot.getAllScreenShot(w, h);
	}
	
	private Bitmap getScreenBitmapByLayer(int w,int h,int rotation, int windowType){
		return ScreenShot.getScreenShotByWindowType(w, h,rotation, windowType);
	}
	
	public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
		if (background == null) {
			return foreground;
		}
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		int fgWidth = foreground.getWidth();
		int fgHeight = foreground.getHeight();
		Bitmap newmap = Bitmap
				.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(newmap);
		canvas.drawBitmap(background, 0, 0, null);
		canvas.drawBitmap(foreground, 0, 0, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newmap;
	}	
		
	public static int getRotation(Context context){
		WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
		Display display = mWindowManager.getDefaultDisplay(); 
		
		return display.getRotation(); 
	}

}
