package com.asus.instantpage.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.asus.instantpage.R;

@SuppressLint("InflateParams")
public class HintPopup extends PopupWindow{
	private TextView mContentControl;
	
	public HintPopup(Context context){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentControl = (TextView) inflater.inflate(R.layout.asus_air_view_hint, null);
		this.setContentView(mContentControl);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(false);
		this.setOutsideTouchable(true);
	}
	
	public void destory(){
		mContentControl = null;
	}
	

	public void setText(String content){
		//mContentControl.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		if(mContentControl != null){
			mContentControl.setText(content);
		}
	}
		
}