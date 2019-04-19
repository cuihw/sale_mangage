package com.zhongwang.sale.module;

import com.google.gson.Gson;

public class Bean {
    private int code;
    private String message = "没有查询结果";
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
