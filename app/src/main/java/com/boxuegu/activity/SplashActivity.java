package com.boxuegu.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.boxuegu.MainActivity;
import com.boxuegu.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private TextView mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //设置页面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        try{

            //获取程序包信息
            PackageInfo info=getPackageManager().getPackageInfo(getPackageName(),0);
            mTvVersion.setText("V"+info.versionName);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            mTvVersion.setText("V");
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },2000);

    }
}
