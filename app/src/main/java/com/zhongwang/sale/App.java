package com.zhongwang.sale;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.zhongwang.sale.network.HttpRequest;

public class App extends Application {

    private static final String TAG = "App";
    private static App instence;

    public static App getInstence(){
        return instence;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "App onCreate");
        super.onCreate();
        instence = this;
        initOkgo();
        CrashReport.initCrashReport(this, "注册时申请的APPID", false);
    }

    private void initOkgo() {

        HttpRequest.initOkGo(this);
    }


}
