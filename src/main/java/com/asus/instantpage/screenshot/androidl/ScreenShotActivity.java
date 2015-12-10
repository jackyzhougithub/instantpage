//详细可以参考老版instantpage

//package com.asus.instantpage.screenshot.androidl;
//
//import com.asus.instantpage.UI.UIManager;
//import com.asus.instantpage.util.Const;
//import com.asus.instantpage.util.MetaData;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Point;
//import android.media.projection.MediaProjectionManager;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.widget.Toast;
//
//public class ScreenShotActivity extends Activity {
//	private static final String TAG = "ScreenShotActivity";
//	private int mScreenDensity;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "getDefaultDisplay Begin");
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        mScreenDensity = metrics.densityDpi;
//        Log.d(TAG, "getDefaultDisplay end");
//        callScreenIntent();
//        Log.d(TAG, "callScreenIntent end");
//    }
//    
//    private void callScreenIntent(){
//        MediaProjectionManager projectionManager =
//                (MediaProjectionManager) getSystemService(Const.MEDIA_PROJECTION_SERVICE);
//      startActivityForResult(projectionManager.createScreenCaptureIntent(),
//      Const.PERMISSION_CODE);
//    }
//    
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	Log.d(TAG, "onActivityResult ");
//        if (requestCode != Const.PERMISSION_CODE) {
//            Log.e(TAG, "Unknown request code: " + requestCode);
//            return;
//        }
//        if (resultCode != RESULT_OK) {
//            Toast.makeText(this,
//                    "User denied screen sharing permission", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        ScreenShotSingleton screenShotSingleton = ScreenShotSingleton.getInstance(getApplicationContext());
//        screenShotSingleton.setScreenDensity(mScreenDensity);
//        if(screenShotSingleton.getImageReader() == null){
//            Point point = UIManager.getDisplayPoint(getApplicationContext());            
//        	screenShotSingleton.reSetImageReader(point.x , point.y );        	
//        }
//        screenShotSingleton.createVirtualDisplay(resultCode, data);
//        MetaData.SETSCREENPERMISSIOM = true;
//        SharedPreferences sharedPreferences = getSharedPreferences(Const.LOCALDATA, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(Const.SETSCREENSHOTPERMISSIOM, MetaData.SETSCREENPERMISSIOM);
//        editor.commit(); 
//        this.finish();
//    }
//
//}
