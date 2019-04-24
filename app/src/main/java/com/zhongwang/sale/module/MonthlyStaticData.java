package com.zhongwang.sale.module;

public class MonthlyStaticData {
    private String wid;

    private String out_number;

    private String price;

    private String dmoney;

    private String rmoney;

    private String dsend;

    private String drecycling;

    private int dresidue;

    private int dbalance;

    private String wname;

    private String report_square;

    private String report_money;

    private int dmoney2;

    private String init_money;

    private String init_tray;

    private String billing_price;

    private String billing_number;

    private String billing_dmoney;

    private int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
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

    public String getDmoney() {
        return dmoney;
    }

    public void setDmoney(String dmoney) {
        this.dmoney = dmoney;
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

    public int getDbalance() {
        return dbalance;
    }

    public void setDbalance(int dbalance) {
        this.dbalance = dbalance;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
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

    public int getDmoney2() {
        return dmoney2;
    }

    public void setDmoney2(int dmoney2) {
        this.dmoney2 = dmoney2;
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

    public String getBilling_price() {
        return billing_price;
    }

    public void setBilling_price(String billing_price) {
        this.billing_price = billing_price;
    }

    public String getBilling_number() {
        return billing_number;
    }

    public void setBilling_number(String billing_number) {
        this.billing_number = billing_number;
    }

    public String getBilling_dmoney() {
        return billing_dmoney;
    }

    public void setBilling_dmoney(String billing_dmoney) {
        this.billing_dmoney = billing_dmoney;
    }

    public boolean isJiaQi() {
        return type == 0;
    }
}
