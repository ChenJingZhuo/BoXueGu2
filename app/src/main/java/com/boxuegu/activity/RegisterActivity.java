package com.boxuegu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.boxuegu.R;
import com.boxuegu.utils.MD5Utils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvBack;       //返回按钮
    private TextView mTvMainTitle;  //标题
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
     * 注 册
     */
    private Button mBtnRegister;    //注册按钮

    private String userName,psw,pswAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //设置页面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setOnClickListener(this);
        mTvMainTitle = (TextView) findViewById(R.id.tv_main_title);
        mTvMainTitle.setText("注册");
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPsw = (EditText) findViewById(R.id.et_psw);
        mEtPswAgain = (EditText) findViewById(R.id.et_psw_again);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_register:
                //获取输入在相应控件中的字符串
                getEditString();
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"再次输入的密码不一样",Toast.LENGTH_SHORT).show();
                    return;
                }else if (isExistUserName(userName)){
                    Toast.makeText(RegisterActivity.this,"此账户名已存在",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    saveRegisterInfo(userName,psw);
                    Intent data=new Intent();
                    data.putExtra("userName",userName);
                    setResult(RESULT_OK,data);
                    RegisterActivity.this.finish();
                }
                break;
            case R.id.tv_back:
                RegisterActivity.this.finish();
                break;
        }
    }

    /**
     * 保存用户名和密码到SharedPreferences中
     * @param userName
     * @param psw
     */
    private void saveRegisterInfo(String userName, String psw) {
        String md5Psw= MD5Utils.md5(psw);//把密码用MD5加密
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor=sp.edit();
        //以用户名为key,密码为value保存到SharedPreferences中（键值对）
        editor.putString(userName,md5Psw);
        editor.commit();
    }

    /**
     * 从SharedPreferences中读取输入的用户名，并判断此用户名是否存在
     * @param userName  输入的用户名
     * @return  如果存在返回true，不存在返回false
     */
    private boolean isExistUserName(String userName) {
        boolean has_userName=false;
        SharedPreferences sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
        String spPsw=sp.getString(userName,"");
        if (!TextUtils.isEmpty(spPsw)){
            has_userName=true;
        }
        return has_userName;
    }

    /**
     * 获取控件中的字符串
     */
    private void getEditString() {
        userName=mEtUserName.getText().toString().trim();
        psw=mEtPsw.getText().toString().trim();
        pswAgain=mEtPswAgain.getText().toString().trim();
    }
}
