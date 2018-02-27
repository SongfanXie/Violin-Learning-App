package com.eq;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.graphics.drawable.BitmapDrawable;

import com.eq.TableAdapter.TableCell;
import com.eq.TableAdapter.TableRow;
import android.R.drawable;
import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class Eq extends Activity {
	private int selec;
	private Button mode2back, choose;
	private DrawPic pic;
	private static int toLeft = 0;
	private ListView myListView;
	private ImageView iv;
	private int count = 0;
	private SharedPreferences[] correctYinfu, allYinfu;
	private SharedPreferences[] correctYinfu2, allYinfu2;
	private Integer[] musImag;
	private static CountDownTimer mTimer;
	private PopupWindow popupWindow;
	private static int RATE = 60;
	private Animation fadeinAnimation, zoominAnimation, zoomoutAnimation,
			fadeoutAnimation;
	private SoundPool soundPool;
	private int l5id, m2id, m6id, h3id;
	private static int alpha = 120;
	private static int skinid = 1;
	private Integer[] menuBackgSkin, backgSkin;
	private static SharedPreferences scoreData, skinData;
	private static SharedPreferences.Editor scoreDataEditor, skinDataEditor;
	private boolean runnable3flag = false;
	private static Handler handler, handler3;
	private static Runnable runnable3, runnable2, runnable, runnable22;
	private TextView[] mid, low, hig;
	private static Boolean flag = false;
	public static int current = 88;
	private static int correct, cachCorrect, black;
	private int nextShow;
	private int[][] selected;
	private ArrayList<Object> distance0, distance1, distance2;
	private AbsoluteLayout main, mode2, mode3, setMode;
	public static TextView mode2TextView;
	static VisualizerView mVisualizerView;
	private static int ZAO = 2;
	private static int YUZHI = 20;//8
	boolean isRecording = false;
	static final int frequency = 11025;
	static int recBufSize=4096, playBufSize;
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	static AudioRecord audioRecord;
	static AudioTrack audioTrack;
	static short[] buffer = new short[recBufSize];
	short[] tmpBuf = new short[recBufSize];
	int bufferReadResult;
	static int pinlv = 0, lastPinlv = 0;
	private int intScreenX, intScreenY;
    private static Boolean isBegin=false;  
    private static int maxScore;
    
    
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏

		// 取得屏幕的大小
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		intScreenX = dm.widthPixels;
		intScreenY = dm.heightPixels;

		// 存储记录和皮肤
		scoreData = getSharedPreferences("scoreData", 0);
		scoreDataEditor = scoreData.edit();
		maxScore = scoreData.getInt("scoreData", 0);

		skinData = getSharedPreferences("skinData", 0);
		skinDataEditor = skinData.edit();
		skinid = skinData.getInt("skinData", 0);

		correctYinfu = new SharedPreferences[18];
		allYinfu = new SharedPreferences[18];

		correctYinfu2 = new SharedPreferences[18];
		allYinfu2 = new SharedPreferences[18];

		for (int i = 0; i < 18; i++) {
			correctYinfu[i] = getSharedPreferences("correctYinfu" + i, 0);
			allYinfu[i] = getSharedPreferences("allYinfu" + i, 0);

			correctYinfu2[i] = getSharedPreferences("correctYinfu2" + i, 0);
			allYinfu2[i] = getSharedPreferences("allYinfu2" + i, 0);

		}

		// 建立皮肤资源数组
		menuBackgSkin = new Integer[] { R.drawable.menubackg,
				R.drawable.menubackg1, R.drawable.menubackg2,
				R.drawable.menubackg3, R.drawable.menubackg4,
				R.drawable.menubackg5, R.drawable.menubackg6 };
		backgSkin = new Integer[] { R.drawable.backg, R.drawable.backg1,
				R.drawable.backg2, R.drawable.backg3, R.drawable.backg4,
				R.drawable.backg5, R.drawable.backg6 };

		runnableInit();

		// 创建特效对象
		zoominAnimation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
		zoomoutAnimation = AnimationUtils.loadAnimation(this, R.anim.zoomout);
		fadeinAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
		fadeoutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);

		// 加载主界面
		main = new AbsoluteLayout(Eq.this);
		main.setBackgroundResource(menuBackgSkin[skinid]);

		setContentView(main);
		// main.startAnimation(zoominAnimation);

		// 创建画廊对象
		GalleryFlow g = new GalleryFlow(Eq.this, intScreenX / 6,
				2 * intScreenY / 3);
		main.addView(g);// 将画廊添加到主界面
		final TextView myTextView = new TextView(Eq.this);
		myTextView.setTextColor(Color.BLACK);
		myTextView.setLayoutParams(new AbsoluteLayout.LayoutParams(
				2 * intScreenX / 3, LayoutParams.WRAP_CONTENT, intScreenX / 6,
				6 * intScreenY / 7));
		main.addView(myTextView);
		// 新增几ImageAdapter并设定给Gallery对象
		Integer[] imageInteger = { R.drawable.mode1, R.drawable.mode2,
				R.drawable.mode3, R.drawable.set, R.drawable.jilu,
				R.drawable.about, R.drawable.exit };

		// 画廊的设置和初始化
		g.setAdapter(new ImageAdapter(this, imageInteger, intScreenX / 6,
				intScreenY / 2));
		g.setLayoutParams(new AbsoluteLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0,
				intScreenY / 6));
		g.setSpacing(30);

		// 为画廊设置监听，监听对图片的单击动作
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				// 点击画廊的不同图片时执行不同的函数
				switch (position) {
				case 0:
					// 由于在平板上直接执行goToMode1()会出错，目前的解决方法是把函数放进倒计时器里执行
					new CountDownTimer(1, 1) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							goToMode1();
						}
					}.start();

					break;
				case 1:
					new CountDownTimer(1, 1) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							goToMode2();
						}
					}.start();

					break;
				case 2:
					new CountDownTimer(1, 1) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							goToMode3();
						}
					}.start();

					break;
				case 3:
					new CountDownTimer(1, 1) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							set();
						}
					}.start();

					break;
				case 4:
					new CountDownTimer(1, 1) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							jilu();
						}
					}.start();

					break;
				case 5:
					new CountDownTimer(1, 1) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							about();
						}
					}.start();

					break;

				case 6:
					exit();
					break;

				}
				// Toast.makeText(Eq.this, "点击了第"+(position+1)+"张图片",
				// Toast.LENGTH_SHORT).show();
			}
		});

		// 为画廊设置监听，监听图片的选中动作
		g.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				switch (position) {
				case 0:
					myTextView.setText("音还没调好？快进入调音模式吧！");
					break;
				case 1:
					myTextView.setText("这里有我们为你精选的曲子！");
					break;
				case 2:
					myTextView.setText("随便练习一下？就是它了！");
					break;
				case 3:
					myTextView.setText("设置");
					break;
				case 4:
					myTextView.setText("查看记录");
					break;
				case 5:
					myTextView.setText("关于");
					break;
				case 6:
					myTextView.setText("退出");
					break;

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// 创建曲目的图片资源数组
		musImag = new Integer[] { R.drawable.mus1, // R.drawable.mus2,
				R.drawable.mus3 };
		// 创建乐谱数组
		int[][] mus0 = { { 1, 4 }, { 1, 4 }, { 5, 4 }, { 5, 4 }, { 6, 4 },
				{ 6, 4 }, { 5, 8 }, { 4, 4 }, { 4, 4 }, { 3, 4 }, { 3, 4 },
				{ 2, 4 }, { 2, 4 }, { 1, 8 }, { 5, 4 }, { 5, 4 }, { 4, 4 },
				{ 4, 4 }, { 3, 4 }, { 3, 4 }, { 2, 8 }, { 5, 4 }, { 5, 4 },
				{ 4, 4 }, { 4, 4 }, { 3, 4 }, { 3, 4 }, { 2, 8 }, { 1, 4 },
				{ 1, 4 }, { 5, 4 }, { 5, 4 }, { 6, 4 }, { 6, 4 }, { 5, 8 },
				{ 4, 4 }, { 4, 4 }, { 3, 4 }, { 3, 4 }, { 2, 4 }, { 2, 4 },
				{ 1, 8 }, { 88, 4 }, { 88, 4 }, { 88, 4 } };
		// 创建List存储曲目的一些基本信息
		distance0 = new ArrayList<Object>();
		distance0.add(mus0);
		distance0.add("小星星");
		distance0.add(R.drawable.pic1);
		distance0.add(52);
		distance0.add(1);

		int[][] mus1 = { { 3, 4 }, { 3, 2 }, { 5, 2 }, { 6, 2 }, { 8, 2 },
				{ 8, 2 }, { 6, 2 }, { 5, 4 }, { 5, 2 }, { 6, 2 }, { 5, 8 },
				{ 3, 4 }, { 3, 2 }, { 5, 2 }, { 6, 2 }, { 8, 2 }, { 8, 2 },
				{ 6, 2 }, { 5, 4 }, { 5, 2 }, { 6, 2 }, { 5, 8 }, { 5, 4 },
				{ 5, 4 }, { 5, 4 }, { 3, 2 }, { 5, 2 }, { 6, 4 }, { 6, 4 },
				{ 5, 8 }, { 3, 4 }, { 2, 2 }, { 3, 2 }, { 5, 4 }, { 3, 2 },
				{ 2, 2 }, { 1, 4 }, { 1, 2 }, { 2, 2 }, { 1, 8 }, { 88, 4 },
				{ 88, 4 }, { 88, 4 } };
		distance1 = new ArrayList<Object>();
		distance1.add(mus1);
		distance1.add("茉莉花");
		distance1.add(R.drawable.pic2);
		distance1.add(36);
		distance1.add(2);

		int[][] mus2 = { { 3, 8 }, { 5, 4 }, { 9, 12 }, { 8, 8 }, { 5, 4 },
				{ 4, 12 }, { 3, 8 }, { 3, 4 }, { 3, 4 }, { 4, 4 }, { 5, 4 },
				{ 6, 12 }, { 5, 12 }, { 3, 8 }, { 5, 4 }, { 9, 12 }, { 8, 8 },
				{ 5, 4 }, { 4, 12 }, { 3, 8 }, { 5, 4 }, { 5, 4 }, { 6, 4 },
				{ 7, 4 }, { 8, 12 }, { 8, 12 }, { 9, 6 }, { 5, 2 }, { 5, 4 },
				{ 7, 4 }, { 6, 4 }, { 5, 4 }, { 3, 8 }, { 5, 4 }, { 8, 12 },
				{ 6, 8 }, { 8, 4 }, { 9, 8 }, { 8, 4 }, { 7, 12 }, { 5, 12 },
				{ 3, 8 }, { 5, 4 }, { 9, 12 }, { 8, 8 }, { 5, 4 }, { 4, 12 },
				{ 3, 8 }, { 5, 4 }, { 5, 4 }, { 6, 4 }, { 7, 4 }, { 8, 12 },
				{ 8, 8 }, { 88, 4 }, { 88, 4 }, { 88, 4 } };
		distance2 = new ArrayList<Object>();
		distance2.add(mus2);
		distance2.add("雪绒花");
		distance2.add(R.drawable.pic3);
		distance2.add(100);
		distance2.add(3);

	}

	// 调音模式
	@SuppressWarnings("deprecation")
	public void goToMode1() {
		AbsoluteLayout mode1 = new AbsoluteLayout(Eq.this);

		mode1.setBackgroundResource(backgSkin[skinid]);
		setContentView(mode1);// 跳转到调音界面
		mode1.startAnimation(zoominAnimation);// 为界面跳转添加特效

		mVisualizerView = new VisualizerView(Eq.this);// VisualizerView视图用来显示五线谱和乐符
		mVisualizerView.setLayoutParams(new AbsoluteLayout.LayoutParams(
				intScreenX, 2 * intScreenY / 3, 0, intScreenY / 6));
		mode1.addView(mVisualizerView);

		// 创建4个button分别对应小提琴的4根弦
		Button b1 = new Button(Eq.this);
		b1.setText("5(低音)");
		b1.getBackground().setAlpha(alpha);// 设置透明度
		b1.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 8,
				intScreenY / 6, 2 * intScreenX / 8, 5 * intScreenY / 6));
		mode1.addView(b1);

		Button b2 = new Button(Eq.this);
		b2.setText("2");
		b2.getBackground().setAlpha(alpha);
		b2.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 8,
				intScreenY / 6, 3 * intScreenX / 8, 5 * intScreenY / 6));
		mode1.addView(b2);

		Button b3 = new Button(Eq.this);
		b3.setText("6");
		b3.getBackground().setAlpha(alpha);
		b3.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 8,
				intScreenY / 6, 4 * intScreenX / 8, 5 * intScreenY / 6));
		mode1.addView(b3);

		Button b4 = new Button(Eq.this);
		b4.setText("3(高音)");
		b4.getBackground().setAlpha(alpha);
		b4.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 8,
				intScreenY / 6, 5 * intScreenX / 8, 5 * intScreenY / 6));
		mode1.addView(b4);

		// 返回主菜单
		Button mode1back = new Button(Eq.this);
		mode1back.setText("返回");
		mode1back.getBackground().setAlpha(alpha);
		mode1back.setLayoutParams(new AbsoluteLayout.LayoutParams(
				intScreenX / 3, intScreenY / 6, intScreenX / 3, 0));
		mode1.addView(mode1back);

		// 为每一个button设置监听器
		b1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(isBegin==false)
				{begin();isBegin=true;}
				current = -2;
				soundPool.play(l5id, 1f, 1f, 0, 0, 1f);
				mVisualizerView.updateVisualizer(pinlv);
				// myMediaplayer = MediaPlayer.create(Eq.this, R.raw.h3);
				// myMediaplayer.start();
			}
		});

		b2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(isBegin==false)
				{begin();isBegin=true;}
				current = 2;
				soundPool.play(m2id, 1f, 1f, 0, 0, 1f);
				mVisualizerView.updateVisualizer(pinlv);
			}
		});

		b3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(isBegin==false)
				{begin();isBegin=true;}
				current = 6;
				soundPool.play(m6id, 1f, 1f, 0, 0, 1f);
				mVisualizerView.updateVisualizer(pinlv);
			}
		});

		b4.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(isBegin==false)
				{begin();isBegin=true;}
				current = 10;
				soundPool.play(h3id, 1f, 1f, 0, 0, 1f);
				mVisualizerView.updateVisualizer(pinlv);
			}
		});

		mode1back.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(isBegin)stop();
				isBegin=false;
				setContentView(main);
				main.startAnimation(zoominAnimation);
			}
		});

	}

	// 教学模式
	@SuppressWarnings("deprecation")
	public void goToMode2() {
		mode2 = new AbsoluteLayout(Eq.this);
		setContentView(mode2);
		mode2.startAnimation(zoominAnimation);
		mode2.setBackgroundResource(backgSkin[skinid]);

		mid = new TextView[17];
		// { t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10,
		// t11, t12, t13, t14, t15, t16 };
		low = new TextView[17];
		// { low0, low1, low2, low3, low4, low5, low6, low7,
		// low8, low9, low10, low11, low12, low13, low14, low15, low16 };
		hig = new TextView[17];
		// { hig0, hig1, hig2, hig3, hig4, hig5, hig6, hig7,
		// hig8, hig9, hig10, hig11, hig12, hig13, hig14, hig15, hig16 };
		for (int i = 0; i <= 16; i++) {
			mid[i] = new TextView(Eq.this);
			low[i] = new TextView(Eq.this);
			hig[i] = new TextView(Eq.this);
		}

		for (int i = 0; i < 17; i++) {
			mode2.addView(mid[i]);
			mode2.addView(low[i]);
			mode2.addView(hig[i]);
			mid[i].setLayoutParams(new AbsoluteLayout.LayoutParams(
					3 * intScreenX / 68, intScreenY / 12, intScreenX / 4 + i
							* 3 * intScreenX / 68, intScreenY / 12));
			low[i].setLayoutParams(new AbsoluteLayout.LayoutParams(
					3 * intScreenX / 68, intScreenY / 12, intScreenX / 4 + i
							* 3 * intScreenX / 68, intScreenY / 8));
			hig[i].setLayoutParams(new AbsoluteLayout.LayoutParams(
					3 * intScreenX / 68, intScreenY / 12, intScreenX / 4 + i
							* 3 * intScreenX / 68, 0));
			mid[i].setTextSize(20);
			low[i].setTextSize(20);
			hig[i].setTextSize(20);
			mid[i].setGravity(Gravity.CENTER);
			low[i].setGravity(Gravity.CENTER);
			hig[i].setGravity(Gravity.CENTER);
		}

		/*
		 * mVisualizerView = new VisualizerView(Eq.this);
		 * mVisualizerView.setLayoutParams(new AbsoluteLayout.LayoutParams(
		 * intScreenX, 2 * intScreenY / 3, 0, intScreenY / 6));
		 * mode2.addView(mVisualizerView);
		 */
		choose = new Button(Eq.this);
		choose.setText("选择歌曲");
		choose.getBackground().setAlpha(alpha);
		choose.setLayoutParams(new AbsoluteLayout.LayoutParams(
				3 * intScreenX / 16, intScreenY / 6, 3 * intScreenX / 8,
				5 * intScreenY / 6));
		mode2.addView(choose);

		mode2back = new Button(Eq.this);
		mode2back.setText("返回");
		mode2back.getBackground().setAlpha(alpha);
		mode2back.setLayoutParams(new AbsoluteLayout.LayoutParams(
				3 * intScreenX / 16, intScreenY / 6, 9 * intScreenX / 16,
				5 * intScreenY / 6));
		mode2.addView(mode2back);

		Toast.makeText(Eq.this, "长按屏幕调节速度", Toast.LENGTH_SHORT).show();

		mode2TextView = new TextView(Eq.this);
		mode2.addView(mode2TextView);
		mode2TextView.setLayoutParams(new AbsoluteLayout.LayoutParams(
				3 * intScreenX / 8, intScreenY / 12, 0, 7 * intScreenY / 8));
		mode2TextView.setTextColor(Color.BLACK);
		mode2TextView.setTextSize(15);

		final TextView mText = new TextView(Eq.this);
		mText.setGravity(Gravity.CENTER);
		mText.setText("60拍/分");

		final SeekBar skb = new SeekBar(Eq.this);
		skb.setMax(100);
		skb.setProgress(RATE);
		skb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				RATE = seekBar.getProgress();
				mText.setText(RATE + "拍/分");

				mTimer.cancel();
				mTimer = new CountDownTimer(2000, 100) {
					public void onTick(long millisUntilFinished) {
						mode2.startAnimation(fadeoutAnimation);

					}

					public void onFinish() {
						popupWindow.dismiss();
						// skb.startAnimation(fadeoutAnimation);
						mode2.startAnimation(fadeinAnimation);
						// mode2.getBackground().setAlpha(255);
					}
				}.start();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				RATE = seekBar.getProgress();
				mText.setText(RATE + "拍/分");
			}
		});
		skb.setBackgroundColor(Color.GRAY);
		final AbsoluteLayout mLinearLayout = new AbsoluteLayout(Eq.this);

		mLinearLayout.addView(mText, new AbsoluteLayout.LayoutParams(
				LayoutParams.FILL_PARENT, intScreenY / 6, 0, 0));
		mLinearLayout.addView(skb, new AbsoluteLayout.LayoutParams(
				LayoutParams.FILL_PARENT, intScreenY / 10, 0, intScreenY / 6));

		// 设置长按屏幕监听
		mode2.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub

				// 弹出框
				popupWindow = new PopupWindow(mode2, 2 * intScreenX / 3,
						intScreenY / 3);
				// 为弹出框设定自定义的布局
				popupWindow.setContentView(mLinearLayout);

				popupWindow.setFocusable(true);
				popupWindow.showAtLocation(mode2, Gravity.CENTER, 0, 0);
				// skb.startAnimation(fadeinAnimation);
				// mode2.getBackground().setAlpha(100);

				mode2.startAnimation(fadeoutAnimation);

				// 设置弹出框消失的时间
				mTimer = new CountDownTimer(4000, 100) {
					public void onTick(long millisUntilFinished) {
						mode2.startAnimation(fadeoutAnimation);

					}

					public void onFinish() {
						// skb.startAnimation(fadeoutAnimation);
						mode2.startAnimation(fadeinAnimation);
						popupWindow.dismiss();

						// mode2.getBackground().setAlpha(100);
					}
				}.start();

				return true;
			}
		});

		// 选择曲子
		choose.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (runnable3flag == true) {
					// TimerGo.cancel();
					mode2.removeView(pic);
					audioRecord.stop();
					handler3.removeCallbacks(runnable3);
					handler.removeCallbacks(runnable22);
					handler.removeCallbacks(runnable);
					runnable3flag = false;

				}
				AbsoluteLayout pick = new AbsoluteLayout(Eq.this);
				setContentView(pick);
				pick.startAnimation(zoominAnimation);
				pick.setBackgroundResource(backgSkin[skinid]);

				Button pickback = new Button(Eq.this);
				pickback.setText("返回");
				pickback.getBackground().setAlpha(alpha);
				pickback.setLayoutParams(new AbsoluteLayout.LayoutParams(
						intScreenX / 4, intScreenY / 6, 3 * intScreenX / 8,
						5 * intScreenY / 6));
				pick.addView(pickback);

				pickback.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {

						setContentView(mode2);
						mode2.startAnimation(zoominAnimation);
					}
				});

				GalleryFlow g = new GalleryFlow(Eq.this, intScreenX / 4,
						2 * intScreenY / 4);
				// g.changePos();
				pick.addView(g);
				final TextView myTextView = new TextView(Eq.this);

				g.setAdapter(new ImageAdapter(Eq.this, musImag, intScreenX / 4,
						intScreenY / 2));
				g.setLayoutParams(new AbsoluteLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0,
						intScreenY / 6));
				// g.setSpacing(10);
				g.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {

						switch (position) {
						case 0:
							myTextView.setText((String) distance0.get(1));
							break;
						/*
						 * case 1: myTextView.setText((String)
						 * distance1.get(1)); break;
						 */
						case 1:
							myTextView.setText((String) distance2.get(1));
							break;
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
				g.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						switch (position) {
						case 0:
							new CountDownTimer(1, 1) {
								public void onTick(long millisUntilFinished) {
								}

								public void onFinish() {
									select(distance0);
								}
							}.start();

							break;
						/*
						 * case 1: new CountDownTimer(1, 1) { public void
						 * onTick(long millisUntilFinished) {} public void
						 * onFinish() { select(distance1); } }.start(); break;
						 */
						case 1:
							new CountDownTimer(1, 1) {
								public void onTick(long millisUntilFinished) {
								}

								public void onFinish() {
									select(distance2);
								}
							}.start();
							break;
						}

						// Toast.makeText(Eq.this, "点击了第"+(position+1)+"张图片",
						// Toast.LENGTH_SHORT).show();
					}
				});

			}
		});

		// 返回主菜单
		mode2back.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				if (runnable3flag == true) {

					mode2.removeView(pic);

					// stop();
					audioRecord.stop();// 停止采集声音
					// 结束采集和处理线程
					handler3.removeCallbacks(runnable3);
					handler.removeCallbacks(runnable22);
					handler.removeCallbacks(runnable);
					runnable3flag = false;
				}
				setContentView(main);
				main.startAnimation(zoominAnimation);
			}
		});

	}

	// 练习模式
	@SuppressWarnings("deprecation")
	public void goToMode3() {
		current = 88;
		mode3 = new AbsoluteLayout(Eq.this);
		setContentView(mode3);
		mode3.startAnimation(zoominAnimation);
		mode3.setBackgroundResource(backgSkin[skinid]);

		mVisualizerView = new VisualizerView(Eq.this);
		// mVisualizerView.setBackgroundColor(Color.WHITE);
		mVisualizerView.setLayoutParams(new AbsoluteLayout.LayoutParams(
				intScreenX, 2 * intScreenY / 3, 0, intScreenY / 6));
		mode3.addView(mVisualizerView);

		
		final Button start = new Button(Eq.this);
		start.setText("开始");
		start.getBackground().setAlpha(alpha);
		start.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 4,
				intScreenY / 6, intScreenX / 4, 5 * intScreenY / 6));
		mode3.addView(start);

		//final Button 
		final Button mode3back = new Button(Eq.this);
		mode3back.setText("返回");
		mode3back.getBackground().setAlpha(alpha);
		mode3back.setLayoutParams(new AbsoluteLayout.LayoutParams(
				intScreenX / 4, intScreenY / 6, intScreenX / 2,
				5 * intScreenY / 6));
		mode3.addView(mode3back);

		// 为开始button设置监听，由于开始和停止是同一个button，由目前的状态决定执行什么操作
		start.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (isBegin == false) {
					begin();
					isBegin = true;
					//start.setText(bufferReadResult+"");
					start.setText("停止");
				} else {
					stop();
					isBegin = false;
					start.setText("开始");
				}

			}
		});

		mode3back.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				start.setText("开始");
				stop();
				isBegin = false;
				setContentView(main);
				main.startAnimation(zoominAnimation);
			}
		});

	}

	// 设置
	@SuppressWarnings("deprecation")
	public void set() {

		setMode = new AbsoluteLayout(Eq.this);
		setMode.setBackgroundResource(backgSkin[skinid]);
		setContentView(setMode);
		setMode.startAnimation(zoominAnimation);

		/*
		 * Button b1 = new Button(Eq.this); b1.setText("查看记录");
		 * b1.getBackground().setAlpha(alpha); b1.setLayoutParams(new
		 * AbsoluteLayout.LayoutParams(intScreenX / 3, intScreenY / 6,
		 * intScreenX / 3, intScreenY / 9)); setMode.addView(b1);
		 */

		Button b2 = new Button(Eq.this);
		b2.setText("环境噪声");
		b2.getBackground().setAlpha(alpha);
		b2.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 3,
				intScreenY / 6, intScreenX / 3, intScreenY / 8));
		setMode.addView(b2);

		Button b3 = new Button(Eq.this);
		b3.setText("更换皮肤");
		b3.getBackground().setAlpha(alpha);
		b3.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 3,
				intScreenY / 6, intScreenX / 3, 5 * intScreenY / 12));
		setMode.addView(b3);

		Button b4 = new Button(Eq.this);
		b4.setText("返回");
		b4.getBackground().setAlpha(alpha);
		b4.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 3,
				intScreenY / 6, intScreenX / 3, 17 * intScreenY / 24));
		setMode.addView(b4);

		b2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				AbsoluteLayout zao = new AbsoluteLayout(Eq.this);
				zao.setBackgroundResource(backgSkin[skinid]);// 加载皮肤
				setContentView(zao);
				zao.startAnimation(zoominAnimation);
				SeekBar skb = new SeekBar(Eq.this);// 创建拖动条
				zao.addView(skb);
				skb.setMax(10);
				skb.setProgress(ZAO);
				skb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						ZAO = seekBar.getProgress();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
					}
				});
				skb.setLayoutParams(new AbsoluteLayout.LayoutParams(
						2 * intScreenX / 3, intScreenY / 10, intScreenX / 6,
						intScreenY / 3));

				Button setZao = new Button(Eq.this);
				setZao.setText("确定");
				setZao.getBackground().setAlpha(alpha);
				skb.setBackgroundColor(Color.GRAY);
				setZao.setLayoutParams(new AbsoluteLayout.LayoutParams(
						intScreenX / 3, intScreenY / 6, intScreenX / 3,
						2 * intScreenY / 3));
				zao.addView(setZao);

				setZao.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						setContentView(setMode);
						setMode.startAnimation(zoominAnimation);
						setMode.setBackgroundResource(backgSkin[skinid]);
					}
				});

			}
		});

		b3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				final AbsoluteLayout chooseSkin = new AbsoluteLayout(Eq.this);
				chooseSkin.setBackgroundResource(menuBackgSkin[skinid]);
				setContentView(chooseSkin);
				chooseSkin.startAnimation(zoominAnimation);

				GalleryFlow g = new GalleryFlow(Eq.this, intScreenX / 4,
						2 * intScreenY / 3);
				chooseSkin.addView(g);
				final TextView myTextView = new TextView(Eq.this);
				myTextView.setTextColor(Color.BLACK);
				myTextView.setLayoutParams(new AbsoluteLayout.LayoutParams(
						intScreenX / 3, LayoutParams.WRAP_CONTENT,
						intScreenX / 3, 6 * intScreenY / 7));
				chooseSkin.addView(myTextView);
				// 新增几ImageAdapter并设定给Gallery对象

				g.setAdapter(new ImageAdapter(Eq.this, backgSkin,
						intScreenX / 4, intScreenY / 2));
				g.setLayoutParams(new AbsoluteLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0,
						intScreenY / 6));
				g.setSpacing(30);

				g.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						skinid = position;
						// chooseSkin.startAnimation(zoominAnimation);
						chooseSkin.setBackgroundResource(menuBackgSkin[skinid]);
						switch (position) {
						case 0: {
							myTextView.setText("动感时尚");
						}
							break;
						case 1: {
							myTextView.setText("水墨江南");
						}
							break;
						case 2: {
							myTextView.setText("清新自然");
						}
							break;
						case 3: {
							myTextView.setText("复古典雅");
						}
							break;
						case 4: {
							myTextView.setText("卡通魔法");
						}
							break;
						case 5: {
							myTextView.setText("粉红甜蜜");
						}
							break;
						case 6: {
							myTextView.setText("绚丽卡酷");
						}
							break;

						}
						// myTextView.setText("第"+(position+1)+"张图片");
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
				g.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						skinid = position;
						new CountDownTimer(1, 1) {
							public void onTick(long millisUntilFinished) {
							}

							public void onFinish() {
								// 将选中的皮肤方案存入文件
								skinDataEditor.putInt("skinData", skinid);
								skinDataEditor.commit();
								setContentView(setMode);
								setMode.startAnimation(zoominAnimation);
								setMode.setBackgroundResource(backgSkin[skinid]);

							}
						}.start();

					}
				});
			}
		});

		// 返回
		b4.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				setContentView(main);
				main.startAnimation(zoominAnimation);
				main.setBackgroundResource(menuBackgSkin[skinid]);
			}
		});

	}

	// 查看记录
	public void jilu() {
		final AbsoluteLayout recorde = new AbsoluteLayout(Eq.this);
		setContentView(recorde);
		recorde.startAnimation(zoominAnimation);
		recorde.setBackgroundResource(backgSkin[skinid]);
		myListView = getlv();
		recorde.addView(myListView);

		TextView myTv1 = new TextView(Eq.this);
		myTv1.setTextColor(Color.BLACK);
		myTv1.setGravity(Gravity.CENTER);
		myTv1.setTextSize(20);
		myTv1.setText("最高得分：" + scoreData.getInt("scoreData", 0) + "分");
		myTv1.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 2,
				intScreenY / 6, intScreenX / 4, 0));
		recorde.addView(myTv1);

		TextView myTv2 = new TextView(Eq.this);
		myTv2.setTextColor(Color.BLACK);
		myTv2.setGravity(Gravity.CENTER);

		myTv2.setText("统计数据");
		myTv2.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 2,
				intScreenY / 12, intScreenX / 4, intScreenY / 6));
		recorde.addView(myTv2);

		Button clean = new Button(Eq.this);
		clean.setText("清除数据");
		clean.getBackground().setAlpha(alpha);
		clean.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 4,
				intScreenY / 6, intScreenX / 4, 5 * intScreenY / 6));
		recorde.addView(clean);

		Button back = new Button(Eq.this);
		back.setText("返回");

		back.getBackground().setAlpha(alpha);
		back.setLayoutParams(new AbsoluteLayout.LayoutParams(intScreenX / 4,
				intScreenY / 6, intScreenX / 2, 5 * intScreenY / 6));
		recorde.addView(back);

		clean.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				new AlertDialog.Builder(Eq.this)
						// 弹出提示框

						.setMessage("确定清除统计数据?")

						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								})

						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {

										// 清除数据
										for (int i = 0; i < 18; i++) {
											SharedPreferences.Editor correctYinfuEditor = correctYinfu[i]
													.edit();
											correctYinfuEditor.putInt(
													"correctYinfu" + i, 0);
											correctYinfuEditor.commit();

											SharedPreferences.Editor allYinfuEditor = allYinfu[i]
													.edit();
											allYinfuEditor.putInt("allYinfu"
													+ i, 0);
											allYinfuEditor.commit();
										}
										recorde.removeView(myListView);
										myListView = getlv();
										recorde.addView(myListView);
										Toast.makeText(Eq.this, "统计数据已清除！",
												Toast.LENGTH_LONG).show();

									}

								}).show();

			}
		});

		back.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				setContentView(main);
				main.startAnimation(zoominAnimation);
			}
		});

	}

	// 关于
	public void about() {
		final AbsoluteLayout about = new AbsoluteLayout(Eq.this);
		setContentView(about);
		about.startAnimation(zoominAnimation);
		about.setBackgroundResource(backgSkin[skinid]);

		ImageView im = new ImageView(Eq.this);
		im.setImageResource(R.drawable.aboutpic);
		im.setLayoutParams(new LayoutParams(intScreenX, 5 * intScreenY / 6));
		about.addView(im);

		Button aboutback = new Button(Eq.this);
		aboutback.setText("返回");
		aboutback.getBackground().setAlpha(alpha);
		aboutback.setLayoutParams(new AbsoluteLayout.LayoutParams(
				intScreenX / 3, intScreenY / 6, intScreenX / 3,
				5 * intScreenY / 6));
		about.addView(aboutback);
		aboutback.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				setContentView(main);
				main.startAnimation(zoominAnimation);
			}
		});

	}

	// 退出
	public void exit() {
		Eq.this.finish();

	}

	// 选中曲目
	@SuppressWarnings({ "deprecation", "deprecation" })
	public void select(final ArrayList<Object> mArrayList) {
		selec = (Integer) mArrayList.get(4);
		toLeft = 0;
		count = 0;
		black = 0;
		cachCorrect = 0;
		correct = 0;
		nextShow = 9;
		selected = (int[][]) mArrayList.get(0);
		// 当前曲目统计数据清空
		for (int i = 0; i < 18; i++) {
			SharedPreferences.Editor correctYinfu2Editor = correctYinfu2[i]
					.edit();
			correctYinfu2Editor.putInt("correctYinfu2" + i, 0);
			correctYinfu2Editor.commit();

			SharedPreferences.Editor allYinfu2Editor = allYinfu2[i].edit();
			allYinfu2Editor.putInt("allYinfu2" + i, 0);
			allYinfu2Editor.commit();
		}

		setContentView(mode2);
		mode2back.setEnabled(false);
		choose.setEnabled(false);
		mode2TextView.setText("当前曲目：" + mArrayList.get(1));

		// 五线谱
		Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(
				(Integer) mArrayList.get(2))).getBitmap();
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale((float) ((Integer) mArrayList.get(3) - 0) * intScreenX
				/ (16 * bmpWidth), (float) intScreenY / (3 * bmpHeight));
		Bitmap bit = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
				matrix, true);

		pic = new DrawPic(Eq.this, bit);

		mode2.addView(pic, new AbsoluteLayout.LayoutParams(3 * intScreenX / 4,
				2 * intScreenY / 3, intScreenX / 8, intScreenY / 3));

		// 简谱显示
		mid[8].setBackgroundColor(Color.YELLOW);

		for (int i = 0; i < 8; i++) {
			mid[i].setText(null);
			low[i].setText(null);
			hig[i].setText(null);
		}

		for (int i = 8; i < 17; i++) {

			if (selected[i - 8][0] != 88) {
				mid[i].setText(textFormat2(selected[i - 8][0]));
				low[i].setText(textFormat3(selected[i - 8][0]));
				hig[i].setText(textFormat1(selected[i - 8][0]));
			} else {
				mid[i].setText(null);
				low[i].setText(null);
				hig[i].setText(null);
				black++;
			}

		}

		// 3秒倒计时
		CountDownTimer Timer321 = new CountDownTimer(1000, 500) {
			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {

				switch (count) {
				case 0: {
					iv = new ImageView(Eq.this);
					iv.setImageResource(R.drawable.p3);
					mode2.addView(iv);
					iv.startAnimation(zoominAnimation);
					iv.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 3, intScreenY / 2, intScreenX / 3,
							intScreenY / 4));
				}
					break;
				case 1: {
					mode2.removeView(iv);
					iv = new ImageView(Eq.this);
					iv.setImageResource(R.drawable.p2);
					mode2.addView(iv);
					iv.startAnimation(zoominAnimation);
					iv.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 3, intScreenY / 2, intScreenX / 3,
							intScreenY / 4));
				}
					break;
				case 2: {
					mode2.removeView(iv);
					iv = new ImageView(Eq.this);
					iv.setImageResource(R.drawable.p1);
					mode2.addView(iv);
					iv.startAnimation(zoominAnimation);
					iv.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 3, intScreenY / 2, intScreenX / 3,
							intScreenY / 4));
				}
					break;
				case 3: {
					mode2.removeView(iv);
					iv = new ImageView(Eq.this);
					iv.setImageResource(R.drawable.start);
					mode2.addView(iv);
					iv.startAnimation(zoominAnimation);
					iv.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 3, intScreenY / 2, intScreenX / 3,
							intScreenY / 4));
				}
					break;
				case 4: {
					mode2.removeView(iv);
					mode2back.setEnabled(true);
					choose.setEnabled(true);
					// begin();// 开始

					// begin();
                   if(isBegin==false)
					{
                	audioRecord.startRecording();// 开始录制
					}
					handler.postDelayed(runnable, 0);

					handler.postDelayed(runnable22, 0);
					
					handler3.postDelayed(runnable3, 0);
					runnable3flag = true;
				}
					break;
				}
				count++;
				if (count < 5)
					this.start();
			}
		};
		Timer321.start();

	}

	public void begin() {

		audioRecord.startRecording();// 开始录制

		handler.postDelayed(runnable, 0);

		handler.postDelayed(runnable2, 0);
	}

	public void stop() {
		audioRecord.stop();
		handler.removeCallbacks(runnable2);
		handler.removeCallbacks(runnable);
	}

	public void runnableInit() {

		soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);
		l5id = soundPool.load(this, R.raw.l5, 1);
		m2id = soundPool.load(this, R.raw.m2, 1);
		m6id = soundPool.load(this, R.raw.m6, 1);
		h3id = soundPool.load(this, R.raw.h3, 1);

		//recBufSize = AudioRecord.getMinBufferSize(frequency,channelConfiguration, audioEncoding);
		//recBufSize=4096;

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize);

		handler = new Handler();

		runnable22 = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 要做的事情

				Eq.this.start(1);

				handler.postDelayed(this, 5);

			}
		};

		runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 要做的事情
				
				buffer = new short[recBufSize];// 缓冲数组
				bufferReadResult = audioRecord.read(buffer, 0, recBufSize);
				
				tmpBuf = new short[recBufSize];
				System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
				handler.postDelayed(this, 5);
			}
		};
		runnable2 = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 要做的事情

				start(0);

				handler.postDelayed(this, 5);

			}
		};

		handler3 = new Handler();
		runnable3 = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 要做的事情
				flag = true;

				current = selected[nextShow - 8][0];
				// 记录每个音符总的出现次数
				if (current != 88) {
					int arg0 = allYinfu[2 + current].getInt("allYinfu"
							+ (current + 2), 0);
					SharedPreferences.Editor allYinfuEditor = allYinfu[current + 2]
							.edit();
					allYinfuEditor.putInt("allYinfu" + (current + 2), arg0 + 1);
					allYinfuEditor.commit();

					int arg1 = allYinfu2[2 + current].getInt("allYinfu2"
							+ (current + 2), 0);
					SharedPreferences.Editor allYinfu2Editor = allYinfu2[current + 2]
							.edit();
					allYinfu2Editor.putInt("allYinfu2" + (current + 2),
							arg1 + 1);
					allYinfu2Editor.commit();
				}

				if (nextShow - 10 >= 0) {
					if (selec == 1) {
						toLeft -= intScreenX * selected[nextShow - 10][1]
								* 1.02 / 64;
					} else if (selec == 2) {
						toLeft -= intScreenX * selected[nextShow - 10][1]
								* 1.01 / 64;
					} else if (selec == 3) {
						toLeft -= intScreenX * selected[nextShow - 10][1]
								* 1.01 / 64;
					}

				} else
					toLeft -= intScreenX * 4 / 64;
				pic.udateDrawPic(toLeft);

				for (int i = 0; i < 16; i++) {
					mid[i].setText(mid[i + 1].getText());
					mid[i].setTextColor(mid[i + 1].getTextColors());
					low[i].setText(low[i + 1].getText());
					low[i].setTextColor(low[i + 1].getTextColors());
					hig[i].setText(hig[i + 1].getText());
					hig[i].setTextColor(hig[i + 1].getTextColors());

				}
				mid[8].setTextColor(Color.BLACK);
				low[8].setTextColor(Color.BLACK);
				hig[8].setTextColor(Color.BLACK);

				if (nextShow < selected.length && selected[nextShow][0] != 88) {
					mid[16].setText(textFormat2(selected[nextShow][0]));
					low[16].setText(textFormat3(selected[nextShow][0]));
					hig[16].setText(textFormat1(selected[nextShow][0]));
				} else {
					mid[16].setText(null);
					low[16].setText(null);
					hig[16].setText(null);
					black++;
				}

				if (cachCorrect == correct) {
					if (selec == 1)
						pic.drawLine(intScreenX / 4 - toLeft - 35, 30, false);
					else if (selec == 2)
						pic.drawLine(intScreenX / 4 - toLeft - 35, 30, false);

					else if (selec == 3)
						pic.drawLine(intScreenX / 4 - toLeft - 35, 30, false);
					mid[7].setTextColor(Color.RED);
					low[7].setTextColor(Color.RED);
					hig[7].setTextColor(Color.RED);

				} else {

					if (selec == 1)
						pic.drawLine(intScreenX / 4 - toLeft - 35, 30, true);
					else if (selec == 2)
						pic.drawLine(intScreenX / 4 - toLeft - 35, 30, true);
					else if (selec == 3)
						pic.drawLine(intScreenX / 4 - toLeft - 35, 30, true);
					mid[7].setTextColor(Color.GREEN);
					low[7].setTextColor(Color.GREEN);
					hig[7].setTextColor(Color.GREEN);
				}
				cachCorrect = correct;

				nextShow++;

				if (nextShow == selected.length + 8) {
					handler3.removeCallbacks(this);
					if(isBegin)stop();
					isBegin=false;
					// TimerGo.cancel();
					mode2.removeView(pic);
					runnable3flag = false;
					mid[8].setBackgroundColor(0);

					int score = 100 * correct / (selected.length - black);
					if (score > scoreData.getInt("scoreData", 0)) {
						scoreDataEditor.putInt("scoreData", score);
						scoreDataEditor.commit();
						maxScore = scoreData.getInt("scoreData", 0);
					}

					final AbsoluteLayout recorde2 = new AbsoluteLayout(Eq.this);
					setContentView(recorde2);
					recorde2.startAnimation(zoominAnimation);
					recorde2.setBackgroundResource(backgSkin[skinid]);
					myListView = getlv2();
					recorde2.addView(myListView);

					TextView myTv1 = new TextView(Eq.this);
					myTv1.setTextColor(Color.BLACK);
					myTv1.setGravity(Gravity.CENTER);
					myTv1.setText("得分：" + score);
					myTv1.setTextSize(20);
					myTv1.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 2, intScreenY / 6, intScreenX / 4, 0));
					recorde2.addView(myTv1);

					TextView myTv2 = new TextView(Eq.this);
					myTv2.setTextColor(Color.BLACK);
					myTv2.setGravity(Gravity.CENTER);
					myTv2.setText("统计数据");
					myTv2.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 2, intScreenY / 6, intScreenX / 4,
							intScreenY / 12));
					recorde2.addView(myTv2);

					final Button back = new Button(Eq.this);
					back.setText("返回");
					back.getBackground().setAlpha(alpha);
					back.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 4, intScreenY / 6, 3 * intScreenX / 8,
							5 * intScreenY / 6));
					recorde2.addView(back);

					back.setOnClickListener(new Button.OnClickListener() {
						public void onClick(View v) {
							setContentView(mode2);
							mode2.startAnimation(zoominAnimation);
						}
					});

					iv = new ImageView(Eq.this);
					if (score <= 49)
						iv.setImageResource(R.drawable.re49);
					else if (score >= 50 && score <= 69)
						iv.setImageResource(R.drawable.re69);
					else if (score >= 70 && score <= 89)
						iv.setImageResource(R.drawable.re89);
					else if (score >= 90 && score <= 99)
						iv.setImageResource(R.drawable.re99);
					else if (score == 100)
						iv.setImageResource(R.drawable.re100);
					recorde2.addView(iv);
					iv.startAnimation(zoominAnimation);
					iv.setLayoutParams(new AbsoluteLayout.LayoutParams(
							intScreenX / 3, intScreenY / 2, intScreenX / 3,
							intScreenY / 4));
					back.setEnabled(false);

					new CountDownTimer(4000, 1000) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							recorde2.removeView(iv);
							back.setEnabled(true);
						}
					}.start();

				} else
					handler3.postDelayed(this, selected[nextShow - 9][1]
							* 15000 / RATE);
			}
		};
	}

	public static Complex[] changedLow(Complex[] a) {
		int mr = 0;
		int length = a.length;
		for (int m = 1; m < length; ++m) {
			int l = length / 2;
			while (mr + l >= length) {
				l = l >> 1; // 右移相当于,l除以2
			}
			mr = mr % l + l;
			if (mr > m) {
				Complex t = new Complex();
				t = a[m];
				a[m] = a[mr];
				a[mr] = t;
			}
		}

		return a;
	}

	public static Complex[] fft(Complex[] a, int fft_tepy) {
		a = changedLow(a);
		int length = a.length;
		double pisign = fft_tepy * Math.PI;

		Complex t = new Complex();
		int l = 1;

		while (l < length) {
			for (int m = 0; m < l; ++m) {
				int temp_int = l * 2; // 左移相当于,l乘以2
				for (int i = m; temp_int < 0 ? i >= (length - 1) : i < length; i += temp_int) {
					Complex temp = new Complex(0.0, m * pisign / l);

					Complex temp_exp = temp.exp();
					
					if(i+l<length)
					{
					t.re = a[i + l].re * temp_exp.re - a[i + l].im* temp_exp.im;
					t.im = a[i + l].re * temp_exp.im + a[i + l].im
							* temp_exp.re;

					a[i + l].re = a[i].re - t.re;
					a[i + l].im = a[i].im - t.im;
					a[i].re = a[i].re + t.re;
					a[i].im = a[i].im + t.im;

					}
					
				} // end for i

			} // end for m
			l = l * 2;
		}// end while
			// 左移相当于,l乘以2
		return a;
	}

	
	
	 /* public static Complex[] fft(Complex[] x) {
	        int N = x.length;
	 
	        // base case
	        if (N == 1) return new Complex[] { x[0] };
	 
	        // radix 2 Cooley-Tukey FFT
	        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }
	 
	        // fft of even terms
	        Complex[] even = new Complex[N/2];
	        for (int k = 0; k < N/2; k++) {
	            even[k] = x[2*k];
	        }
	        Complex[] q = fft(even);
	 
	        // fft of odd terms
	        Complex[] odd  = even;  // reuse the array
	        for (int k = 0; k < N/2; k++) {
	            odd[k] = x[2*k + 1];
	        }
	        Complex[] r = fft(odd);
	 
	        // combine
	        Complex[] y = new Complex[N];
	        for (int k = 0; k < N/2; k++) {
	            double kth = -2 * k * Math.PI / N;
	            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
	            y[k]       = q[k].plus(wk.times(r[k]));
	            y[k + N/2] = q[k].minus(wk.times(r[k]));
	        }
	        return y;
	    }*/
	
	
	
	// 开始处理采集到的数据
	public void start(int which) {

		Complex[] comBuf = new Complex[bufferReadResult];
		//Complex[] comBuf = new Complex[2048];
		for (int i = 0; i < bufferReadResult; i++) {
			comBuf[i] = new Complex(tmpBuf[i] / 100, 0);
		}
		Complex[] fftBuf = new Complex[bufferReadResult];
		fftBuf = fft(comBuf,1);
		//fftBuf = fft(comBuf);
		double[] fftres = new double[bufferReadResult / 2];
		double[] fftres2 = new double[bufferReadResult / 2];// 处理后的fft结果
		for (int i = 0; i < bufferReadResult / 2; i++) {
			fftres[i] = fftBuf[i].abs();
		}


		for (int i = 0; i < fftres.length; i++) {
			if (fftres[i] > max(fftres) / YUZHI)
				fftres2[i] = fftres[i];
			else
				fftres2[i] = 0;
		}
		// 求极大值对应的频率
		ArrayList<Integer> extreme = new ArrayList<Integer>();
		for (int i = 1; i < fftres2.length - 1; i++)// 由于低频的噪声较严重所以截掉低频部分
		{
			if (fftres2[i] > fftres2[i - 1] && fftres2[i] > fftres2[i + 1])
				extreme.add(i);
		}
		// 谐波间隔
		ArrayList<Integer> distance = new ArrayList<Integer>();
		if (extreme.size() == 1)
			distance.add(extreme.get(0));
		else if (extreme.size() != 0)
			for (int i = 1; i < extreme.size(); i++) {
				if (fre(fftres2) % (extreme.get(i) - extreme.get(i - 1)) < 6)
					distance.add(extreme.get(i) - extreme.get(i - 1));
			}

		if (max(fftres) > ZAO * 20) {
			if (distance.size() != 0)
				pinlv = min(distance) * frequency / (2 * fftres.length);

		} else
			pinlv = 0;

		if (which == 0)
			if (pinlv != lastPinlv) {
				mVisualizerView.updateVisualizer(pinlv);
				lastPinlv = pinlv;
			} else {

			}
		if (fFormat(pinlv) == current && flag == true && current != 88) {
			correct++;
			if (flag == true) {

				int arg0 = correctYinfu[2 + current].getInt("correctYinfu"
						+ (2 + current), 0);
				SharedPreferences.Editor correctYinfuEditor = correctYinfu[2 + current]
						.edit();
				correctYinfuEditor.putInt("correctYinfu" + (2 + current),
						arg0 + 1);
				correctYinfuEditor.commit();

				int arg1 = correctYinfu2[2 + current].getInt("correctYinfu2"
						+ (2 + current), 0);
				SharedPreferences.Editor correctYinfu2Editor = correctYinfu2[2 + current]
						.edit();
				correctYinfu2Editor.putInt("correctYinfu2" + (2 + current),
						arg1 + 1);
				correctYinfu2Editor.commit();

			}
			flag = false;

		}
	}

	// 取数组最大值
	double max(double[] f) {
		double max = f[0];
		for (int i = 1; i < f.length; i++) {
			if (max < f[i]) {
				max = f[i];
			}
		}
		return max;
	}

	// 去最大值对应的一维坐标
	int fre(double[] f) {
		double max = f[0];
		int k = 0;
		for (int i = 1; i < f.length; i++) {
			if (max < f[i]) {
				max = f[i];
				k = i;
			}
		}
		return k;
	}

	// 取最小值，但要比20大
	int min(ArrayList<Integer> f) {
		int min = 0;
		if (f.get(0) > 20)
			min = f.get(0);

		for (int i = 1; i < f.size(); i++) {
			if (f.get(i) < min && f.get(i) > 20)
				min = f.get(i);// 20对应100hz
		}
		return min;
	}

	// 取均值
	double average(double[] f) {
		int sum = 0;
		for (int i = 0; i < f.length; i++)
			sum += f[i];
		return sum / f.length;
	}

	// 将频率对应成音符
	public static int fFormat(int freq) {
		int yinfu;
		if (freq >= 257 && freq <= 280)
			yinfu = 1;
		else if (freq >= 284 && freq <= 302)
			yinfu = 2;
		else if (freq >= 316 && freq <= 339)
			yinfu = 3;
		else if (freq >= 344 && freq <= 355)
			yinfu = 4;
		else if (freq >= 381 && freq <= 404)
			yinfu = 5;
		else if (freq >= 430 && freq <= 458)
			yinfu = 6;
		else if (freq >= 483 && freq <= 506)
			yinfu = 7;
		else if (freq >= 510 && freq <= 555)
			yinfu = 8;// 高音1
		else if (freq >= 563 && freq <= 617)
			yinfu = 9;
		else if (freq >= 628 && freq <= 674)
			yinfu = 10;
		else if (freq >= 682 && freq <= 705)
			yinfu = 11;
		else if (freq >= 768 && freq <= 797)
			yinfu = 12;
		else if (freq >= 865 && freq <= 899)
			yinfu = 13;
		else if (freq >= 984 && freq <= 1012)
			yinfu = 14;
		else if (freq >= 1040 && freq <= 1071)
			yinfu = 15;
		else if (freq >= 230 && freq <= 253)
			yinfu = 0;
		else if (freq >= 215 && freq <= 226)
			yinfu = -1;
		else if (freq >= 193 && freq <= 204)
			yinfu = -2;
		else
			yinfu = 99;
		return yinfu;
	}

	// 音高的标识匹配
	public String textFormat1(int m) {
		String s;
		if (m <= 7)
			s = "";
		else if (m >= 8 && m <= 14)
			s = ".";
		else
			s = ":";
		return s;
	}

	public String textFormat2(int m) {
		String s;
		if (m >= 1 && m <= 7)// 中音
			s = "" + m;
		else if (m <= 0)// 低音
			s = "" + (m + 7);
		else if (m >= 8 && m <= 14)
			s = "" + (m - 7);// 高音
		else
			s = "" + (m - 14);// 高高音
		return s;
	}

	public String textFormat3(int m) {
		String s;
		if (m >= 1)
			s = "";
		else
			s = ".";
		return s;
	}

	// 用于listView显示
	public String textFormat(int m) {
		String s;
		if (m >= 0 && m <= 2)
			s = (5 + m) + "(低音）";
		else if (m >= 3 && m <= 9)
			s = m - 2 + "";
		else if (m >= 10 && m <= 16)
			s = (m - 9) + "(高音)";
		else
			s = "1(高高音)";
		return s;
	}

	// 返回总记录的ListView
	public ListView getlv() {
		ListView lv = new ListView(Eq.this);

		lv.setBackgroundColor(Color.TRANSPARENT);
		lv.setCacheColorHint(0);
		lv.setLayoutParams(new AbsoluteLayout.LayoutParams(4 * intScreenX / 5,
				intScreenY / 2, intScreenX / 10, intScreenY / 4));
		ArrayList<TableRow> table = new ArrayList<TableRow>();
		TableCell[] titles = new TableCell[4];// 每行4个单元
		titles[0] = new TableCell("音符", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		titles[1] = new TableCell("出现总次数", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		titles[2] = new TableCell("错误次数", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		titles[3] = new TableCell("错误率", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		table.add(new TableRow(titles));

		TableCell[][] data = new TableCell[18][4];
		for (int i = 0; i < 18; i++) {
			data[i][0] = new TableCell(textFormat(i), intScreenX / 5,
					LayoutParams.FILL_PARENT, TableCell.STRING);
			data[i][1] = new TableCell(""
					+ allYinfu[i].getInt("allYinfu" + i, 0), intScreenX / 5,
					LayoutParams.FILL_PARENT, TableCell.STRING);
			data[i][2] = new TableCell(
					""
							+ (allYinfu[i].getInt("allYinfu" + i, 0) - correctYinfu[i].getInt(
									"correctYinfu" + i, 0)), intScreenX / 5,
					LayoutParams.FILL_PARENT, TableCell.STRING);
			float f = (float) (allYinfu[i].getInt("allYinfu" + i, 0) - correctYinfu[i]
					.getInt("correctYinfu" + i, 0))
					/ allYinfu[i].getInt("allYinfu" + i, 1);

			data[i][3] = new TableCell((float) (Math.round(f * 10000)) / 100
					+ "%", intScreenX / 5, LayoutParams.FILL_PARENT,
					TableCell.STRING);

			table.add(new TableRow(data[i]));
		}

		TableAdapter tableAdapter = new TableAdapter(Eq.this, table);
		lv.setAdapter(tableAdapter);
		return lv;
	}

	// 返回当前演奏曲目的统计结果的ListView
	public ListView getlv2() {
		ListView lv = new ListView(Eq.this);

		lv.setBackgroundColor(Color.TRANSPARENT);
		lv.setCacheColorHint(0);
		lv.setLayoutParams(new AbsoluteLayout.LayoutParams(4 * intScreenX / 5,
				intScreenY / 2, intScreenX / 10, 3 * intScreenY / 12));
		ArrayList<TableRow> table = new ArrayList<TableRow>();
		TableCell[] titles = new TableCell[4];// 每行4个单元
		titles[0] = new TableCell("音符", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		titles[1] = new TableCell("出现总次数", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		titles[2] = new TableCell("错误次数", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		titles[3] = new TableCell("错误率", intScreenX / 5,
				LayoutParams.FILL_PARENT, TableCell.STRING);
		table.add(new TableRow(titles));

		TableCell[][] data = new TableCell[18][4];
		for (int i = 0; i < 18; i++) {
			data[i][0] = new TableCell(textFormat(i), intScreenX / 5,
					LayoutParams.FILL_PARENT, TableCell.STRING);
			data[i][1] = new TableCell(""
					+ allYinfu2[i].getInt("allYinfu2" + i, 0), intScreenX / 5,
					LayoutParams.FILL_PARENT, TableCell.STRING);
			data[i][2] = new TableCell(
					""
							+ (allYinfu2[i].getInt("allYinfu2" + i, 0) - correctYinfu2[i].getInt(
									"correctYinfu2" + i, 0)), intScreenX / 5,
					LayoutParams.FILL_PARENT, TableCell.STRING);
			float f = (float) (allYinfu2[i].getInt("allYinfu2" + i, 0) - correctYinfu2[i]
					.getInt("correctYinfu2" + i, 0))
					/ allYinfu2[i].getInt("allYinfu2" + i, 1);

			data[i][3] = new TableCell((float) (Math.round(f * 10000)) / 100
					+ "%", intScreenX / 5, LayoutParams.FILL_PARENT,
					TableCell.STRING);

			table.add(new TableRow(data[i]));
		}

		TableAdapter tableAdapter = new TableAdapter(Eq.this, table);
		lv.setAdapter(tableAdapter);
		return lv;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new AlertDialog.Builder(this)

			.setMessage("Sure to exit?")

			.setNegativeButton("No", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			})

			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					finish();

				}

			}).show();

			return true;

		} else {

			return super.onKeyDown(keyCode, event);

		}

	}

}
