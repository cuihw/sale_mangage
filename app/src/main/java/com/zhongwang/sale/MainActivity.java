package com.zhongwang.sale;

import android.os.Bundle;

import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.zhongwang.sale.activity.LoginActivity;
import com.zhongwang.sale.fragment.FragmentDaily;
import com.zhongwang.sale.fragment.FragmentDayStatistic;
import com.zhongwang.sale.fragment.FragmentMonthStatistic;
import com.zhongwang.sale.fragment.FragmentPersonCenter;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.utils.PreferencesUtils;
import com.zhongwang.sale.utils.ToastUtil;

import butterknife.BindString;
import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabbar)
    JPTabBar jpTabBar;

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
    }

    private void initListener() {
        jpTabBar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
                switch (index) {
                    case 0: // 日报
                        tranToFragment(fragmentDaily);
                        break;
                    case 1: // 日报查询
                        tranToFragment(fragmentDayStatistic);
                        break;
                    case 2: // 月报
                        tranToFragment(fragmentMonthStatistic);
                        break;
                    case 3: // 个人中心
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        tranToFragment(fragmentDaily);
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
                ToastUtil.showTextToast(this, "请登录系统");
                LoginActivity.startActivity(this,null);
            }
        } else {
            ToastUtil.showTextToast(this, "请登录系统");
            LoginActivity.startActivity(this,null);
        }
    }

    private void initTab() {
        jpTabBar.setTitles(home, day_statistics, month_statistics, personal)
                .setNormalIcons(R.mipmap.daily_default, R.mipmap.day_statistical_default, R.mipmap.month_statistical_default, R.mipmap.personal_center_default)
                .setSelectedIcons(R.mipmap.daily, R.mipmap.day_statistical, R.mipmap.month_statistical, R.mipmap.personal_center)
                .generate();
    }

}
