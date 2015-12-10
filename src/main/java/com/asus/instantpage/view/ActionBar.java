package com.asus.instantpage.view;

import com.asus.draw.doodleview.DoodleView;
import com.asus.draw.doodleview.commandsmanager.ICommandsManager;
import com.asus.instantpage.R;
import com.asus.instantpage.Task.StoreBitmap;
import com.asus.instantpage.Utils.Listener.PickerAndEraseListener;
import com.asus.instantpage.Utils.Utils;
import com.asus.instantpage.screenshot.ScreenShot;
import com.asus.instantpage.view.AsusDialog.IDialogListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

//管理bar上的view及行为
//功能类
public class ActionBar implements View.OnClickListener {
	private FuncBarFrameLayout mActionBarLayout;
	private PickerView mPickerViewObserver;
	private ShareView mShareViewObserver;
	private EraseView mEraseViewObserver;
	private SaveView mSaveViewObserver;
	protected boolean mIsPenOnly = false;
	private DoodleView mDoodleView;
	private Context mContext;
	
	//local observerview 
	private ImageView mUndoView;
	private ImageView mRedoView;
	private ImageView mPenOnlyView;
	private ImageView mCloseView;

	public ActionBar(FuncBarFrameLayout actionbar,Context context,DoodleView doodleView) {
		mActionBarLayout = actionbar;
		setDoodleView(doodleView);
		mContext = context;
		initOnservers();
		observer();
	}
	
	private void setDoodleView(DoodleView doodleView){
		mDoodleView = doodleView;//将画布传人，在这边处理相关功能
		mDoodleView.setOnTouchListener(doodleTouchListener);
	}

	public void destory() {
		mActionBarLayout = null;
		mPickerViewObserver = null;
		mEraseViewObserver = null;
		mSaveViewObserver = null;
		mShareViewObserver = null;
		mDoodleView = null;
	}
	
	private void observer(){
		mSaveViewObserver.obServeView();
		mEraseViewObserver.obServeView();
		mPickerViewObserver.obServeView();
		mShareViewObserver.obServeView();
		
		setUndoRedoAvaiable();
	}

	private void initOnservers() {
		View view = mActionBarLayout.findViewById(R.id.note_kb_share);
		mShareViewObserver = new ShareView(view,mContext,mDoodleView);
		view = mActionBarLayout.findViewById(R.id.note_kb_d_brush);
		mPickerViewObserver = new PickerView(view,mContext,mDoodleView);
		mPickerViewObserver.addStatuChangeLiteners(pickerAndEraseListener);
		view = mActionBarLayout.findViewById(R.id.note_kb_d_eraser);
		mEraseViewObserver = new EraseView(view,mContext,mDoodleView);
		mEraseViewObserver.addStatuChangeLiteners(pickerAndEraseListener);
		view = mActionBarLayout.findViewById(R.id.note_kb_save);
		mSaveViewObserver = new SaveView(mContext, mDoodleView, view);
		// 简单的事件在actionbar监测

		mRedoView =(ImageView) mActionBarLayout.findViewById(R.id.note_kb_redo);
		mRedoView.setOnClickListener(this);
		mUndoView =(ImageView) mActionBarLayout.findViewById(R.id.note_kb_undo);
		mUndoView.setOnClickListener(this);
		mPenOnlyView = (ImageView) mActionBarLayout.findViewById(R.id.note_kb_pen_only);
		mPenOnlyView.setOnClickListener(this);
		mCloseView = (ImageView) mActionBarLayout.findViewById(R.id.note_kb_close);
		mCloseView.setOnClickListener(this);

		// 隐藏一些views
		view = mActionBarLayout.findViewById(R.id.note_kb_play);
		view.setVisibility(View.GONE);
		view = mActionBarLayout.findViewById(R.id.note_kb_merge);
		view.setVisibility(View.GONE);
		view = mActionBarLayout.findViewById(R.id.note_kb_d_shape);
		view.setVisibility(View.GONE);
	}
	
	private PickerAndEraseListener pickerAndEraseListener = new PickerAndEraseListener() {
		
		@Override
		public void statuChange(int id, boolean isSelected) {
			// TODO 自动生成的方法存根
			switch (id) {
			case R.id.note_kb_d_brush:
				mPickerViewObserver.setImageSeclected(null, isSelected);
				mEraseViewObserver.setImageSeclected(null, !isSelected);
				break;
			case R.id.note_kb_d_eraser:
				mEraseViewObserver.setImageSeclected(null, isSelected);
				mPickerViewObserver.setImageSeclected(null, !isSelected);
				break;
			default:
				break;
			}
			
		}
	};

	public void operateBar(boolean show) {
		if (show) {
			mActionBarLayout.setVisibility(View.VISIBLE);
			mActionBarLayout.openFuncBar();
		} else {
			mActionBarLayout.closeFuncBar();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		switch (arg0.getId()) {
		case R.id.note_kb_redo:
			if(mDoodleView != null){
				mDoodleView.redo();
			}
			break;
		case R.id.note_kb_undo:
			if(mDoodleView != null){
				mDoodleView.undo();
			}
			break;
        case R.id.note_kb_pen_only:
        	mIsPenOnly = !mIsPenOnly;
        	hide3SysBtn(mIsPenOnly);
        	setImageSeclected(mPenOnlyView, mIsPenOnly);
        	break;
        case R.id.note_kb_close:
        	showComfirm_close();
        	break;
		default:
			break;
		}

	}
	
	private void hide3SysBtn(boolean hide){
		if(hide){
			hideSystemUI();
		}else {
			showSystemUI();
		}
	}
	
	 private void hideSystemUI() {
	       
	       mDoodleView.setSystemUiVisibility(
	               View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//	                     | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//	                     | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                       | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//	                     | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	                       | Utils.SYSTEM_UI_FLAG_IMMERSIVE
	                       );
	    }

	    private void showSystemUI() {
	       int systemUiVisibility = mDoodleView.getSystemUiVisibility();
	       int hideNavigation = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
	       if (systemUiVisibility % (2 * hideNavigation) >= hideNavigation) {
	    	   mDoodleView.setSystemUiVisibility(systemUiVisibility - hideNavigation);
	       }
	    }

	


	
	private void showComfirm_close(){
		AsusDialog.getInstance(mContext).showDialog(mDialogListener, mContext.getResources().getString(R.string.leave_instantpage));
	}
	IDialogListener mDialogListener = new IDialogListener() {

		@Override
		public void positiveClick() {
			// TODO 自动生成的方法存根
			mContext.sendBroadcast(new Intent(Utils.HIDEINSTANTPAGE));
		}

		@Override
		public void negativeClick() {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onDismiss() {
			// TODO 自动生成的方法存根
			
		}
		
	};
	// 处理本地简单事件
	OnTouchListener doodleTouchListener = new OnTouchListener(){

		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			//如果是penonly 且当前不是笔输入则 return true
			if(mIsPenOnly  && (MotionEvent.TOOL_TYPE_FINGER == arg1.getToolType(0))){
				return true;
			}
			return false;
		}
		
	};
	
	public void setImageSeclected(View view, boolean isSelected) {
		// TODO 自动生成的方法存根
		if(!isSelected){
			switch (view.getId()) {
			case R.id.note_kb_undo:
				mUndoView.setImageResource(R.drawable.asus_instantpg_undo_n);
				break;
			case R.id.note_kb_redo:
				mRedoView.setImageResource(R.drawable.asus_instantpg_redo_n);
				break;
			case R.id.note_kb_pen_only:
				mPenOnlyView.setImageResource(R.drawable.asus_instantpg_pen_only_d);
				break;
			default:
				break;
			}
		}else {
			switch (view.getId()) {
			case R.id.note_kb_undo:
				mUndoView.setImageResource(R.drawable.asus_instantpg_undo_d);
				break;
			case R.id.note_kb_redo:
				mRedoView.setImageResource(R.drawable.asus_instantpg_redo_d);
				break;
			case R.id.note_kb_pen_only:
				mPenOnlyView.setImageResource(R.drawable.asus_instantpg_pen_only_n);
				break;
			default:
				break;
			}
		}

	}
	
	private void initViewStatus(){
		mUndoView.setEnabled(false);
		mRedoView.setEnabled(false);
		setImageSeclected(mUndoView, false);
		setImageSeclected(mRedoView, false);
		setImageSeclected(mPenOnlyView, false);
	}
	
	private void setUndoRedoAvaiable(){
		mDoodleView
        .addOnDoAvailabilityChangedListener(new ICommandsManager.onDoAvailabilityChangedListener() {

	        @SuppressLint("NewApi")
			@Override
	        public void onUndoAvailabilityChanged(boolean available) {
		        // TODO Auto-generated method stub
	        	setImageSeclected(mUndoView, available);
	        	mUndoView.setEnabled(available);

	        }

	        @SuppressLint("NewApi")
			@Override
	        public void onRedoAvailabilityChanged(boolean available) {
		        // TODO Auto-generated method stub
	        	setImageSeclected(mRedoView, available);
	        	mRedoView.setEnabled(available);
	        }
        });
	}
	
	public void setDoodleViewBackground(){
		mHandler.sendEmptyMessageDelayed(Utils.SETSCREENSHOT,Utils.SCREENSHOTTIME);
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Utils.SETSCREENSHOT:
				Point point = Utils.getDisplayPoint(mContext);
				int rotation = StoreBitmap.getRotation(mContext);
				ScreenShot.setContext(mContext);
				//该方法需要系统权限，如果要unbandle 请用androidl里的方法
				
				Bitmap bitmap = ScreenShot.getScreenShotByWindowType((int) point.x, 
						(int) point.y,rotation,WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
				Log.d("InstatnPageService","setBackgroundBitmap " + bitmap);
				mDoodleView.setBackgroundBitmap(bitmap);
				break;

			default:
				break;
			}

		};
	};

	public void saveAndHide(){
		if(mSaveViewObserver != null){
			mSaveViewObserver.save();
		}
	}
}
