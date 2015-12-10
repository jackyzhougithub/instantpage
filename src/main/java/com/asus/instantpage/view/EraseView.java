package com.asus.instantpage.view;

import java.util.ArrayList;
import java.util.List;

import com.asus.draw.doodleview.DoodleView;
import com.asus.draw.doodleview.DoodleView.InputMode;
import com.asus.draw.erasepopupwindow.ErasePopupWindow;
import com.asus.draw.erasepopupwindow.ErasePopupWindow.EraseType;
import com.asus.draw.erasepopupwindow.ErasePopupWindow.onEraseChangedListener;
import com.asus.draw.modelmanager.IModelManager;
import com.asus.instantpage.R;
import com.asus.instantpage.Utils.Listener.PickerAndEraseListener;
import com.asus.instantpage.tools.DrawTools;
import com.asus.instantpage.view.AsusDialog.IDialogListener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class EraseView extends IBaseView implements View.OnClickListener {
	private ErasePopupWindow mErasePopupWindow;
	private EraseType mEraseType = EraseType.TYPE1;
	private EraseType mRecordEraseType;
	private Handler mHandler = new Handler();
	private Context mContext;
	private IModelManager mIModelManager;
	private List<PickerAndEraseListener> listeners;

	public EraseView(View view, Context context, DoodleView doodleView) {
		mContext = context;
		mView = view;
		mDoodleView = doodleView;
		listeners = new ArrayList<PickerAndEraseListener>();
		mErasePopupWindow = new ErasePopupWindow(mContext);
		mIModelManager = mDoodleView.getModelManager();
		initEraserPopup();
	}

	@Override
	public void obServeView() {
		// TODO 自动生成的方法存根
		mView.setOnClickListener(this);
	}

	@Override
	public void destory() {
		// TODO 自动生成的方法存根
		if (mErasePopupWindow != null) {
			mErasePopupWindow.dismiss();
			mErasePopupWindow = null;
		}
	}

	@Override
	public void setImageSeclected(View view, boolean isSelected) {
		// TODO 自动生成的方法存根
		ImageView eraseView = (ImageView) mView;
		try {
			int drawableId = DrawTools.drawCurrentEraserIcon(false, false,
					mEraseType, isSelected);
			if (drawableId != -1) {
				eraseView.setImageResource(drawableId);
			} else {
				// eraseView.setImageResource(DrawTools.drawCurrentEraserIcon(selected,
				// false, EraseType.TYPE2));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void initEraserPopup() {

		mErasePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mErasePopupWindow.setOutsideTouchable(true);
		mErasePopupWindow.setFocusable(true);
		mEraseType = EraseType.TYPE2;
		mRecordEraseType = mEraseType;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mErasePopupWindow.setEraseType(mEraseType);
			}
		});

		mErasePopupWindow
				.addOnEraseChangedListener(new onEraseChangedListener() {

					@Override
					public void onEraseChanged(float width) {
						// TODO Auto-generated method stub
						if (mDoodleView == null || mView == null)
							return;
						if (width == -1) {
							// empty
							showCommonDialog_Clear();

						} else if (width >= 0) {
							mDoodleView.setEraseWidth(width);
							mEraseType = ErasePopupWindow
									.getEraseTypeByWidth(width);
							mRecordEraseType = mEraseType;
							if (mDoodleView.getInputMode() == InputMode.ERASE) {
								setImageSeclected(mView, true);
							}
						}
					}
				});
	}

	private void showCommonDialog_Clear() {

		if (doodleviewHasStrokes()) {
			AsusDialog.getInstance(mContext)
					.showDialog(
							mClearDialogListener,
							mContext.getResources().getString(
									R.string.clear_all_alert));
		} else {
			mErasePopupWindow.setEraseType(mEraseType);
		}
	}

	private void fireEraseViewStatuChanged(boolean isSelected) {
		if (listeners != null && listeners.size() > 0) {
			for (PickerAndEraseListener listener : listeners) {
				listener.statuChange(R.id.note_kb_d_eraser, isSelected);
			}
		}
	}
	
	public void addStatuChangeLiteners(PickerAndEraseListener listener){
		if(listener != null){
			listeners.add(listener);
		}
	}

	IDialogListener mClearDialogListener = new IDialogListener() {

		@Override
		public void positiveClick() {
			// TODO Auto-generated method stub
			mDoodleView.clearStrokes();
			// mEraseType = EraseType.CLEAR;
			if (mErasePopupWindow.isShowing()) {
				mErasePopupWindow.dismiss();
			}
			mErasePopupWindow.setEraseType(mEraseType);
			setImageSeclected(mView, true);
		}

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub

		}

		@Override
		public void negativeClick() {
			// TODO Auto-generated method stub
			if (mErasePopupWindow.isShowing()) {
				mErasePopupWindow.dismiss();
			}
		}
	};

	public boolean doodleviewHasStrokes() {
		boolean hasstrokes = true;
		if (mIModelManager.getInsertableObjectList().isEmpty())
			hasstrokes = false;
		return hasstrokes;
	}

	private void eraserClick() {
		mDoodleView.exitSelectionMode();
		fireEraseViewStatuChanged(true);
		if (mEraseType == EraseType.CLEAR) {
			mEraseType = mRecordEraseType; //
		}

		if (mView.isSelected()) {

			mDoodleView.setInputMode(InputMode.ERASE);
			int xOffset = mContext.getResources().getDimensionPixelSize(
					R.dimen.gb_erasepopupwindow_x_offset);
			int yOffset = mContext.getResources().getDimensionPixelSize(
					R.dimen.gb_erasepopupwindow_y_offset);
			mErasePopupWindow.showAsDropDown(mView, xOffset, yOffset);
			mErasePopupWindow.setEraseType(mEraseType);

		} else {
			mView.setSelected(true);
			setImageSeclected(mView, true);
			if (mDoodleView.getInputMode() != InputMode.ERASE) {
				mDoodleView.setInputMode(InputMode.ERASE);
			}

		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		eraserClick();
	}

}
