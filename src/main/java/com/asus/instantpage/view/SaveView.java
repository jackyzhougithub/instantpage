package com.asus.instantpage.view;

import com.asus.draw.doodleview.DoodleView;
import com.asus.instantpage.Task.StoreBitmap;
import com.asus.instantpage.Utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

public class SaveView extends IBaseView implements View.OnClickListener{
    private Context mContext;
    private StoreBitmap mStoreBitmap;
    
    public SaveView(Context context,DoodleView doodleView,View view){
    	mContext = context;
    	mView = view;
    	mDoodleView = doodleView;
    	mStoreBitmap = new StoreBitmap(mContext);
    }
	
	@Override
	public void obServeView() {
		// TODO 自动生成的方法存根
		mView.setOnClickListener(this);
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
		save();
	}
	
	public void save(){
		Bitmap bitmap = mDoodleView.newWholeBitmap(true);
		mStoreBitmap.StoreBitmapInBackground(bitmap);
		mDoodleView.clearStrokes();
		mContext.sendBroadcast(new Intent(Utils.HIDEINSTANTPAGE));
	}

}
