package com.linzhi.utils;

/**
 * app重构 重复使用 及转换方法
 * Created by sjy on 2017/4/26.
 */

public class AppCommonUtils {
    //数字转等级名称
    public static String getTransLevel(String numString){
        String strLevel;
        switch (numString) {
            case "1":
                strLevel= "钻石会员";
                break;
            case "2":
                strLevel= "铂金会员";
                break;
            case "3":
                strLevel= "黄金会员";
                break;
            case "4":
                strLevel= "白银会员";
                break;
            case "5":
                strLevel= "普通会员";
                break;
            default:
                strLevel= "";
        }
        return strLevel;
    }

    //等级名称转数字
    public static String getLevelNum(String levelName){
        String levlelNum;
        switch (levelName) {
            case "钻石会员":
                levlelNum = "1";
                break;
            case "铂金会员":
                levlelNum = "2";
                break;
            case "黄金会员":
                levlelNum = "3";
                break;
            case "白银会员":
                levlelNum = "4";
                break;
            case "普通会员":
                levlelNum = "5";
                break;
            default:
                levlelNum= "";
        }
        return levlelNum;
    }
}
