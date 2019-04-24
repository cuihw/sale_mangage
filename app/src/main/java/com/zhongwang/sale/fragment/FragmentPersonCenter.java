package com.zhongwang.sale.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.zhongwang.sale.module.WorkSiteBean;
import com.zhongwang.sale.module.WorkSiteData;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PackageUtils;
import com.zhongwang.sale.utils.PreferencesUtils;
import com.zhongwang.sale.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @BindView(R.id.add_ground)
    TextView add_ground;

    LoginResult loginResult;
    CommonAdapter<WorkSiteData> adapter;
    List<WorkSiteData> dataList;

    public static FragmentPersonCenter newInstance() {
        FragmentPersonCenter fragment = new FragmentPersonCenter();
        return fragment;
    }

    AlertDialog addGroundDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HwLog.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        bindButterKnife(view);
        initListener();
        return view;
    }

    private String requestRecoder = "";

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
            ToastUtil.showTextToast(getContext(), "当前已经是最新版本");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initListener() {
        logout.setOnClickListener(v -> {
            String s = logout.getText().toString();
            if (s.equals("退出登录")) {
                // 退出登录
                logout.setText("登    录");
                LoginResult.clearLoginData(getContext());
                // 用户名设置为空，
                name.setText("用户未登录");
                // 工地信息清空
                if (adapter != null) adapter.replaceAll(new ArrayList<>());
            } else {
                logout.setText("退出登录");
                LoginActivity.startActivity(getContext(), null);
            }

        });

        check_version_layout.setOnClickListener(v -> {
            checkVersion();
        });
    }

    private void initView() {
        loginResult = LoginResult.getLoginDataFromPreference(getContext());
        if (loginResult == null) {
//          LoginActivity.startActivity(getContext(), null);
            name.setText("用户未登录");
            logout.setText("登     录");
            if (adapter != null) adapter.replaceAll(new ArrayList<>());
            return;
        }
        name.setText("用户名：" + loginResult.getData().getInfo().getName());
        initListview();

        version.setText("当前版本： " + PackageUtils.getVersionName(getContext()));
    }

    private void downloadApkUrl(String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url)); // Url 就是你要打开的网址
        intent.setAction(Intent.ACTION_VIEW);
        getActivity().startActivity(intent); //启动浏览器
    }

    private void initListview() {
        loginResult = LoginResult.getLoginDataFromPreference(getContext());
        if (loginResult == null) {
            return;
        }
        dataList = loginResult.getData().getResponsible(); // 工地数据

        if (dataList == null || dataList.size() == 0) {
            PopupDialog.create(getContext(), "工地数据为空",
                    "这个用户负责的工地数据为不正确，请联系管理员", "确定",
                    null).show();
            return;
        }
        adapter = new CommonAdapter<WorkSiteData>(getContext(), R.layout.item_personal_ground, dataList) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, WorkSiteData item, int position) {
                helper.setText(R.id.wid, "工地名字:" + item.getName());
                helper.setText(R.id.wname, "合同价:" + item.getContract_price());
            }
        };
        listview.setAdapter(adapter);

        add_ground.setOnClickListener(v -> {
            showInitDataDialog();
        });
    }

    private void showInitDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialog_Common);
        // 获取布局
        View view2 = View.inflate(getContext(), R.layout.dialog_ground, null);
        // 获取布局中的控件
        final EditText name = view2.findViewById(R.id.name);
        final EditText price = view2.findViewById(R.id.price);
        final TextView cancel = view2.findViewById(R.id.common_dialog_cancel_tv);
        final TextView confirm = view2.findViewById(R.id.common_dialog_confirm_tv);
        final TextView title = view2.findViewById(R.id.common_dialog_title_tv);
        title.setText("添加工地信息");
        cancel.setText("取消");
        confirm.setText("添加");
        // 设置参数
        builder.setView(view2);

        // 创建对话框
        addGroundDialog = builder.create();
        addGroundDialog.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener(v -> {
            addGroundDialog.dismiss();
        });
        confirm.setOnClickListener(v -> {
            String nameString = name.getText().toString();
            String priceString = price.getText().toString();
            uploadWorkSiteData(nameString, priceString);
        });
        addGroundDialog.show();
    }

    private synchronized void uploadWorkSiteData(String nameString, String priceString) {
        if (TextUtils.isEmpty(nameString)) {
            ToastUtil.showTextToast(getContext(), "必须填写名字");
            return;
        }

        Map<String, Object> params = new HashMap<>();

        priceString = transform2Decimal(priceString);

        params.put("name", nameString);
        params.put("contract_price", priceString);
        params.put("uid", loginResult.getData().getInfo().getId());

        JSONObject jsonObject = new JSONObject(params);
        if (requestRecoder.equals(jsonObject.toString())) {
            HwLog.i(TAG, "request process...");
            return;
        }
        requestRecoder = jsonObject.toString();
        if (requestRecoder == null) requestRecoder = "";

        HttpRequest.postData(getContext(), Constants.ADD_GROUND, params, new HttpRequest.RespListener<String>() {
            @Override
            public void onResponse(int status, String bean) {
                addGroundDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    int code = jsonObject.getInt("code");
                    if (code == Constants.SUCCEED_CODE) {
                        WorkSiteBean workSiteBean = new Gson().fromJson(bean, WorkSiteBean.class);
                        handleWorkSite(workSiteBean.getData());
                    } else {
                        ToastUtil.showTextToast(getContext(), jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String transform2Decimal(String priceString) {
        if (!priceString.contains(".")) {
            return priceString + ".00";
        } else {
            int end = priceString.lastIndexOf(".");
            int length = priceString.length();
            if (end == length - 1) {
                return priceString + "00";
            } else if (end == length - 2) {
                return priceString + "0";
            }
        }
        return priceString;
    }

    private void handleWorkSite(WorkSiteData data) {

        loginResult.getData().getResponsible().add(data);
        PreferencesUtils.putString(getContext(), Constants.LOGIN_DATA, loginResult.toJson());

        adapter.replaceAll(dataList);
    }
}
