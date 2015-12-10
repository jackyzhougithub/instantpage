package com.asus.instantpage.tools;

import com.asus.draw.erasepopupwindow.ErasePopupWindow.EraseType;
import com.asus.draw.model.stroke.InsertableObjectStroke;
import com.asus.instantpage.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class DrawTools {

	//for template use 
	private static final int MAX_STROKE = 80;
	private static final int MIN_STROKE = 2;
	private ImageView mCurrentDoodleBrushButton = null;
	private ImageView mCurrentDoodleEraserButton = null;
	private Context mContext;
	public DrawTools(Context context,ImageView doodlebrushButton,ImageView eraseImageView){
		mCurrentDoodleBrushButton = doodlebrushButton;
		mCurrentDoodleEraserButton = eraseImageView;
		mContext = context;
	}
	
	
	public static int drawCurrentMergeIcon(boolean isSelected,int id){
		int drawableID = -1;
		switch (id) {
	    case R.id.quick_note_merge_transparent:
	    	drawableID = isSelected?R.drawable.asus_instantpg_merge2_s:R.drawable.asus_instantpg_merge2_n;
			break;
	    case R.id.quick_note_merge_notepager:
	    	drawableID = isSelected?R.drawable.asus_instantpg_merge3_s:R.drawable.asus_instantpg_merge3_n;
	    	break;
	    	
	    case R.id.quick_note_merge_screenshot:
	    	drawableID = isSelected?R.drawable.asus_instantpg_merge1_s:R.drawable.asus_instantpg_merge1_n;
	    	break;
		default:
			break;
		}
		return drawableID;
	}

	public static Bitmap createNotePaperBG(int color,int width,int height,Context context){
		BitmapDrawable bgDrawable=(BitmapDrawable)context.getResources().getDrawable(R.drawable.quick_note_bg_notepaper);
		Bitmap bitmap=null;
		try {
			bitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.eraseColor(Color.WHITE);
			Canvas canvas=new Canvas(bitmap);
			bgDrawable.setBounds(0, 0, width, height);
			bgDrawable.draw(canvas);

		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return bitmap;
	}
	public static int drawCurrentEraserIcon(Boolean isHoverStatus, Boolean isPressed,EraseType eraseType,boolean selected) {
  		int drawableID = -1;

  		if(eraseType == EraseType.TYPE1) {
  			drawableID =isHoverStatus||selected?R.drawable.asus_instantpg_eraser1_s: isPressed?R.drawable.asus_instantpg_eraser1_p:R.drawable.asus_instantpg_eraser1_n;
  		} else if(eraseType == EraseType.TYPE2) {
  			drawableID =isHoverStatus||selected?R.drawable.asus_instantpg_eraser2_s: isPressed?R.drawable.asus_instantpg_eraser2_p:R.drawable.asus_instantpg_eraser2_n;
  		} else if(eraseType == EraseType.TYPE3){
  			drawableID = isHoverStatus||selected?R.drawable.asus_instantpg_eraser3_s: isPressed?R.drawable.asus_instantpg_eraser3_p:R.drawable.asus_instantpg_eraser3_n;;
  		}
  		else {
			
		}
  		return drawableID;
	}
	
	public Drawable drawCurrentBrush(Boolean isHoverStatus, Boolean isPressed ,int brushID,float strokeWidth,int strokeColor) {
		final boolean selected=false;//mCurrentDoodleBrushButton!=null?mCurrentDoodleBrushButton.isSelected():false;
       
		Bitmap bmpTmp = BitmapFactory.decodeResource(mContext.getResources(),
				isHoverStatus||selected?R.drawable.asus_instantpg_pen1_s:R.drawable.asus_instantpg_pen1_n);
		Bitmap bmpTmp1 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.asus_instantpg_stroke1);
		
		//brushID = getIdFrom(brushID);
		switch (brushID) {
		//case R.id.editor_func_d_brush_normal:
		case InsertableObjectStroke.STROKE_TYPE_NORMAL:
			bmpTmp = BitmapFactory.decodeResource(mContext.getResources(),
					isHoverStatus||selected?R.drawable.asus_instantpg_pen1_s:isPressed?R.drawable.asus_instantpg_pen1_p:R.drawable.asus_instantpg_pen1_n);
			break;
		//case R.id.editor_func_d_brush_mark:
		case InsertableObjectStroke.STROKE_TYPE_AIRBRUSH:
			bmpTmp = BitmapFactory.decodeResource(mContext.getResources(),
					isHoverStatus||selected?R.drawable.asus_instantpg_pen6_s:isPressed?R.drawable.asus_instantpg_pen6_p:R.drawable.asus_instantpg_pen6_n);
			break;
		//case R.id.editor_func_d_brush_markpen:
		case InsertableObjectStroke.STROKE_TYPE_MARKER:
			bmpTmp = BitmapFactory.decodeResource(mContext.getResources(),
					isHoverStatus||selected?R.drawable.asus_instantpg_pen5_s:isPressed?R.drawable.asus_instantpg_pen5_p:R.drawable.asus_instantpg_pen5_n);
			break;
		//case R.id.editor_func_d_brush_scribble:
		case InsertableObjectStroke.STROKE_TYPE_PEN:
			bmpTmp = BitmapFactory.decodeResource(mContext.getResources(),
					isHoverStatus||selected?R.drawable.asus_instantpg_pen2_s:isPressed?R.drawable.asus_instantpg_pen2_p:R.drawable.asus_instantpg_pen2_n);
			break;
		//case R.id.editor_func_d_brush_writingbrush:
		case InsertableObjectStroke.STROKE_TYPE_BRUSH:
			bmpTmp = BitmapFactory.decodeResource(mContext.getResources(),
					isHoverStatus||selected?R.drawable.asus_instantpg_pen3_s:isPressed?R.drawable.asus_instantpg_pen3_p:R.drawable.asus_instantpg_pen3_n);
			break;
		//case R.id.editor_func_d_brush_sketch:
		case InsertableObjectStroke.STROKE_TYPE_PENCIL:
			bmpTmp = BitmapFactory.decodeResource(mContext.getResources(),
					isHoverStatus||selected?R.drawable.asus_instantpg_pen4_s:isPressed?R.drawable.asus_instantpg_pen4_p:R.drawable.asus_instantpg_pen4_n);
			break;
			
		}

		Bitmap bitmap = Bitmap.createBitmap(bmpTmp.getWidth(),
				bmpTmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();

		float delta = (MAX_STROKE - MIN_STROKE)/5.0f;
		 if ( strokeWidth >= MIN_STROKE && strokeWidth <= MIN_STROKE + delta ) {
				bmpTmp1 = setDrawableColor(R.drawable.asus_instantpg_stroke1,strokeColor);
			}
			else if ( strokeWidth > MIN_STROKE + delta && strokeWidth <= MIN_STROKE + 2.0*delta ) {
				bmpTmp1 = setDrawableColor(R.drawable.asus_instantpg_stroke2,strokeColor);
			}
			else if ( strokeWidth > MIN_STROKE + 2.0*delta && strokeWidth <= MIN_STROKE + 3.0*delta ) {
				bmpTmp1 = setDrawableColor(R.drawable.asus_instantpg_stroke3,strokeColor);
			}
			else if ( strokeWidth > MIN_STROKE + 3.0*delta && strokeWidth <= MIN_STROKE + 4.0*delta ) {
				bmpTmp1 = setDrawableColor(R.drawable.asus_instantpg_stroke4,strokeColor);
			}
			else if ( strokeWidth > MIN_STROKE + 4.0*delta && strokeWidth <= MAX_STROKE ) {
				bmpTmp1 = setDrawableColor(R.drawable.asus_instantpg_stroke5,strokeColor);
			}
		
		    canvas.drawBitmap(bmpTmp, 0, 0, paint);
			Matrix matrix = new Matrix();
			double scale = 1.0;
			matrix.setScale((float) scale, (float) scale);
			canvas.drawBitmap(bmpTmp1, 0, 0, paint);
			Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);

			return new BitmapDrawable(mContext.getResources(), newbmp);
	}
	
	private Bitmap setDrawableColor(int id,int color) {
  		
  		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
  		if(bitmap!=null){
  		int width = bitmap.getWidth();
  		int height = bitmap.getHeight();
  		int[] colors = new int[width * height]; 
  		bitmap.getPixels(colors, 0, width, 0, 0, width, height);
  		for (int i= 0; i < width * height; i++) {
  			colors[i] = (colors[i] & 0xFF000000) | (color & 0x00FFFFFF);
  		}
  		Bitmap newbitmap =  Bitmap.createBitmap(width, height, Config.ARGB_8888);
  		newbitmap.setPixels(colors, 0, width, 0, 0, width, height);
  		return newbitmap;
  		}
  		else return null;
  		
  	}

}
