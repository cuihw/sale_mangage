package com.zhongwang.sale;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.zhongwang.sale.activity.LoginActivity;
import com.zhongwang.sale.dialog.PopupDialog;
import com.zhongwang.sale.fragment.FragmentDaily;
import com.zhongwang.sale.fragment.FragmentDayStatistic;
import com.zhongwang.sale.fragment.FragmentMonthStatistic;
import com.zhongwang.sale.fragment.FragmentPersonCenter;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PreferencesUtils;

import butterknife.BindString;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.tabbar)
    JPTabBar jpTabBar;

    @BindView(R.id.main)
    RelativeLayout main;


    @BindString(R.string.home)
    String home;

    @BindString(R.string.day_statistics)
    String day_statistics;

    @BindString(R.string.month_statistics)
    String month_statistics;

    @BindString(R.string.personal)
    String personal;

    LoginResult loginData;

    FragmentDaily fragmentDaily = FragmentDaily.newInstance();
    FragmentDayStatistic fragmentDayStatistic = FragmentDayStatistic.newInstance();
    FragmentMonthStatistic fragmentMonthStatistic = FragmentMonthStatistic.newInstance();
    FragmentPersonCenter fragmentPersonCenter = FragmentPersonCenter.newInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        initListener();
        tranToFragment(fragmentDaily);
    }

    private void initListener() {

        jpTabBar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
                switch (index) {
                    case 0: // 日报
                        setTitle("日报");
                        tranToFragment(fragmentDaily);
                        break;
                    case 1: // 日报查询
                        setTitle("日报查询");
                        tranToFragment(fragmentDayStatistic);
                        break;
                    case 2: // 月报
                        setTitle("月报查询");
                        tranToFragment(fragmentMonthStatistic);
                        break;
                    case 3: // 个人中心
                        setTitle("个人中心");
                        tranToFragment(fragmentPersonCenter);
                        break;
                    default: // 转到主页
                        tranToFragment(fragmentDaily);
                }
            }

            @Override
            public boolean onInterruptSelect(int index) {
                return false;
            }
        });

        setListenerToRootView();
    }

    private void setListenerToRootView() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                boolean mKeyboardUp = isKeyboardShown(rootView);
                if (mKeyboardUp) {
                    //键盘弹出
                    HwLog.i(TAG, "mKeyboardUp = " + mKeyboardUp);
                    jpTabBar.setVisibility(View.GONE);
                } else {
                    //键盘收起
                    HwLog.i(TAG, "mKeyboardUp = " + mKeyboardUp);
                    jpTabBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private boolean isKeyboardShown(View rootView) {
//        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//        return inputMethodManager.isActive();
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        // DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int height = MainActivity.this.getWindow().getDecorView().getRootView().getHeight();
        int heightDiff = height - r.bottom;
        return heightDiff > softKeyboardHeight;
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

        String loginAddress = PreferencesUtils.getString(this, Constants.LOGIN_ADDRESS, "");
        if (!Constants.LOGIN_URL.equals(loginAddress)) {
            PreferencesUtils.putBoolean(this, Constants.IS_LOGIN, false);
            LoginResult.clearLoginData(this);
        }

        boolean isLogin = PreferencesUtils.getBoolean(this, Constants.IS_LOGIN, false);
        if (isLogin) {
            loginData = LoginResult.getLoginDataFromPreference(this);
            if (loginData == null) {
                loginActivity();
            }
        } else {
            loginActivity();
        }
    }

    private void loginActivity() {
        PopupDialog.create(this, "请登录", "请登录后进行操作"
                , "登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginActivity.startActivity(MainActivity.this, null);
                    }
                }, "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTitle("个人中心");
                        tranToFragment(fragmentPersonCenter);
                    }
                });
    }

    private void initTab() {
        setTitle("日报");
        jpTabBar.setTitles(home, day_statistics, month_statistics, personal)
                .setNormalIcons(R.mipmap.daily_default, R.mipmap.day_statistical_default, R.mipmap.month_statistical_default, R.mipmap.personal_center_default)
                .setSelectedIcons(R.mipmap.daily, R.mipmap.day_statistical, R.mipmap.month_statistical, R.mipmap.personal_center)
                .generate();
    }

}
