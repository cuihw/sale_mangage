package com.zhongwang.sale.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.google.gson.Gson;
import com.zhongwang.sale.Constants;
import com.zhongwang.sale.R;
import com.zhongwang.sale.activity.LoginActivity;
import com.zhongwang.sale.dialog.PopupDialog;
import com.zhongwang.sale.module.CheckVersionInfo;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PackageUtils;
import com.zhongwang.sale.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentPersonCenter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPersonCenter extends FragmentBase {
    private static final String TAG = "FragmentMonthStatistic";

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.check_version_layout)
    LinearLayout check_version_layout;

    @BindView(R.id.listview)
    ListView listview;

    @BindView(R.id.logout)
    TextView logout;

    LoginResult loginResult;
    CommonAdapter<LoginResult.GroundData> adapter;
    public static FragmentPersonCenter newInstance() {
        FragmentPersonCenter fragment = new FragmentPersonCenter();
        return fragment;
    }

    List<LoginResult.GroundData> dataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HwLog.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        bindButterKnife(view);
        initListener();
        return view;
    }

    private void initListener() {
        logout.setOnClickListener(v -> {
            LoginResult.clearLoginData(getContext());
            LoginActivity.startActivity(getContext(), null);
        });

        check_version_layout.setOnClickListener(v -> {
            checkVersion();
        });
    }

    private void checkVersion() {
        HttpRequest.getRequest(getContext(), Constants.UPDATE_INFO, new HttpRequest.ResponseListener<String>() {
            @Override
            public void onResponse(int status, String bean) {
                if (bean != null || bean.contains("versionName")) {
                    CheckVersionInfo checkVersionInfo = new Gson().fromJson(bean, CheckVersionInfo.class);
                    handleCheckVersion(checkVersionInfo);

                } else {
                    if (getContext() != null) {
                        ToastUtil.showTextToast(getContext(), "没有检测到新版本。");
                    }
                }
            }
        });
    }

    private void handleCheckVersion(CheckVersionInfo checkVersionInfo) {
        if (getContext() == null) return;
        if (PackageUtils.getVersionCode(getContext()) < checkVersionInfo.getCode()) {
            PopupDialog.create(getContext(), "发现新版本", "更新内容：" + checkVersionInfo.getVersionInfo()
                            + "\n 点击开始下载",
                    "下载", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadApkUrl(Constants.UPDATE_URL);
                        }
                    }).show();
        } else {
            // ToastUtil.showTextToast(getContext(), "当前已经是最新版本");
            PopupDialog.create(getContext(), "发现新版本", "更新内容：" + checkVersionInfo.getVersionInfo()
                            + "\n 点击开始下载",
                    "下载", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadApkUrl(Constants.UPDATE_URL);
                        }
                    }).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        loginResult = LoginResult.getLoginDataFromPreference(getContext());
        if (loginResult == null) {
            LoginActivity.startActivity(getContext(), null);
            return;
        }
        name.setText("用户名：" + loginResult.getUsername());
        initListview();

        version.setText("当前版本： " + PackageUtils.getVersionName(getContext()));
    }

    private void initListview() {
        loginResult = LoginResult.getLoginDataFromPreference(getContext());
        if (loginResult == null) {
            LoginActivity.startActivity(getContext(), null);
            return;
        }
        dataList = loginResult.getData(); // 工地数据
        if (dataList == null || dataList.size() == 0) {
            PopupDialog.create(getContext(), "工地数据为空",
                    "这个用户负责的工地数据为不正确，请联系管理员", "确定",
                    null).show();
            return;
        }
        adapter = new CommonAdapter<LoginResult.GroundData>(getContext(), R.layout.item_personal_ground, dataList) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, LoginResult.GroundData item, int position) {
                helper.setText(R.id.wid, "工地名字：" + item.getName());
                helper.setText(R.id.wname, "合同价格：" + item.getContract_price());
            }
        };
        listview.setAdapter(adapter);
    }

    private void downloadApkUrl(String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url)); // Url 就是你要打开的网址
        intent.setAction(Intent.ACTION_VIEW);
        getActivity().startActivity(intent); //启动浏览器
    }
}
