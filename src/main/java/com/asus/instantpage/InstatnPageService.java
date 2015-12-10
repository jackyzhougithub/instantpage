package com.asus.instantpage;

import com.asus.instantpage.Utils.Utils;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/*
 * 入口 启动service开启即使笔记
 */
public class InstatnPageService extends Service {
	private InstantPageWindow mInstantPageWindow;
	private boolean mIsLockScreen = false;
	private String TAG = "InstatnPageService";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO 自动生成的方法存根
		super.onConfigurationChanged(newConfig);
		mInstantPageWindow.destroy();
		mServiceHandler.sendEmptyMessageDelayed(Utils.RECREATEUI, 400);

	}

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		Log.d(TAG,"onCreate");
		// 处理启动显示任务，该service不会常驻
		mInstantPageWindow = new InstantPageWindow(this);
		mInstantPageWindow.showUi();
		try {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			filter.addAction(Utils.HIDEINSTANTPAGE);
			filter.addAction(Utils.SHOWINSTANTPAGT);
			filter.addAction(Utils.HIDEANDSAVEINSTANTPAGE);
			filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);//系统home按键
			registerReceiver(broadcastReceiver, filter);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自动生成的方法存根
		Log.d(TAG,"onStartCommand");
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		try {
			unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

	Handler mServiceHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Utils.IS_LOCK_SCREEN_OPEN:
				handleScreenOnButLocked();
				break;
			case Utils.STOPSERVICE:
				stopService();
				break;
			case Utils.RECREATEUI:
				mInstantPageWindow = new InstantPageWindow(InstatnPageService.this);
				mInstantPageWindow.showUi();
				break;
			default:
				break;

			}
		}
	};
	private void stopService(){
		this.stopSelf();
	}
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context arg0, Intent arg1) {
			String action = arg1.getAction();
			if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				mIsLockScreen = true;
				RunningLockScreenDetect = false;
				// 隐藏dragview
				mInstantPageWindow.hideUiKeepSt();

			} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
				RunningLockScreenDetect = true;				
				handleScreenOnButLocked();
			}else if (action.equals(Utils.SHOWINSTANTPAGT)) {
				if(mInstantPageWindow != null)
					mInstantPageWindow.showUi();
			}else if (action.equals(Utils.HIDEINSTANTPAGE)) {
				mInstantPageWindow.hideUi();
				mServiceHandler.sendEmptyMessageDelayed(Utils.STOPSERVICE, Utils.DELAYSTOP);
			}else if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = arg1.getStringExtra(Utils.SYSTEM_REASON);
				if (reason != null) {
					if (reason.equals(Utils.SYSTEM_HOME_KEY)) {
						// home key处理点
						mInstantPageWindow.hideAndSave();
						mServiceHandler.sendEmptyMessageDelayed(Utils.STOPSERVICE, Utils.DELAYSTOP);
					} else if (reason.equals(Utils.SYSTEM_RECENT_APPS)) {
						// long home key处理点
					}
				}
			} else if ( action.equals(Utils.HIDEANDSAVEINSTANTPAGE)) {
				mInstantPageWindow.hideAndSave();
				mServiceHandler.sendEmptyMessageDelayed(Utils.STOPSERVICE, Utils.DELAYSTOP);
			}
		};
	};

	//
	private void handleScreenOnButLocked() {
		if (RunningLockScreenDetect) {
			if (isLockSreenOpened()) {
				mIsLockScreen = false;
				RunningLockScreenDetect = false;
				
				if(Utils.CURRENTISSHOW)
					mInstantPageWindow.showUi();

			} else {
				// show
				mServiceHandler.sendEmptyMessageDelayed(Utils.IS_LOCK_SCREEN_OPEN, 300);
			}
		}
	}

	private KeyguardManager kManager = null;
	private boolean RunningLockScreenDetect = false;

	private boolean isLockSreenOpened() {
		if (kManager == null) {
			kManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		}
		return !kManager.inKeyguardRestrictedInputMode();
	}

}
