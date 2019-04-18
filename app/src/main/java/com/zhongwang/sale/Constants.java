package com.zhongwang.sale;

public class Constants {
    public static final String LOGIN_ADDRESS = "loginAddress";
    public static final String USERNAME = "username";
    // http://218.28.225.140
    // http://192.168.1.253
    private static final String BASE_URL = "http://192.168.1.253/zwjxgj/index.php/app/";

    public static String LOGIN_URL = BASE_URL + "user/login";
    public static String SALES_UPDATE = BASE_URL + "SalesRecord/upDate";  // 加气
    public static String SALES_UPDATE_STANDARD = BASE_URL + "SalesRecord/upNormTypeDate";  // 标砖
    public static String DAILY_REQUEST = BASE_URL + "SalesRecord/daySearch";
    public static String MONTH_REQUEST = BASE_URL + "SalesRecord/monthSearch";
    public static String REQUEST_INIT_DATA = BASE_URL + "InitData/searchData";
    public static String SAVE_INIT_DATA = BASE_URL + "InitData/saveData";
    public static final int SUCCEED_CODE = 1;
    public static final String IS_LOGIN = "isLogin";
    public static final String LOGIN_DATA = "login_data";
}
