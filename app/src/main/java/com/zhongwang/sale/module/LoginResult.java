package com.zhongwang.sale.module;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhongwang.sale.Constants;
import com.zhongwang.sale.utils.PreferencesUtils;

import java.util.List;

public class LoginResult extends Bean{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<WorkSiteData> responsible;
        private UserInfo info;

        public List<WorkSiteData> getResponsible() {
            return responsible;
        }

        public void setResponsible(List<WorkSiteData> responsible) {
            this.responsible = responsible;
        }

        public UserInfo getInfo() {
            return info;
        }

        public void setInfo(UserInfo info) {
            this.info = info;
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

    public static void clearLoginData(Context context) {
        PreferencesUtils.putBoolean(context, Constants.IS_LOGIN, false);
        PreferencesUtils.putString(context, Constants.LOGIN_DATA, "");
    }

}
