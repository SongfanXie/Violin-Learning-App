package com.eq;
//��ҪͼƬ�ĵ�ӰЧ��
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter 
{
  /*��������*/
  //int mGalleryItemBackground;
  private Context mContext;
  private int layoutWidth;
  private int layoutHeight;
  private Integer[] imageInteger;  
  /*ImageAdapter�Ĺ����*/
  public ImageAdapter(Context c,Integer[] integer,int x,int y) 
  {
    mContext = c;
    imageInteger=integer;
    layoutWidth=x;
	  layoutHeight=y;
  }
  public ImageAdapter(Context c) 
  {
    mContext = c;
    
  }
  
  private Bitmap createReflectedImage(Bitmap originalBitmap) {  
      // ͼƬ�뵹Ӱ�������  
      final int reflectionGap = 4;  
        
      // ͼƬ�Ŀ��  
      int width = originalBitmap.getWidth();  
      // ͼƬ�ĸ߶�  
      int height = originalBitmap.getHeight();  
        
      Matrix matrix = new Matrix();  
      // ͼƬ���ţ�x���Ϊԭ����1����y��Ϊ-1��,ʵ��ͼƬ�ķ�ת  
      matrix.preScale(1, -1);  
      // ������ת���ͼƬBitmap����ͼƬ����ԭͼ��һ�롣  
      Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,  
              height / 2, width, height / 2, matrix, false);  
      // ������׼��Bitmap���󣬿��ԭͼһ�£�����ԭͼ��1.5����  
      Bitmap withReflectionBitmap = Bitmap.createBitmap(width, (height  
              + height / 2 + reflectionGap), Config.ARGB_8888);  

      // ���캯������Bitmap����Ϊ����ͼƬ�ϻ�ͼ  
      Canvas canvas = new Canvas(withReflectionBitmap);  
      // ��ԭʼͼƬ  
      canvas.drawBitmap(originalBitmap, 0, 0, null);  

      // ���������  
      //Paint defaultPaint = new Paint();  
      //canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);  

      // ����ӰͼƬ  
      canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);  

      // ʵ�ֵ�ӰЧ��  
      Paint paint = new Paint();  
      LinearGradient shader = new LinearGradient(0, originalBitmap.getHeight(),   
              0, withReflectionBitmap.getHeight(), 0x70ffffff, 0x00ffffff,  
              TileMode.MIRROR);  
      paint.setShader(shader);  
      paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));  

      // ����Ч��  
      canvas.drawRect(0, height, width, withReflectionBitmap.getHeight(), paint);  

      return withReflectionBitmap;  
  }  
  
  
  
  /*����Ҫ��д�ķ���getCount,����ͼƬ��Ŀ*/
  public int getCount() 
  {
    //return lis.size();
  	return imageInteger.length;
  }
  
  /*һ��Ҫ��д�ķ���getItem,����position*/
  public Object getItem(int position) 
  {
    return position;
  }
  
  /*һ��Ҫ��д�ķ���getItemId,����position*/
  public long getItemId(int position) 
  {
    return position;
  }
  
  /*����Ҫ��д�ķ���getView,������View����*/
  public View getView(int position, View convertView, 
                        ViewGroup parent) 
  {
    /*����ImageView����*/
    ImageView i = new ImageView(mContext);
    /*�趨ͼƬ��imageView����*/
   // Bitmap bm = BitmapFactory.decodeFile(lis.
                      //    get(position).toString());
    //i.setImageBitmap(bm);
    Bitmap bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(imageInteger[position])).getBitmap();
   // i.setImageResource(imageInteger[position]); 
    i.setImageBitmap(createReflectedImage(bitmap));
    /*�����趨ͼƬ�Ŀ��*/
    i.setScaleType(ImageView.ScaleType.FIT_XY);
    /*�����趨Layout�Ŀ��*/
    i.setLayoutParams(new Gallery.LayoutParams(layoutWidth, layoutHeight));
    //i.setAlpha(60);
    /*�趨Gallery����ͼ*/
    //i.setBackgroundResource(mGalleryItemBackground);
    //����imageView����
    return i;
  }     
} 
