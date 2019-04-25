package com.zhongwang.sale.module;

import com.google.gson.Gson;

public class UpdateDataBean extends Bean {

    private DailyData data;

    public static UpdateDataBean fromJson(String json) {
        return new Gson().fromJson(json, UpdateDataBean.class);
    }

    public DailyData getData() {
        return data;
    }

    public void setData(DailyData data) {
        this.data = data;
    }

    public static class DailyData {

        private String id;

        private String wid;

        private String wname;

        private String up_date;

        private String out_number;

        private String price;

        private String rmoney;

        private String dsend;

        private String drecycling;

        private int dresidue;

        private String dmoney;

        private float dbalance;

        private String uname;

        private float dmoney2;

        private String report_square;

        private String report_money;

        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWid() {
            return wid;
        }

        public void setWid(String wid) {
            this.wid = wid;
        }

        public String getWname() {
            return wname;
        }

        public void setWname(String wname) {
            this.wname = wname;
        }

        public String getUp_date() {
            return up_date;
        }

        public void setUp_date(String up_date) {
            this.up_date = up_date;
        }

        public String getOut_number() {
            return out_number;
        }

        public void setOut_number(String out_number) {
            this.out_number = out_number;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRmoney() {
            return rmoney;
        }

        public void setRmoney(String rmoney) {
            this.rmoney = rmoney;
        }

        public String getDsend() {
            return dsend;
        }

        public void setDsend(String dsend) {
            this.dsend = dsend;
        }

        public String getDrecycling() {
            return drecycling;
        }

        public void setDrecycling(String drecycling) {
            this.drecycling = drecycling;
        }

        public int getDresidue() {
            return dresidue;
        }

        public void setDresidue(int dresidue) {
            this.dresidue = dresidue;
        }

        public String getDmoney() {
            return dmoney;
        }

        public void setDmoney(String dmoney) {
            this.dmoney = dmoney;
        }

        public float getDbalance() {
            return dbalance;
        }

        public void setDbalance(int dbalance) {
            this.dbalance = dbalance;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public float getDmoney2() {
            return dmoney2;
        }

        public void setDmoney2(int dmoney2) {
            this.dmoney2 = dmoney2;
        }

        public String getReport_square() {
            return report_square;
        }

        public void setReport_square(String report_square) {
            this.report_square = report_square;
        }

        public String getReport_money() {
            return report_money;
        }

        public void setReport_money(String report_money) {
            this.report_money = report_money;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }


}
