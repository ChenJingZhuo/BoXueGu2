package com.boxuegu.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Analyzer;

import com.boxuegu.R;

public class ChangeUserInfoActivity extends AppCompatActivity {
    private TextView tv_main_title,tv_save;
    private RelativeLayout rl_title_bar;
    private TextView tv_back;
    private String title,content;
    private int flag;
    private EditText et_content;
    private ImageView iv_delete;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         init();
}
    private void init(){
    title=getIntent().getStringExtra("title");
    content=getIntent().getStringExtra("content");
    flag=getIntent().getIntExtra("flag",0);
    tv_main_title=(TextView)findViewById(R.id.tv_main_title);
    tv_main_title.setText(title);
    rl_title_bar=(RelativeLayout)findViewById(R.id.title_bar);
    rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
    tv_back=(TextView)findViewById(R.id.tv_back);
    tv_save=(TextView)findViewById(R.id.tv_save);
    tv_save.setVisibility(View.VISIBLE);
    et_content=(EditText)findViewById(R.id.et_content);
    iv_delete=(ImageView)findViewById(R.id.iv_delete);
    if(!TextUtils.isEmpty(content)){
        et_content.setText(content);
        et_content.setSelection(content.length());

    }
    contentListener();
    tv_back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ChangeUserInfoActivity.this.finish();
        }
    });
    iv_delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            et_content.setText("");

        }
    });
    tv_save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent data =new Intent();
            String etContent=et_content.getText().toString().trim();
            switch (flag){
                case 1:
                    if(!TextUtils.isEmpty(etContent)){
                        data.putExtra("nickName",etContent);
                        setResult(RESULT_OK,data);
                        Toast.makeText(ChangeUserInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        ChangeUserInfoActivity.this.finish();
                    }else{
                        Toast.makeText(ChangeUserInfoActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 2:
                    if(!TextUtils.isEmpty(etContent)){
                        data.putExtra("signature",etContent);
                        setResult(RESULT_OK,data);
                        Toast.makeText(ChangeUserInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        ChangeUserInfoActivity.this.finish();
                    }else{
                        Toast.makeText(ChangeUserInfoActivity.this,"签名不能为空",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    });

    }
    private void contentListener(){
        et_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                Editable editable=et_content.getText();
                int len=editable.length();
                if(len>0){
                    iv_delete.setVisibility(View.VISIBLE);

            }else {
                    iv_delete.setVisibility(View.GONE);
                }
            switch (flag){
                case 1:
                    if(len>8){
                        int selEndIndex= Selection.getSelectionEnd(editable);
                        String str=editable.toString();
                        String newStr=str.substring(0,8);
                        et_content.setText(newStr);
                        editable=et_content.getText();
                        int newLen=editable.length();
                        if (selEndIndex>newLen){
                            selEndIndex=editable.length();
                        }
                        Selection.setSelection(editable,selEndIndex);
                    }
                    break;
                case 2:
                    if(len>16){
                        int selEndIndex=Selection.getSelectionEnd(editable);
                        String str=editable.toString();
                        String newStr=str.substring(0,16);
                        et_content.setText(newStr);
                        editable=et_content.getText();
                        int newLen=editable.length();
                        if (selEndIndex>newLen){
                            selEndIndex=editable.length();
                        }
                        Selection.setSelection(editable,selEndIndex);
                    }
                    break;
                    default:
                        break;
            }

            }
            public  void beforeTextChanged(CharSequence s,int start,int count,int after){

            }
            public void afterTextChanged(Editable arg0){

            }
        });
    }
}
