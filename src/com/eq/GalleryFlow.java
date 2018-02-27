package com.eq;
//画廊效果的实现
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {
	private int layoutWidth;
	  private int layoutHeight;
	//相机类
    private Camera mCamera = new Camera();
    //最大转动角度
    private int mMaxRotationAngle = 45;
    //最大缩放值
    private int mMaxZoom = -100;
    //半径值
    private int mCoveflowCenter;
    private boolean pos=true;

    public GalleryFlow(Context context,int x,int y) {
            super(context);
            this.setStaticTransformationsEnabled(true);
            layoutWidth=x;
      	  layoutHeight=y;
    }

    public GalleryFlow(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setStaticTransformationsEnabled(true);
    }

    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setStaticTransformationsEnabled(true);
    }

    public int getMaxRotationAngle() {
            return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
            mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
            return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
            mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
            return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                            + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
    }
    
    public void changePos()
    {
    	pos=false;
    }
    
    
    

    //控制gallery中每个图片的旋转
    protected boolean getChildStaticTransformation(View child, Transformation t) {

    		//取得当前子View的半径值
            final int childCenter = getCenterOfView(child);
            final int childWidth = child.getWidth();
            
            //旋转角度
            int rotationAngle = 0;
            //重置转换状态
            t.clear();
            //设置转换类型
            t.setTransformationType(Transformation.TYPE_MATRIX);
            //如果图片位于中心位置则不需要进行旋转
            if (childCenter == mCoveflowCenter) {
                    transformImageBitmap((ImageView) child, t, 0);
                    //child.setLayoutParams(new LayoutParams(childWidth,(int)(childWidth*1.5)));
            } else {
            	
            		//根据图片在gallery中得位置来计算图片的旋转角度
                    rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
                    //如果旋转角度绝对值大于最大旋转角度返回（-mMaxRotationAngle或mMaxRotationAngle;）
                    if (Math.abs(rotationAngle) > mMaxRotationAngle) {
                            rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
                                            : mMaxRotationAngle;
                    }
                    transformImageBitmap((ImageView) child, t, rotationAngle);
                   
               
                  // child.setLayoutParams(new LayoutParams(120*Math.abs(mCoveflowCenter - childCenter)/childWidth+50, 120*Math.abs(mCoveflowCenter - childCenter)/childWidth+50));
            }

            return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	
    	
            mCoveflowCenter = getCenterOfCoverflow();
            super.onSizeChanged(w, h, oldw, oldh);
    }
    
    private void transformImageBitmap(ImageView child, Transformation t,
                    int rotationAngle) {
    	
    		//对效果进行保存
            mCamera.save();
            final Matrix imageMatrix = t.getMatrix();
            final int imageHeight = child.getLayoutParams().height;
            final int imageWidth = child.getLayoutParams().width;
            final int rotation = Math.abs(rotationAngle);

            // 在Z轴上正向移动camera的视角，实际效果为放大图片。
            // 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。
            mCamera.translate(0.0f, 0.0f, 100.0f);
            if(Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter)<child.getWidth()/2)
           	 mCamera.translate(0.25f*layoutWidth*2*(child.getWidth()/2-Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter))/child.getWidth(),
           			 -0.5f*layoutWidth*2*(child.getWidth()/2-Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter))/child.getWidth(),
           			-1*layoutHeight*2*(child.getWidth()/2-Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter))/child.getWidth());
           
            if (rotation < mMaxRotationAngle) {
                    float zoomAmount = (float) (mMaxZoom +(rotation * 1.5));
                    mCamera.translate(0.0f, 0.0f, zoomAmount);
            }

            // 在Y轴上旋转，对应图片竖向向里翻转。
            // 如果在X轴上旋转，则对应图片横向向里翻转。
            if(pos==true)
            mCamera.rotateY(rotationAngle);
            else 
            	mCamera.rotateZ(rotationAngle);
            mCamera.getMatrix(imageMatrix);
            imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
            imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
            mCoveflowCenter = getCenterOfCoverflow();
            
            imageMatrix.preScale(Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter)/500f+0.5f, Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter)/500f+0.5f);
            
            
            
            mCamera.restore();
    }
}
