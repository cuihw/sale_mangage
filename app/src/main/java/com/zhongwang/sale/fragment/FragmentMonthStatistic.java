package com.zhongwang.sale.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhongwang.sale.Constants;
import com.zhongwang.sale.R;
import com.zhongwang.sale.dialog.PopupDialog;
import com.zhongwang.sale.module.Bean;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.DateUtils;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PreferencesUtils;
import com.zhongwang.sale.utils.ToastUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentMonthStatistic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMonthStatistic extends FragmentBase {
    private static final String TAG = "FragmentMonthStatistic";

    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.spinner)
    Spinner spinner;
    LoginResult loginData;
    private Calendar calendar;

    public static FragmentMonthStatistic newInstance() {
        FragmentMonthStatistic fragment = new FragmentMonthStatistic();
        return fragment;
    }

    private int type = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HwLog.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_month_statistic, container, false);
        bindButterKnife(view);
        initListener();
        initData();
        return view;
    }

    private void initListener() {
        datetime.setOnClickListener(v -> showDatePicker());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
                getDataMonthlyReport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePicker() {
        if (calendar == null) calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                handleDate(year, monthOfYear, dayOfMonth);
            }
        }, yy, mm, dd) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                LinearLayout mSpinners = findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                if (mSpinners != null) {
                    NumberPicker mMonthSpinner = findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                    NumberPicker mYearSpinner = findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                    mSpinners.removeAllViews();
                    if (mYearSpinner != null) {
                        mSpinners.addView(mYearSpinner);
                    }
                    if (mMonthSpinner != null) {
                        mSpinners.addView(mMonthSpinner);
                    }
                }
                View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                if (dayPickerView != null) {
                    dayPickerView.setVisibility(View.GONE);
                }
            }
        };
        DatePicker datePicker1 = dlg.getDatePicker();
        datePicker1.setMaxDate(System.currentTimeMillis());
        dlg.show();
    }

    private void handleDate(int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);
        String sDate = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);
        datetime.setText(sDate);
        getDataMonthlyReport();
    }

    private void getDataMonthlyReport() {
        HwLog.i(TAG, "getDataMonthlyReport");
        // 类型，场地，时间 去查询
        if (calendar == null) {
            ToastUtil.showTextToast(getContext(), "请选择时间");
            return;
        }
        List<LoginResult.GroundData> datas = loginData.getData();
        if (datas == null || datas.size() == 0) {
            ToastUtil.showTextToast(getContext(), "没有工地数据，请联系管理员");
            return;
        }
        String ids = "";
        for (LoginResult.GroundData data : datas) {
            ids = ids + data.getId() + ",";
        }
        ids = ids.substring(0, (ids.length()) - 1);

        String month = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);
        Map<String, Object> params = new HashMap<>();
        params.put("wids", ids);
        params.put("month", month);
        params.put("type", type + 1);

        HttpRequest.postData(getContext(), Constants.MONTH_REQUEST, params, new HttpRequest.RespListener<Bean>() {
            @Override
            public void onResponse(int status, Bean bean) {
                ToastUtil.showTextToast(getContext(), bean.getMessage());
                if (bean.getCode() == Constants.SUCCEED_CODE) {

                }
            }
        });

    }

    private void initData() {
        boolean isLogin = PreferencesUtils.getBoolean(getContext(), Constants.IS_LOGIN, false);
        if (isLogin) {
            loginData = LoginResult.getLoginDataFromPreference(getContext());
            if (loginData == null) {
                PopupDialog.create(getContext(), "工地数据为空",
                        "这个用户负责的工地数据为不正确，请联系管理员", "确定",
                        null).show();
                return;
            }
        }
    }

    @Override
    public void onPause() {
        calendar = null;
        super.onPause();
    }
}
