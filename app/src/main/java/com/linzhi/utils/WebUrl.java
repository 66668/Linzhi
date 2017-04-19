package com.linzhi.utils;

public class WebUrl {

    /**
     * 根接口
     */
    // 云端
    private static final String LOGIN_URL = "http://192.168.1.221:8080/";//192.168.1.221:8080/YueVisionLzh/Select/site


    /**
     * 管理根目录
     */
    public static final String GEN = "YueVisionLzh/";

    //图片路径要使用
    public static String getURL() {
        return "http://192.168.1.188:8080/YueVisionLzh/";
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
         * http://192.168.1.221:8080/YueVisionLzh/Select/site
         */
        public static final String GET_SITEID = "http://192.168.1.221:8080/YueVisionLzh/Select/site";

        /**
         * 获取列表数据
         */
        public static final String GET_MSG_LIST = "http://192.168.1.221:8080/YueVisionLzh/getMessageList";

        /**
         * 查询列表数据
         */
        public static final String SEARCH_MSG_LIST = "http://192.168.1.221:8080/YueVisionLzh/searchMessageList";

        /**
         * 注册
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String POST_VIP_REG = "http://192.168.1.221:8080/YueVisionLzh/Register";

        /**
         * 02 注册
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String ITEM_DETAIL = "http://192.168.1.221:8080/YueVisionLzh/Select";

    }


}
