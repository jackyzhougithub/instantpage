package com.asus.instantpage.view;

import java.util.ArrayList;
import java.util.List;

import com.asus.draw.doodleview.DoodleView;
import com.asus.draw.doodleview.DoodleView.InputMode;
import com.asus.draw.drawtoolspicker.DrawToolsPicker;
import com.asus.draw.drawtoolspicker.IPickerControl;
import com.asus.draw.idrawtoolschanged.DrawToolAttribute;
import com.asus.draw.idrawtoolschanged.IDrawtoolsChanged;
import com.asus.instantpage.R;
import com.asus.instantpage.Utils.Listener.PickerAndEraseListener;
import com.asus.instantpage.Utils.Utils;
import com.asus.instantpage.tools.DrawTools;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PickerView extends IBaseView {
	private DrawToolsPicker mDrawToolPicker;
	private DoodleView mDoodleView;
	private DrawToolAttribute mCurrenDrawToolAttribute;
	private DrawTools mDrawTools;
	private View mEaserView;
	private Context mContext;
	
	//
	List<PickerAndEraseListener> listeners;
	@Override
	public void obServeView() {
		// TODO 自动生成的方法存根
		mView.setOnClickListener(onClickListener);
		
	}
	
	public PickerView(View view,Context context,DoodleView doodleView){
		mView = view;
		mDoodleView = doodleView;
		mDrawToolPicker = new DrawToolsPicker(context);
		mContext = context;
		mDrawToolPicker.add(new DrawToolsChangedListener());
		listeners = new ArrayList<PickerAndEraseListener>();
		
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mDrawToolPicker != null){
					if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
						mDrawToolPicker.setlayoutOffsetY(-410);
						mDrawToolPicker.initDrawToolPicker();
						mDrawToolPicker.setScale(0.81f);
					}else {
						mDrawToolPicker.initDrawToolPicker();
					}
				}

			}
		});
		if(mDrawTools == null){
			mDrawTools = new DrawTools(context, (ImageView)view,(ImageView)mEaserView);
		}
	}
	
	public void addStatuChangeLiteners(PickerAndEraseListener listener){
		if(listener != null){
			listeners.add(listener);
		}
	}
	
	private void firePickerStatuChageListener(boolean isSelected){
		if (listeners != null && listeners.size() > 0) {
			for (PickerAndEraseListener listener : listeners) {
				listener.statuChange(R.id.note_kb_d_brush, isSelected);
			}
		}
	}
	
	public void setEraserView(View view){
		mEaserView = view;
	}
	
	OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			firePickerStatuChageListener(true);
			pickerClick(v);
		}
	};

	
	private void pickerClick(View v){
		IPickerControl ic = mDrawToolPicker.getDrawToolPickerControl();		
		if(mDoodleView.getInputMode() != InputMode.DRAW){
			mDoodleView.setInputMode(InputMode.DRAW);
		}
			
		if(mView.isSelected())
		{		
			if (ic.isPickerShowing()) {
				ic.dismissPicker();
			} else {
				try {

					if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
							&& Utils.MODEL.contains("P01M")){
						ic.showPickerUnderView(mView,-48,-50);
					}else {
						ic.showPickerUnderView(mView,0,0);
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			//set select first 
			mView.setSelected(true);
			//高亮图标
			setImageSeclected(mView, true);
		}
	}
	
	private class DrawToolsChangedListener implements IDrawtoolsChanged {

		@Override
		public void drawToolInit(DrawToolAttribute drawToolAtt) {
			// TODO Auto-generated method stub
			if (mDoodleView != null) {
				mDoodleView.setStrokeType(drawToolAtt.type);// 设置当前画笔类型
				mDoodleView.setStrokeAttrs(drawToolAtt.type, drawToolAtt.color,
				        drawToolAtt.width, drawToolAtt.alpha);// 设置画笔属性

				mCurrenDrawToolAttribute = drawToolAtt;
				setImageSeclected(mView,true);
				
			}
		}

		@Override
		public void onDrawToolChanged(DrawToolAttribute newToolAtt,
		        DrawToolAttribute oldToolAtt, AttType[] attTypesChanged) {
			// TODO Auto-generated method stub
			if (mDoodleView != null && !newToolAtt.equals(oldToolAtt)) {
				mDoodleView.setStrokeType(newToolAtt.type);// 设置当前画笔类型
				mDoodleView.setStrokeAttrs(newToolAtt.type, newToolAtt.color,
				        newToolAtt.width, newToolAtt.alpha);// 设置画笔属性

				mCurrenDrawToolAttribute = newToolAtt;	
				setImageSeclected(mView,true);
			}
		}
		
		

	}
	
	@Override
	public void destory() {
		// TODO 自动生成的方法存根
		if(mDrawToolPicker != null){
			mDrawToolPicker.dismissPenPickerWindow();//for save brush 
		}
		mDrawToolPicker = null;
	}

	@Override
	public void setImageSeclected(View view, boolean isSelected) {
		// TODO 自动生成的方法存根
		ImageView picker = (ImageView) mView;
		picker.setImageDrawable(mDrawTools.drawCurrentBrush(isSelected, false, 
				mCurrenDrawToolAttribute.type, mCurrenDrawToolAttribute.width, mCurrenDrawToolAttribute.color));
	}
}
