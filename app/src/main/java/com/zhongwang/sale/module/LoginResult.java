package com.zhongwang.sale.module;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhongwang.sale.Constants;
import com.zhongwang.sale.activity.LoginActivity;
import com.zhongwang.sale.utils.PreferencesUtils;
import com.zhongwang.sale.utils.ToastUtil;

import java.util.List;

public class LoginResult extends Bean{

    List<GroundData> data;

    public List<GroundData> getData() {
        return data;
    }

    public void setData(List<GroundData> data) {
        this.data = data;
    }

    public static class GroundData {
        String id;
        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public String toJson(){
        return new Gson().toJson(this);
    }

    public static LoginResult fromJson(String json) {
        return new Gson().fromJson(json,LoginResult.class);
    }

    public static LoginResult getLoginDataFromPreference(Context context) {
        String stringData = PreferencesUtils.getString(context, Constants.LOGIN_DATA, null);
        if (TextUtils.isEmpty(stringData)) {
           return null;
        } else {
            return fromJson(stringData);
        }
    }
}
