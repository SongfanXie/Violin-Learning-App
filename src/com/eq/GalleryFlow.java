package com.eq;
//����Ч����ʵ��
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
	//�����
    private Camera mCamera = new Camera();
    //���ת���Ƕ�
    private int mMaxRotationAngle = 45;
    //�������ֵ
    private int mMaxZoom = -100;
    //�뾶ֵ
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
    
    
    

    //����gallery��ÿ��ͼƬ����ת
    protected boolean getChildStaticTransformation(View child, Transformation t) {

    		//ȡ�õ�ǰ��View�İ뾶ֵ
            final int childCenter = getCenterOfView(child);
            final int childWidth = child.getWidth();
            
            //��ת�Ƕ�
            int rotationAngle = 0;
            //����ת��״̬
            t.clear();
            //����ת������
            t.setTransformationType(Transformation.TYPE_MATRIX);
            //���ͼƬλ������λ������Ҫ������ת
            if (childCenter == mCoveflowCenter) {
                    transformImageBitmap((ImageView) child, t, 0);
                    //child.setLayoutParams(new LayoutParams(childWidth,(int)(childWidth*1.5)));
            } else {
            	
            		//����ͼƬ��gallery�е�λ��������ͼƬ����ת�Ƕ�
                    rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
                    //�����ת�ǶȾ���ֵ���������ת�Ƕȷ��أ�-mMaxRotationAngle��mMaxRotationAngle;��
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
    	
    		//��Ч�����б���
            mCamera.save();
            final Matrix imageMatrix = t.getMatrix();
            final int imageHeight = child.getLayoutParams().height;
            final int imageWidth = child.getLayoutParams().width;
            final int rotation = Math.abs(rotationAngle);

            // ��Z���������ƶ�camera���ӽǣ�ʵ��Ч��Ϊ�Ŵ�ͼƬ��
            // �����Y�����ƶ�����ͼƬ�����ƶ���X���϶�ӦͼƬ�����ƶ���
            mCamera.translate(0.0f, 0.0f, 100.0f);
            if(Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter)<child.getWidth()/2)
           	 mCamera.translate(0.25f*layoutWidth*2*(child.getWidth()/2-Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter))/child.getWidth(),
           			 -0.5f*layoutWidth*2*(child.getWidth()/2-Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter))/child.getWidth(),
           			-1*layoutHeight*2*(child.getWidth()/2-Math.abs(child.getLeft() + child.getWidth() / 2-mCoveflowCenter))/child.getWidth());
           
            if (rotation < mMaxRotationAngle) {
                    float zoomAmount = (float) (mMaxZoom +(rotation * 1.5));
                    mCamera.translate(0.0f, 0.0f, zoomAmount);
            }

            // ��Y������ת����ӦͼƬ�������﷭ת��
            // �����X������ת�����ӦͼƬ�������﷭ת��
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
