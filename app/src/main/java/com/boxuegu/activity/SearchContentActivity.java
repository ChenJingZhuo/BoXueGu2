package com.boxuegu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.boxuegu.R;
import com.boxuegu.adapter.SearchRecordsAdapter;
import com.boxuegu.sqlite.dao.RecordsDao;

import java.util.ArrayList;
import java.util.List;

public class SearchContentActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText searchContentEt;
    private SearchRecordsAdapter recordsAdapter;
    private View recordsHistoryView;
    private ListView recordsListLv;
    private TextView clearAllRecordsTv;
    private LinearLayout searchRecordsLl;

    private List<String> searchRecordsList;
    private List<String> tempList;
    private RecordsDao recordsDao;
    private TextView tv_history;
    private ImageView mIvSearch;
    /**
     * 取消
     */
    private TextView mSearchContentCancelTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
        initView();
        initData();
        bindAdapter();
        initListener();
    }

    private void initView() {
//        setHideHeader();
        initRecordsView();

        searchRecordsLl = (LinearLayout) findViewById(R.id.search_content_show_ll);
        searchContentEt = (EditText) findViewById(R.id.input_search_content_et);
        tv_history = (TextView) findViewById(R.id.tv_history);

        //添加搜索view
        searchRecordsLl.addView(recordsHistoryView);

        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(this::onClick);
        mSearchContentCancelTv = (TextView) findViewById(R.id.search_content_cancel_tv);
        mSearchContentCancelTv.setOnClickListener(this::onClick);
    }

    //初始化搜索历史记录View
    private void initRecordsView() {
        recordsHistoryView = LayoutInflater.from(this).inflate(R.layout.search_layout, null);
        //显示历史记录lv
        recordsListLv = (ListView) recordsHistoryView.findViewById(R.id.search_records_lv);
        //清除搜索历史记录
        clearAllRecordsTv = (TextView) recordsHistoryView.findViewById(R.id.clear_all_records);
    }


    private void initData() {
        recordsDao = new RecordsDao(this);
        searchRecordsList = new ArrayList<>();
        tempList = new ArrayList<>();
        tempList.addAll(recordsDao.getRecordsList());

        reversedList();
        //第一次进入判断数据库中是否有历史记录，没有则不显示
        checkRecordsSize();
    }


    private void bindAdapter() {
        recordsAdapter = new SearchRecordsAdapter(this, searchRecordsList);
        recordsListLv.setAdapter(recordsAdapter);
    }

    private void initListener() {
        clearAllRecordsTv.setOnClickListener(this);
        searchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchContentEt.getText().toString().length() > 0) {

                        String record = searchContentEt.getText().toString();

                        //判断数据库中是否存在该记录
                        if (!recordsDao.isHasRecord(record)) {
                            tempList.add(record);
                        }
                        //将搜索记录保存至数据库
                        recordsDao.addRecords(record);
                        reversedList();
                        checkRecordsSize();
                        recordsAdapter.notifyDataSetChanged();
                        Toast.makeText(SearchContentActivity.this, "" + record, Toast.LENGTH_SHORT).show();
                        //根据关键词去搜索
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("http://www.baidu.com/#wd=" + searchContentEt.getText());
                        intent.setData(uri);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SearchContentActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        //根据输入的信息去模糊搜索
        searchContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    tv_history.setText("搜索历史");
                    tempList.clear();//清空列表
                    tempList.addAll(recordsDao.getRecordsList());//添加列表数据
                    reversedList();//反序
                    checkRecordsSize();//显示或隐藏控件
                    recordsAdapter.notifyDataSetChanged();//刷新适配器数据
                } else {
                    tv_history.setText("搜索结果");
                    tempList.clear();
                    tempList.addAll(recordsDao.querySimlarRecord(s.toString()));
                    reversedList();
                    checkRecordsSize();
                    recordsAdapter.notifyDataSetChanged();
                }

            }
        });
        //历史记录点击事件d
        recordsListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将获取到的字符串传到搜索结果界面
                //点击后搜索对应条目内容
                searchContentEt.setText(searchRecordsList.get(position));
                Toast.makeText(SearchContentActivity.this, searchRecordsList.get(position) + "", Toast.LENGTH_SHORT).show();
                searchContentEt.setSelection(searchContentEt.length());
            }
        });
        recordsListLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchContentActivity.this)
                        .setMessage("是否删除该记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView tv = view.findViewById(R.id.search_content_tv);
                                recordsDao.delete(tv.getText().toString());
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    //当没有匹配的搜索数据的时候不显示历史记录栏
    private void checkRecordsSize() {
        if (searchRecordsList.size() == 0) {
            searchRecordsLl.setVisibility(View.GONE);
        } else {
            searchRecordsLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                String record = searchContentEt.getText().toString();
                //判断数据库中是否存在该记录
                if (!recordsDao.isHasRecord(record)) {
                    tempList.add(record);
                }
                //将搜索记录保存至数据库
                recordsDao.addRecords(record);
                reversedList();
                checkRecordsSize();
                recordsAdapter.notifyDataSetChanged();
                Toast.makeText(SearchContentActivity.this, "" + record, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("http://www.baidu.com/#wd=" + searchContentEt.getText());
                intent.setData(uri);
                startActivity(intent);
                finish();
                break;
            case R.id.search_content_cancel_tv:
                finish();
                break;
            //清空所有历史数据
            case R.id.clear_all_records:
                tempList.clear();//清空列表
                reversedList();//清空搜索记录
                recordsDao.deleteAllRecords();//清空数据库记录
                checkRecordsSize();//隐藏清空历史记录控件
                recordsAdapter.notifyDataSetChanged();//刷新适配器数据
                searchContentEt.setText("");
                searchContentEt.setHint("请输入你要搜索的内容");
                break;
        }
    }

    //颠倒list顺序，用户输入的信息会从上依次往下显示
    private void reversedList() {
        searchRecordsList.clear();
        for (int i = tempList.size() - 1; i >= 0; i--) {
            searchRecordsList.add(tempList.get(i));
        }
    }
}
