package com.asus.instantpage;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.asus.draw.doodleview.DoodleView;//鐢诲浘jar鍖�
import com.asus.instantpage.Utils.Utils;
import com.asus.instantpage.view.ActionBar;
import com.asus.instantpage.view.FuncBarFrameLayout;
import com.asus.instantpage.view.QuickNoteFrameLayout;


/*
 * author = jacky_zhou
 */
public class InstantPageWindow {
	//
	private Context mContext = null;
	private DoodleView mDoodleView = null;
	private WindowManager mWindowManager = null;
	private QuickNoteFrameLayout mMainView = null;
	private int mStatusBarHeight = 0;
	private WindowManager.LayoutParams mParamsMain;
	private WindowManager.LayoutParams mParamsFuncBar;
	private WindowManager.LayoutParams mDragLayoutParams;
	private FuncBarFrameLayout mDoodleActionBarParent;
	
	private ActionBar mActionBar;//


	public InstantPageWindow(Context context){
		mContext = context;
		initUi();
	}
	
	// 

	//
	private void initUi(){
		mWindowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainView = (QuickNoteFrameLayout) inflater.inflate(R.layout.quicknote_main_view, null);
		mDoodleActionBarParent = (FuncBarFrameLayout) inflater.inflate(
				R.layout.quicknote_editor_func_bar_doodle, null);
		mDoodleView = (DoodleView) mMainView.findViewById(R.id.paintView);
		
		initLayouts();
		addViews();
		Utils.CURRENTISSHOW = true;
		initOperation();
	}
	
	private void initLayouts(){
		Rect contentRect = Utils.getStatuBarRect(mContext);
		Utils.CONTENT_HEIGHT = contentRect.height();
		Utils.CONTENT_WIDTH = contentRect.width();//
		mParamsMain = new WindowManager.LayoutParams(contentRect.width(),
				contentRect.height(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
						| WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
						& ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.RGBA_8888);
		mParamsMain.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		mParamsMain.gravity = Gravity.LEFT | Gravity.TOP;
		mParamsMain.x = 0;
		mParamsMain.y = mStatusBarHeight;

		mParamsFuncBar = new WindowManager.LayoutParams(contentRect.width(),
				LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
				PixelFormat.RGBA_8888);
		mParamsFuncBar.gravity = Gravity.TOP | Gravity.LEFT;
		mParamsFuncBar.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		mParamsFuncBar.x = 0;
		mParamsFuncBar.y = 0;
		
		mDragLayoutParams = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
				PixelFormat.RGBA_8888);
		mDragLayoutParams.gravity = Gravity.TOP | Gravity.CENTER;
		mDragLayoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		mDragLayoutParams.x = 0;
		mDragLayoutParams.y = 0;

	}
	
	private void initOperation(){
		mActionBar = new ActionBar(mDoodleActionBarParent,mContext,mDoodleView);
	}
	
	public void addViews(){
		mWindowManager.addView(mMainView, mParamsMain);
		mWindowManager.addView(mDoodleActionBarParent, mParamsFuncBar);
		hideUi();
	}
	
	public void removeViews(){
		mWindowManager.removeView(mMainView);
		mWindowManager.removeView(mDoodleActionBarParent);
	}
	
	public void showUi(){
		mMainView.setVisibility(View.VISIBLE);
		mDoodleActionBarParent.setVisibility(View.VISIBLE);
		mActionBar.operateBar(true);//show bar

		// 延时设置背景，等动画完成后设置
		mActionBar.setDoodleViewBackground();
		Utils.CURRENTISSHOW = true;
	}
	
	public void hideAndSave(){
		mActionBar.saveAndHide();
	}
	
	public void hideUi(){
		hideUiKeepSt();
		Utils.CURRENTISSHOW = false;
	}
	
	public void hideUiKeepSt(){
		mMainView.setVisibility(View.GONE);
		mDoodleActionBarParent.setVisibility(View.GONE);
	}
	public void destroy(){
		removeViews();
		mMainView = null;
		mDoodleView = null;
		mActionBar.destory();
		mDoodleActionBarParent = null;
		
	}
	
}
