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
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;

class DrawPic extends View
{
	private int intX;//�ƶ���λ��
	private int picW,picH;//��Դ�ĳߴ�
	 /* ����Bitmap���� */
    Bitmap        mBitQQ        = null;
    
    Paint   mPaint = null;
    
    /* ����һ�������� */
    Bitmap        mSCBitmap = null;
    
    /* ����Canvas���� */
    private static Canvas mCanvas = null;   
	private void init()
	{
		mPaint = new Paint();
        
        mPaint.setAntiAlias(true);
	}

	public DrawPic(Context context,Bitmap res)
	{
		super(context);
		init();
		picW=res.getWidth();
		picH=res.getHeight();
		mPaint.setStrokeWidth(picH/4);
		
		mCanvas = new Canvas();  
		 /* ������Ļ��С�Ļ����� */
        mSCBitmap=Bitmap.createBitmap(picW, picH, Config.ARGB_8888);  
        /* ���ý����ݻ�����mSCBitmap�� */
        mCanvas.setBitmap(mSCBitmap); 
      
        mCanvas.drawBitmap(res, 0, 0, null);

		
	}
	public void udateDrawPic(int x)
	{
		intX=x;
		invalidate();

		 //mBitQQ = ((BitmapDrawable) getResources().getDrawable(R.drawable.pic1)).getBitmap();
		
	}
        public void drawLine(int x,int w,boolean f)
        {
        	if(f)
        	{
        	mPaint.setColor(Color.GREEN);//��ȷ��������
            mPaint.setAlpha(100);
        	}
        	else 
        	{
        		mPaint.setColor(Color.RED);//�����������
                mPaint.setAlpha(100);
        	}
        	mCanvas.drawLine(x, picH/2, x+w, picH/2, mPaint);
        }
		
	

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		
		canvas.drawBitmap(mSCBitmap, intX,0,  null);
	}
}
