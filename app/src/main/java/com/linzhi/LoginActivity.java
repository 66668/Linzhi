package com.linzhi;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.linzhi.adapter.SiteIDSpinnerAdapter;
import com.linzhi.base.BaseActivity;
import com.linzhi.common.MyException;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.inject.ViewInject;
import com.linzhi.model.SiteIDModel;
import com.linzhi.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    @ViewInject(id = R.id.tv_SiteID)
    Spinner spinner_SiteID;

    @ViewInject(id = R.id.tv_userName)
    EditText tv_userName;

    @ViewInject(id = R.id.tv_psd)
    EditText tv_psd;

    @ViewInject(id = R.id.btn_login,click = "toLogin")
    Button btn_login;

    //变量
    private String siteID = "";
    private String userName = "";
    private String passWord = "";
    List<SiteIDModel> listSiteID;
    private SiteIDSpinnerAdapter spinnerAdapter;
    //常量
    private static final int POST_SUCCESS = 11;
    private static final int POST_FAILED = 12;
    private static final int GET_SITE_SUCCESS = 13;
    private static final String TAG = "SJY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_login);
        Log.d(TAG, "onCreate: oncreate次数");
        getSiteIDDate();
        initListener();

    }


    private void initListener() {
        //选择spinner
        spinner_SiteID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listSiteID != null) {
                    siteID = spinnerAdapter.getItem(position).getSiteID();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                siteID = "";
            }
        });
    }

    private void getSiteIDDate() {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {

                    List<SiteIDModel> listSiteID = UserHelper.getSiteID(LoginActivity.this);
                    sendMessage(GET_SITE_SUCCESS, listSiteID);
                } catch (MyException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private boolean isEmpty() {
        if (TextUtils.isEmpty(siteID)) {
            PageUtil.DisplayToast("网点号不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(userName)) {
            PageUtil.DisplayToast("用户名不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(passWord)) {
            PageUtil.DisplayToast("密码不能为空！");
            return false;
        }
        return true;
    }

    public void toLogin(View view) {
        //        siteID = tv_SiteID.getText().toString().trim();
        userName = "aaa";//tv_userName.getText().toString().trim();// aaa
        passWord = "111";//tv_psd.getText().toString().trim();//123

        //非空判断
        if (!isEmpty()) {
            return;
        }

        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: siteID=" + siteID + "--userName=" + userName + "--passWord=" + passWord);

                    UserHelper.loginByPs(getApplicationContext(), siteID, userName, passWord);//userName passWord
                    sendMessage(POST_SUCCESS);
                } catch (MyException e) {
                    Log.d("SJY", "登录异常：" + e.getMessage());
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });

    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                startActivity(MainActivity.class);
                this.finish();
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;

            case GET_SITE_SUCCESS:
                listSiteID = (List<SiteIDModel>) msg.obj;
                bindSiteSpinner(listSiteID);// 动态绑定到spinner
                break;
        }
    }

    //spinner绑定
    private void bindSiteSpinner(List<SiteIDModel> listSiteID) {
        spinnerAdapter = new SiteIDSpinnerAdapter(this, (ArrayList<SiteIDModel>) listSiteID);
        spinner_SiteID.setAdapter(spinnerAdapter);
        try {
            //设置默认网点
            spinner_SiteID.setSelection(getSiteIDIndex());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSiteIDIndex() {
        //可自定义登录保存的网点号
        return 0;//默认spinner显示第一个值
    }
}

