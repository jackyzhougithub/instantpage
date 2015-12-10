package com.asus.instantpage.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class ShareableAppInfo {
	private ResolveInfo mInfo;
	private Context mContext;
	public ShareableAppInfo(ResolveInfo info,Context context){
		mInfo=info;
		mContext = context;
	}
	public String getAppName(){
		//String name=mInfo.activityInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
		String name=mInfo.loadLabel(mContext.getPackageManager()).toString();
		return name;
	}
	public Drawable getAppIconDrawable(){
		Drawable d=mInfo.loadIcon(mContext.getPackageManager());
		return d;
	}
	public String getPackageName(){
		String packageName=mInfo.activityInfo.packageName;
		return packageName;
	}
	public String getClassName(){
	    String clsName=mInfo.activityInfo.name;
		return clsName;
	}
	public void setShareIntentCompnentName(Intent intent){
		if (intent!=null) {
			intent.setComponent(new ComponentName(getPackageName(), getClassName()));
		}
	}
}
