package com.asus.instantpage.view;

import com.asus.instantpage.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


public class FuncBarFrameLayout extends ViewGroup implements View.OnClickListener{
	private final int ANIMATION_FRAME_DURATION=1000 / 60;
	private static final int MSG_ANIMATE=1;
	private static final float			MAXIMUM_ACCELERATION			= 900.0f;	
	private static final float 			START_VELOCITY                  = 200.0f;
	private float OPEN_ACCELERATION=0.0f;
	private float CLOSE_ACCELERATION=0.0f;
	private float OPEN_VELOCITY=0.0f;
	private float CLOSE_VELOCITY=0.0f;
	private float currVelocity=0.0f;
	private float currAcceleration=0.0f;
	private final int						mDragId;
	private final int						mFuncBarId;
	private final int 						mMoreHideId;
	private final int						mMoreContentId;
	private View							mDrag;
	private View							mFunbar;
	private View							mMoreHideView = null;
	private View							mMoreContentView =null;
	private boolean mMoreModel=false;
	private boolean mMoreShow=false;
	@SuppressWarnings("unused")
	private HideFuncBarCounter mHideFuncBarCounter;
	private IFuncBarActionListner mFuncBarActionListner=null;
	private WindowManager wm=null;
	private final float density;
	public FuncBarFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	public FuncBarFrameLayout(Context context, AttributeSet attrs, int defStyle ){
		super( context, attrs, defStyle );
		TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.FuncBarFrameLayout, defStyle, 0 );
		mDragId=a.getResourceId(R.styleable.FuncBarFrameLayout_drag, 0);
		mFuncBarId=a.getResourceId(R.styleable.FuncBarFrameLayout_funcbar, 0);
		mMoreHideId=a.getResourceId(R.styleable.FuncBarFrameLayout_more_hide, 0);
		mMoreContentId=a.getResourceId(R.styleable.FuncBarFrameLayout_more_content, 0);
		if (mDragId==0||mFuncBarId==0) {
			throw new IllegalArgumentException( "The FuncBarFrameLayout_drag or FuncBarFrameLayout_funcbar attribute is must refer to an" + " existing child." );
		}
		if (mMoreContentId!=0&&mMoreHideId!=0) {
			mMoreModel = true;
		}

	    density = getResources().getDisplayMetrics().density;
		OPEN_VELOCITY=START_VELOCITY*density+0.5f;
		OPEN_ACCELERATION=MAXIMUM_ACCELERATION*density+0.5f;
		CLOSE_VELOCITY=-OPEN_VELOCITY;
		CLOSE_ACCELERATION=-OPEN_ACCELERATION;
		mHideFuncBarCounter=new HideFuncBarCounter(HideFuncBarCounter.HIDE_TIME, HideFuncBarCounter.HIDE_TIME);
		a.recycle();
		wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	}
	public void setisExpended(boolean e){
		isExpended=e;
	}
	
	public View getDragView(){
		return mDrag;
	}
	
	public void showMoreContent(boolean show){
		if (mMoreModel) {
			mMoreShow = show;
			if (show) {
				mMoreContentView.setVisibility(View.VISIBLE);
				mMoreHideView.setVisibility(View.VISIBLE);
			}else {
				mMoreContentView.setVisibility(View.GONE);
				mMoreHideView.setVisibility(View.GONE);
			}
			this.requestLayout();
			this.invalidate();
		}
	}
	public boolean isMoreShow(){
		return mMoreShow;
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		mDrag=findViewById(mDragId);
		mFunbar=findViewById(mFuncBarId);
		if (mDrag==null||mFunbar==null) {
			throw new IllegalArgumentException( "The FuncBarFrameLayout_drag or FuncBarFrameLayout_funcbar attribute is must refer to an" + " existing child." );
		}
		if (mMoreModel) {
			mMoreContentView=findViewById(mMoreContentId);
			mMoreHideView=findViewById(mMoreHideId);			
		}

		mDrag.setOnClickListener(this);
		mDrag.setVisibility(View.INVISIBLE);
		mFunbar.setVisibility(View.GONE);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = MeasureSpec.getMode( widthMeasureSpec );	
		int heightSpecMode = MeasureSpec.getMode( heightMeasureSpec );
		if ( widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED ) { throw new RuntimeException(
				"FuncBarFrameLayout cannot have UNSPECIFIED dimensions" ); }
		final View drag=mDrag;
		measureChild(drag, widthMeasureSpec, heightMeasureSpec);
		final View funcbar=mFunbar;
		measureChild(funcbar, widthMeasureSpec, heightMeasureSpec);
		if (mMoreModel&&isExpended&&mMoreShow) {
			measureChild(mMoreContentView, widthMeasureSpec, heightMeasureSpec);
			measureChild(mMoreHideView, widthMeasureSpec, heightMeasureSpec);
			int padding =(int) (-6*density);
			setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), mMoreHideView.getMeasuredHeight()+padding);		
		}else {
			setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), funcbar.getMeasuredHeight()+drag.getMeasuredHeight());			
		}
		
	};

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width=r-l;
		View drag=mDrag;
		int dragHeight=drag.getMeasuredHeight();
		int dragWidth=drag.getMeasuredWidth();
		int dragLeft=0;

		int dragTop=isExpended?mFunbar.getMeasuredHeight():0;
		dragLeft=(width-dragWidth)/2;
		drag.layout(dragLeft, dragTop, dragLeft+dragWidth, dragTop+dragHeight);//horizontal center parent
		mFunbar.layout(0, 0,mFunbar.getMeasuredWidth(), mFunbar.getMeasuredHeight());
		if (mMoreModel&&isExpended&&mMoreShow) {
			int padding =(int) (-6*density);
			int tl=r-l-(mMoreContentView.getMeasuredWidth()+mMoreHideView.getMeasuredWidth());
			int tr=tl+mMoreContentView.getMeasuredWidth();
			mMoreContentView.layout(tl, dragTop, tr, dragTop+mMoreContentView.getMeasuredHeight());
			mMoreHideView.layout(r-l-mMoreHideView.getMeasuredWidth(), t+padding, r, t+mMoreHideView.getMeasuredHeight());
		}
	};
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		final long drawingTime = getDrawingTime();
		View drag=mDrag;
		View funbar=mFunbar;
		if (mDrag.getVisibility()==View.VISIBLE) {
			drawChild(canvas, drag, drawingTime);
		}
		
		if (isAnimating||scrollByOuter) {
			canvas.save();
			canvas.translate(0,drag.getTop()-funbar.getMeasuredHeight());//above of the drag
			drawChild(canvas, funbar, drawingTime);
			canvas.restore();
			invalidate();
		}else if(isExpended&&mFunbar.getVisibility()==View.VISIBLE){
			drawChild(canvas, funbar, drawingTime);
			if (mMoreModel&&mMoreShow) {
				mMoreContentView.setVisibility(View.VISIBLE);
				mMoreHideView.setVisibility(View.VISIBLE);
				drawChild(canvas, mMoreContentView, drawingTime);
				drawChild(canvas, mMoreHideView, drawingTime);
			}
		}
		
		
	}
	private WindowManager.LayoutParams layoutParams=null;
	
	@SuppressLint("NewApi")
	@Override
	public void setAlpha(float alpha) {
		// TODO Auto-generated method stub
		try {
			if (layoutParams!=null) {
				layoutParams.alpha=alpha;
				wm.updateViewLayout(this, layoutParams);
			}else if(getLayoutParams()instanceof WindowManager.LayoutParams){
				layoutParams=(WindowManager.LayoutParams)getLayoutParams();
				layoutParams.alpha=alpha;
				wm.updateViewLayout(this, layoutParams);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.setAlpha(alpha);
	}
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		mFunbar.setVisibility(visibility);
		mDrag.setVisibility(visibility);
	}
	private void doAnimation(){
		if (!isAnimating) {
			return;
		}
		incrementAnimation();
		if (mAnimationPosition<0) {
			isExpended=false;
			moveDrag(true, false);
			isAnimating=false;
			closeAnimationEnd();
		}else if(mAnimationPosition>=mFunbar.getMeasuredHeight()){
			isExpended=true;
			moveDrag(false, true);
			isAnimating=false;
			openAnimationEnd();
		}else {
			moveDrag(false, false);
			mAnimationHandler.sendMessageAtTime(mAnimationHandler.obtainMessage(MSG_ANIMATE), SystemClock.uptimeMillis()+ANIMATION_FRAME_DURATION);
		}
	}
	float mAnimationPosition=0;
	long mAnimationLastTime;
	
	private void incrementAnimation() {
		long now=SystemClock.uptimeMillis();
		float t=(now-mAnimationLastTime)/1000.0f;
		mAnimationPosition+=currVelocity*t+currAcceleration*t*t/2;
		currVelocity+=currAcceleration*t;
		mAnimationLastTime=now;
	}
	private void moveDrag(boolean toTop,boolean toBottom) {
		if (toTop) {//collapsed
			mDrag.offsetTopAndBottom(0-mDrag.getTop());
		}else if (toBottom) {// opened
			mDrag.offsetTopAndBottom(mFunbar.getMeasuredHeight()-mDrag.getTop());
		}else {// animating
			mDrag.offsetTopAndBottom((int)mAnimationPosition-mDrag.getTop());
		}
		invalidate();
	}
	private boolean onceDispatch=false;
	private Rect mDragRect=null;
	public void setOnPointerEventDispatcher(IFuncBarActionListner d) {
		mFuncBarActionListner=d;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isExpended) {//not intercept for children
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				//mHideFuncBarCounter.myCancelCount();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				//mHideFuncBarCounter.myStartCount();
				break;
			default:
				break;
			}
		}else {
			//dispatch the point to DooleView when the point is not hit in the mDrag
			if (mDragRect==null) {
				mDragRect=new Rect();
				mDragRect.set(0, 0, mFunbar.getWidth()+0, mDrag.getHeight());
			}
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				if (!mDragRect.contains((int)ev.getX(), (int)ev.getY())) {
					if (mFuncBarActionListner!=null) {
						onceDispatch=true;
						return true;
					}
				}
				break;
			default:
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	};
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (onceDispatch&&mFuncBarActionListner!=null) {
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				 mFuncBarActionListner.dispatchPointerEvent(cloneMotionEventForDoodleView(ev));
				 return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:	
				 mFuncBarActionListner.dispatchPointerEvent(cloneMotionEventForDoodleView(ev));	
				 onceDispatch=false;
				return true;
			default:
				break;
			}
		}
//		if (isExpended) {
//			switch (ev.getActionMasked()) {
//			case MotionEvent.ACTION_DOWN:
//				mHideFuncBarCounter.myCancelCount();
//				return true;
//			case MotionEvent.ACTION_UP:
//			case MotionEvent.ACTION_CANCEL:
//				mHideFuncBarCounter.myStartCount();
//				return true;
//			default:
//				break;
//			}
//		}
		return super.onTouchEvent(ev);
	};
	private MotionEvent cloneMotionEventForDoodleView(MotionEvent srcEvent){
		MotionEvent eventClone=MotionEvent.obtain(srcEvent);
		eventClone.offsetLocation(0, -mDrag.getHeight());
		return eventClone;
	}
	/***
	 * drag clicked to open funcbar
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub 
		invalidate();
		if (isExpended) {
			closeFuncBar();	
		}else {
			openFuncBar();
		}
		
	}
	public void setDragVisiable(int viable){
		mDrag.setVisibility(viable);
		this.invalidate();
	}
	private boolean scrollByOuter=false;
	public boolean initScroll(){
		if (!isAnimating) {
			scrollByOuter=true;
		}
		return scrollByOuter;
	}
	public void scrollByPercent(float percent){
		if (isAnimating||!scrollByOuter) {
			return;
		}
		mAnimationPosition=percent*mFunbar.getMeasuredHeight();		
		if (mAnimationPosition<=0) {
			mDrag.setVisibility(View.INVISIBLE);
			isExpended=false;
			moveDrag(true, false);			
		}else if(mAnimationPosition>=mFunbar.getMeasuredHeight()){
			mDrag.setVisibility(View.VISIBLE);
			isExpended=true;
			moveDrag(false, true);		
		}else {			
			moveDrag(false, false);
			mDrag.setVisibility(View.VISIBLE);
		}
	}
	public void endScroll(boolean extended){
		if(!scrollByOuter){
			if(!isExpended){
				this.setVisibility(View.GONE);
				invalidate();
			}
			return;
		}
		scrollByOuter=false;
		isExpended=extended;
		if (extended) {
			moveDrag(false, true);
			openAnimationEnd();
		}else {
			moveDrag(true, false);		
			closeAnimationEnd();
		}
	}
	private boolean isExpended=false;
	private boolean isAnimating=false;
	public void openFuncBar(){
		if (mFuncBarActionListner!=null) {
			mFuncBarActionListner.beginOpen();
		}
		if (isExpended||isAnimating||scrollByOuter) {
			return;
		}
		scrollByOuter=false;
		//mDrag.setVisibility(View.VISIBLE);
		mAnimationHandler.removeMessages(MSG_ANIMATE);
		currVelocity=OPEN_VELOCITY;
		currAcceleration=OPEN_ACCELERATION;
		mAnimationPosition=0;
		mAnimationLastTime=SystemClock.uptimeMillis();
		mAnimationHandler.sendMessageAtTime(mAnimationHandler.obtainMessage(MSG_ANIMATE), mAnimationLastTime+ANIMATION_FRAME_DURATION);
		isAnimating=true;
				
	}
	public boolean isExtended(){
		return isExpended;
	}
	public void closeFuncBar(){
		//mHideFuncBarCounter.myCancelCount();
		if (mMoreModel&&mMoreShow) {
			showMoreContent(false);
		}
		if (isExpended&&!isAnimating&&!scrollByOuter) {
			if (mFuncBarActionListner!=null) {
				mFuncBarActionListner.beginClose();
			}
			mAnimationHandler.removeMessages(MSG_ANIMATE);
			currVelocity=CLOSE_VELOCITY;
			currAcceleration=CLOSE_ACCELERATION;
			mAnimationPosition=mDrag.getTop();
			mAnimationLastTime=SystemClock.uptimeMillis();
			mAnimationHandler.sendMessageAtTime(mAnimationHandler.obtainMessage(MSG_ANIMATE), mAnimationLastTime+ANIMATION_FRAME_DURATION);
			isAnimating=true;
		}

	}
	
	public Drawable getNewDragDrawable(Context context){
		Bitmap bmpTmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.asus_instantbg_show_btn);
		Bitmap bmpTmp1 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.asus_instantbg_arrow_b);
		Bitmap bitmap = Bitmap.createBitmap(bmpTmp.getWidth(),
				bmpTmp.getHeight(), Config.ARGB_8888);

		float offset_left = (bmpTmp.getWidth() - bmpTmp1.getWidth())/2;
		float offset_top = (bmpTmp.getHeight() - bmpTmp1.getHeight())/2;
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		canvas.drawBitmap(bmpTmp, 0, 0, paint);
		Matrix matrix = new Matrix();
		double scale = 1.0;
		matrix.setScale((float) scale, (float) scale);
		canvas.drawBitmap(bmpTmp1, offset_left, offset_top, paint);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
			
		return new BitmapDrawable(context.getResources(), newbmp);
	}

	private void openAnimationEnd(){
		//mHideFuncBarCounter.myStartCount();
		mFunbar.setVisibility(View.VISIBLE);
		mDrag.setVisibility(View.VISIBLE);
		//this.setVisibility(View.VISIBLE);
		invalidate();
		 if (mFuncBarActionListner!=null) {
			 mFuncBarActionListner.animationEndOpen();
		}
	}
	private void closeAnimationEnd(){
		//this.setVisibility(View.GONE);
		invalidate();
		 if (mFuncBarActionListner!=null) {
			 mFuncBarActionListner.animationEndClose();
		}
	}
	public void closeByTouch(){
		closeFuncBar();
	}
	public int getDragHeight(){
		return mDrag.getMeasuredHeight();
	}
	
//	public void keepOnScreen(){
//		mHideFuncBarCounter.myCancelCount();
//	}
//	public void keepOffScreen(){
//		mHideFuncBarCounter.myStartCount();
//	}
	private Handler mAnimationHandler =new  Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch ( msg.what ) {
			case MSG_ANIMATE:
				doAnimation();
				break;
		}
		};
	};
	private class HideFuncBarCounter extends CountDownTimer{
		public static final int HIDE_TIME=4*1000;
		public HideFuncBarCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}
		private Boolean isStated=false;
		public void myStartCount(){
			if (!isStated) {
				super.start();
				isStated=true;
			}
			
		}
		
		@SuppressWarnings("unused")
		public void myCancelCount(){
			if (isStated) {
				super.cancel();
				isStated=false;
			}
		}
		
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			if (mFuncBarActionListner!=null) {
				if (mFuncBarActionListner.isStopCloseByTimeOut()) {
					this.myStartCount();
					return ;
				}
			}
			if (isAnimating||scrollByOuter) {
				return ;
			}			
			if (isExpended) {
				closeFuncBar();
			}
			
		}

		@Override
		public void onTick(long arg0) {
			// TODO Auto-generated method stub
			
		}		
	}
	public static interface IFuncBarActionListner{
		void dispatchPointerEvent(MotionEvent event);
		void beginOpen();
		void animationEndOpen();
		void beginClose();
		void animationEndClose();
		boolean isStopCloseByTimeOut();
	}
}