package com.boxuegu.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.boxuegu.R;

import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private VideoView videoView;
    private MediaController controller;
    private MediaPlayer mediaPlayer;
    private PlaybackParams playbackParams;
    private String videoPath;    //本地视频地址
    private int position;          //  传递视频详情界面的点击的视频位置
    //弹幕功能
    private boolean showDanmaku;
    private DanmakuView danmakuView;
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser parser=new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };


    /**
     * 播放速度×1
     */
    private TextView mPlaySpeed1;
    /**
     * 播放速度×0.5
     */
    private TextView mPlaySpeed05;
    /**
     * 播放速度×2
     */
    private TextView mPlaySpeed2;
    private FrameLayout mPlaySpeed;
    private static float SPEED=1.0f;

    private Spinner spinner;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置界面全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        //设置此界面为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //获取从播放记录界面传递过来的视频地址
        videoPath = getIntent().getStringExtra("videoPath");
        Log.d("VideoPlayActivity", "" + videoPath);
        position = getIntent().getIntExtra("position", 0);
        initView();

        danmakuView=(DanmakuView)findViewById(R.id.danmaku_view);
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmaku = true;
                danmakuView.start();
//                generateSomeDanmaku();
            }
            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext=DanmakuContext.create();
        danmakuView.prepare(parser,danmakuContext);

        final LinearLayout operationLayout = (LinearLayout) findViewById(R.id.operation_layout);
        final Button send = (Button) findViewById(R.id.send);
        final EditText editText = (EditText) findViewById(R.id.edit_text);
        danmakuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operationLayout.getVisibility() == View.GONE) {
                    operationLayout.setVisibility(View.VISIBLE);
                } else {
                    operationLayout.setVisibility(View.GONE);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    addDanmaku(content, true);
                    editText.setText("");
                }
            }
        });
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                    onWindowFocusChanged(true);
                }
            }
        });

        //下拉框
        Resources resources=getResources();
        String[] city=resources.getStringArray(R.array.province1);
        spinner=(Spinner)findViewById(R.id.spacer1);
        //创建ArrayAdapter适配器
        final ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,city);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text=spinner.getItemAtPosition(i).toString();
                Toast.makeText(VideoPlayActivity.this,"你对这个视频的满意度是："+text,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * 初始化UI控件
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoView);
        controller = new MediaController(this);
        videoView.setMediaController(controller);
        play();

        mPlaySpeed1 = (TextView) findViewById(R.id.play_speed_1);
        mPlaySpeed1.setOnClickListener(this);
        mPlaySpeed05 = (TextView) findViewById(R.id.play_speed_0_5);
        mPlaySpeed05.setOnClickListener(this);
        mPlaySpeed2 = (TextView) findViewById(R.id.play_speed_2);
        mPlaySpeed2.setOnClickListener(this);
        mPlaySpeed = (FrameLayout) findViewById(R.id.play_speed);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (videoView.isPlaying()){
                    mPlaySpeed.setVisibility(View.VISIBLE);
                }else {
                   mPlaySpeed.setVisibility(View.GONE);
                }

                return false;
            }
        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what==MediaPlayer.MEDIA_INFO_BUFFERING_START){
//                    bufferingCoverView.setVisibility(View.VISIBLE);
                }
                else {
//                    bufferingCoverView.setVisibility(View.GONE);
                }
                return true;
            }
        });
        //在视频预处理完成后调用
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer=mp;
//                bufferingCoverView.setVisibility(View.GONE);
                playbackParams=new PlaybackParams();
                playbackParams.setSpeed(SPEED);
                mp.setPlaybackParams(playbackParams);
            }
        });


    }

    /**
     * 播放视频
     */
    private void play() {
        /*if (TextUtils.isEmpty(videoPath)) {
            Toast.makeText(this, "本地没有此视频，暂无法播放", Toast.LENGTH_SHORT).show();
            return;
        }*/
        /*String uri = "android.resource://" + getPackageName() + "/" + R.raw.video11; //未有视频 需自找;
        videoView.setVideoPath(uri);
        videoView.start();*/

        String url = videoPath;
        videoView.setVideoPath(url);
        videoView.findFocus();
        videoView.start();
    }


    /**
     * 点击后退按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //把视频详情界面传递过来的被点击视频的位置传递回去
        Intent data = new Intent();
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_speed_1:
                Toast.makeText(this,"播放速度x1",Toast.LENGTH_SHORT).show();
                SPEED=1.0f;
                playbackParams.setSpeed(SPEED);
                mediaPlayer.setPlaybackParams(playbackParams);
                mediaPlayer.start();
                mPlaySpeed.setVisibility(View.GONE);
                break;
            case R.id.play_speed_0_5:
                Toast.makeText(this,"播放速度x0.5",Toast.LENGTH_SHORT).show();
                SPEED=0.5f;
                playbackParams.setSpeed(SPEED);
                mediaPlayer.setPlaybackParams(playbackParams);
                mediaPlayer.start();
                mPlaySpeed.setVisibility(View.GONE);
                break;
            case R.id.play_speed_2:
                Toast.makeText(this,"播放速度x2",Toast.LENGTH_SHORT).show();
                mPlaySpeed.setVisibility(View.GONE);
                SPEED=2.0f;
                playbackParams.setSpeed(SPEED);
                mediaPlayer.setPlaybackParams(playbackParams);
                mediaPlayer.start();
                break;
        }
    }

    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     *          弹幕的具体内容
     * @param  withBorder
     *          弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(danmakuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        danmakuView.addDanmaku(danmaku);
    }

//    /**
//     * 随机生成一些弹幕内容以供测试
//     */
//    private void generateSomeDanmaku() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(showDanmaku) {
//                    int time = new Random().nextInt(300);
//                    String content = "" + time + time;
//                    addDanmaku(content, false);
//                    try {
//                        Thread.sleep(time);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }
//
    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }
    /**
     * 沉浸式状态栏效果
     */ @Override public void onWindowFocusChanged(boolean hasFocus) {
         super.onWindowFocusChanged(hasFocus);
         if (hasFocus && Build.VERSION.SDK_INT >= 19) {
             View decorView = getWindow().getDecorView();
             decorView.setSystemUiVisibility(
                     View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                             | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                             | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                             | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                             | View.SYSTEM_UI_FLAG_FULLSCREEN
                             | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
         }
     }

}
