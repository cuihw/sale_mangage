package com.zhongwang.sale;

public class Constants {

    public static final String LOGIN_ADDRESS = "loginAddress";
    public static final String USERNAME = "username";

    private static final String HOST_RELEASE = "http://218.28.225.140";
    private static final String HOST_DEBUG = "http://192.168.1.253";

    private static final String HOST = HOST_DEBUG;

    public static final String UPDATE_INFO = HOST + "/zwjxgj/app/appInfo.json";
    public static final String UPDATE_URL = HOST + "/zwjxgj/app/app.apk";
    private static final String BASE_URL = HOST + "/zwjxgj/index.php/app/";

    // ==============================================
    public static final String ADD_GROUND = BASE_URL + "Warehouse/doAddWareHouse";  // 增加工地信息。
    public static final String LOGIN_URL = BASE_URL + "user/login";
    public static final String SALES_UPDATE = BASE_URL + "SalesRecord/upDate";  // 加气
    public static final String SALES_UPDATE_STANDARD = BASE_URL + "SalesRecord/upNormTypeDate";  // 标砖
    public static final String DAILY_REQUEST = BASE_URL + "SalesRecord/daySearch";
    public static final String MONTH_REQUEST = BASE_URL + "SalesRecord/monthSearch";
    public static final String REQUEST_INIT_DATA = BASE_URL + "InitData/searchData";
    public static final String SAVE_INIT_DATA = BASE_URL + "InitData/saveData";

    public static final int SUCCEED_CODE = 1;
    public static final String IS_LOGIN = "isLogin";
    public static final String LOGIN_DATA = "login_data";
}
