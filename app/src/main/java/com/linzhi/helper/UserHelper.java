package com.linzhi.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linzhi.R;
import com.linzhi.application.MyApplication;
import com.linzhi.common.HttpResult;
import com.linzhi.common.MyException;
import com.linzhi.common.NetworkManager;
import com.linzhi.db.entity.UserEntity;
import com.linzhi.model.DetailModel;
import com.linzhi.model.MessageListModel;
import com.linzhi.model.SiteIDModel;
import com.linzhi.utils.APIUtils;
import com.linzhi.utils.ConfigUtil;
import com.linzhi.utils.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 用户管理者帮助类
 * <p/>
 * 处理访问服务端信息类 解析js对象，调用Gson外部包：gson-2.2.2.jar
 *
 * @author JackSong
 */

public class UserHelper<T> {
    private static final String TAG = "SJY";
    static UserEntity mCurrentUser = null;
    static String configUserManager = null;

    /**
     * (2)获取用户账号
     *
     * @return
     */
    public static UserEntity getCurrentUser() {
        // 调用下边的方法
        return getCurrentUser(true);
    }

    public static UserEntity getCurrentUser(boolean isAutoLoad) {

        if (mCurrentUser == null && isAutoLoad) {// 判断MemberModel类是否为空
            // 中断保存
            ConfigUtil config = new ConfigUtil(MyApplication.getInstance());// 中断保存获取信息
            String workId = config.getWorkId();
            if (!"".equals(workId)) {
                // 获取所有当前用户信息，保存到mCurrentUser对象中
                mCurrentUser = config.getUserEntity();
            }
        }
        return mCurrentUser;
    }

    public static void setmCurrentUser(UserEntity u) {//退出登录调用
        mCurrentUser = u;
    }

    /**
     * 01 密码登录
     */
    public static void loginByPs(Context context, String siteID, String userName, String password) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);

        JSONObject js = new JSONObject();
        try {
            js.put("siteID", siteID);
            js.put("userName", userName);
            js.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.UserManager.LOGIN_POST, js);

        if (hr.hasError()) {
            throw hr.getError();
        }

        //返回个人信息保存
        UserEntity uEntity = new UserEntity();
        //登录信息保存
        uEntity.setWorkId(userName);
        uEntity.setPassword(password);

        // ConfigUtil中断保存，在退出后重新登录用getAccount()调用
        ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
        config.setWorkId(userName);
        config.setPassword(password);
        config.setAutoLogin(true);
        config.setUserEntity(uEntity);// 保存已经登录成功的对象信息
        mCurrentUser = uEntity;// 将登陆成功的对象信息，赋值给全局变量
    }

    /**
     * 01-02 登录前获取网点号
     *
     * @param context
     * @throws MyException
     */
    public static List<SiteIDModel> getSiteID(Context context) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);

        HttpResult hr = APIUtils.getForObject(WebUrl.UserManager.GET_SITEID);

        if (hr.hasError()) {
            throw hr.getError();
        }
        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<SiteIDModel>>() {
        }.getType());
    }

    /**
     * 获取信息列表
     *
     * @param context
     * @param maxtTime
     * @param minTime
     * @return
     * @throws MyException
     */
    public static List<MessageListModel> getMessageList(Context context, String maxtTime, String minTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        JSONObject js = new JSONObject();
        try {
            js.put("maxtime", maxtTime);
            js.put("mintime", minTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getMessageList: js=" + js.toString());
        HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.GET_MSG_LIST, js);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<MessageListModel>>() {
        }.getType());
    }

    /**
     * 注册
     *
     * @param context
     * @return
     * @throws MyException
     */
    public static void postVipRegist(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.POST_VIP_REG, js);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

    }

    /**
     * 详情
     *
     * @param context
     * @return
     * @throws MyException
     */
    public static DetailModel getItemDetail(Context context, String clientid) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        JSONObject js = new JSONObject();
        try {
            js.put("clientid", clientid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getMessageList: js=" + js.toString());
        HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.ITEM_DETAIL, js);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonObject.toString(), DetailModel.class);

    }

}
