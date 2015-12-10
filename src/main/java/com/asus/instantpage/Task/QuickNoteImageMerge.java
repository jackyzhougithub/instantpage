package com.asus.instantpage.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.asus.instantpage.Utils.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


public class QuickNoteImageMerge {
		 	
	public static String storeBitmap(Context context,Bitmap bitmap){
		File file=new File(Utils.INSTANT_PAGE_PIC);
		 if (file.exists() == false) {
	            file.mkdirs();
	            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
	                       + Environment.getExternalStorageDirectory())));
	     }
		 SimpleDateFormat dateformat=new SimpleDateFormat("yyyyMMdd_HHmmss");
		 String name="InstantNote_"+dateformat.format(new Date()) + ".png";
		 String path = Utils.INSTANT_PAGE_PIC + name;
		 try {
	            file = new File(path);
	            FileOutputStream fos = new FileOutputStream(file);
	            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
	            bitmap.recycle();
	            fos.flush();
	            fos.close();
	            // insert by Media store
	            ContentValues values = new ContentValues();
	            ContentResolver resolver = context.getContentResolver();
	            values.put(MediaStore.Images.ImageColumns.DATA, path);
	            values.put(MediaStore.Images.ImageColumns.TITLE, name);
	            values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, name);
	            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN,  System.currentTimeMillis());
	            values.put(MediaStore.Images.ImageColumns.DATE_ADDED,  System.currentTimeMillis());
	            values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED,  System.currentTimeMillis());	          
	            values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png");
	            values.put(MediaStore.Images.ImageColumns.SIZE, file.length());
	            
	            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        }
	        catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
		 return path;
	}
	
	public static void updatePicturesContentDatbase(Context context){
//		if (context!=null) {
//			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
//                    + MetaData.INSTANT_PAGE_PIC)));
//		}
	}
	
	public static Bitmap zoomBitmap(Bitmap bitmap, float sx, float sy) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }
}
