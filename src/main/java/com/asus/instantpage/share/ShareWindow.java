package com.asus.instantpage.share;

import java.util.List;

import com.asus.instantpage.R;
import com.asus.instantpage.Utils.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class ShareWindow {
	private Context mContext;
	private View mainView;
	private Dialog mDlg=null;
	protected Bitmap mCurrentDoodleBitmap = null;
	protected Bitmap mScreenshot = null;
	private ProgressDialog mProgressDialog = null;
	public ShareWindow(Context context){
		mContext=context;
	}
	
	public void dismissDialog(){

		if (mDlg!=null) {
			mDlg.dismiss();
			mDlg=null;
			mainView = null;
		}

	}
	
	public boolean isShowing(){
		if (mDlg!=null) {
			return mDlg.isShowing();
		}
		return false;
	}
	
	public void showShareUIWindow(Bitmap currentDoodleBitmap,Bitmap screenshot){
		reSetShareClicked();
		dismissDialog();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    mainView=inflater.inflate(R.layout.quick_note_sharelist_popu, null, false);
	    mainView.setFocusableInTouchMode(true);
	    mDlg=new Dialog(mContext);
	    mDlg.setTitle(mContext.getResources().getString(R.string.quick_note_share_to));
	    mDlg.setContentView(mainView, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	  	mDlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
	  	mCurrentDoodleBitmap = currentDoodleBitmap;
	  	mScreenshot = screenshot;
		prepareData();
		setDataToViewGroup((ViewGroup)mainView);
		mDlg.show();
	}
	
	private List<ShareableAppInfo> shareAppList=null;
	private ShareableAppInfo runningActivity=null;
	private void prepareData(){
		ShareQuickNote sqn=new ShareQuickNote(mContext);
		shareAppList=sqn.getShareableAppInfo();
		 runningActivity=sqn.getRunningActivity(shareAppList);
		 if (runningActivity !=null) {
			shareAppList.remove(runningActivity);
		}

	}
	
	private void setDataToViewGroup(ViewGroup vg){
		ListView lv=(ListView)vg.findViewById(R.id.quick_note_shareableapps);
		if (shareAppList!=null) {
			ShareListAdapter adapter=new ShareListAdapter(shareAppList,runningActivity, mContext);
			lv.setAdapter(adapter);
			
			 lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						shareClicked=true;
						currentAppInfo=(ShareableAppInfo)arg0.getAdapter().getItem(arg2);
						if (currentAppInfo == null) { // to super note
							mainView.setVisibility(View.INVISIBLE);
							clicked();
							superNoteItemClicked();
						}else {							
							shareListItemClicked(currentAppInfo);
							clicked();
							showProgreeDialog();
							
						}
					}
				}); 
		}		
	}
	
	private void showProgreeDialog(){
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setProgressStyle(R.style.progress_dialog);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
		mProgressDialog.setMessage(mContext.getResources().getString(R.string.sharing));
		mProgressDialog.show();	
	}
	protected Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Utils.SHARECOMPLETED:
				if(mProgressDialog != null){
					mProgressDialog.dismiss();
				}
				break;

			default:
				break;
			}
		};
	};
	protected void clicked(){

		dismissDialog();
	}
	
	protected ShareableAppInfo currentAppInfo=null;
	protected boolean shareToAppNotSupernote=true;
	protected boolean shareClicked=false;
	public boolean isShareClicked(){
		return shareClicked;
	}
	public void reSetShareClicked(){
		shareClicked=false;
	}
	
    abstract public void shareListItemClicked(ShareableAppInfo shareableAppInfo);
    abstract public void superNoteItemClicked();
    abstract public boolean shareTo(String contentPath);
    abstract public boolean shareToSuperNote();
}
