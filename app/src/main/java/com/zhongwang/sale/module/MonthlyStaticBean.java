package com.zhongwang.sale.module;

import java.util.List;

public class MonthlyStaticBean extends Bean {

    private List<MonthlyStaticData> data;

    public List<MonthlyStaticData> getData() {
        return data;
    }

    public void setData(List<MonthlyStaticData> data) {
        this.data = data;
    }
}
