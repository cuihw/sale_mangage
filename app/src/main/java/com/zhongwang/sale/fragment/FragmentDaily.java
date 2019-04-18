package com.zhongwang.sale.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhongwang.sale.Constants;
import com.zhongwang.sale.R;
import com.zhongwang.sale.dialog.PopupDialog;
import com.zhongwang.sale.module.Bean;
import com.zhongwang.sale.module.InitDataBean;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.DateUtils;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PreferencesUtils;
import com.zhongwang.sale.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @BindView(R.id.init_data)
    TextView init_data;

    @BindView(R.id.commit_action)
    TextView commit_action;

    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.price)
    EditText price;
    @BindView(R.id.backmoney)
    EditText backmoney;
    @BindView(R.id.send_tray_number)
    EditText send_tray_number;
    @BindView(R.id.back_tray_number)
    EditText back_tray_number;


    LoginResult loginData;

    LoginResult.GroundData groundData;

    int type = BIAO_ZHUAN;

    Calendar calendar = Calendar.getInstance();

    private ArrayAdapter<String> adapter;

    AlertDialog initDataAlertDialog;

    InitDataBean initDataBean; // 初期值

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
                if (groundData != null) {
                    HwLog.i(TAG, "groundData name = " + groundData.getName() + ", id = " + groundData.getId());
                    getInitDataMonth(calendar);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        datetime.setOnClickListener(v -> showDatePicker());

        commit_action.setOnClickListener(v -> upLoadDailyReport());
        init_data.setOnClickListener(v -> {
            if (groundData != null) {
                showInitDataDialog();
            } else {
                alertDia("请选择工地");
            }
        });
    }

    private void upLoadDailyReport() {
        // check type

        if (type == JIA_QI) {

        }
        if (groundData == null) {
            // 请选择工地
            alertDia("请选择工地");
            return;
        }

        if (initDataBean == null || initDataBean.getData() == null) {
            // 上报初期值
            showInitDataDialog();
            return;
        }
        String numbers = number.getText().toString(); // 数量
        String unitPrice = price.getText().toString(); // 单价
        String backMoney = backmoney.getText().toString(); // 回款
        String sendTrayNumber = send_tray_number.getText().toString(); // 发出托盘数
        String backTrayNumber = back_tray_number.getText().toString(); // 发出托盘数

        if (TextUtils.isEmpty(numbers)) {
            alertDia("请输入数量");
            return;
        }
        if (TextUtils.isEmpty(unitPrice)) {
            alertDia("请输入单价");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("wid", groundData.getId());
        params.put("wname", groundData.getName());
        params.put("out_number", numbers);
        params.put("price", unitPrice);
        if (!TextUtils.isEmpty(backMoney)) {
            params.put("rmoney", backMoney);
        }
        if (!TextUtils.isEmpty(sendTrayNumber)) {
            params.put("dsend", sendTrayNumber);
        }

        if (!TextUtils.isEmpty(backTrayNumber)) {
            params.put("drecycling", backTrayNumber);
        }

        params.put("uname", loginData.getUsername());
        String date = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMMDD);
        params.put("up_date", date);

        HttpRequest.postData(getContext(), Constants.SALES_UPDATE, params, new HttpRequest.RespListener<Bean>() {
            @Override
            public void onResponse(int status, Bean bean) {
                HwLog.i(TAG, bean.getMessage());
            }
        });
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
        String initDataKey = getInitDataKey(groundData.getId(), month);
        String init = PreferencesUtils.getString(getContext(), initDataKey, null);

        if (!TextUtils.isEmpty(init)) {
            initDataBean = InitDataBean.fromJson(init);
            InitDataBean.InitData data = initDataBean.getData();
            if (data != null) {
                // 已经得到，不再去请求。
                showInitData(initDataBean);
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("wid", groundData.getId());
        params.put("on_time", month);

        HttpRequest.postData(null, Constants.REQUEST_INIT_DATA, params,
                new HttpRequest.RespListener<InitDataBean>() {
                    @Override
                    public void onResponse(int status, InitDataBean bean) {
                        HwLog.i(TAG, "InitDataBean = " + bean.toJson());
                        if (bean.getCode() == Constants.SUCCEED_CODE) {
                            saveInitData(bean);
                        } else {
                            ToastUtil.showTextToast(getContext(), "查询失败！");
                        }
                    }
                });
    }

    private void showInitData(InitDataBean initDataBean) {
        if (initDataBean == null) {
            init_data.setText("初期数： 无");
            return;
        }

        HwLog.i(TAG, "showInitData = " + initDataBean.toJson());
        InitDataBean.InitData data = initDataBean.getData();
        if (data == null) {
            init_data.setText("初期数： 无");
        } else {
            init_data.setText("初期数：上报时间：" + initDataBean.getData().getUpdata_time() + ";    金额：" +
                    initDataBean.getData().getInit_money() + ";    托盘：" + initDataBean.getData().getInit_tray());
        }
    }

    private void saveInitData(InitDataBean bean) {
        initDataBean = bean;
        showInitData(bean);

        if (bean == null) return;
        InitDataBean.InitData data = bean.getData();
        if (data == null) {
            // TODO: upload init data. init money and tray. dialog
            showInitDataDialog();
            return;
        }
        String key = getInitDataKey(data.getWid(), data.getOn_time());
        PreferencesUtils.putString(getContext(), key, bean.toJson());
    }

    private void showInitDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialog_Common);
        // 获取布局
        View view2 = View.inflate(getContext(), R.layout.initdata_dialog, null);
        // 获取布局中的控件
        final EditText init_money = view2.findViewById(R.id.init_money);
        final EditText init_tray = view2.findViewById(R.id.init_tray);
        final TextView cancel = view2.findViewById(R.id.common_dialog_cancel_tv);
        final TextView confirm = view2.findViewById(R.id.common_dialog_confirm_tv);
        final TextView title = view2.findViewById(R.id.common_dialog_title_tv);
        title.setText("请输入初期数");
        cancel.setText("取消");
        confirm.setText("上传");
        // 设置参数
        builder.setView(view2);

        // 创建对话框
        initDataAlertDialog = builder.create();
        initDataAlertDialog.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener(v -> {
            initDataAlertDialog.dismiss();
            if (groundData != null) {
                String month = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);
                String key = getInitDataKey(groundData.getId(), month);
                String string = PreferencesUtils.getString(getContext(), key, null);
                if (TextUtils.isEmpty(string)) {
                    initDataBean = null;
                } else {
                    initDataBean = InitDataBean.fromJson(string);
                }
            } else {
                initDataBean = null;
            }
        });
        confirm.setOnClickListener(v -> {
            String initMoney = init_money.getText().toString();
            String initTray = init_tray.getText().toString();
            postInitData(initMoney, initTray);
        });
        initDataAlertDialog.show();
    }


    // 上传初期数
    private void postInitData(String init_money, String init_tray) {
        if (TextUtils.isEmpty(init_money)) {
            alertDia("初期金额为空，不能上传");
            return;
        }
        if (TextUtils.isEmpty(init_tray)) {
            alertDia("初期托盘为空，不能上传");
            return;
        }

        String month = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMM);
        Map<String, Object> params = new HashMap<>();
        params.put("wid", groundData.getId());
        params.put("on_time", month);
        params.put("init_money", init_money);
        params.put("init_tray", init_tray);
        HttpRequest.postData(getContext(), Constants.SAVE_INIT_DATA, params, new HttpRequest.RespListener<Bean>() {
            @Override
            public void onResponse(int status, Bean bean) {
                if (bean.getCode() == Constants.SUCCEED_CODE) {
                    HwLog.i(TAG, "upload init data succeed.");
                    InitDataBean initDataBean = new InitDataBean();
                    initDataBean.setCode(1);
                    initDataBean.setMessage("succeed.");
                    InitDataBean.InitData data = new InitDataBean.InitData();
                    data.setInit_money(init_money);
                    data.setInit_tray(init_tray);
                    data.setOn_time(month);
                    data.setWid(groundData.getId());
                    data.setUpdata_time(DateUtils.formatDateByFormat(new Date(), DateUtils.fmtYYYYMMDDhhmmss));
                    initDataBean.setData(data);
                    saveInitData(initDataBean);
                    initDataAlertDialog.dismiss();
                    initDataAlertDialog = null;
                    ToastUtil.showTextToast(getContext(), "数据上传成功。");
                } else {
                    ToastUtil.showTextToast(getContext(), bean.getMessage());
                }
            }
        });
    }

    private void alertDia(String s) {
        PopupDialog dialog = PopupDialog.create(getContext(), "警   告", s, "确定", null);
        dialog.show();
    }

    private String getInitDataKey(String wid, String on_time) {
        return "Init" + wid + on_time;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
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

            List<LoginResult.GroundData> data = loginData.getData();
            if (data != null && data.size() > 0) {
                initSpinner(data);
            } else {
                PopupDialog.create(getContext(), "工地数据为空",
                        "这个用户负责的工地数据为不正确，请联系管理员", "确定",
                        null).show();
            }
        }
    }

    private void initView() {
        tablayout.addTab(tablayout.newTab().setText("标砖"));
        tablayout.addTab(tablayout.newTab().setText("加气"));
        String sDate = DateUtils.formatDateByFormat(calendar, DateUtils.fmtYYYYMMDD);
        datetime.setText(sDate);
        init_data.setSelected(true);
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
        // 加载适配器
        spinner.setAdapter(adapter);
    }
}
