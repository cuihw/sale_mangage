package com.zhongwang.sale.module;

public class WorkSiteData {
    private String id;
    private String name;
    private String contract_price;
    private String norm_price;

    public String getNorm_price() {
        return norm_price;
    }

    public void setNorm_price(String norm_price) {
        this.norm_price = norm_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContract_price() {
        return contract_price;
    }

    public void setContract_price(String contract_price) {
        this.contract_price = contract_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
