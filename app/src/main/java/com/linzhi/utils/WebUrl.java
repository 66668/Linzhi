package com.linzhi.utils;

public class WebUrl {

    /**
     * 根接口
     */
    // 云端
    private static final String LOGIN_URL = "http://192.168.1.227:8080";//192.168.1.111:8080/YueVisionLzh/Select/site


    /**
     * 管理根目录
     */
    public static final String GEN = "/YueVisionLzh";

    //图片路径要使用
    public static String getURL() {
        return "http://192.168.1.198:8080/YueVisionLzh/";
    }

    /**
     * 使用者管理
     *
     * @author JackSong
     */
    public class UserManager {

        /**
         * 01-01 密码登录
         */
        public static final String LOGIN_POST = LOGIN_URL + GEN + "/Login";

        /**
         * 01-02 获取网点号
         * http://192.168.1.111:8080/YueVisionLzh/Select/site
         */
        public static final String GET_SITEID = LOGIN_URL + GEN + "/Select/site";

        /**
         * 02-01 获取列表数据
         */
        public static final String GET_MSG_LIST = LOGIN_URL + GEN + "/getRecordList";

        /**
         * 02-02查询列表数据
         */
        public static final String SEARCH_MSG_LIST = LOGIN_URL + GEN + "/searchRecord/getRecordMessage";

        /**
         * 02-03 详情
         */
        public static final String ITEM_DETAIL = LOGIN_URL + GEN + "/getRecord/getRecordDetail";
/**
         * 02-03 详情
         */
        public static final String VIP_ITEM_DETAIL = LOGIN_URL + GEN + "/getRecord/getVipDetail";

        /**
         * 02-04修改注册
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String CHANGE_VIP_DETAIL = LOGIN_URL + GEN + "/Update/Client";
        /**
         * 03-01查询vip
         */
        public static final String SERACH_VIP_LIST = LOGIN_URL + GEN + "/searchVip/getRecordVip";
        /**
         * 04-01注册
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String POST_VIP_REG = LOGIN_URL + GEN + "/Register";


    }

}
