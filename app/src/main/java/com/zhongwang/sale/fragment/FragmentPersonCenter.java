package com.zhongwang.sale.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.zhongwang.sale.R;
import com.zhongwang.sale.activity.LoginActivity;
import com.zhongwang.sale.dialog.PopupDialog;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.utils.HwLog;

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

    @BindView(R.id.listview)
    ListView listview;


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
        name.setText(loginResult.getUsername());
        initListview();
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
                helper.setText(R.id.wid, "工地号：" + item.getId());
                helper.setText(R.id.wname, "名字：" + item.getName());
            }
        };
        listview.setAdapter(adapter);
    }
}
