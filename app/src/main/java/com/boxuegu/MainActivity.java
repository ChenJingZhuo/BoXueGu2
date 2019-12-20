package com.boxuegu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.boxuegu.activity.LoginActivity;
import com.boxuegu.activity.SearchContentActivity;
import com.boxuegu.activity.TestActivity;
import com.boxuegu.utils.AnalysisUtils;
import com.boxuegu.utils.PerfectClickListener;
import com.boxuegu.view.CourseView;
import com.boxuegu.view.ExercisesView;
import com.boxuegu.view.MyInfoView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvTitleMenu;
    private TextView mTvMainTitle;
    private ImageView mIvMenu;
    /**
     * 课程
     */
    private TextView mBottomBarTextCourse;
    private ImageView mBottomBarImageCourse;
    private RelativeLayout mBottomBarCourseBtn;
    /**
     * 课程
     */
    private TextView mBottomBarTextExercises;
    private ImageView mBottomBarImageExercises;
    private RelativeLayout mBottomBarExercisesBtn;
    /**
     * 课程
     */
    private TextView mBottomBarTextMyinfo;
    private ImageView mBottomBarImageMyinfo;
    private RelativeLayout mBottomBarMyinfoBtn;
    private LinearLayout mMainBottomBar;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;

    private RelativeLayout r1_title_bar;
    private ExercisesView mExercisesView;
    private CourseView mCourseView;

    private FrameLayout mBodyLayout;
    private LinearLayout mBottomLayout;
    private MyInfoView mMyInfoView;

    TextView userName1;
    private ImageView mIvSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initDrawerLayout();
        if (!AnalysisUtils.readLoginStatus(this)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /*@Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }*/

    private void initView() {
        mIvTitleMenu = (ImageView) findViewById(R.id.iv_title_menu);
        mIvTitleMenu.setOnClickListener(this::onClick);
        mTvMainTitle = (TextView) findViewById(R.id.tv_main_title);
        mTvMainTitle.setText("博学谷课程");
        r1_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        r1_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        initBodyLayout();

        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mIvMenu.setOnClickListener(this::onClick);
        mBottomLayout = (LinearLayout) findViewById(R.id.main_bottom_bar);
        mBottomBarTextCourse = (TextView) findViewById(R.id.bottom_bar_text_course);
        mBottomBarImageCourse = (ImageView) findViewById(R.id.bottom_bar_image_course);
        mBottomBarCourseBtn = (RelativeLayout) findViewById(R.id.bottom_bar_course_btn);
        mBottomBarCourseBtn.setOnClickListener(this);
        mBottomBarTextExercises = (TextView) findViewById(R.id.bottom_bar_text_exercises);
        mBottomBarImageExercises = (ImageView) findViewById(R.id.bottom_bar_image_exercises);
        mBottomBarExercisesBtn = (RelativeLayout) findViewById(R.id.bottom_bar_exercises_btn);
        mBottomBarExercisesBtn.setOnClickListener(this);
        mBottomBarTextMyinfo = (TextView) findViewById(R.id.bottom_bar_text_myinfo);
        mBottomBarImageMyinfo = (ImageView) findViewById(R.id.bottom_bar_image_myinfo);
        mBottomBarMyinfoBtn = (RelativeLayout) findViewById(R.id.bottom_bar_myinfo_btn);
        mBottomBarMyinfoBtn.setOnClickListener(this);
        mMainBottomBar = (LinearLayout) findViewById(R.id.main_bottom_bar);


        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
    }

    private void initBodyLayout() {
        mBodyLayout = (FrameLayout) findViewById(R.id.main_body);
    }

    private void initDrawerLayout() {
        mNavView.inflateHeaderView(R.layout.nav_header_main);
        View headerView = mNavView.getHeaderView(0);
        ImageView iv_avatar = headerView.findViewById(R.id.iv_avatar);
//        GlideUtil.displayCircle(iv_avatar, IC_AVATAR);
        userName1 = headerView.findViewById(R.id.tv_username);
        userName1.setOnClickListener(listener);
        if (readLoginStatus()) {
            userName1.setText(AnalysisUtils.readLoginUserName(this));
        }
        LinearLayout ll_nav_account = headerView.findViewById(R.id.ll_nav_account);
        ll_nav_account.setOnClickListener(listener);
        LinearLayout ll_nav_password = headerView.findViewById(R.id.ll_nav_password);
        ll_nav_password.setOnClickListener(listener);
        LinearLayout ll_nav_feedback = headerView.findViewById(R.id.ll_nav_feedback);
        ll_nav_feedback.setOnClickListener(listener);
        LinearLayout ll_nav_version_update = headerView.findViewById(R.id.ll_nav_version_update);
        ll_nav_version_update.setOnClickListener(listener);
        LinearLayout ll_nav_score = headerView.findViewById(R.id.ll_nav_score);
        ll_nav_score.setOnClickListener(listener);
        LinearLayout ll_nav_account_switch = headerView.findViewById(R.id.ll_nav_account_switch);
        ll_nav_account_switch.setOnClickListener(listener);
        LinearLayout ll_nav_logout = headerView.findViewById(R.id.ll_nav_logout);
        ll_nav_logout.setOnClickListener(listener);

        selectDisplayView(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!readLoginStatus()) {
            if (userName1 != null) {
                userName1.setText("点击登录");
            }
            if (mMyInfoView != null) {
                setUserUpdate();
            }
        }

    }

    public void setUserUpdate() {
        if (!AnalysisUtils.readLoginStatus(this)) {
            clearBottomImageState();
            selectDisplayView(0);
        }
        if (mMyInfoView != null) {
            mMyInfoView.setLoginParams(AnalysisUtils.readLoginStatus(this));
        }
    }

    private PerfectClickListener listener = new PerfectClickListener() {

        @Override
        protected void onNoDoubleClick(final View v) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.postDelayed(() -> {
                switch (v.getId()) {
                    case R.id.ll_nav_account:
                        Toast.makeText(MainActivity.this, "个人中心", Toast.LENGTH_SHORT).show();
                        Intent accountIntent = new Intent(MainActivity.this, TestActivity.class);
                        accountIntent.putExtra("text", "个人中心");
                        startActivity(accountIntent);
                        break;
                    case R.id.ll_nav_password:
                        Toast.makeText(MainActivity.this, "密码设置", Toast.LENGTH_SHORT).show();
                        Intent passwordIntent = new Intent(MainActivity.this, TestActivity.class);
                        passwordIntent.putExtra("text", "密码设置");
                        startActivity(passwordIntent);
                        break;
                    case R.id.ll_nav_feedback:
                        Toast.makeText(MainActivity.this, "意见反馈", Toast.LENGTH_SHORT).show();
                        Intent feedbackIntent = new Intent(MainActivity.this, TestActivity.class);
                        feedbackIntent.putExtra("text", "意见反馈");
                        startActivity(feedbackIntent);
                        break;
                    case R.id.ll_nav_version_update:
                        Toast.makeText(MainActivity.this, "版本更新", Toast.LENGTH_SHORT).show();
                        Intent updateIntent = new Intent(MainActivity.this, TestActivity.class);
                        updateIntent.putExtra("text", "版本更新");
                        startActivity(updateIntent);
                        break;
                    case R.id.ll_nav_score:
                        Toast.makeText(MainActivity.this, "给个评分呗", Toast.LENGTH_SHORT).show();
                        Intent scoreIntent = new Intent(MainActivity.this, TestActivity.class);
                        scoreIntent.putExtra("text", "给个评分呗");
                        startActivity(scoreIntent);
                        break;
                    case R.id.ll_nav_account_switch:
                        Toast.makeText(MainActivity.this, "切换账号", Toast.LENGTH_SHORT).show();
                        Intent switchIntent = new Intent(MainActivity.this, TestActivity.class);
                        switchIntent.putExtra("text", "切换账号");
                        startActivity(switchIntent);
                        break;
                    case R.id.ll_nav_logout:
                        if (readLoginStatus()) {
                            clearLoginStatus();
                            AnalysisUtils.saveLoginStatus(MainActivity.this, false);
                            Toast.makeText(MainActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
                            userName1.setText("点击登录");
                            if (mMyInfoView != null) {
                                mMyInfoView.setLoginParams(AnalysisUtils.readLoginStatus(MainActivity.this));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "您还未登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.tv_username:
                        if (readLoginStatus()) {

                        } else {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, 2);
                        }
                        break;
                    default:
                        break;
                }
            }, 0);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_title_menu:
                // 开启抽屉式菜单
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_menu:
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.menu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
                //显示(这一行代码不要忘记了)
                popup.show();
                break;
            case R.id.bottom_bar_course_btn:
                clearBottomImageState();
                selectDisplayView(0);
                break;
            case R.id.bottom_bar_exercises_btn:
                clearBottomImageState();
                selectDisplayView(1);
                break;
            case R.id.bottom_bar_myinfo_btn:
                clearBottomImageState();
                selectDisplayView(2);
                break;
        }
    }

    private void setListener() {
        for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    private void clearBottomImageState() {
        mBottomBarTextCourse.setTextColor(Color.parseColor("#666666"));
        mBottomBarTextExercises.setTextColor(Color.parseColor("#666666"));
        mBottomBarTextMyinfo.setTextColor(Color.parseColor("#666666"));
        mBottomBarImageCourse.setImageResource(R.drawable.main_course_icon);
        mBottomBarImageExercises.setImageResource(R.drawable.main_exercises_icon);
        mBottomBarImageMyinfo.setImageResource(R.drawable.main_my_icon);
        for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setSelected(false);
        }
    }

    private void setSelectedStatus(int index) {
        switch (index) {
            case 0:
                mBottomBarCourseBtn.setSelected(true);
                mBottomBarImageCourse.setImageResource(R.drawable.main_course_icon_selected);
                mBottomBarTextCourse.setTextColor(Color.parseColor("#0097F7"));
                r1_title_bar.setVisibility(View.VISIBLE);
                mTvMainTitle.setText("博学谷教程");
                mIvSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this, SearchContentActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 1:
                mBottomBarExercisesBtn.setSelected(true);
                mBottomBarImageExercises.setImageResource(R.drawable.main_exercises_icon_selected);
                mBottomBarTextExercises.setTextColor(Color.parseColor("#0097F7"));
                r1_title_bar.setVisibility(View.VISIBLE);
                mTvMainTitle.setText("博学谷习题");
                break;
            case 2:
                mBottomBarMyinfoBtn.setSelected(true);
                mBottomBarImageMyinfo.setImageResource(R.drawable.main_my_icon_selected);
                mBottomBarTextMyinfo.setTextColor(Color.parseColor("#0097F7"));
                r1_title_bar.setVisibility(View.VISIBLE);
                mTvMainTitle.setText("我的资料");
                break;
        }
    }

    private void removeAllView() {
        for (int i = 0; i < mBodyLayout.getChildCount(); i++) {
            mBodyLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    private void setInitStatus() {
        clearBottomImageState();
        setSelectedStatus(0);
        createView(0);
    }

    private void selectDisplayView(int index) {
        removeAllView();
        createView(index);
        setSelectedStatus(index);
    }

    private void createView(int viewIndex) {
        switch (viewIndex) {
            case 0:
                if (mCourseView == null) {
                    mCourseView = new CourseView(this);
                    mBodyLayout.addView(mCourseView.getView());
                } else {
                    mCourseView.getView();
                }
                mCourseView.showView();
                break;
            case 1:
                if (mExercisesView == null) {
                    mExercisesView = new ExercisesView(this);
                    mBodyLayout.addView(mExercisesView.getView());
                } else {
                    mExercisesView.getView();
                }
                mExercisesView.showView();
                break;
            case 2:
                if (mMyInfoView == null) {
                    mMyInfoView = new MyInfoView(this);
                    mBodyLayout.addView(mMyInfoView.getView());
                } else {
                    mMyInfoView.getView();
                    TextView tv_user_name = mMyInfoView.getView().findViewById(R.id.tv_user_name);
                    if (readLoginStatus()) {
                        tv_user_name.setText(AnalysisUtils.readLoginUserName(this));
                    } else {
                        userName1.setText("点击登录");
                        tv_user_name.setText("点击登录");
                    }
                }
                mMyInfoView.showView();
                break;
        }
    }

    protected long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出博学谷", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                if (readLoginStatus()) {
                    clearLoginStatus();
                }
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean readLoginStatus() {
        SharedPreferences sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }

    private void clearLoginStatus() {
        SharedPreferences sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false);
        editor.putString("loginUserName", "");
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AnalysisUtils.readLoginStatus(this)) {
            userName1.setText(AnalysisUtils.readLoginUserName(this));
            clearBottomImageState();
            selectDisplayView(0);
        }
        if (mMyInfoView != null) {
            mMyInfoView.setLoginParams(AnalysisUtils.readLoginStatus(this));
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fresh:
                // 在onCreate()中开启线程
                Toast.makeText(this, "点击了刷新！", Toast.LENGTH_SHORT).show();

//                new Thread(new GameThread()).start();
                break;
            case R.id.about:
                Toast.makeText(this, "点击了关于！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

}
