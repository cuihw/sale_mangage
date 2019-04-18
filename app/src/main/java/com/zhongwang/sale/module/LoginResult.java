package com.zhongwang.sale.module;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhongwang.sale.Constants;
import com.zhongwang.sale.utils.PreferencesUtils;

import java.util.List;

public class LoginResult extends Bean{
    private String username;

    private List<GroundData> data;

    public String getUsername() {
        return username;
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public static void clearLoginData(Context context) {
        PreferencesUtils.putBoolean(context, Constants.IS_LOGIN, false);
        PreferencesUtils.putString(context, Constants.LOGIN_DATA, "");
    }

}
