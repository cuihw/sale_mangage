package com.zhongwang.sale.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongwang.sale.BaseActivity;
import com.zhongwang.sale.Constants;
import com.zhongwang.sale.R;
import com.zhongwang.sale.module.LoginResult;
import com.zhongwang.sale.network.HttpRequest;
import com.zhongwang.sale.utils.HwLog;
import com.zhongwang.sale.utils.PreferencesUtils;
import com.zhongwang.sale.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.title_text)
    TextView title_text;

    @BindView(R.id.back_title)
    ImageView back_title;
    @BindView(R.id.login_action)
    TextView login_action;

    @BindView(R.id.edit_user)
    EditText edit_user;
    @BindView(R.id.edit_password)
    EditText edit_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        title_text.setText("登录");
        back_title.setVisibility(View.GONE);
        initListener();
    }

    private void initListener() {
        login_action.setOnClickListener(v -> startLogin());
    }

    private void startLogin() {
        String username = edit_user.getText().toString();
        String password = edit_password.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showTextToast(this, "用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showTextToast(this, "密码不符合要求");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        HttpRequest.postData(this, Constants.LOGIN_URL, params, new HttpRequest.RespListener<LoginResult>() {
            @Override
            public void onResponse(int status, LoginResult bean) {
                ToastUtil.showTextToast(LoginActivity.this, bean.getMessage());
                if (bean.getCode() == Constants.SUCCEED_CODE){
                    bean.setUsername(username);
                    loginSucceed(bean);
                } else {
                    HwLog.i(TAG, "request code: " + bean.getCode());
                }
            }
        });
        PreferencesUtils.putString(this, Constants.LOGIN_ADDRESS, Constants.LOGIN_URL);
    }

    private void loginSucceed(LoginResult bean) {
        PreferencesUtils.putBoolean(this,Constants.IS_LOGIN, true);
        PreferencesUtils.putString(this,Constants.LOGIN_DATA, bean.toJson());
        HwLog.i(TAG, "data = " + bean.getData());
        finish();
    }

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        HttpRequest.cancleRequest(this);
        super.onDestroy();
    }
}
