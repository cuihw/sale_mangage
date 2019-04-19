package com.zhongwang.sale.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.zhongwang.sale.Constants;
import com.zhongwang.sale.R;
import com.zhongwang.sale.dialog.PopupDialog;
import com.zhongwang.sale.module.DayStatisticBean;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.DateUtils;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PreferencesUtils;
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
 * Use the {@link FragmentDayStatistic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDayStatistic extends FragmentBase {
    private static final String TAG = "FragmentDayStatistic";
    private static final int BIAO_ZHUAN = 1;
    private static final int JIA_QI = 0;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.spinner2)
    Spinner spinner2;

    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.listview)
    ListView listview;

    Calendar calendar;

    LoginResult.GroundData groundData;

    int type = 0;

    LoginResult loginData;
    MultipleLayoutAdapter commonAdapter;
    List<DayStatisticBean.DayStatisticsData> listData;
    private ArrayAdapter<String> adapter;

    public static FragmentDayStatistic newInstance() {
        FragmentDayStatistic fragment = new FragmentDayStatistic();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HwLog.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_day_statistic, container, false);
        bindButterKnife(view);
        initView();
        initListener();
        initData();
        return view;
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

            List<LoginResult.GroundData> datas = loginData.getData();
            if (datas != null && datas.size() > 0) {
                initSpinner(datas);
                groundData = datas.get(0);
            } else {
                PopupDialog.create(getContext(), "工地数据为空",
                        "这个用户负责的工地数据为不正确，请联系管理员", "确定",
                        null).show();
            }
        }
    }

    private void initSpinner(List<LoginResult.GroundData> datas) {
        List<String> spinerItems = new ArrayList<>();
        for (LoginResult.GroundData data : datas) {
            spinerItems.add(data.getName());
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 加载适配器
        spinner2.setAdapter(adapter);
    }

    private void initListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
                HwLog.i(TAG, "type = " + type);
                getDataDailyReport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                getDataDailyReport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        datetime.setOnClickListener(v -> showDatePicker());
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
        dlg.setTitle("请选择月份");
        DatePicker datePicker1 = dlg.getDatePicker();
        datePicker1.setMaxDate(System.currentTimeMillis());
        dlg.show();
    }

    private void initView() {
        commonAdapter = new MultipleLayoutAdapter(getContext(), R.layout.item_daily_query, listData);
        listview.setAdapter(commonAdapter);
    }

    private void handleDate(int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);
        String sDate = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);
        datetime.setText(sDate);
        getDataDailyReport(); // 查询数据
    }

    private void getDataDailyReport() {
        HwLog.i(TAG, "getDataDailyReport");
        // 类型，场地，时间 去查询
        if (calendar == null) {
            ToastUtil.showTextToast(getContext(), "请选择时间");
            return;
        }
        if (groundData == null) {
            ToastUtil.showTextToast(getContext(), "工地选择错误");
            return;
        }
        String month = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);
        Map<String, Object> params = new HashMap<>();
        params.put("wid", groundData.getId());
        params.put("month", month);
        params.put("type", type + 1);

        HttpRequest.postData(getContext(), Constants.DAILY_REQUEST, params, new HttpRequest.RespListener<DayStatisticBean>() {
            @Override
            public void onResponse(int status, DayStatisticBean bean) {
                ToastUtil.showTextToast(getContext(), bean.getMessage());
                if (bean.getCode() == Constants.SUCCEED_CODE) {
                    List<DayStatisticBean.DayStatisticsData> data = bean.getData();
                    if (data == null || data.size() == 0) {
                        ToastUtil.showTextToast(getContext(), "本月还没有数据上传");
                        commonAdapter.replaceAll(new ArrayList<>());
                        return;
                    }
                    commonAdapter.replaceAll(data);
                }
            }
        });
    }

    private void alertMessage(String s) {
        PopupDialog dialog = PopupDialog.create(getContext(), "警   告", s, "确定", null);
        dialog.show();
    }

    @Override
    public void onPause() {
        calendar = null;
        super.onPause();
    }


    private final class MultipleLayoutAdapter extends CommonAdapter<DayStatisticBean.DayStatisticsData> {

        public MultipleLayoutAdapter(Context context, int layoutResId, List<DayStatisticBean.DayStatisticsData> data) {
            super(context, layoutResId, data);
        }

        //多种布局重写此方法即可
        @Override
        public int getLayoutResId(DayStatisticBean.DayStatisticsData item, int position) {
            int layoutResId = -1;
            if (item.isJiaQi()) {
                layoutResId = R.layout.item_daily_query;
            } else {
                layoutResId = R.layout.item_daily_query1;
            }
            return layoutResId;
        }

        @Override
        public void onUpdate(BaseAdapterHelper helper, DayStatisticBean.DayStatisticsData item, int position) {
            helper.setText(R.id.data_tv, item.getUp_date() + "（负责人： " + item.getUname() + "）");
            helper.setText(R.id.number2, "" + item.getOut_number()); // 出货数量
            helper.setText(R.id.number3, "" + item.getPrice());  // 价格
            helper.setText(R.id.number, "" + item.getRmoney()); // 回款金额
            helper.setText(R.id.number12, "" + item.getRemark()); // 备注

            if (item.isJiaQi()) {
                helper.setText(R.id.number1, "" + item.getDsend()); // 当日发出托盘
                helper.setText(R.id.number4, "" + item.getDrecycling()); // 当日回收托盘
                helper.setText(R.id.number5, "" + item.getDresidue()); // 当日剩余托盘
                helper.setText(R.id.number6, "" + item.getDmoney()); // 当日金额
                helper.setText(R.id.number7, "" + item.getDbalance()); // 当日余款
                helper.setText(R.id.number8, "" + item.getDmoney2()); // 当日发生金额
                helper.setText(R.id.number9, "" + item.getReport_square()); // 上报平方
            } else {
                helper.setText(R.id.number1, "" + item.getDbalance()); // 当日余款
                helper.setText(R.id.number4, "" + item.getBilling_number()); // 开票数量
                helper.setText(R.id.number5, "" + item.getBilling_price()); // 开票价格
            }
        }
    }
}
