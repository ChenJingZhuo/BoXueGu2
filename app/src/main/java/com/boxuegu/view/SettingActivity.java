package com.boxuegu.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.boxuegu.R;

public class SettingActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private  TextView tv_back;
    private RelativeLayout r1_title_bar;
    private RelativeLayout r1_modify_psw,r1_security_setting,r1_exit_login;
    public static SettingActivity instance = null;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        instance=this;
        init();
    }
    private void init(){
        tv_main_title=(TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置");
        tv_back= (TextView) findViewById(R.id.tv_back);
        r1_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        r1_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        r1_modify_psw=(RelativeLayout) findViewById(R.id.r1_modify_pasw);
        r1_security_setting=(RelativeLayout) findViewById(R.id.r1_security_setting);
        r1_exit_login= (RelativeLayout) findViewById(R.id.r1_exit_login);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        r1_modify_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ModifyPswActivity.class);
                startActivity(intent);
            }
        });
        r1_security_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,FindPswActivity.class);
                intent.putExtra("from","security");
                startActivity(intent);
            }
        });
        r1_exit_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this,"退出登录成功",Toast.LENGTH_SHORT).show();
                clearLoginStatus();
                Intent data = new Intent();
                setResult(3);
                SettingActivity.this.finish();
            }
        });

    }
    private void clearLoginStatus(){
        SharedPreferences sp = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putBoolean("isLogin",false);
        editor.putString("loginUserName","点击登录");
        editor.commit();
    }

}
