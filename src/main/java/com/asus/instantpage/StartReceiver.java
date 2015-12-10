package com.asus.instantpage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO 自动生成的方法存根
		Log.d("InstantPage", "received and start");
		Intent intent = new Intent(arg0,InstatnPageService.class);
		arg0.startService(intent);
		
	}

}
