//package com.asus.instantpage.screenshot.androidl;
//
//import java.nio.ByteBuffer;
//
//import com.asus.instantpage.Utils.Utils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.hardware.display.VirtualDisplay;
//import android.media.Image;
//import android.media.ImageReader;
//import android.media.projection.MediaProjection;
//import android.media.projection.MediaProjectionManager;
//import android.os.Handler;
//import android.util.DisplayMetrics;
//
//
//@SuppressLint("NewApi")
//public class ScreenShotSingleton {
//	private volatile static ScreenShotSingleton mScreenShotSingleton;
//	private MediaProjection mMediaProjection;
//    private MediaProjectionManager mProjectionManager;
//	private ImageReader mImageReader;
//	private int mScreenDensity;
//    private int mDisplayWidth ;
//    private int mDisplayHeight;
//    private int mRecordresultCode = -4;
//    private Intent mRecordData;
//    private Context mContext;
//    
//    public static ScreenShotSingleton getInstance(Context context){
//    	if(mScreenShotSingleton == null){
//    		synchronized (ScreenShotSingleton.class) {
//				if(mScreenShotSingleton == null){
//					mScreenShotSingleton = new ScreenShotSingleton(context);
//				}
//			}
//    	}
//    	return mScreenShotSingleton;
//    }
//    
//
//    
//    private ScreenShotSingleton(Context context){
//    	mContext = context;
//        mProjectionManager =
//                (MediaProjectionManager) context.getSystemService(Utils.MEDIA_PROJECTION_SERVICE);
//
//    }
//    
//	public void setDisplayWidthAndHeight(int width,int height){
//		mDisplayWidth = width;
//		mDisplayHeight = height;
//	}
//	
//	public void setScreenDensity(int screenDensity){
//		mScreenDensity = screenDensity;
//	}
//	
//	public int getScreenDensity(){
//		return mScreenDensity;
//	}
//	
//	public int getResultCode(){
//		return mRecordresultCode;
//	}
//	
//	public void setResultCode(int result){
//		mRecordresultCode = result;
//	}
//	
//	public ImageReader getImageReader(){
//		return mImageReader;
//	}
//	
//	public void reSetImageReader(int width ,int height){
//		mDisplayWidth = width;
//		mDisplayHeight = height;
//		if(mImageReader == null){
//			mImageReader = ImageReader.newInstance(width, height, Utils.CAPTUREIMAGEFORMAT, 2);
//			//mImageReader.setOnImageAvailableListener(imageAvailableListener, null);
//		}
//	}
//	
//	public void createVirtualDisplayForRecord(){
//		//mMediaProjection = mProjectionManager.getMediaProjection(mRecordresultCode, mRecordData);
//		if(mMediaProjection != null){
//			createVirtualDisplay();
//		}
//	}
//	
//	@SuppressLint("NewApi")
//	public void createVirtualDisplay(int resultCode,Intent data){
//		mRecordresultCode = resultCode;
//		mRecordData = data;
//		mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
//		if(mMediaProjection != null){
//			createVirtualDisplay();
//		}
//	}
//	
//    private VirtualDisplay createVirtualDisplay() {
//    	 
//        return mMediaProjection.createVirtualDisplay("ScreenSharingDemo",
//                mDisplayWidth, mDisplayHeight, mScreenDensity,
//                Const.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//                /*mSurface*/mImageReader.getSurface(), null /*Callbacks*/, null /*Handler*/);
//    }
//
//
//    // func
//    public Bitmap getScreenshotBitmap(){
//
//    	Bitmap bitmap = getBitmapFromImageReader(mImageReader);
//    	return bitmap;
//    }
//    
//    @SuppressLint("NewApi")
//	private Bitmap getBitmapFromImageReader(ImageReader imageReader){
//    	Image image = null;
//    	try {
//            image = imageReader.acquireLatestImage();
//            int width = image.getWidth();
//            int height = image.getHeight();
//            final Image.Plane[] planes = image.getPlanes();
//            final ByteBuffer buffer = planes[0].getBuffer();
//            int pixelStride = planes[0].getPixelStride();
//            int rowStride = planes[0].getRowStride();
//            int rowPadding = rowStride - pixelStride * width;
//            
//            DisplayMetrics metrics = new DisplayMetrics();
//            metrics.density = mScreenDensity;
//
//            Bitmap bitmap = Bitmap.createBitmap(metrics,width, height, Bitmap.Config.ARGB_8888);
//
//            BitmapUtilNative.convertRGBAToARGB(bitmap,buffer,pixelStride,rowPadding);
//            return Bitmap.createBitmap(bitmap, 0, 0, Utils.CONTENT_WIDTH, Utils.CONTENT_HEIGHT);
//
//        } catch (Exception e) {
//            if (image!=null)
//                image.close();
//        }
//    	return null;
//    }
//    
//    public void release(){
//    	if(mImageReader != null){
//    		mImageReader = null;
//    	}
//    	if(mUIManager != null){
//    		mUIManager = null;
//    	}
//
//    }
//    
//}
