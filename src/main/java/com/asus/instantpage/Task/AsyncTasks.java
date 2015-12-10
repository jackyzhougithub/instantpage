package com.asus.instantpage.Task;

import com.asus.instantpage.Utils.Utils;
import com.asus.instantpage.share.ShareWindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;


public class AsyncTasks {
	
	private volatile static AsyncTasks uniqueInstantce;
	
	public static AsyncTasks getInstance(){
		if(uniqueInstantce == null){
			synchronized (AsyncTasks.class) {
				if(uniqueInstantce == null){
					uniqueInstantce = new AsyncTasks();
				}
			}
		}
		return uniqueInstantce;
	}
	

	public MergeBitmapAsyncTask generateMergeBitmapAsyncTask(Bitmap doodleBitmap){
		return new MergeBitmapAsyncTask(doodleBitmap);
	}
	
	public AsyncTaskPostExecuteShare generateAsyncTaskPostExecuteShare(Context context,ShareWindow shareWindow) {
		return new AsyncTaskPostExecuteShare(context,shareWindow);
	}
	
	private interface IAsyncTaskPostExecute{
		void onPreSaveExecute(String path);
		void onSavePostExecute(String path);
		void onMergePostExecute(Bitmap bitmap);
	}
	public AsyncTaskPostExcuteSaveBitmap generateAsyncTaskPostExcuteSaveBitmap(Context context){
		return new AsyncTaskPostExcuteSaveBitmap(context);
	}
	private class AsyncTaskPostExcuteSaveBitmap implements IAsyncTaskPostExecute{

		Context mContext = null;
		public AsyncTaskPostExcuteSaveBitmap(Context context){
			mContext = context;
		}
		
		@Override
		public void onPreSaveExecute(String path) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSavePostExecute(String path) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMergePostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub		
			SaveBitmapAsyncTask task=	new SaveBitmapAsyncTask(mContext);
			task.addAsyncTaskPostExecute(this);
			task.execute(bitmap);
		}
		
	}
	
	public class AsyncTaskPostExecuteShare implements IAsyncTaskPostExecute{

		private ShareWindow mShareWindow = null;
		private Context mContext ;
		private Handler mHandler = null;
		public AsyncTaskPostExecuteShare(Context context,ShareWindow shareWindow){
			mContext = context;
			mShareWindow = shareWindow;
		}
		
		public void setHandler(Handler handler){
			mHandler = handler;
		}
		

		@Override
		public void onPreSaveExecute(String path) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSavePostExecute(String path) {
			// TODO Auto-generated method stub
			if(mHandler != null){
				mHandler.sendEmptyMessage(Utils.SHARECOMPLETED);
				mContext.sendBroadcast(new Intent(Utils.HIDEINSTANTPAGE));
				
			}
			if(mShareWindow!=null && mShareWindow.isShareClicked()){
				mShareWindow.reSetShareClicked();
				mShareWindow.shareTo(path);
			}
			
		}

		
		@Override
		public void onMergePostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub
			SaveBitmapAsyncTask task=	new SaveBitmapAsyncTask(mContext);
			task.addAsyncTaskPostExecute(this);
			task.setHandler(mHandler);
			task.execute(bitmap);
		}
	
	}
	
	public class SaveBitmapAsyncTask extends AsyncTask<Bitmap, Void, String>{
		
		private String saveString= null;
		private IAsyncTaskPostExecute mOnPostExecute=null;
		private Context mContext;
		private Handler mHandler = null;
		public void addAsyncTaskPostExecute(IAsyncTaskPostExecute execute){
			mOnPostExecute=execute;
		}
		
		public void setHandler(Handler handler){
			mHandler = handler;
		}
		
		public SaveBitmapAsyncTask(Context context) {
			//saveString=context.getResources().getString(R.string.quicknote_savehint_dlg_saved);
			mContext = context;
		}
		
		@Override
		protected String doInBackground(Bitmap... arg0) {
			// TODO Auto-generated method stub
			String path= QuickNoteImageMerge.storeBitmap(mContext,arg0[0]);
			for (Bitmap bitmap : arg0) {
				bitmap.recycle();
				bitmap=null;
			}
			return path;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
			mOnPostExecute.onPreSaveExecute(saveString);
		}
		
		@Override
		protected void onPostExecute(String result) {
			QuickNoteImageMerge.updatePicturesContentDatbase(mContext);
			if (mOnPostExecute!=null) {
				if (saveString!=null) {
					mOnPostExecute.onSavePostExecute(saveString);
					saveString=null;
				}else {
					mOnPostExecute.onSavePostExecute(result);
				}
			}
		}
		
	}

	public class MergeBitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {
		private IAsyncTaskPostExecute mOnPostExecute=null;
	
		private Bitmap mDoodleBitmap = null;
		private Bitmap mScreenBitmap = null;
		
		public MergeBitmapAsyncTask (Bitmap doodleBitmap){

			mDoodleBitmap = doodleBitmap;
		}
		
		public void addAsyncTaskPostExecute(IAsyncTaskPostExecute execute){
			mOnPostExecute=execute;
		}
		
		public void setScreenBitmap(Bitmap screenshot){
			mScreenBitmap = screenshot;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();

		}

		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);

			if (mOnPostExecute!=null && null!= result) {
				mOnPostExecute.onMergePostExecute(result);
			}
		}

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			// TODO 自动生成的方法存根
			//do something
			
			return mDoodleBitmap;
		}
	
	}
}
