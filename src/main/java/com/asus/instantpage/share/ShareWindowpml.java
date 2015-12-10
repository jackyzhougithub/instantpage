package com.asus.instantpage.share;

import java.io.File;

import com.asus.draw.doodleview.DoodleView;
import com.asus.instantpage.Task.AsyncTasks;
import com.asus.instantpage.Task.AsyncTasks.AsyncTaskPostExecuteShare;
import com.asus.instantpage.Task.AsyncTasks.MergeBitmapAsyncTask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class ShareWindowpml extends ShareWindow{

	private Context mContext = null;
	private DoodleView mDoodleView;

	public ShareWindowpml(Context context,DoodleView doodleView) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		mDoodleView = doodleView;

	}
	
	private Intent getShareGraphicIntent(String path,ShareableAppInfo appInfo){
		if (path==null||appInfo==null) {
			return null;
		}
		Intent intent=new Intent(Intent.ACTION_SEND);
		appInfo.setShareIntentCompnentName(intent);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		return intent;
	}

	@Override
	public void shareListItemClicked(ShareableAppInfo shareableAppInfo) {
		// TODO Auto-generated method stub
		
		shareToAppNotSupernote = true;
		
		MergeBitmapAsyncTask task= AsyncTasks.getInstance().generateMergeBitmapAsyncTask(mCurrentDoodleBitmap) ;
		AsyncTaskPostExecuteShare execute = AsyncTasks.getInstance().generateAsyncTaskPostExecuteShare(mContext,this);
		execute.setHandler(mHandler);
		task.addAsyncTaskPostExecute(execute);
		task.setScreenBitmap(mScreenshot);
		task.execute();			
	}

	@Override
	public void superNoteItemClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shareTo(String contentPath) {
		// TODO Auto-generated method stub
		if(shareToAppNotSupernote){
			Intent intent=getShareGraphicIntent(contentPath, currentAppInfo);
			mContext.startActivity(intent);
		}
		if(mDoodleView != null)
			mDoodleView.clearStrokes();

		return shareToAppNotSupernote;
	}

	@Override
	public boolean shareToSuperNote() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void destory(){
		if(mCurrentDoodleBitmap != null){
			mCurrentDoodleBitmap.recycle();
			mCurrentDoodleBitmap = null;
		}
		if(mScreenshot != null){
			mScreenshot.recycle();
			mScreenshot = null;
		}
		//mContext = null;
			
	}

}
