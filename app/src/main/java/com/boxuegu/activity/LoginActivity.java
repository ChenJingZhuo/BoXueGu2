package com.boxuegu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boxuegu.MainActivity;
import com.boxuegu.R;
import com.boxuegu.utils.MD5Utils;
import com.boxuegu.view.FindPswActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvBack;
    private TextView mTvMainTitle;
    private RelativeLayout mTitleBar;
    private ImageView mIvHead;
    /**
     * 请输入用户名
     */
    private EditText mEtUserName;
    /**
     * 请输入密码
     */
    private EditText mEtPsw;
    /**
     * 请再次输入密码
     */
    private EditText mEtPswAgain;
    /**
     * 登 录
     */
    private Button mBtnLogin;
    /**
     * 立即注册
     */
    private TextView mTvRegister;
    /**
     * 找回密码
     */
    private TextView mTvFindPsw;

    private String userName,psw,spPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置页面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setOnClickListener(this);
        mTvMainTitle = (TextView) findViewById(R.id.tv_main_title);
        mTvMainTitle.setText("登录");
        mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPsw = (EditText) findViewById(R.id.et_psw);
        mEtPswAgain = (EditText) findViewById(R.id.et_psw_again);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
        mTvFindPsw = (TextView) findViewById(R.id.tv_find_psw);
        mTvFindPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_login:
                userName=mEtUserName.getText().toString().trim();
                psw=mEtPsw.getText().toString().trim();
                String md5Psw= MD5Utils.md5(psw);
                spPsw=readPsw(userName);
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (md5Psw.equals(spPsw)){
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    saveLoginStatus(true,userName);
                    setResult(2);
                    finish();
                    return;
                }else if (!TextUtils.isEmpty(spPsw)&&!md5Psw.equals(spPsw)){
                    Toast.makeText(LoginActivity.this,"输入的用户名和密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(LoginActivity.this,"此用户名不存在",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_back:
                LoginActivity.this.finish();
                break;
            case R.id.tv_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.tv_find_psw:
                //跳转到找回密码界面（未实现）
                Intent intent2=new Intent(LoginActivity.this, FindPswActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 保存登录状态和登录用户到SharedPreferences中
     * @param status    登录状态
     * @param userName  用户名
     */
    private void saveLoginStatus(boolean status, String userName) {
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        editor.putBoolean("isLogin",status);
        editor.putString("loginUserName",userName);//存入登录时的用户名
        editor.commit();//提交修改
    }

    /**
     * 从SharedPreferences中根据用户名读取密码
     * @param userName  用户名
     * @return  返回读取到的密码
     */
    private String readPsw(String userName) {
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        return sp.getString(userName,"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            //从注册界面传递过来的用户名
            String userName=data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)){
                mEtUserName.setText(userName);
                //设置光标的位置
                mEtUserName.setSelection(userName.length());
            }
        }
    }
}
