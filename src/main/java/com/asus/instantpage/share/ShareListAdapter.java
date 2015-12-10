package com.asus.instantpage.share;

import java.util.List;

import com.asus.instantpage.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShareListAdapter extends BaseAdapter {

	//private final int COUNT;
	//private Context mContext;
	private List<ShareableAppInfo> mContent;
	private LayoutInflater inflater;
	//private ShareableAppInfo mRunningAppInfo=null;
	
	public ShareListAdapter(List<ShareableAppInfo> src,ShareableAppInfo runningAppInfo,Context context){
		mContent=src;
		//mRunningAppInfo = runningAppInfo;
		//COUNT=(src==null?0:src.size())+(runningAppInfo==null?1:2);
		 inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		 
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContent.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
//		if ((COUNT-mContent.size()) == 1) {
//			if (position == 0) {
//				return null;
//			}else {
//				return mContent.get(position-1);
//			}
//		}else {
//			if (position == 0) {
//				return mRunningAppInfo;
//			}else if(position == 1) {
//				return null;
//			}else {
//				return mContent.get(position-2);
//			}
//		}
		return mContent.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ShareableAppInfo appInfo=(ShareableAppInfo)getItem(position);
		TextView tv=null;
		if (convertView!=null && convertView instanceof TextView) {
			tv=(TextView)convertView;
		}else {
			tv=(TextView)inflater.inflate(R.layout.quick_note_share_list_item, parent, false);
		}
		 if (appInfo!=null) {
		    tv.setText(appInfo.getAppName());
		    tv.setCompoundDrawablesWithIntrinsicBounds(appInfo.getAppIconDrawable(), null, null, null);
		}
		 
		return tv;
	}

}
