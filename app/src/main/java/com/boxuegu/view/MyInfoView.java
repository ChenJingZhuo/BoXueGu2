package com.boxuegu.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boxuegu.R;
import com.boxuegu.activity.LoginActivity;
import com.boxuegu.activity.PlayHistoryActivity;
import com.boxuegu.activity.UserInfoActivity;
import com.boxuegu.utils.AnalysisUtils;

public class MyInfoView {
    public ImageView iv_head_icon;
    private LinearLayout ll_head;
    private RelativeLayout r1_course_history,r1_setting;
    private TextView tv_user_name;
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrenView;
    public MyInfoView(Activity context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
    private void createView(){
        initView();
    }
    private void initView(){
        mCurrenView = mInflater.inflate(R.layout.main_view_myinfo,null);
        ll_head= (LinearLayout) mCurrenView.findViewById(R.id.ll_head);
        iv_head_icon  =(ImageView)  mCurrenView.findViewById(R.id.iv_head_icon);
        r1_course_history = (RelativeLayout) mCurrenView.findViewById(R.id.r1_course_history);
        r1_setting = (RelativeLayout) mCurrenView.findViewById(R.id.r1_setting);
        tv_user_name = (TextView) mCurrenView.findViewById(R.id.tv_user_name);
        mCurrenView.setVisibility(View.VISIBLE);
        setLoginParams(readLoginStatus());
        ll_head.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(readLoginStatus()){
                    Intent intent=new Intent(mContext, UserInfoActivity.class);
                    mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivityForResult(intent,1);
                }
            }
        });
        r1_course_history.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(readLoginStatus()){
                    Intent intent=new Intent(mContext, PlayHistoryActivity.class);
                    mContext.startActivity(intent);
                }else{
                    Toast.makeText(mContext,"您还未登录，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
        r1_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(readLoginStatus()){
                    Intent intent=new Intent(mContext,SettingActivity.class);
                    mContext.startActivity(intent);
                }else{
                    Toast.makeText(mContext,"您还未登录，请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setLoginParams(boolean isLogin){
        if(isLogin){
            tv_user_name.setText(AnalysisUtils.readLoginUserName(mContext));
        }else{
            tv_user_name.setText("点击登录");
        }
    }

    public View getView(){
        if(mCurrenView == null){
            createView();
        }
        return mCurrenView;
    }
    public void showView(){
        if(mCurrenView == null){
            createView();
        }
        mCurrenView.setVisibility(View.VISIBLE);
    }
    private boolean readLoginStatus(){
        SharedPreferences sp = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }
}
