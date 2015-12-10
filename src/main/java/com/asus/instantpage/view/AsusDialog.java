package com.asus.instantpage.view;

import com.asus.instantpage.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.WindowManager;

public class AsusDialog {
	private volatile static AsusDialog mInstance;
	private Context mContext;
	private AlertDialog mDlg= null;
	private IDialogListener mDlgListener=null;
	private DialogInterface.OnClickListener clickListener;
	private OnDismissListener onDismissListener ;
	private AsusDialog(Context context){
		
		 clickListener = new DialogInterface.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1 == Dialog.BUTTON_POSITIVE){
					if(mDlgListener != null){
						mDlgListener.positiveClick();
					}
				}else if(arg1 == Dialog.BUTTON_NEGATIVE){
					 mDlgListener.negativeClick();
				}
				 if(mDlg != null){
					 mDlg.dismiss();
				 }
			}
		};
		onDismissListener =  new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if(mDlgListener != null){
					mDlgListener.onDismiss();
					mDlgListener = null;
				}
				mDlg = null;
			}
		};
		mContext = context;

	}
	
	public static AsusDialog getInstance(Context context){
		if(mInstance == null){
			synchronized (AsusDialog.class) {
				if(mInstance == null){
					mInstance = new AsusDialog(context);
				}
			}
		}
		
		return mInstance;
	}
	
	@SuppressLint("NewApi")
	public void showDialog(IDialogListener dialogListener,String message){
		mDlgListener = dialogListener;
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setPositiveButton(mContext.getResources().getString(R.string.sync_cancel_confirmText),clickListener);
		builder.setNegativeButton(mContext.getResources().getString(R.string.editor_func_button_cancle), clickListener);
		builder.setTitle(mContext.getString(android.R.string.dialog_alert_title));
		builder.setMessage(message);
		mDlg = builder.create();
		mDlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
		mDlg.setOnDismissListener(onDismissListener);
		mDlg.setCanceledOnTouchOutside(false);		
		if(!mDlg.isShowing()){
			mDlg.show();
		}

	}
	
	public void dismissDialog(){
		if(mDlg != null && mDlg.isShowing()){
			mDlg.dismiss();
		}
	}
	
	public void destory(){
		mDlg = null;
		onDismissListener = null;
		clickListener = null;
	}
	
	public interface IDialogListener{
		void positiveClick();
		void negativeClick();
		void onDismiss();
	}
}
