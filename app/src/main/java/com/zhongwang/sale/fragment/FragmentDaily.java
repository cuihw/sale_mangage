package com.zhongwang.sale.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhongwang.sale.Constants;
import com.zhongwang.sale.R;
import com.zhongwang.sale.dialog.PopupDialog;
import com.zhongwang.sale.module.InitDataBean;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.DateUtils;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentDaily#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDaily extends FragmentBase {
    private static final String TAG = "FragmentDaily";
    private static final int BIAO_ZHUAN = 0;
    private static final int JIA_QI = 1;

    @BindView(R.id.tablayout)
    TabLayout tablayout;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.datetime)
    TextView datetime;

    LoginResult loginData;

    LoginResult.GroundData groundData;

    int type = BIAO_ZHUAN;
    Calendar calendar = Calendar.getInstance();
    private ArrayAdapter<String> adapter;

    public static FragmentDaily newInstance() {
        FragmentDaily fragment = new FragmentDaily();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HwLog.i(TAG, "onCreateView");
        view  = inflater.inflate(R.layout.fragment_daily, container, false);
        bindButterKnife(view);
        initView();
        initListener();
        return view;
    }

    private void initListener() {
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                type = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                HwLog.i(TAG, "spinner id = " + id + ", str = " + str);
                for (LoginResult.GroundData data : loginData.getData()) {
                    if (data.getName().equals(str)) {
                        groundData = data;
                        break;
                    }
                }
                if (groundData != null)
                    HwLog.i(TAG, "groundData name = " + groundData.getName() + ", id = " + groundData.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        datetime.setOnClickListener(v -> showDatePicker());
    }


    private void showDatePicker() {

        DatePickerDialog datePicker = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        dealDate(year, monthOfYear, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }

    private void dealDate(int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);
        String sDate = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMMDD);
        datetime.setText(sDate);

        getInitDataMonth(calendar); // get init data.
    }

    private void getInitDataMonth(Calendar calendar) {
        if (groundData == null) {
            ToastUtil.showTextToast(getContext(), "请选择工地");
            return;
        }
        String month = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);

        Map<String, Object> params = new HashMap<>();
        params.put("wid", groundData.getId());
        params.put("on_time", month);

        HttpRequest.postData(getContext(), Constants.INIT_DATA, params,
                new HttpRequest.RespListener<InitDataBean>() {
                    @Override
                    public void onResponse(int status, InitDataBean bean) {
                        HwLog.i(TAG, "InitDataBean = " + bean.toJson());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initView() {
        tablayout.addTab(tablayout.newTab().setText("标砖"));
        tablayout.addTab(tablayout.newTab().setText("加气"));
        loginData = LoginResult.getLoginDataFromPreference(getContext());
        if (loginData == null) {
            PopupDialog.create(getContext(), "工地数据为空",
                    "这个用户负责的工地数据为不正确，请联系管理员", "确定",
                    null).show();

            return;
        }

        List<LoginResult.GroundData> data = loginData.getData();
        if (data != null && data.size() > 0) {
            initSpinner(data);
        } else {
            PopupDialog.create(getContext(), "工地数据为空",
                    "这个用户负责的工地数据为不正确，请联系管理员", "确定",
                    null).show();
        }

    }

    private void initSpinner(List<LoginResult.GroundData> datas) {
        List<String> spinerItems = new ArrayList<>();
        if (datas.size() == 1) {
            spinerItems.add(datas.get(0).getName());
            groundData = datas.get(0);
        } else {
            spinerItems.add("请选择工地");
            for (LoginResult.GroundData data : datas) {
                spinerItems.add(data.getName());
            }
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(adapter);
    }
}
