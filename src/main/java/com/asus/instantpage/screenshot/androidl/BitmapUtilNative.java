package com.asus.instantpage.screenshot.androidl;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;

public class BitmapUtilNative {
    static {
        System.loadLibrary("jni_jpegutil2");
    }
    
	public static native int convertRGBAToARGB(Bitmap bitmap,ByteBuffer buff,int pixelStride,int rowPadding);

}
