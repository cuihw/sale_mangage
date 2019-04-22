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
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.module.MonthlyStaticBean;
import com.zhongwang.sale.module.MonthlyStaticData;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.DateUtils;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PreferencesUtils;
import com.zhongwang.sale.utils.ToastUtil;

import org.json.JSONObject;

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
 * Use the {@link FragmentMonthStatistic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMonthStatistic extends FragmentBase {
    private static final String TAG = "FragmentMonthStatistic";

    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.spinner)
    Spinner spinner;


    @BindView(R.id.listview)
    ListView listview;
    LoginResult loginData;
    private Calendar calendar = Calendar.getInstance();

    MultipleLayoutAdapter multiAdapter;

    private int type = 0;
    private List<MonthlyStaticData> listData;
    private String requestRecoder = "";

    public static FragmentMonthStatistic newInstance() {
        FragmentMonthStatistic fragment = new FragmentMonthStatistic();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HwLog.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_month_statistic, container, false);
        bindButterKnife(view);
        initListener();
        initView();
        initData();

        return view;
    }

    private void initView() {
        multiAdapter = new MultipleLayoutAdapter(getContext(), R.layout.item_monthly_query, listData);
        listview.setAdapter(multiAdapter);
        String sDate = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);
        datetime.setText(sDate);
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

    private synchronized void getDataMonthlyReport() {
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

        JSONObject jsonObject = new JSONObject(params);
        if (requestRecoder.equals(jsonObject)) {
            HwLog.i(TAG, "request process...");
            return;
        }
        requestRecoder = jsonObject.toString();
        if (requestRecoder == null) requestRecoder = "";

        HttpRequest.postData(getContext(), Constants.MONTH_REQUEST, params, new HttpRequest.RespListener<MonthlyStaticBean>() {
            @Override
            public void onResponse(int status, MonthlyStaticBean bean) {
                if (getContext() == null) return;
                ToastUtil.showTextToast(getContext(), bean.getMessage());
                if (bean.getCode() == Constants.SUCCEED_CODE) {
                    multiAdapter.replaceAll(new ArrayList<>());
                    List<MonthlyStaticData> data = bean.getData();
                    if (data == null || data.size() == 0) {
                        multiAdapter.replaceAll(new ArrayList<>());
                        ToastUtil.showTextToast(getContext(), bean.getMessage());
                        return;
                    }
                    multiAdapter.replaceAll(data);
                } else {
                    multiAdapter.replaceAll(new ArrayList<>());
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

    private final class MultipleLayoutAdapter extends CommonAdapter<MonthlyStaticData> {

        public MultipleLayoutAdapter(Context context, int layoutResId, List<MonthlyStaticData> data) {
            super(context, layoutResId, data);
        }

        //多种布局重写此方法即可
        @Override
        public int getLayoutResId(MonthlyStaticData item, int position) {
            int layoutResId = -1;
            if (item.isJiaQi()) {
                layoutResId = R.layout.item_monthly_query;
            } else {
                layoutResId = R.layout.item_monthly_query1;
            }
            return layoutResId;
        }

        @Override
        public void onUpdate(BaseAdapterHelper helper, MonthlyStaticData item, int position) {
            helper.setText(R.id.data_tv, item.getWname());
            helper.setText(R.id.number2, "" + item.getOut_number()); // 出货数量
            helper.setText(R.id.number3, "" + item.getPrice());  // 价格
            helper.setText(R.id.number, "" + item.getRmoney()); // 回款金额
            helper.setText(R.id.number1, "" + item.getDmoney()); // 销售金额
            helper.setText(R.id.number4, "" + item.getDbalance()); // 余款

            if (item.isJiaQi()) {
                helper.setText(R.id.number5, "" + item.getInit_money()); // 初期金额
                helper.setText(R.id.number6, "" + item.getDsend()); // 发出托盘
                helper.setText(R.id.number7, "" + item.getDrecycling()); // 回收托盘
                helper.setText(R.id.number8, "" + item.getDresidue()); // 剩余托盘
                helper.setText(R.id.number9, "" + item.getInit_tray()); // 初期托盘数
                helper.setText(R.id.number10, "" + item.getInit_tray()); // 上报平方
            } else {
                helper.setText(R.id.number5, "" + item.getBilling_price()); // 开票价格
                helper.setText(R.id.number6, "" + item.getBilling_number()); // 开票数量
                helper.setText(R.id.number6, "" + item.getBilling_dmoney()); // 开票总金额
            }
        }
    }

}
