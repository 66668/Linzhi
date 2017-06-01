package com.linzhi.utils;

public class WebUrl {

    /**
     * 根接口
     */
    // 云端
    //    private static final String LOGIN_URL = "http://192.168.1.111:8080";//192.168.1.111:8080/YueVisionLzh/Select/site
    private static final String LOGIN_URL = "http://192.168.1.171:8080";
    //    private static final String LOGIN_URL = "http://192.168.1.227:8080";//我的主机


    /**
     * 管理根目录
     */
    public static final String GEN = "/YueVisionLzh";

    //图片路径要使用
    public static String getURL() {
        return LOGIN_URL + GEN + "/";
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
        public static final String GET_SITEID = LOGIN_URL + GEN + "/Select/Site";

        /**
         * 02-01 获取列表数据
         */
        public static final String GET_MSG_LIST = LOGIN_URL + GEN + "/getRecordList";

        /**
         * 02-02查询列表数据
         */
        public static final String SEARCH_MSG_LIST = LOGIN_URL + GEN + "/searchRecord/getRecordMessage";

        /**
         * 02-03 识别详情
         */
        public static final String ITEM_DETAIL = LOGIN_URL + GEN + "/GetRecord/GetVIPDetail";
        /**
         * 02-03 vip详情
         */
        public static final String VIP_ITEM_DETAIL = LOGIN_URL + GEN + "/GetRecord/GetVIPDetail";

        /**
         * 02-04修改注册
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String CHANGE_VIP_DETAIL = LOGIN_URL + GEN + "/Client/Update";
        /**
         * 03-01查询vip
         */
        public static final String SERACH_VIP_LIST = LOGIN_URL + GEN + "/searchVip/getRecordVip";
        /**
         * 04-01注册
         * http://localhost:8080/YueVisionLzh/Select
         */

        public static final String POST_VIP_REG = LOGIN_URL + GEN + "/Register";
        /**
         * 测试
         * http://localhost:8080/YueVisionLzh/Select
         */
        public static final String TEXT = "http://59.110.26.83:8096/openapi//RegisterConferenceInfo/GetConferenceInfo";


    }

}
