package com.linzhi.utils;

public class WebUrl {

    /**
     * 根接口
     */
    // 云端
    private static final String LOGIN_URL = "http://192.168.1.227:8080/";//192.168.1.111:8080/YueVisionLzh/Select/site


    /**
     * 管理根目录
     */
    public static final String GEN = "YueVisionLzh/";

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
         * 01 密码登录
         */
        public static final String LOGIN_POST = LOGIN_URL + GEN + "Login";

        /**
         * 02 获取网点号
         * http://192.168.1.111:8080/YueVisionLzh/Select/site
         */
        public static final String GET_SITEID = LOGIN_URL + GEN + "Select/site";

        /**
         * 获取列表数据
         */
        public static final String GET_MSG_LIST = LOGIN_URL + GEN + "Select";

        /**
         * 查询列表数据
         */
        public static final String SEARCH_MSG_LIST = LOGIN_URL + GEN + "/searchRecord/getRecordMessage";

        /**
         * 注册
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String POST_VIP_REG = LOGIN_URL + GEN + "Register";

        /**
         * 修改注册
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String CHANGE_VIP_DETAIL = LOGIN_URL + GEN + "Update/Client";

        /**
         * 02 详情
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String ITEM_DETAIL = LOGIN_URL + GEN + "Select";

    }

}
