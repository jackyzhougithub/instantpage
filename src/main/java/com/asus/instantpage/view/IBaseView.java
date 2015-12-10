package com.asus.instantpage.view;

import com.asus.draw.doodleview.DoodleView;
import com.asus.instantpage.Utils.Utils;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;


public abstract class IBaseView{
	protected View mView;
	protected DoodleView mDoodleView;
	
	@SuppressLint("NewApi")
	public void setImageAvailable(boolean available,View view){
		if(view == null)
			return;
    	int alpha = available ? Utils.BUTTON_ALPHA_NEW_ENABLE : Utils.BUTTON_ALPHA_NEW_DISABLE;
        if (view instanceof ImageView) {
        	((ImageView)view).setImageAlpha(alpha);
		}
        view.setEnabled(available);
	}
	
	public void setViewEnable(boolean isEnable){
		if(mView!=null){
			mView.setEnabled(isEnable);
		}
	}
	
	public void setObserveView(View view){
		mView = view;
	}
	
	public void setDoodleView(DoodleView doodleView){
		mDoodleView = doodleView;
	}
	
	public abstract void obServeView();
	public abstract void destory();
	public abstract void setImageSeclected(View view ,boolean isSelected);
}
