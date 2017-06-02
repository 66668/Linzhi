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
import com.linzhi.db.entityimpl.UserEntity;
import com.linzhi.model.DetailModel;
import com.linzhi.model.MessageListModel;
import com.linzhi.model.SiteIDModel;
import com.linzhi.model.VipSearchListModel;
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
            String workId = config.getUserName();
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
     * 01-01密码登录
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
        uEntity.setUserName(userName);
        uEntity.setPassword(password);
        uEntity.setSiteID(siteID);

        // ConfigUtil中断保存，在退出后重新登录用getAccount()调用
        ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
        config.setUserName(userName);
        config.setPassword(password);
        config.setSiteID(siteID);

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
        Log.d("HTTP", "getSiteID:hr.jsonArray.toString()=" + hr.jsonArray.toString());
        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<SiteIDModel>>() {
        }.getType());
    }

    /**
     * 02-01获取信息列表
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
            js.put("siteID", UserHelper.getCurrentUser().getSiteID());
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
     * 02-02查询信息列表
     *
     * @return
     * @throws MyException
     */
    public static List<MessageListModel> searchMessageList(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            js.put("siteID", UserHelper.getCurrentUser().getSiteID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getMessageList: js=" + js.toString());
        HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.SEARCH_MSG_LIST, js);//WebUrl.UserManager.SEARCH_MSG_LIST
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<MessageListModel>>() {
        }.getType());
    }


    /**
     * 02-03详情
     *
     * @param context
     * @return
     * @throws MyException
     */
    public static DetailModel getItemDetail(Context context, JSONObject js) throws MyException {//DetailModel
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        Log.d(TAG, "getMessageList: js=" + js.toString());
        HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.ITEM_DETAIL, js);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }
        Log.d(TAG, "getItemDetail: httpResult.jsonObject.toString()=" + httpResult.jsonObject.toString());
        return (new Gson()).fromJson(httpResult.jsonObject.toString(), DetailModel.class);

    }

    /**
     * 02-04修改注册
     *
     * @param context
     * @return
     * @throws MyException
     */
    public static void postChangeVip(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.CHANGE_VIP_DETAIL, js);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

    }

    /**
     * 03-01查询注册人员
     */
    public static List<VipSearchListModel> searchVip(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        Log.d(TAG, "getMessageList: js=" + js.toString());
        HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.SERACH_VIP_LIST, js);//WebUrl.UserManager.SEARCH_MSG_LIST
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<VipSearchListModel>>() {
        }.getType());
    }


    /**
     * 04-01注册
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

}
