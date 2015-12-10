package com.asus.instantpage.share;

import java.util.ArrayList;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class ShareQuickNote {
	private Context mContext;
	private String mPackageName  =null;
	public ShareQuickNote(Context context){
		mContext=context;
		mPackageName = mContext.getPackageName();
	}
	
	public List<ShareableAppInfo> getShareableAppInfo(){
		PackageManager pm=mContext.getPackageManager();
		Intent intent=new Intent(Intent.ACTION_SEND,null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("image/*");
		List<ShareableAppInfo> retAppList=null;
		List<ResolveInfo> rInfo=pm.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		if (rInfo!=null&&rInfo.size()>0) {
			retAppList=new ArrayList<ShareableAppInfo>(rInfo.size());
			for (ResolveInfo resolveInfo : rInfo) {
				if (resolveInfo.activityInfo.packageName.equals(mPackageName) ) {
					continue;
				}
				retAppList.add(new ShareableAppInfo(resolveInfo,mContext));
			}
		}
		return retAppList;
		
	}
	
	public ShareableAppInfo getRunningActivity(List<ShareableAppInfo> src){
		if (src==null||src.size()<=0) {
			return null;
		}
		ActivityManager am=(ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);  
		List<RunningTaskInfo>tasks=am.getRunningTasks(1);
		if(tasks!=null&&tasks.size()>0){
			RunningTaskInfo taskInfo=	tasks.get(0);
			ComponentName name= taskInfo.topActivity;
			if (name.getPackageName().equals(mContext.getPackageName())) {
				return null;
			}
			for (ShareableAppInfo appInfo : src) {
				if (appInfo.getPackageName().equals(name.getPackageName())) {
					return appInfo;
				}
			}
		}
		return null;
	}
	
}
