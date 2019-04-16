package com.zhongwang.sale;

import android.app.Application;
import android.util.Log;

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
    }

    private void initOkgo() {

        HttpRequest.initOkGo(this);
    }


}
