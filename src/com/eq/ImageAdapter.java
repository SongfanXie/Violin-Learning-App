package com.eq;
//主要图片的倒影效果
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
  /*声明变量*/
  //int mGalleryItemBackground;
  private Context mContext;
  private int layoutWidth;
  private int layoutHeight;
  private Integer[] imageInteger;  
  /*ImageAdapter的构造符*/
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
      // 图片与倒影间隔距离  
      final int reflectionGap = 4;  
        
      // 图片的宽度  
      int width = originalBitmap.getWidth();  
      // 图片的高度  
      int height = originalBitmap.getHeight();  
        
      Matrix matrix = new Matrix();  
      // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转  
      matrix.preScale(1, -1);  
      // 创建反转后的图片Bitmap对象，图片高是原图的一半。  
      Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,  
              height / 2, width, height / 2, matrix, false);  
      // 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。  
      Bitmap withReflectionBitmap = Bitmap.createBitmap(width, (height  
              + height / 2 + reflectionGap), Config.ARGB_8888);  

      // 构造函数传入Bitmap对象，为了在图片上画图  
      Canvas canvas = new Canvas(withReflectionBitmap);  
      // 画原始图片  
      canvas.drawBitmap(originalBitmap, 0, 0, null);  

      // 画间隔矩形  
      //Paint defaultPaint = new Paint();  
      //canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);  

      // 画倒影图片  
      canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);  

      // 实现倒影效果  
      Paint paint = new Paint();  
      LinearGradient shader = new LinearGradient(0, originalBitmap.getHeight(),   
              0, withReflectionBitmap.getHeight(), 0x70ffffff, 0x00ffffff,  
              TileMode.MIRROR);  
      paint.setShader(shader);  
      paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));  

      // 覆盖效果  
      canvas.drawRect(0, height, width, withReflectionBitmap.getHeight(), paint);  

      return withReflectionBitmap;  
  }  
  
  
  
  /*几定要重写的方法getCount,传回图片数目*/
  public int getCount() 
  {
    //return lis.size();
  	return imageInteger.length;
  }
  
  /*一定要重写的方法getItem,传回position*/
  public Object getItem(int position) 
  {
    return position;
  }
  
  /*一定要重写的方法getItemId,传并position*/
  public long getItemId(int position) 
  {
    return position;
  }
  
  /*几定要重写的方法getView,传并几View对象*/
  public View getView(int position, View convertView, 
                        ViewGroup parent) 
  {
    /*产生ImageView对象*/
    ImageView i = new ImageView(mContext);
    /*设定图片给imageView对象*/
   // Bitmap bm = BitmapFactory.decodeFile(lis.
                      //    get(position).toString());
    //i.setImageBitmap(bm);
    Bitmap bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(imageInteger[position])).getBitmap();
   // i.setImageResource(imageInteger[position]); 
    i.setImageBitmap(createReflectedImage(bitmap));
    /*重新设定图片的宽高*/
    i.setScaleType(ImageView.ScaleType.FIT_XY);
    /*重新设定Layout的宽高*/
    i.setLayoutParams(new Gallery.LayoutParams(layoutWidth, layoutHeight));
    //i.setAlpha(60);
    /*设定Gallery背景图*/
    //i.setBackgroundResource(mGalleryItemBackground);
    //传回imageView对象
    return i;
  }     
} 
