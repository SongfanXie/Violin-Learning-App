package com.eq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

class VisualizerView extends View
{
	private Rect mRect = new Rect();
	//private ArrayList<Integer> mList;
	private int mTime=0,yinfu=0;
	private Paint paint = new Paint();
	private Paint paint2 = new Paint();
		private int freq;
		private int now=88;//当前音符
	// 初始化画笔
	private void init()
	{
		//mList=null;
		
	    
		paint.setStrokeWidth(1f);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		paint2.setStrokeWidth(20f);
		paint2.setColor(Color.GRAY);
		paint2.setAntiAlias(true);
	}

	public VisualizerView(Context context)
	{
		super(context);
		init();
	}
        
		
	public void updateVisualizer(int f)
	{
		freq=f;
		mTime+=4;
		now=Eq.current;
		invalidate();
	}

	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mRect.set(0, 0, getWidth(), getHeight());
		
		
		
		
	    if(now!=88)canvas.drawLine(0, mRect.height()-(now+5)*mRect.height()/24, mRect.width(), mRect.height()-(now+5)*mRect.height()/24, paint2);
	    //画五线谱
		canvas.drawLine(0, 4*mRect.height()/12, mRect.width(), 4*mRect.height()/12, paint);
		canvas.drawLine(0, 5*mRect.height()/12, mRect.width(), 5*mRect.height()/12, paint);
		canvas.drawLine(0, 6*mRect.height()/12, mRect.width(), 6*mRect.height()/12, paint);
		canvas.drawLine(0, 7*mRect.height()/12, mRect.width(), 7*mRect.height()/12, paint);
		canvas.drawLine(0, 8*mRect.height()/12, mRect.width(), 8*mRect.height()/12, paint);
		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.crotchet);
		int bmpWidth=bmp.getWidth();
	    int bmpHeight=bmp.getHeight();
		Matrix matrix=new Matrix();
		 matrix.postScale((float)mRect.width()/(15*bmpWidth), (float)0.28*mRect.height()/bmpHeight);
		Bitmap bit=Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,matrix, true);

		yinfu=Eq.fFormat(freq);
		if(yinfu==99)return;
	
	canvas.drawBitmap(bit, mRect.width()/8+mTime%(mRect.width()*3/4), mRect.height()-5.4f*mRect.height()/12-yinfu*mRect.height()/24, null);
		if(yinfu==1||yinfu==2)
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 9*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 9*mRect.height()/12, paint);
		else if(yinfu==12||yinfu==13)
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 3*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 3*mRect.height()/12, paint);
		else if(yinfu==14||yinfu==15)
			{
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 2*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 2*mRect.height()/12, paint);
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 3*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 3*mRect.height()/12, paint);
			}
		else if(yinfu==0||yinfu==-1)
			{
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 9*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 9*mRect.height()/12, paint);
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 10*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 10*mRect.height()/12, paint);
			
			}
		else if(yinfu==-2)
		{
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 9*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 9*mRect.height()/12, paint);
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 11*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 11*mRect.height()/12, paint);
			canvas.drawLine(mRect.width()/8+mTime%(mRect.width()*3/4), 10*mRect.height()/12, mRect.width()/8+mTime%(mRect.width()*3/4)+60, 10*mRect.height()/12, paint);
		}
	
		
		
		
		
		
		//canvas.drawBitmap(bit, mTime%intScreenX, mRect.height()-freq*mRect.height()/64, mPaint2);
	
	}
}
