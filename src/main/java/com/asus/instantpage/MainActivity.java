package com.asus.instantpage;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
//		startService(new Intent(this,InstatnPageService.class));
//		this.finish();
		Log.d("InstantPage", "send");
		Intent  intent = new Intent("com.asus.pen.action.INSTANTPAGE_EVENT");
		this.sendStickyBroadcast(intent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
