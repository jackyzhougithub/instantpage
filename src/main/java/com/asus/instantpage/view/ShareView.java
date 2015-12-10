package com.asus.instantpage.view;

import com.asus.draw.doodleview.DoodleView;
import com.asus.instantpage.share.ShareWindowpml;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public class ShareView  extends IBaseView implements View.OnClickListener{
	private ShareWindowpml  mShareWindowpml = null;
	private Context mContext;
	@Override
	public void obServeView() {
		// TODO 自动生成的方法存根
		mView.setOnClickListener(this);
	}
	
	public ShareView(View view,Context context,DoodleView doodleView){
		mView = view;
		mContext = context;
		mDoodleView = doodleView;
		if(mShareWindowpml == null){
			mShareWindowpml = new ShareWindowpml(mContext,mDoodleView);
		}
	}

	@Override
	public void destory() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void setImageSeclected(View view, boolean isSelected) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		Bitmap bitmap = mDoodleView.newWholeBitmap(true);
		mShareWindowpml.showShareUIWindow(bitmap, null);
	}

}
