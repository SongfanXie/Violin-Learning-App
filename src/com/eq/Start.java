package com.eq;

//����Ч����������ͼƬ�򶯻�

import android.app.Activity;
import android.content.Intent;


import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.AbsoluteLayout;



public class Start extends Activity{
	private Animation fadeinAnimation,zoominAnimation,zoomoutAnimation;
   	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	
	requestWindowFeature(Window.FEATURE_NO_TITLE);//���ر���
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //����ȫ��
   
    //������Ч
    zoominAnimation = AnimationUtils.loadAnimation(this,R.anim.zoomin);
   	zoomoutAnimation = AnimationUtils.loadAnimation(this,R.anim.zoomout);
   	fadeinAnimation=AnimationUtils.loadAnimation(this,R.anim.fadein);
    
    @SuppressWarnings("deprecation")
	AbsoluteLayout main=new AbsoluteLayout(Start.this);
    main.setBackgroundResource(R.drawable.menu);
 
	setContentView(main);
	main.startAnimation(zoominAnimation);
	CountDownTimer t=new CountDownTimer(2000,10){
		public void onTick(long millisUntilFinished){
		}
		public void onFinish(){
			Intent intent=new Intent();
			intent.setClass(Start.this, Eq.class);
			startActivity(intent);
			Start.this.finish();
	}};
	t.start();
	
    
}
}