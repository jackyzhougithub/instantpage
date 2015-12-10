package com.asus.instantpage.view;

import com.asus.draw.doodleview.DoodleView;
import com.asus.instantpage.R;
import com.asus.instantpage.Utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;


public class QuickNoteFrameLayout extends FrameLayout {
	private DoodleView mDoodleView;
	
	public QuickNoteFrameLayout(Context context, AttributeSet attrs) {

		super(context, attrs);
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mDoodleView=(DoodleView)findViewById(R.id.paintView);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO 自动生成的方法存根
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			mContext.sendBroadcast(new Intent(Utils.HIDEANDSAVEINSTANTPAGE));
		}
		
		return super.dispatchKeyEvent(event);
	}
	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(arg0, l, t, r, b);
	}

}