package com.linzhi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.linzhi.db.entityimpl.UserEntity;

import org.json.JSONObject;

/**
 * SharedPreferences数据保存
 * <p>
 * 程序所有存储类
 * <p>
 * gson包
 *
 * @author JackSong
 */
public class ConfigUtil {
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;


    private static final String USERNAME = "userName";//工号
    private static final String PSD = "password";//密码
    private static final String SITEID = "siteid";//网点号
    private static final String AUTO_LOGIN = "auto_login";
    private static final String USERENTITY = "user_entity";
    private static final String MAXTIME = "maxtime";

    //登录内容保存
    @SuppressLint("CommitPrefEdits")
    public ConfigUtil(Context context) {
        try {
            //获取SharePreferences实例，保存位置是config.xml文件，模式是私有模式
            //文件存放在/data/data/<package name>/shared_prefs/config.xml
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            //获得编辑器 ,方便将内容添加到Sharepreferences中
            editor = sp.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    public void resetConfig() {
        setUserName(null);
        setAutoLogin(true);
    }

    /**
     * 将js转换成对象，需要调用外部jar包，
     * 获取所有当前用户的信息
     */
    public UserEntity getUserEntity() {
        String string = sp.getString(USERENTITY, null);
        if (string != null && string.length() > 0) {
            try {
                return (UserEntity) UserEntity.toEntityBase(new JSONObject(string), UserEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //保存对象，转换成js格式保存，需要调用外部jar包
    //CreateUserActivity--UserHelper--ConfigUtil该方法
    public void setUserEntity(UserEntity userEntity) {
        editor.putString(USERENTITY, userEntity.toJSON().toString());//
        editor.commit();
    }

    //调用该方法，将值保存
    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    //获取值，没有对应值返回""
    public String get(String key) {
        return sp.getString(key, "");
    }

    //
    public String getUserName() {
        return sp.getString(USERNAME, null);//userid
    }

    //登录名
    public void setUserName(String id) {
        editor.putString(USERNAME, id);
        editor.commit();
    }

    //密码
    public String getPassword() {
        return sp.getString(PSD, null);
    }

    public void setPassword(String psd) {
        editor.putString(PSD, psd);
        editor.commit();
    }

    //网点号
    public String getSiteID() {
        return sp.getString(SITEID, null);
    }

    public void setSiteID(String siteid) {
        editor.putString(SITEID, siteid);
        editor.commit();
    }

    //返回自动登录状态
    public boolean getAutoLogin() {
        return sp.getBoolean(AUTO_LOGIN, true);
    }

    public void setAutoLogin(boolean flag) {
        editor.putBoolean(AUTO_LOGIN, flag);
        editor.commit();
    }

    //保存最新的数据时间，定时刷新使用
    public void setMaxTime(String timestr) {
        editor.putString(MAXTIME, timestr);
        editor.commit();
    }

    public String getMaxtime() {
        return sp.getString(MAXTIME, Utils.getCurrentTime());
    }

}

