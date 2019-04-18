package com.zhongwang.sale.module;

import com.google.gson.Gson;

public class InitDataBean extends Bean {

    private InitData data;

    public static InitDataBean fromJson(String json) {
        return new Gson().fromJson(json, InitDataBean.class);
    }

    public InitData getData() {
        return data;
    }

    public void setData(InitData data) {
        this.data = data;
    }

    public static class InitData {
        String wid;
        String on_time;
        String init_money;
        String init_tray;
        String updata_time;

        public String getWid() {
            return wid;
        }

        public void setWid(String wid) {
            this.wid = wid;
        }

        public String getOn_time() {
            return on_time;
        }

        public void setOn_time(String on_time) {
            this.on_time = on_time;
        }

        public String getInit_money() {
            return init_money;
        }

        public void setInit_money(String init_money) {
            this.init_money = init_money;
        }

        public String getInit_tray() {
            return init_tray;
        }

        public void setInit_tray(String init_tray) {
            this.init_tray = init_tray;
        }

        public String getUpdata_time() {
            return updata_time;
        }

        public void setUpdata_time(String updata_time) {
            this.updata_time = updata_time;
        }
    }


}
